import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
  
  @Test
  public void testTokenConstructor1() {
    Token token = new Token("testToken", "testSecret");
    assertEquals("testToken", token.getToken());
    assertEquals("testSecret", token.getSecret());
    assertNull(token.getRawResponse());
    assertEquals("Token[testToken , testSecret]", token.toString());
  }
  
  @Test
  public void testTokenConstructor2() {
    Token token = new Token("testToken", "testSecret", "testRawResponse");
    assertEquals("testToken", token.getToken());
    assertEquals("testSecret", token.getSecret());
    assertEquals("testRawResponse", token.getRawResponse());
    assertEquals("Token[testToken , testSecret]", token.toString());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testGetRawResponseWithoutRawResponse() {
    Token token = new Token("testToken", "testSecret");
    token.getRawResponse();
  }
  
}