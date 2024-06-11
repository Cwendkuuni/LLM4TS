/*
 * $Id: Library.java,v 1.2 2005/09/12 08:40:02 znerd Exp $
 */
package org.znerd.xmlenc;

/**
 * Class that represents this <em>xmlenc</em> library.
 *
 * @version $Revision: 1.2 $ $Date: 2005/09/12 08:40:02 $
 * @author Ernst de Haan (<a href="mailto:wfe.dehaan@gmail.com">wfe.dehaan@gmail.com</a>)
 *
 * @since xmlenc 0.30
 */
public final class Library extends Object {

   //-------------------------------------------------------------------------
   // Class fields
   //-------------------------------------------------------------------------

   //-------------------------------------------------------------------------
   // Class functions
   //-------------------------------------------------------------------------

   /**
    * Returns the version of the <em>xmlenc</em> library.
    *
    * @return
    *    the version of this library, for example <code>"%%VERSION%%"</code>,
    *    never <code>null</code>.
    */
   public static final String getVersion() {
      return "%%VERSION%%";
   }


   //-------------------------------------------------------------------------
   // Constructors
   //-------------------------------------------------------------------------

   /**
    * Constructs a new <code>Library</code> object.
    */
   private Library() {
      // empty
   }


   //-------------------------------------------------------------------------
   // Fields
   //-------------------------------------------------------------------------

   //-------------------------------------------------------------------------
   // Methods
   //-------------------------------------------------------------------------
}
