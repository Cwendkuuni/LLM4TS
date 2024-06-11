import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
  
  @Test
  public void testGetToken() {
    Token token = new Token("testToken", "testSecret");
    assertEquals("testToken", token.getToken());
  }
  
  @Test
  public void testGetSecret() {
    Token token = new Token("testToken", "testSecret");
    assertEquals("testSecret", token.getSecret());
  }
  
  @Test
  public void testGetRawResponse() {
    Token token = new Token("testToken", "testSecret", "testRawResponse");
    assertEquals("testRawResponse", token.getRawResponse());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testGetRawResponseWithoutRawResponse() {
    Token token = new Token("testToken", "testSecret");
    token.getRawResponse();
  }
  
  @Test
  public void testToString() {
    Token token = new Token("testToken", "testSecret");
    assertEquals("Token[testToken , testSecret]", token.toString());
  }
  
  // Edge case - token and secret are empty strings
  @Test
  public void testEmptyTokenAndSecret() {
    Token token = new Token("", "");
    assertEquals("", token.getToken());
    assertEquals("", token.getSecret());
  }
  
  // Edge case - token and secret contain special characters
  @Test
  public void testTokenAndSecretWithSpecialCharacters() {
    Token token = new Token("!@#$%^&*()", "!@#$%^&*()");
    assertEquals("!@#$%^&*()", token.getToken());
    assertEquals("!@#$%^&*()", token.getSecret());
  }
  
  // Error scenario - rawResponse is null for a constructed token
  @Test(expected = IllegalStateException.class)
  public void testGetRawResponseWithNullRawResponse() {
    Token token = new Token("testToken", "testSecret", null);
    token.getRawResponse();
  }
  
  // Error scenario - rawResponse is empty for a constructed token
  @Test(expected = IllegalStateException.class)
  public void testGetRawResponseWithEmptyRawResponse() {
    Token token = new Token("testToken", "testSecret", "");
    token.getRawResponse();
  }
}