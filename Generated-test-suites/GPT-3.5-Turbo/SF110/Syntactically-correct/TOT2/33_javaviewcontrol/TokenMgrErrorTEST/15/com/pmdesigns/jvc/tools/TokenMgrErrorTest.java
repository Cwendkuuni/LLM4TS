package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

  @Test
  public void testAddEscapes() {
    assertEquals("\\b", TokenMgrError.addEscapes("\b"));
    assertEquals("\\t", TokenMgrError.addEscapes("\t"));
    assertEquals("\\n", TokenMgrError.addEscapes("\n"));
    assertEquals("\\f", TokenMgrError.addEscapes("\f"));
    assertEquals("\\r", TokenMgrError.addEscapes("\r"));
    assertEquals("\\\"", TokenMgrError.addEscapes("\""));
    assertEquals("\\'", TokenMgrError.addEscapes("'"));
    assertEquals("\\\\", TokenMgrError.addEscapes("\\"));
  }

  @Test
  public void testLexicalError() {
    assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF> after : \"test\"", TokenMgrError.LexicalError(true, 0, 1, 1, "test", '\0'));
    assertEquals("Lexical error at line 1, column 2.  Encountered: \"a\" (97), after : \"test\"", TokenMgrError.LexicalError(false, 0, 1, 2, "test", 'a'));
  }

  @Test
  public void testGetMessage() {
    TokenMgrError tokenMgrError = new TokenMgrError("Test Message", 0);
    assertEquals("Test Message", tokenMgrError.getMessage());
  }

  @Test
  public void testTokenMgrError() {
    TokenMgrError tokenMgrError = new TokenMgrError();
    assertNotNull(tokenMgrError);
  }

  @Test
  public void testTokenMgrErrorWithReason() {
    TokenMgrError tokenMgrError = new TokenMgrError("Test Message", 1);
    assertEquals(1, tokenMgrError.errorCode);
  }

  @Test
  public void testTokenMgrErrorWithParams() {
    TokenMgrError tokenMgrError = new TokenMgrError(false, 0, 1, 1, "test", 'a', 2);
    assertEquals("Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"test\"", tokenMgrError.getMessage());
    assertEquals(2, tokenMgrError.errorCode);
  }
}