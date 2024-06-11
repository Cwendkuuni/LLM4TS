import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Testing false cases
 assertFalse(TokenStream.isKeyword(""));
 assertFalse(TokenStream.isKeyword("abc"));
 assertFalse(TokenStream.isKeyword("123"));
 assertFalse(TokenStream.isKeyword("ABC123"));
 assertFalse(TokenStream.isKeyword("true"));
 assertFalse(TokenStream.isKeyword("false"));
 assertFalse(TokenStream.isKeyword("null"));
 assertFalse(TokenStream.isKeyword("new"));
 assertFalse(TokenStream.isKeyword("class"));
 assertFalse(TokenStream.isKeyword("break"));
 assertFalse(TokenStream.isKeyword("null"));

 // Testing true cases
 assertTrue(TokenStream.isKeyword("if"));
 assertTrue(TokenStream.isKeyword("else"));
 assertTrue(TokenStream.isKeyword("while"));
 assertTrue(TokenStream.isKeyword("for"));
 assertTrue(TokenStream.isKeyword("try"));
 assertTrue(TokenStream.isKeyword("catch"));
 assertTrue(TokenStream.isKeyword("throw"));
 assertTrue(TokenStream.isKeyword("with"));
 assertTrue(TokenStream.isKeyword("in"));
 assertTrue(TokenStream.isKeyword("var"));
 assertTrue(TokenStream.isKeyword("let"));
 assertTrue(TokenStream.isKeyword("const"));
 assertTrue(TokenStream.isKeyword("function"));
 assertTrue(TokenStream.isKeyword("return"));
 assertTrue(TokenStream.isKeyword("new"));
 assertTrue(TokenStream.isKeyword("this"));
 assertTrue(TokenStream.isKeyword("void"));
 assertTrue(TokenStream.isKeyword("int"));
 assertTrue(TokenStream.isKeyword("float"));
 assertTrue(TokenStream.isKeyword("double"));
 assertTrue(TokenStream.isKeyword("boolean"));
 assertTrue(TokenStream.isKeyword("true"));
 assertTrue(TokenStream.isKeyword("false"));
 assertTrue(TokenStream.isKeyword("null"));
 assertTrue(TokenStream.isKeyword("package"));
 assertTrue(TokenStream.isKeyword("import"));
 assertTrue(TokenStream.isKeyword("export"));
 assertTrue(TokenStream.isKeyword("public"));
 assertTrue(TokenStream.isKeyword("private"));
 assertTrue(TokenStream.isKeyword("protected"));
 assertTrue(TokenStream.isKeyword("static"));
 assertTrue(TokenStream.isKeyword("transient"));
 assertTrue(TokenStream.isKeyword("abstract"));
 assertTrue(TokenStream.isKeyword("interface"));
 assertTrue(TokenStream.isKeyword("implements"));
 assertTrue(TokenStream.isKeyword("extends"));
 assertTrue(TokenStream.isKeyword("instanceof"));
 assertTrue(TokenStream.isKeyword("new"));
 assertTrue(TokenStream.isKeyword("delete"));
 assertTrue(TokenStream.isKeyword("yield"));
 }

 @Test
 public void testIsJSIdentifier() {
 // Testing false cases
 assertFalse(TokenStream.isJSIdentifier(""));
 assertFalse(TokenStream.isJSIdentifier(" "));
 assertFalse(TokenStream.isJSIdentifier("_"));
 assertFalse(TokenStream.isJSIdentifier("123"));
 assertFalse(TokenStream.isJSIdentifier("ABC123"));
 assertFalse(TokenStream.isJSIdentifier("true"));
 assertFalse(TokenStream.isJSIdentifier("false"));
 assertFalse(TokenStream.isJSIdentifier("null"));
 assertFalse(TokenStream.isJSIdentifier("new"));

 // Testing true cases
 assertTrue(TokenStream.isJSIdentifier(""));
 assertTrue(TokenStream.isJSIdentifier("a"));
 assertTrue(TokenStream.isJSIdentifier("_"));
 assertTrue(TokenStream.isJSIdentifier("abc"));
 assertTrue(TokenStream.isJSIdentifier("ABC"));
 assertTrue(TokenStream.isJSIdentifier("123_abc"));
 assertTrue(TokenStream.isJSIdentifier("abc123"));
 assertTrue(TokenStream.isJSIdentifier("ABC123"));
 assertTrue(TokenStream.isJSIdentifier("myVar"));
 assertTrue(TokenStream.isJSIdentifier("my_var"));
 assertTrue(TokenStream.isJSIdentifier("my_long_variable_name"));
 assertTrue(TokenStream.isJSIdentifier("myVariableName"));
 }
}