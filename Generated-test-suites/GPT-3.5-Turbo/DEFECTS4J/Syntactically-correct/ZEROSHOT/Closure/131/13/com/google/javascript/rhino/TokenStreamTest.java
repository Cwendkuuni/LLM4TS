import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {
  
  @Test
  public void testIsKeywordValid() {
    assertTrue(TokenStream.isKeyword("if"));
    assertTrue(TokenStream.isKeyword("for"));
    assertTrue(TokenStream.isKeyword("new"));
    assertTrue(TokenStream.isKeyword("break"));
  }
  
  @Test
  public void testIsKeywordInvalid() {
    assertFalse(TokenStream.isKeyword("test"));
    assertFalse(TokenStream.isKeyword("number"));
    assertFalse(TokenStream.isKeyword("true"));
    assertFalse(TokenStream.isKeyword("while"));
  }
  
  @Test
  public void testIsJSIdentifierValid() {
    assertTrue(TokenStream.isJSIdentifier("test"));
    assertTrue(TokenStream.isJSIdentifier("number123"));
    assertTrue(TokenStream.isJSIdentifier("$var"));
    assertTrue(TokenStream.isJSIdentifier("_value"));
  }
  
  @Test
  public void testIsJSIdentifierInvalid() {
    assertFalse(TokenStream.isJSIdentifier("alert()"));
    assertFalse(TokenStream.isJSIdentifier("123test"));
    assertFalse(TokenStream.isJSIdentifier("#value"));
    assertFalse(TokenStream.isJSIdentifier("if"));
  }

}