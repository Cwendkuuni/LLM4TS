import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Testing true cases
 assertTrue(TokenStream.isKeyword("abstract"));
 assertTrue(TokenStream.isKeyword("as"));
 assertTrue(TokenStream.isKeyword("boolean"));
 assertTrue(TokenStream.isKeyword("break"));
 assertTrue(TokenStream.isKeyword("byte"));
 assertTrue(TokenStream.isKeyword("case"));
 assertTrue(TokenStream.isKeyword("catch"));
 assertTrue(TokenStream.isKeyword("class"));
 assertTrue(TokenStream.isKeyword("const"));
 assertTrue(TokenStream.isKeyword("continue"));
 assertTrue(TokenStream.isKeyword("default"));
 assertTrue(TokenStream.isKeyword("delete"));
 assertTrue(TokenStream.isKeyword("do"));
 assertTrue(TokenStream.isKeyword("double"));
 assertTrue(TokenStream.isKeyword("else"));
 assertTrue(TokenStream.isKeyword("export"));
 assertTrue(TokenStream.isKeyword("extends"));
 assertTrue(TokenStream.isKeyword("false"));
 assertTrue(TokenStream.isKeyword("final"));
 assertTrue(TokenStream.isKeyword("finally"));
 assertTrue(TokenStream.isKeyword("float"));
 assertTrue(TokenStream.isKeyword("for"));
 assertTrue(TokenStream.isKeyword("function"));
 assertTrue(TokenStream.isKeyword("goto"));
 assertTrue(TokenStream.isKeyword("if"));
 assertTrue(TokenStream.isKeyword("import"));
 assertTrue(TokenStream.isKeyword("in"));
 assertTrue(TokenStream.isKeyword("instanceof"));
 assertTrue(TokenStream.isKeyword("int"));
 assertTrue(TokenStream.isKeyword("interface"));
 assertTrue(TokenStream.isKeyword("let"));
 assertTrue(TokenStream.isKeyword("long"));
 assertTrue(TokenStream.isKeyword("native"));
 assertTrue(TokenStream.isKeyword("new"));
 assertTrue(TokenStream.isKeyword("null"));
 assertTrue(TokenStream.isKeyword("package"));
 assertTrue(TokenStream.isKeyword("private"));
 assertTrue(TokenStream.isKeyword("protected"));
 assertTrue(TokenStream.isKeyword("public"));
 assertTrue(TokenStream.isKeyword("return"));
 assertTrue(TokenStream.isKeyword("short"));
 assertTrue(TokenStream.isKeyword("static"));
 assertTrue(TokenStream.isKeyword("super"));
 assertTrue(TokenStream.isKeyword("switch"));
 assertTrue(TokenStream.isKeyword("synchronized"));
 assertTrue(TokenStream.isKeyword("this"));
 assertTrue(TokenStream.isKeyword("throw"));
 assertTrue(TokenStream.isKeyword("transient"));
 assertTrue(TokenStream.isKeyword("true"));
 assertTrue(TokenStream.isKeyword("try"));
 assertTrue(TokenStream.isKeyword("typeof"));
 assertTrue(TokenStream.isKeyword("var"));
 assertTrue(TokenStream.isKeyword("void"));
 assertTrue(TokenStream.isKeyword("volatile"));
 assertTrue(TokenStream.isKeyword("while"));
 assertTrue(TokenStream.isKeyword("with"));

 // Testing false cases (non-keywords)
 assertFalse(TokenStream.isKeyword(""));
 assertFalse(TokenStream.isKeyword("123"));
 assertFalse(TokenStream.isKeyword("abcd"));
 assertFalse(TokenStream.isKeyword("ABCDef"));
 assertFalse(TokenStream.isKeyword("__invalidIdentifier__"));
 assertFalse(TokenStream.isKeyword("myVar"));
 assertFalse(TokenStream.isKeyword("myFunction"));
 }

 @Test
 public void testIsJSIdentifier() {
 // Testing true cases
 assertTrue(TokenStream.isJSIdentifier(""));
 assertTrue(TokenStream.isJSIdentifier("_"));
 assertTrue(TokenStream.isJSIdentifier("abc"));
 assertTrue(TokenStream.isJSIdentifier("AbC"));
 assertTrue(TokenStream.isJSIdentifier("123"));
 assertTrue(TokenStream.isJSIdentifier("AbC123"));
 assertTrue(TokenStream.isJSIdentifier("abC123"));
 assertTrue(TokenStream.isJSIdentifier("myIdentifier"));
 assertTrue(TokenStream.isJSIdentifier("myIdentifier123"));
 assertTrue(TokenStream.isJSIdentifier("myIdentifier_123"));
 assertTrue(TokenStream.isJSIdentifier("myIdentifier123_"));

 // Testing false cases
 assertFalse(TokenStream.isJSIdentifier("123abc"));
 assertFalse(TokenStream.isJSIdentifier("123_"));
 assertFalse(TokenStream.isJSIdentifier("_123"));
 assertFalse(TokenStream.isJSIdentifier("__invalidIdentifier__"));
 }
}