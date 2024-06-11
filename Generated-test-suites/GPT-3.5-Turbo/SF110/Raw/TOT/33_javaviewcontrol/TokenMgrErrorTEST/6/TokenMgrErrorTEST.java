package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test message", TokenMgrError.LEXICAL_ERROR);
        assertEquals("Test message", error.getMessage());
    }

    @Test
    public void testTokenMgrError() {
        TokenMgrError error = new TokenMgrError();
        assertNotNull(error);
        assertEquals(0, error.errorCode);
    }

    @Test
    public void testTokenMgrErrorWithDetails() {
        TokenMgrError error = new TokenMgrError(false, 0, 1, 2, "error", 'c', TokenMgrError.INVALID_LEXICAL_STATE);
        assertNotNull(error);
        assertEquals("Lexical error at line 1, column 2.  Encountered: \"c\" (99), after : \"error\"", error.getMessage());
        assertEquals(TokenMgrError.INVALID_LEXICAL_STATE, error.errorCode);
    }

    @Test
    public void testLexicalErrorWithEOF() {
        String message = TokenMgrError.LexicalError(true, 2, 3, 4, "error", 'a');
        assertEquals("Lexical error at line 3, column 4.  Encountered: <EOF> after : \"error\"", message);
    }

    @Test
    public void testLexicalErrorWithoutEOF() {
        String message = TokenMgrError.LexicalError(false, 1, 5, 6, "error", 'z');
        assertEquals("Lexical error at line 5, column 6.  Encountered: \"z\" (122), after : \"error\"", message);
    }

}