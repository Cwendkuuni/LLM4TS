import org.junit.*;
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
  
  @Test(expected = IllegalStateException.class)
  public void testGetRawResponse_NullRawResponse() {
    Token token = new Token("testToken", "testSecret");
    token.getRawResponse();
  }
  
  @Test
  public void testGetRawResponse_ValidRawResponse() {
    Token token = new Token("testToken", "testSecret", "rawResponse");
    assertEquals("rawResponse", token.getRawResponse());
  }
  
  @Test
  public void testToString() {
    Token token = new Token("testToken", "testSecret");
    assertEquals("Token[testToken , testSecret]", token.toString());
  }
}