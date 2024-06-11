import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.TokenStream;

public class TokenStreamTest {
  
  @Test
  public void testIsKeyword() {
    assertTrue(TokenStream.isKeyword("if"));
    assertTrue(TokenStream.isKeyword("else"));
    assertFalse(TokenStream.isKeyword("abc"));
  }
  
  @Test
  public void testIsJSIdentifier() {
    assertTrue(TokenStream.isJSIdentifier("myVariable"));
    assertTrue(TokenStream.isJSIdentifier("_internalVariable"));
    assertFalse(TokenStream.isJSIdentifier("123variable"));
  }
  
}