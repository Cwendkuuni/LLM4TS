import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

  @Test
  public void testIsKeyword() {
    // Typical use cases
    assertTrue(TokenStream.isKeyword("if"));
    assertTrue(TokenStream.isKeyword("for"));
    assertTrue(TokenStream.isKeyword("new"));
    assertTrue(TokenStream.isKeyword("else"));
    assertTrue(TokenStream.isKeyword("class"));
    
    // Edge cases
    assertTrue(TokenStream.isKeyword("true"));
    assertTrue(TokenStream.isKeyword("false"));
    assertTrue(TokenStream.isKeyword("null"));
    
    // Error scenarios
    assertFalse(TokenStream.isKeyword("ifnotexists"));
    assertFalse(TokenStream.isKeyword(""));
    assertFalse(TokenStream.isKeyword("%%"));
  }
  
  @Test
  public void testIsJSIdentifier() {
    // Typical use cases
    assertTrue(TokenStream.isJSIdentifier("myVariable"));
    assertTrue(TokenStream.isJSIdentifier("anotherVariable"));
    assertTrue(TokenStream.isJSIdentifier("camelCase"));
    
    // Edge cases
    assertTrue(TokenStream.isJSIdentifier("a"));
    assertTrue(TokenStream.isJSIdentifier("x"));
    assertTrue(TokenStream.isJSIdentifier("_foo"));
    assertTrue(TokenStream.isJSIdentifier("___"));
    
    // Error scenarios
    assertFalse(TokenStream.isJSIdentifier(""));
    assertFalse(TokenStream.isJSIdentifier("3variable"));
    assertFalse(TokenStream.isJSIdentifier("my-variable"));
    assertFalse(TokenStream.isJSIdentifier("my variable"));
  }

}