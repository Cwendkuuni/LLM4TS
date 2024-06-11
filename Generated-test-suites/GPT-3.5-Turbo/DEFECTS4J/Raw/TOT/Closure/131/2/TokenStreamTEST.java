import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {
  
  @Test
  public void testIsKeyword() {
    assertTrue(TokenStream.isKeyword("if")); // typical use case
    assertTrue(TokenStream.isKeyword("int")); // typical use case
    assertTrue(TokenStream.isKeyword("null")); // typical use case
    assertFalse(TokenStream.isKeyword("abc")); // typical use case (not a keyword)
    assertFalse(TokenStream.isKeyword("123")); // edge case (not a keyword)
  }
  
  @Test
  public void testIsJSIdentifier() {
    assertTrue(TokenStream.isJSIdentifier("abc")); // typical use case
    assertTrue(TokenStream.isJSIdentifier("_def")); // typical use case (valid identifier starting with underscore)
    assertFalse(TokenStream.isJSIdentifier("1abc")); // edge case (invalid identifier starting with number)
    assertFalse(TokenStream.isJSIdentifier("@abc")); // error scenario (invalid identifier starting with special character)
  }
}