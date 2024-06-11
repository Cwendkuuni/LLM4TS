/*
 * Created on 21.10.2004
 */
package net.sf.xbus.protocol.simple;

import net.sf.xbus.base.xbussystem.XBUSSystem;
import net.sf.xbus.protocol.Message;
import net.sf.xbus.protocol.ObjectMessage;
import net.sf.xbus.protocol.TextMessage;
import net.sf.xbus.protocol.XMLMessage;

import org.w3c.dom.Document;

/**
 * <code>EmptyMessage</code> is a message with content <code>null</code>,
 * which is compatible with all message types (text, object, XML).
 * 
 * @author Stephan D�wel
 */
public class EmptyMessage extends Message
		implements
			TextMessage,
			ObjectMessage,
			XMLMessage
{

	/**
	 * @see Message
	 */
	public EmptyMessage(XBUSSystem source)
	{
		super(source);
	}

	/**
	 * @see Message
	 */
	public EmptyMessage(String function, XBUSSystem source, String id)
	{
		super(function, source, id);
	}

	/**
	 * Setting the request text just does nothing. The <code>EmptyMessage</code>
	 * is not intended to have any content.
	 * 
	 * @see TextMessage#setRequestText(String,
	 *      XBUSSystem)
	 */
	public void setRequestText(String text, XBUSSystem source)
	{}

	/**
	 * Setting the response text just does nothing. The
	 * <code>EmptyMessage</code> is not intended to have any content.
	 * 
	 * @see TextMessage#setResponseText(String,
	 *      XBUSSystem)
	 */
	public void setResponseText(String text, XBUSSystem destination)
	{}

	/**
	 * Getting the request text always returns <code>null</code>. The
	 * <code>EmptyMessage</code> is not intended to have any content.
	 * 
	 * @see TextMessage#getRequestText(XBUSSystem)
	 */
	public String getRequestText(XBUSSystem system)
	{
		return null;
	}

	/**
	 * Getting the response text always returns <code>null</code>. The
	 * <code>EmptyMessage</code> is not intended to have any content.
	 * 
	 * @see TextMessage#getResponseText()
	 */
	public String getResponseText()
	{
		return null;
	}

	/**
	 * Setting the request object just does nothing. The
	 * <code>EmptyMessage</code> is not intended to have any content.
	 * 
	 * @see ObjectMessage#setRequestObject(Object,
	 *      XBUSSystem)
	 */
	public void setRequestObject(Object obj, XBUSSystem source)
	{}

	/**
	 * Setting the response object just does nothing. The
	 * <code>EmptyMessage</code> is not intended to have any content.
	 * 
	 * @see ObjectMessage#setResponseObject(Object,
	 *      XBUSSystem)
	 */
	public void setResponseObject(Object obj, XBUSSystem destination)
	{}

	/**
	 * Getting the response object always returns <code>null</code>. The
	 * <code>EmptyMessage</code> is not intended to have any content.
	 * 
	 * @see ObjectMessage#getRequestObject(XBUSSystem)
	 */
	public Object getRequestObject(XBUSSystem system)
	{
		return null;
	}

	/**
	 * Getting the response object always returns <code>null</code>. The
	 * <code>EmptyMessage</code> is not intended to have any content.
	 * 
	 * @see ObjectMessage#getResponseObject()
	 */
	public Object getResponseObject()
	{
		return null;
	}

	/**
	 * Getting the resquest document always returns <code>null</code>. The
	 * <code>EmptyMessage</code> is not intended to have any content.
	 * 
	 * @see XMLMessage#getRequestDocument(
	 *      XBUSSystem)
	 */
	public Document getRequestDocument(XBUSSystem system)
	{
		return null;
	}

	/**
	 * Setting the request document just does nothing. The
	 * <code>EmptyMessage</code> is not intended to have any content.
	 * 
	 * @see XMLMessage#setRequestDocument(Document,
	 *      XBUSSystem)
	 */
	public void setRequestDocument(Document doc, XBUSSystem source)
	{}

	/**
	 * Getting the response document always returns <code>null</code>. The
	 * <code>EmptyMessage</code> is not intended to have any content.
	 * 
	 * @see XMLMessage#getResponseDocument()
	 */
	public Document getResponseDocument()
	{
		return null;
	}

	/**
	 * Setting the response document just does nothing. The
	 * <code>EmptyMessage</code> is not intended to have any content.
	 * 
	 * @see XMLMessage#setResponseDocument(Document,
	 *      XBUSSystem)
	 */
	public void setResponseDocument(Document doc, XBUSSystem destination)
	{}

} // EmptyMessage
