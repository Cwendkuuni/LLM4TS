import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

  @Test
  public void testIsKeyword() {
    assertTrue(TokenStream.isKeyword("if"));
    assertTrue(TokenStream.isKeyword("new"));
    assertTrue(TokenStream.isKeyword("null"));
    assertFalse(TokenStream.isKeyword("abc"));
  }

  @Test
  public void testIsJSIdentifier() {
    assertTrue(TokenStream.isJSIdentifier("validIdentifier"));
    assertFalse(TokenStream.isJSIdentifier("12invalid"));
    assertFalse(TokenStream.isJSIdentifier(""));
  }

  @Test
  public void testIsKeywordEdgeCases() {
    assertTrue(TokenStream.isKeyword("long"));
    assertTrue(TokenStream.isKeyword("byte"));
    assertTrue(TokenStream.isKeyword("synchronized"));
  }

  @Test
  public void testIsJSIdentifierEdgeCases() {
    assertTrue(TokenStream.isJSIdentifier("underscore_valid"));
    assertTrue(TokenStream.isJSIdentifier("$valid"));
    assertFalse(TokenStream.isJSIdentifier("in.valid"));
  }

  @Test
  public void testIsKeywordExceptions() {
    assertTrue(TokenStream.isKeyword("public"));
    assertTrue(TokenStream.isKeyword("private"));
    assertTrue(TokenStream.isKeyword("static"));
  }

  @Test
  public void testIsJSIdentifierExceptions() {
    assertTrue(TokenStream.isJSIdentifier("notReservedWord"));
    assertFalse(TokenStream.isJSIdentifier(""));
    assertFalse(TokenStream.isJSIdentifier("null"));
  }
}