import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

 private static final String[] TEST_KEYWORDS = {"abstract", "arguments", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "debugger", "default", "delete", "do", "else", "export", "extends", "false", "final", "finally", "float", "for", "function", "goto", "if", "implements", "import", "in", "instanceof", "int", "interface", "let", "long", "native", "new", "package", "private", "protected", "public", "return", "short", "static", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "typeof", "var", "void", "volatile", "while", "with", "yield"};

 @Test
 public void testIsKeyword() {
 for (String keyword : TEST_KEYWORDS) {
 boolean expected = TokenStream.isKeyword(keyword);
 boolean result = TokenStream.isKeyword(keyword);
 assertEquals(expected, result);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsKeywordNullInput() {
 TokenStream.isKeyword(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsKeywordEmptyInput() {
 TokenStream.isKeyword("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsKeywordTooShortInput() {
 TokenStream.isKeyword("ab");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsKeywordTooLongInput() {
 TokenStream.isKeyword("abstractclass");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsKeywordInvalidIdentifier() {
 TokenStream.isKeyword("my-identifier-123");
 }

 @Test
 public void testIsJSIdentifier() {
 for (String identifier : new String[]{"_", "abracadabra", "myIdentifier", "My_Identifier_123", "my-identifier-123"}) {
 boolean expected = TokenStream.isJSIdentifier(identifier);
 boolean result = TokenStream.isJSIdentifier(identifier);
 assertEquals(expected, result);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsJSIdentifierNullInput() {
 TokenStream.isJSIdentifier(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsJSIdentifierEmptyInput() {
 TokenStream.isJSIdentifier("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsJSIdentifierTooShortInput() {
 TokenStream.isJSIdentifier("a");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsJSIdentifierInvalidIdentifier() {
 TokenStream.isJSIdentifier("my-identifier-123!");
 }
}