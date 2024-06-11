import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TokenStreamTest {

  @Test
  public void testIsKeyword_False() {
    assertFalse(TokenStream.isKeyword("InvalidKeyword"));
  }
  
  @Test
  public void testIsKeyword_True() {
    assertTrue(TokenStream.isKeyword("class"));
  }
  
  @Test
  public void testIsJSIdentifier_True() {
    assertTrue(TokenStream.isJSIdentifier("validIdentifier"));
  }
  
  @Test
  public void testIsJSIdentifier_InvalidStartCharacter() {
    assertFalse(TokenStream.isJSIdentifier("12invalidIdentifier"));
  }
  
  @Test
  public void testIsJSIdentifier_InvalidCharacter() {
    assertFalse(TokenStream.isJSIdentifier("validIdentifier!"));
  }
  
  @Test
  public void testIsJSIdentifier_EmptyString() {
    assertFalse(TokenStream.isJSIdentifier(""));
  }
}