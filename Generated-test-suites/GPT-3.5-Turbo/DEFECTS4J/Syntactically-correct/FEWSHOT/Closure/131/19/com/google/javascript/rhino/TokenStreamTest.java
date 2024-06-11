import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenStreamTest {
  
  @Test
  public void testIsKeyword() {
    assertTrue(TokenStream.isKeyword("if"));
    assertTrue(TokenStream.isKeyword("else"));
    assertTrue(TokenStream.isKeyword("while"));
    assertFalse(TokenStream.isKeyword("test"));
    assertFalse(TokenStream.isKeyword("abc"));
  }
  
  @Test
  public void testIsJSIdentifier() {
    assertTrue(TokenStream.isJSIdentifier("test"));
    assertTrue(TokenStream.isJSIdentifier("abc"));
    assertTrue(TokenStream.isJSIdentifier("def123"));
    assertFalse(TokenStream.isJSIdentifier("1test"));
    assertFalse(TokenStream.isJSIdentifier("@abc"));
  }

}