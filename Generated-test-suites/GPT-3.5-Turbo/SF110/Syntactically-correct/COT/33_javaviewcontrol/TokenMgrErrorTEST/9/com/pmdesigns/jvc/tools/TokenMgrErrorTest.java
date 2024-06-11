
package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TokenMgrErrorTest {

  @Test
  public void testGetMessage() {
    TokenMgrError error = new TokenMgrError("Test Message", TokenMgrError.LEXICAL_ERROR);
    assertEquals("Test Message", error.getMessage());
  }

  @Test
  public void testTokenMgrError() {
    TokenMgrError error = new TokenMgrError();
    assertEquals(null, error.getMessage());
    assertEquals(0, error.errorCode);
  }

  @Test
  public void testTokenMgrErrorWithMessageAndReason() {
    TokenMgrError error = new TokenMgrError("Test Message", TokenMgrError.STATIC_LEXER_ERROR);
    assertEquals("Test Message", error.getMessage());
    assertEquals(TokenMgrError.STATIC_LEXER_ERROR, error.errorCode);
  }

  @Test
  public void testTokenMgrErrorWithLexicalError() {
    TokenMgrError error = new TokenMgrError(true, 1, 2, 3, "Test Error", 'a', TokenMgrError.INVALID_LEXICAL_STATE);
    assertEquals("Lexical error at line 2, column 3.  Encountered: \"a\" (97), after : \"Test Error\"", error.getMessage());
    assertEquals(TokenMgrError.INVALID_LEXICAL_STATE, error.errorCode);
  }
}
