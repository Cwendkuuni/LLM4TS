package net.sf.xbus.technical.mail;

import net.sf.xbus.base.core.Constants;
import net.sf.xbus.base.core.TAManager;
import net.sf.xbus.base.core.XException;
import net.sf.xbus.base.xbussystem.XBUSSystem;
import net.sf.xbus.technical.ReceiverThreadBase;

/**
 * <p>
 * The <code>POP3XMLReceiverThread</code> runs in the background and receives
 * emails.
 * 
 * @author Dominique Boivin
 */

public class POP3XMLReceiverThread extends ReceiverThreadBase
{
	private POP3XMLReceiver mReceiver = null;

	/**
	 * Stores the interface name
	 * 
	 * @param source name of the interface definition
	 */
	public POP3XMLReceiverThread(XBUSSystem source)
	{
		super(source);
	}

	/**
	 * Creates an instance of {@link POP3XMLReceiver}
	 * used to read from a mailbox.
	 * 
	 * @return an instance of {@link POP3XMLReceiver}
	 */
	protected POP3XMLReceiver createReceiver()
	{
		return new POP3XMLReceiver();
	}

	/**
	 * Opens the connection to the POP3 server.
	 */
	protected void initializeThread() throws XException
	{
		mReceiver = createReceiver();
		mReceiver.readConfiguration(getSource().getName());
		mReceiver.open();
	}

	/**
	 * Registers the {@link POP3XMLReceiver} in the
	 * {@link TAManager}.
	 */
	protected void registerResources(TAManager taManager) throws XException
	{
		mReceiver.closeFolder();
		taManager.registerResource(mReceiver);
		mReceiver.open();
	}

	/**
	 * @see ReceiverThreadBase#receive()
	 */
	protected Object receive() throws XException
	{
		return mReceiver.getRequestContent(getSource().getName());
	}

	/**
	 * Returns {@link Constants#TYPE_OBJECT}
	 * 
	 * @return {@link Constants#TYPE_OBJECT}
	 */
	public String getType()
	{
		return Constants.TYPE_OBJECT;
	}

	/**
	 * @see ReceiverThreadBase#getReceiverClassName()
	 */
	protected String getReceiverClassName()
	{
		return "POP3ReceiverThread";
	}

	/**
	 * Returns a concatenation of the mailbox name and the server name.
	 * 
	 * @return a concatenation of the mailbox name and the server name
	 */
	protected String getAddress()
	{
		return mReceiver.getAddress();
	}
}
