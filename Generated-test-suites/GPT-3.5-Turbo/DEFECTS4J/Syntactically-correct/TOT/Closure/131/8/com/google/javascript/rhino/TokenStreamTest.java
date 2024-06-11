import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {
  
  @Test
  public void testIsKeyword() {
    // Typical use case
    assertTrue(TokenStream.isKeyword("null"));
    
    // Edge case
    assertTrue(TokenStream.isKeyword("class"));
    
    // Error scenario
    assertFalse(TokenStream.isKeyword("notakeyword"));
  }
  
  @Test
  public void testIsJSIdentifier() {
    // Typical use case
    assertTrue(TokenStream.isJSIdentifier("identifier123"));
    
    // Edge case
    assertTrue(TokenStream.isJSIdentifier("a"));
    
    // Error scenario
    assertFalse(TokenStream.isJSIdentifier("@#$"));
  }
  
  // Additional test cases for each method can be added here
  
}