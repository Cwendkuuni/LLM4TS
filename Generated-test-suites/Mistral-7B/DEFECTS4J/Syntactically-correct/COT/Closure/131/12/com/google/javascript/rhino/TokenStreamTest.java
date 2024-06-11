import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TokenStreamTest {

 private static final String[] KNOWN_KEYWORDS = {"abstract", "arguments", "await", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "debugger", "default", "delete", "do", "double", "else", "enum", "export", "extends", "false", "final", "finally", "float", "for", "function", "goto", "if", "implements", "import", "in", "instanceof", "int", "interface", "let", "long", "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "typeof", "var", "void", "volatile", "while", "with", "yield"};

 @Test
 public void testIsKeyword() {
 for (String keyword : KNOWN_KEYWORDS) {
 assertEquals(true, TokenStream.isKeyword(keyword));
 }
 }

 @Test
 public void testIsKeyword_InvalidIdentifier() {
 for (String invalidIdentifier : new String[]{"invalidIdentifier123", "invalidIdentifier_123", "invalidIdentifier123_", "invalidIdentifier_123_"}) {
 assertFalse(TokenStream.isKeyword(invalidIdentifier));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsKeyword_NullInput() {
 assertThrows(IllegalArgumentException.class, () -> TokenStream.isKeyword(null));
 }

 @Test
 public void testIsJSIdentifier() {
 // Basic valid identifiers
 assertEquals(true, TokenStream.isJSIdentifier("identifier"));
 assertEquals(true, TokenStream.isJSIdentifier("_identifier"));
 assertEquals(true, TokenStream.isJSIdentifier("identifier_"));

 // Valid identifiers with digits
 assertEquals(true, TokenStream.isJSIdentifier("identifier123"));
 assertEquals(true, TokenStream.isJSIdentifier("_identifier123"));
 assertEquals(true, TokenStream.isJSIdentifier("identifier_123"));

 // Valid identifiers with underscores
 assertEquals(true, TokenStream.isJSIdentifier("identifier_underscore"));
 assertEquals(true, TokenStream.isJSIdentifier("_identifier_underscore"));

 // Valid identifiers with digits and underscores
 assertEquals(true, TokenStream.isJSIdentifier("identifier_underscore123"));
 assertEquals(true, TokenStream.isJSIdentifier("_identifier_underscore123"));

 // Invalid identifiers
 assertFalse(TokenStream.isJSIdentifier("invalidIdentifier"));
 assertFalse(TokenStream.isJSIdentifier("invalidIdentifier123"));
 assertFalse(TokenStream.isJSIdentifier("invalidIdentifier_123"));
 assertFalse(TokenStream.isJSIdentifier("invalidIdentifier123_"));
 }

 @Test
 public void testIsJSIdentifier_EmptyString() {
 assertFalse(TokenStream.isJSIdentifier(""));
 }

 @Test
 public void testIsJSIdentifier_NullInput() {
 assertNull(TokenStream.isJSIdentifier(null));
 }
}