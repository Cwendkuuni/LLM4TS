import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Testing positive cases
 assertEquals(true, TokenStream.isKeyword("false"));
 assertEquals(true, TokenStream.isKeyword("null"));
 assertEquals(true, TokenStream.isKeyword("break"));
 assertEquals(true, TokenStream.isKeyword("case"));
 assertEquals(true, TokenStream.isKeyword("catch"));
 assertEquals(true, TokenStream.isKeyword("class"));
 assertEquals(true, TokenStream.isKeyword("const"));
 assertEquals(true, TokenStream.isKeyword("continue"));
 assertEquals(true, TokenStream.isKeyword("default"));
 assertEquals(true, TokenStream.isKeyword("delete"));
 assertEquals(true, TokenStream.isKeyword("do"));
 assertEquals(true, TokenStream.isKeyword("else"));
 assertEquals(true, TokenStream.isKeyword("export"));
 assertEquals(true, TokenStream.isKeyword("extends"));
 assertEquals(true, TokenStream.isKeyword("false"));
 assertEquals(true, TokenStream.isKeyword("finally"));
 assertEquals(true, TokenStream.isKeyword("for"));
 assertEquals(true, TokenStream.isKeyword("function"));
 assertEquals(true, TokenStream.isKeyword("goto"));
 assertEquals(true, TokenStream.isKeyword("if"));
 assertEquals(true, TokenStream.isKeyword("import"));
 assertEquals(true, TokenStream.isKeyword("in"));
 assertEquals(true, TokenStream.isKeyword("instanceof"));
 assertEquals(true, TokenStream.isKeyword("new"));
 assertEquals(true, TokenStream.isKeyword("null"));
 assertEquals(true, TokenStream.isKeyword("package"));
 assertEquals(true, TokenStream.isKeyword("private"));
 assertEquals(true, TokenStream.isKeyword("protected"));
 assertEquals(true, TokenStream.isKeyword("return"));
 assertEquals(true, TokenStream.isKeyword("switch"));
 assertEquals(true, TokenStream.isKeyword("synchronized"));
 assertEquals(true, TokenStream.isKeyword("this"));
 assertEquals(true, TokenStream.isKeyword("throw"));
 assertEquals(true, TokenStream.isKeyword("transient"));
 assertEquals(true, TokenStream.isKeyword("true"));
 assertEquals(true, TokenStream.isKeyword("try"));
 assertEquals(true, TokenStream.isKeyword("typeof"));
 assertEquals(true, TokenStream.isKeyword("var"));
 assertEquals(true, TokenStream.isKeyword("void"));
 assertEquals(true, TokenStream.isKeyword("while"));
 assertEquals(true, TokenStream.isKeyword("with"));

 // Testing negative cases
 assertEquals(false, TokenStream.isKeyword("test"));
 assertEquals(false, TokenStream.isKeyword("123"));
 assertEquals(false, TokenStream.isKeyword("_abc"));
 assertEquals(false, TokenStream.isKeyword("abcd1234"));
 }

 @Test
 public void testIsJSIdentifier() {
 // Testing positive cases
 assertEquals(true, TokenStream.isJSIdentifier("identifier"));
 assertEquals(true, TokenStream.isJSIdentifier("_identifier"));
 assertEquals(true, TokenStream.isJSIdentifier("identifier123"));
assertEquals(true, TokenStream.isJSIdentifier("identifier_123"));
 assertEquals(true, TokenStream.isJSIdentifier("identifier123_"));
 assertEquals(true, TokenStream.isJSIdentifier("_identifier123"));

 // Testing negative cases
 assertEquals(false, TokenStream.isJSIdentifier("123"));
 assertEquals(false, TokenStream.isJSIdentifier("identifier123"));
 assertEquals(false, TokenStream.isJSIdentifier("identifier_-identifier"));
 assertEquals(false, TokenStream.isJSIdentifier("identifier-identifier"));
 }
}