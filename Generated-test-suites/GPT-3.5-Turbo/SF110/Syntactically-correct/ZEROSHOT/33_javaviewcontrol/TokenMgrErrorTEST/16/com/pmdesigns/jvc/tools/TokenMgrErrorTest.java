package com.pmdesigns.jvc.tools;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {
  
  private TokenMgrError tokenMgrError;
  
  @Before
  public void setUp() {
    tokenMgrError = new TokenMgrError();
  }
  
  @Test
  public void testGetMessage() {
    String message = tokenMgrError.getMessage();
    assertEquals(null, message);
  }
  
  @Test
  public void testTokenMgrError() {
    TokenMgrError error = new TokenMgrError();
    assertEquals(null, error.getMessage());
  }
  
  @Test
  public void testTokenMgrErrorWithMessageAndReason() {
    TokenMgrError error = new TokenMgrError("Test message", 1);
    assertEquals("Test message", error.getMessage());
    assertEquals(1, error.errorCode);
  }
  
  @Test
  public void testTokenMgrErrorWithLexicalError() {
    TokenMgrError error = new TokenMgrError(true, 0, 1, 1, "Error", 'A', 0);
    assertEquals("Lexical error at line 1, column 1. Encountered: \"A\" (65), after : \"Error\"", error.getMessage());
    assertEquals(0, error.errorCode);
  }
}