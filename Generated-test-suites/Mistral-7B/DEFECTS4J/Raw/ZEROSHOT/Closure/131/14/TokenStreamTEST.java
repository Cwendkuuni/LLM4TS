import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Testing positive cases
 assertTrue(TokenStream.isKeyword("if"));
 assertTrue(TokenStream.isKeyword("else"));
 assertTrue(TokenStream.isKeyword("while"));
 assertTrue(TokenStream.isKeyword("for"));
 assertTrue(TokenStream.isKeyword("switch"));
 assertTrue(TokenStream.isKeyword("case"));
 assertTrue(TokenStream.isKeyword("default"));
 assertTrue(TokenStream.isKeyword("break"));
 assertTrue(TokenStream.isKeyword("continue"));
 assertTrue(TokenStream.isKeyword("return"));
 assertTrue(TokenStream.isKeyword("void"));
 assertTrue(TokenStream.isKeyword("true"));
 assertTrue(TokenStream.isKeyword("false"));
 assertTrue(TokenStream.isKeyword("null"));
 assertTrue(TokenStream.isKeyword("this"));
 assertTrue(TokenStream.isKeyword("super"));
 assertTrue(TokenStream.isKeyword("class"));
 assertTrue(TokenStream.isKeyword("function"));
 assertTrue(TokenStream.isKeyword("export"));
 assertTrue(TokenStream.isKeyword("import"));
 assertTrue(TokenStream.isKeyword("export default"));
 assertTrue(TokenStream.isKeyword("package"));
 assertTrue(TokenStream.isKeyword("public"));
 assertTrue(TokenStream.isKeyword("static"));
 assertTrue(TokenStream.isKeyword("const"));
 assertTrue(TokenStream.isKeyword("let"));
 assertTrue(TokenStream.isKeyword("const"));
 assertTrue(TokenStream.isKeyword("interface"));
 assertTrue(TokenStream.isKeyword("implements"));
 assertTrue(TokenStream.isKeyword("extends"));

 // Testing negative cases
 assertFalse(TokenStream.isKeyword(""));
 assertFalse(TokenStream.isKeyword("1"));
 assertFalse(TokenStream.isKeyword("!"));
 assertFalse(TokenStream.isKeyword("@"));
 assertFalse(TokenStream.isKeyword(" "));
 assertFalse(TokenStream.isKeyword("abc"));
 assertFalse(TokenStream.isKeyword("ABC"));
 assertFalse(TokenStream.isKeyword("ABC123"));
 assertFalse(TokenStream.isKeyword("123ABC"));
 }

 @Test
 public void testIsJSIdentifier() {
 // Testing positive cases
 assertTrue(TokenStream.isJSIdentifier(""));
 assertTrue(TokenStream.isJSIdentifier("foo"));
 assertTrue(TokenStream.isJSIdentifier("_foo"));
 assertTrue(TokenStream.isJSIdentifier("fooBar"));
 assertTrue(TokenStream.isJSIdentifier("_fooBar"));
 assertTrue(TokenStream.isJSIdentifier("foo_bar"));
 assertTrue(TokenStream.isJSIdentifier("fooBar_"));
 assertTrue(TokenStream.isJSIdentifier("foo_bar_"));

 // Testing negative cases
 assertFalse(TokenStream.isJSIdentifier("1"));
 assertFalse(TokenStream.isJSIdentifier("!foo"));
 assertFalse(TokenStream.isJSIdentifier("foo!"));
 assertFalse(TokenStream.isJSIdentifier("foo-bar"));
 assertFalse(TokenStream.isJSIdentifier("foo Bar"));
 assertFalse(TokenStream.isJSIdentifier("foo bar"));
 }
}