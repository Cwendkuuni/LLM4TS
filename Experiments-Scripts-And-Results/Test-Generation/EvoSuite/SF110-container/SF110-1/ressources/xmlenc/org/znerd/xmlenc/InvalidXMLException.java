/*
 * $Id: InvalidXMLException.java,v 1.2 2005/09/12 08:40:02 znerd Exp $
 */
package org.znerd.xmlenc;

/**
 * Exception thrown when invalid XML is detected.
 *
 * @version $Revision: 1.2 $ $Date: 2005/09/12 08:40:02 $
 * @author Ernst de Haan (<a href="mailto:wfe.dehaan@gmail.com">wfe.dehaan@gmail.com</a>)
 *
 * @since xmlenc 0.37
 */
public final class InvalidXMLException extends RuntimeException {

   //-------------------------------------------------------------------------
   // Class fields
   //-------------------------------------------------------------------------

   //-------------------------------------------------------------------------
   // Class functions
   //-------------------------------------------------------------------------

   //-------------------------------------------------------------------------
   // Constructors
   //-------------------------------------------------------------------------

   /**
    * Constructs a new <code>InvalidXMLException</code> with the specified
    * detail message.
    *
    * @param message
    *    the optional detail message, or <code>null</code>.
    */
   public InvalidXMLException(String message) {
      super(message);
   }


   //-------------------------------------------------------------------------
   // Fields
   //-------------------------------------------------------------------------

   //-------------------------------------------------------------------------
   // Methods
   //-------------------------------------------------------------------------
}
