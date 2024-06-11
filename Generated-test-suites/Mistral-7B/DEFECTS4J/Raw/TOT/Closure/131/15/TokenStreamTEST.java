import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenStreamTest {

 @Test
 public void testIsKeyword() {
 // Test true cases
 assertEquals(true, TokenStream.isKeyword("break"));
 assertEquals(true, TokenStream.isKeyword("class"));
 assertEquals(true, TokenStream.isKeyword("const"));
 assertEquals(true, TokenStream.isKeyword("continue"));
 assertEquals(true, TokenStream.isKeyword("default"));
 assertEquals(true, TokenStream.isKeyword("delete"));
 assertEquals(true, TokenStream.isKeyword("do"));
 assertEquals(true, TokenStream.isKeyword("else"));
 assertEquals(true, TokenStream.isKeyword("export"));
 // Test false cases
 assertEquals(false, TokenStream.isKeyword("test"));
 assertEquals(false, TokenStream.isKeyword("HelloWorld"));
 }

 @Test
 public void testIsJSIdentifier() {
 // Test true cases
 assertEquals(true, TokenStream.isJSIdentifier("identifier"));
 assertEquals(true, TokenStream.isJSIdentifier("_identifier"));
 // Test false cases
 assertEquals(false, TokenStream.isJSIdentifier("123"));
 assertEquals(false, TokenStream.isJSIdentifier("test-123"));
 assertEquals(false, TokenStream.isJSIdentifier("test-_123"));
 assertEquals(false, TokenStream.isJSIdentifier("test-123-"));
 }
}