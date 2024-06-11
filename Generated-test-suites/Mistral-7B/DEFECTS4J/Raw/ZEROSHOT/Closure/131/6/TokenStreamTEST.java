import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Testing true cases
 assertTrue(TokenStream.isKeyword("if"));
 assertTrue(TokenStream.isKeyword("else"));
 assertTrue(TokenStream.isKeyword("else if"));
 assertTrue(TokenStream.isKeyword("for"));
 assertTrue(TokenStream.isKeyword("while"));
 assertTrue(TokenStream.isKeyword("try"));
 assertTrue(TokenStream.isKeyword("catch"));
 assertTrue(TokenStream.isKeyword("throw"));
 assertTrue(TokenStream.isKeyword("function"));
 assertTrue(TokenStream.isKeyword("var"));
 assertTrue(TokenStream.isKeyword("return"));
 assertTrue(TokenStream.isKeyword("const"));
 assertTrue(TokenStream.isKeyword("let"));
 assertTrue(TokenStream.isKeyword("class"));
 assertTrue(TokenStream.isKeyword("export"));
 assertTrue(TokenStream.isKeyword("import"));
 assertTrue(TokenStream.isKeyword("package"));
 assertTrue(TokenStream.isKeyword("interface"));
 assertTrue(TokenStream.isKeyword("extends"));
 assertTrue(TokenStream.isKeyword("implements"));
 assertTrue(TokenStream.isKeyword("new"));
 assertTrue(TokenStream.isKeyword("this"));
 assertTrue(TokenStream.isKeyword("super"));
 assertTrue(TokenStream.isKeyword("yield"));

 // Testing false cases (non-keywords)
 assertFalse(TokenStream.isKeyword(""));
 assertFalse(TokenStream.isKeyword("test"));
 assertFalse(TokenStream.isKeyword("HelloWorld"));
 assertFalse(TokenStream.isKeyword("123"));
 assertFalse(TokenStream.isKeyword("MyClass"));
 assertFalse(TokenStream.isKeyword("MyFunction"));
 assertFalse(TokenStream.isKeyword("myVariable"));
 }

 @Test
 public void testIsJSIdentifier() {
 // Testing true cases
 assertTrue(TokenStream.isJSIdentifier(""));
 assertTrue(TokenStream.isJSIdentifier("_"));
 assertTrue(TokenStream.isJSIdentifier("abc"));
 assertTrue(TokenStream.isJSIdentifier("ABC"));
 assertTrue(TokenStream.isJSIdentifier("123"));
 assertTrue(TokenStream.isJSIdentifier("myVariable"));
 assertTrue(TokenStream.isJSIdentifier("my_Variable"));
 assertTrue(TokenStream.isJSIdentifier("MyVariable"));
 assertTrue(TokenStream.isJSIdentifier("my_long_identifier"));
 assertTrue(TokenStream.isJSIdentifier("myLongIdentifier"));

 // Testing false cases
 assertFalse(TokenStream.isJSIdentifier(" "));
 assertFalse(TokenStream.isJSIdentifier("abc "));
 assertFalse(TokenStream.isJSIdentifier("abc-"));
 assertFalse(TokenStream.isJSIdentifier("-abc"));
 assertFalse(TokenStream.isJSIdentifier("abc "));
 assertFalse(TokenStream.isJSIdentifier("myVariable="));
 assertFalse(TokenStream.isJSIdentifier("myVariable()"));
 assertFalse(TokenStream.isJSIdentifier("myVariable[ ]"));
 assertFalse(TokenStream.isJSIdentifier("myVariable{ }"));
 }
}