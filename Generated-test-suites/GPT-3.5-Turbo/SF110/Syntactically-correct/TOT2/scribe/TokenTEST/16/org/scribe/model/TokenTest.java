package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
  
  @Test
  public void testTokenConstructor() {
    Token token = new Token("myToken", "mySecret");
    
    assertEquals("myToken", token.getToken());
    assertEquals("mySecret", token.getSecret());
    assertNull(token.getRawResponse());
  }

  @Test
  public void testTokenConstructorWithRawResponse() {
    Token token = new Token("myToken", "mySecret", "rawResponse");
    
    assertEquals("myToken", token.getToken());
    assertEquals("mySecret", token.getSecret());
    assertEquals("rawResponse", token.getRawResponse());
  }

  @Test
  public void testGetRawResponseThrowsException() {
    Token token = new Token("myToken", "mySecret");
    
    try {
      token.getRawResponse();
      fail("Expected IllegalStateException to be thrown");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testToString() {
    Token token = new Token("myToken", "mySecret");
    
    assertEquals("Token[myToken , mySecret]", token.toString());
  }
}