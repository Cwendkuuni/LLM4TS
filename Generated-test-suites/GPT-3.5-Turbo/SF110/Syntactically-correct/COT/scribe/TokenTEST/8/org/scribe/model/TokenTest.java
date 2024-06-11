import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
  
  @Test
  public void testConstructor1() {
    Token token = new Token("token", "secret");
    
    assertEquals("token", token.getToken());
    assertEquals("secret", token.getSecret());
    assertNull(token.getRawResponse());
    assertEquals("Token[token, secret]", token.toString());
  }
  
  @Test
  public void testConstructor2() {
    Token token = new Token("token", "secret", "response");
    
    assertEquals("token", token.getToken());
    assertEquals("secret", token.getSecret());
    assertEquals("response", token.getRawResponse());
    assertEquals("Token[token, secret]", token.toString());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testGetRawResponseException() {
    Token token = new Token("token", "secret");
    token.getRawResponse();
  }
  
}