package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

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
        assertEquals("\\u0000", TokenMgrError.addEscapes("\0"));
        assertEquals("A", TokenMgrError.addEscapes("A"));
    }

    @Test
    public void testLexicalError() {
        assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF> after : \"test\"",
                TokenMgrError.LexicalError(true, 0, 1, 1, "test", '\0'));
    }

    @Test
    public void testGetMessage() {
        TokenMgrError err = new TokenMgrError();
        assertEquals(null, err.getMessage());

        err = new TokenMgrError("error message", 1);
        assertEquals("error message", err.getMessage());
    }

    @Test
    public void testTokenMgrError() {
        TokenMgrError err = new TokenMgrError();
        assertEquals(null, err.getMessage());

        err = new TokenMgrError("error message", 1);
        assertEquals("error message", err.getMessage());
        assertEquals(1, err.errorCode);
    }

    @Test
    public void testTokenMgrErrorWithParams() {
        assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF> after : \"test\"",
                new TokenMgrError(true, 0, 1, 1, "test", '\0', 1).getMessage());
    }
}