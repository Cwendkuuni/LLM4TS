import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Testing positive cases
 assertEquals(true, TokenStream.isKeyword("if"));
 assertEquals(true, TokenStream.isKeyword("else"));
 assertEquals(true, TokenStream.isKeyword("for"));
 assertEquals(true, TokenStream.isKeyword("while"));
 assertEquals(true, TokenStream.isKeyword("switch"));
 assertEquals(true, TokenStream.isKeyword("case"));
 assertEquals(true, TokenStream.isKeyword("default"));
 assertEquals(true, TokenStream.isKeyword("break"));
 assertEquals(true, TokenStream.isKeyword("continue"));
 assertEquals(true, TokenStream.isKeyword("return"));
 assertEquals(true, TokenStream.isKeyword("throw"));
 assertEquals(true, TokenStream.isKeyword("try"));
 assertEquals(true, TokenStream.isKeyword("catch"));
 assertEquals(true, TokenStream.isKeyword("finally"));
 assertEquals(true, TokenStream.isKeyword("throw"));
 assertEquals(true, TokenStream.isKeyword("void"));
 assertEquals(true, TokenStream.isKeyword("null"));
 assertEquals(true, TokenStream.isKeyword("true"));
 assertEquals(true, TokenStream.isKeyword("false"));
 assertEquals(true, TokenStream.isKeyword("this"));
 assertEquals(true, TokenStream.isKeyword("new"));
 assertEquals(true, TokenStream.isKeyword("class"));
 assertEquals(true, TokenStream.isKeyword("function"));
 assertEquals(true, TokenStream.isKeyword("break"));
 assertEquals(true, TokenStream.isKeyword("package"));
 assertEquals(true, TokenStream.isKeyword("import"));
 assertEquals(true, TokenStream.isKeyword("export"));

 // Testing negative cases
 assertEquals(false, TokenStream.isKeyword("test"));
 assertEquals(false, TokenStream.isKeyword("123"));
 assertEquals(false, TokenStream.isKeyword("ABC"));
 }

 @Test
 public void testIsJSIdentifier() {
 // Testing positive cases
 assertEquals(true, TokenStream.isJSIdentifier("test"));
 assertEquals(true, TokenStream.isJSIdentifier("_test"));
 assertEquals(true, TokenStream.isJSIdentifier("123_test"));
 assertEquals(true, TokenStream.isJSIdentifier("test_123"));
 assertEquals(true, TokenStream.isJSIdentifier("test123"));
 assertEquals(true, TokenStream.isJSIdentifier("_test123"));
 assertEquals(true, TokenStream.isJSIdentifier("test_ABC"));
 assertEquals(true, TokenStream.isJSIdentifier("ABC_test"));
 assertEquals(true, TokenStream.isJSIdentifier("ABCtest"));

 // Testing negative cases
 assertEquals(false, TokenStream.isJSIdentifier("test "));
 assertEquals(false, TokenStream.isJSIdentifier("test\t"));
 assertEquals(false, TokenStream.isJSIdentifier("test\n"));
 assertEquals(false, TokenStream.isJSIdentifier("test\r"));
 assertEquals(false, TokenStream.isJSIdentifier("test!"));
 assertEquals(false, TokenStream.isJSIdentifier("test@"));
 }
}