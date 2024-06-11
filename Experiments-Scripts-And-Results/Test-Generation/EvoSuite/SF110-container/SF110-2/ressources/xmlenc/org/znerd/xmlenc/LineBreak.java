/*
 * $Id: LineBreak.java,v 1.3 2005/09/12 08:40:02 znerd Exp $
 */
package org.znerd.xmlenc;

/**
 * Enumeration type for line breaks.
 *
 * @version $Revision: 1.3 $ $Date: 2005/09/12 08:40:02 $
 * @author Jochen Schwoerer (j.schwoerer [at] web.de)
 * @author Ernst de Haan (<a href="mailto:wfe.dehaan@gmail.com">wfe.dehaan@gmail.com</a>)
 *
 * @since xmlenc 0.35
 */
public final class LineBreak {

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
    * Constructs a new <code>LineBreak</code> that consists of the specified
    * characters.
    *
    * @param lineBreak
    *    the characters the line break consists of.
    */
   private LineBreak(String lineBreak) {
      _lineBreak      = lineBreak;
      _lineBreakChars = lineBreak.toCharArray();
   }


   //-------------------------------------------------------------------------
   // Fields
   //-------------------------------------------------------------------------

   /**
    * The characters this line break consists of. This field is initialized by
    * the constructor.
    */
   private final String _lineBreak;

   /**
    * A character array containing the characters this line break consists of.
    * This field is initialized by the constructor.
    */
   final char[] _lineBreakChars;


   //-------------------------------------------------------------------------
   // Methods
   //-------------------------------------------------------------------------

   /**
    * Empty line break. This is equivalent to using no line breaks.
    */
   public static final LineBreak NONE = new LineBreak("");

   /**
    * Unix and MacOS/X line break. This represents the string <code>"\n"</code>.
    */
   public static final LineBreak UNIX = new LineBreak("\n");

   /**
    * DOS and Windows line break. This represents the string <code>"\r\n"</code>.
    */
   public static final LineBreak DOS = new LineBreak("\r\n");

   /**
    * MacOS line break. This represents the string <code>"\r"</code>.
    *
    * <p>This applies to all MacOS versions before MacOS/X. Use
    * {@link #UNIX} as the MacOS/X line break.
    */
   public static final LineBreak MACOS = new LineBreak("\r");

   public String toString() {
      return _lineBreak;
   }
};
