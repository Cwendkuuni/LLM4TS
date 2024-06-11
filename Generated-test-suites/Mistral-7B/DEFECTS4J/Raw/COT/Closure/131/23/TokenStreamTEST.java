import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TokenStreamTest {

 private static final String[] KEYWORDS = {
   "abstract", "arguments", "break", "byte", "case", "catch", "char",
   "class", "const", "continue", "debugger", "default", "delete",
   "do", "double", "else", "export", "extends", "false", "final",
   "finally", "float", "for", "function", "goto", "if", "implements",
   "import", "in", "instanceof", "int", "interface", "let", "long",
   "native", "new", "null", "package", "private", "protected", "public",
   "return", "short", "super", "switch", "synchronized", "this",
   "throw", "throws", "transient", "try", " typeof", "var", "void",
   "volatile", "while", "with", "yield"
 };

 @Test
 public void testIsKeyword() {
   for (String keyword : KEYWORDS) {
      assertTrue("Expected '"+ keyword +"' to be a keyword", TokenStream.isKeyword(keyword));
   }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsKeywordWithNull() {
    assertThrows("Expected IllegalArgumentException", IllegalArgumentException.class, () -> TokenStream.isKeyword(null));
 }

 @Test
 public void testIsKeywordWithEmptyString() {
    assertFalse("Expected empty string to not be a keyword", TokenStream.isKeyword(""));
 }

 @Test
 public void testIsJSIdentifier() {
   for (String validIdentifier : new String[]{"identifier1", "_identifier1", "identifier_1", "identifier1_"}) {
      assertTrue("Expected '"+ validIdentifier +"' to be a valid identifier", TokenStream.isJSIdentifier(validIdentifier));
   }

   for (String invalidIdentifier : new String[]{"1identifier", "identifier123", "identifier_1-", "identifier_1+"}) {
      assertFalse("Expected '"+ invalidIdentifier +"' to not be a valid identifier", TokenStream.isJSIdentifier(invalidIdentifier));
   }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsJSIdentifierWithNull() {
    assertThrows("Expected IllegalArgumentException", IllegalArgumentException.class, () -> TokenStream.isJSIdentifier(null));
 }

 @Test
 public void testIsJSIdentifierWithEmptyString() {
    assertFalse("Expected empty string to not be a valid identifier", TokenStream.isJSIdentifier(""));
 }
}