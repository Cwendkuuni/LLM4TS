package com.pmdesigns.jvc.tools;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        assertEquals("", TokenMgrError.addEscapes(""));
        assertEquals("\\b", TokenMgrError.addEscapes("\b"));
        assertEquals("\\t", TokenMgrError.addEscapes("\t"));
        assertEquals("\\n", TokenMgrError.addEscapes("\n"));
        assertEquals("\\f", TokenMgrError.addEscapes("\f"));
        assertEquals("\\r", TokenMgrError.addEscapes("\r"));
        assertEquals("\\\"", TokenMgrError.addEscapes("\""));
        assertEquals("\\'", TokenMgrError.addEscapes("'"));
        assertEquals("\\\\", TokenMgrError.addEscapes("\\"));
        assertEquals("\\u00e9", TokenMgrError.addEscapes("é"));
    }

    @Test
    public void testLexicalError() {
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"bc\"", TokenMgrError.LexicalError(false, 0, 1, 1, "bc", 'a'));
        assertEquals("Lexical error at line 5, column 3.  Encountered: <EOF> , after : \"def\"", TokenMgrError.LexicalError(true, 0, 5, 3, "def", '\0'));
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test message", TokenMgrError.LEXICAL_ERROR);
        assertEquals("Test message", error.getMessage());
    }

    @Test
    public void testTokenMgrError() {
        TokenMgrError error = new TokenMgrError();
        assertNotNull(error);
    }

    @Test
    public void testTokenMgrErrorWithReason() {
        TokenMgrError error = new TokenMgrError("Test message", TokenMgrError.STATIC_LEXER_ERROR);
        assertEquals("Test message", error.getMessage());
        assertEquals(TokenMgrError.STATIC_LEXER_ERROR, error.errorCode);
    }

    @Test
    public void testTokenMgrErrorWithDetails() {
        TokenMgrError error = new TokenMgrError(false, 0, 3, 2, "xyz", 'a', TokenMgrError.INVALID_LEXICAL_STATE);
        assertEquals("Lexical error at line 3, column 2.  Encountered: \"a\" (97), after : \"xyz\"", error.getMessage());
        assertEquals(TokenMgrError.INVALID_LEXICAL_STATE, error.errorCode);
    }
}