import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*; 

/** SendMime -- send a multi-part MIME email message.
 * @author Ian F. Darwin
 * @version $Id$
 */
public class SendMime {

	/** The message recipient. */
	protected String message_recip = "spam-magnet@darwinsys.com";
	/* What's it all about, Alfie? */
	protected String message_subject = "Re: your mail";
	/** The message CC recipient. */
	protected String message_cc = "nobody@erewhon.com";
	/** The message body */
	protected String message_body =
		"I am unable to attend to your message, as I am busy sunning " +
		"myself on the beach in Maui, where it is warm and peaceful. " +
		"Perhaps when I return I'll get around to reading your mail. " +
		"Or perhaps not.";
	/* Some html data to be treated as part of a message. */
	protected String html_data = 
		"<HTML><HEAD><TITLE>My Goodness</TITLE></HEAD>" +
		"<BODY><P>You <EM>do</EM> look a little " +
		"<font color=green>GREEN</FONT>" +
		"around the edges..." +
		"</BODY></HTML>";

	/** The JavaMail session object */
	protected Session session;
	/** The JavaMail message object */
	protected Message mesg;

	/** Do the work: send the mail to the SMTP server.  */
	public void doSend() {

		// We need to pass info to the mail server as a Properties, since
		// JavaMail (wisely) allows room for LOTS of properties...
		Properties props = new Properties();

		// Your LAN must define the local SMTP server as "mailhost"
		// for this simple-minded version to be able to send mail...
		props.put("mail.smtp.host", "mailhost");

		// Create the Session object
		session = Session.getDefaultInstance(props, null);
		session.setDebug(true);		// Verbose!
		
		try {
			// create a message
			mesg = new MimeMessage(session);

			// From Address - this should come from a Properties...
			mesg.setFrom(new InternetAddress("nobody@host.domain"));

			// TO Address 
			InternetAddress toAddress = new InternetAddress(message_recip);
			mesg.addRecipient(Message.RecipientType.TO, toAddress);

			// CC Address
			InternetAddress ccAddress = new InternetAddress(message_cc);
			mesg.addRecipient(Message.RecipientType.CC, ccAddress);

			// The Subject
			mesg.setSubject(message_subject);

			// Now the message body.
			Multipart mp = new MimeMultipart();

			BodyPart textPart = new MimeBodyPart();
			textPart.setText(message_body);	// sets type to "text/plain"

			BodyPart pixPart = new MimeBodyPart();
			pixPart.setContent(html_data, "text/html");

			// Collect the Parts into the MultiPart
			mp.addBodyPart(textPart);
			mp.addBodyPart(pixPart);

			// Put the MultiPart into the Message
			mesg.setContent(mp);
			
			// Finally, send the message!
			Transport.send(mesg);

		} catch (MessagingException ex) {
			System.err.println(ex);
			ex.printStackTrace(System.err);
		}
	}

	/** Simple test case driver */
	public static void main(String av[]) {
		SendMime sm = new SendMime();
		sm.doSend();
	}
}
