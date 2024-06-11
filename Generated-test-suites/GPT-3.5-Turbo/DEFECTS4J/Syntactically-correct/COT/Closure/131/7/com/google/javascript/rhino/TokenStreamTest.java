
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {
  
  @Test
  public void testIsKeyword() {
    // Test basic keywords
    assertTrue(TokenStream.isKeyword("if"));
    assertTrue(TokenStream.isKeyword("for"));
    assertTrue(TokenStream.isKeyword("do"));
    
    // Test partial keyword match
    assertFalse(TokenStream.isKeyword("i"));
    assertFalse(TokenStream.isKeyword("f"));
    assertFalse(TokenStream.isKeyword("fo"));
    
    // Test invalid keywords
    assertFalse(TokenStream.isKeyword("abc"));
    assertFalse(TokenStream.isKeyword("123"));
  }
  
  @Test
  public void testIsJSIdentifier() {
    // Test valid identifiers
    assertTrue(TokenStream.isJSIdentifier("hello"));
    assertTrue(TokenStream.isJSIdentifier("_abc"));
    assertTrue(TokenStream.isJSIdentifier("$var"));
    assertTrue(TokenStream.isJSIdentifier("abc123"));
    
    // Test invalid identifiers
    assertFalse(TokenStream.isJSIdentifier("123abc"));
    assertFalse(TokenStream.isJSIdentifier("a*b"));
    assertFalse(TokenStream.isJSIdentifier("va r"));
  }
}
