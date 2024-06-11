package com.pmdesigns.jvc.tools;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        assertEquals("\\b\\t\\n\\f\\r\\\"\\'\\\\", TokenMgrError.addEscapes("\b\t\n\f\r\"\'\\"));
        assertEquals("\\u0020", TokenMgrError.addEscapes("\u0020"));
    }

    @Test
    public void testLexicalError() {
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"hello\"", TokenMgrError.LexicalError(false, 0, 1, 1, "hello", 'a'));
    }

    @Test
    public void testGetMessage() {
        TokenMgrError tokenMgrError = new TokenMgrError("Error message", 1);
        assertEquals("Error message", tokenMgrError.getMessage());
    }

    @Test
    public void testTokenMgrError() {
        TokenMgrError tokenMgrError = new TokenMgrError();
        assertNotNull(tokenMgrError);
    }

    @Test
    public void testTokenMgrErrorWithReason() {
        TokenMgrError tokenMgrError = new TokenMgrError("Error message", 2);
        assertEquals(2, tokenMgrError.errorCode);
    }

    @Test
    public void testTokenMgrErrorWithDetails() {
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"hello\"", new TokenMgrError(false, 0, 1, 1, "hello", 'a', 3).getMessage());
        assertEquals(3, new TokenMgrError(false, 0, 1, 1, "hello", 'a', 3).errorCode);
    }
}