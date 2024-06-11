package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;

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
        assertEquals("\\u001f", TokenMgrError.addEscapes("\u001f"));
        assertEquals("abc", TokenMgrError.addEscapes("abc"));
    }

    @Test
    public void testLexicalError() {
        assertEquals("Lexical error at line 1, column 2.  Encountered: \"a\" (97), after : \"b\"",
                TokenMgrError.LexicalError(false, 0, 1, 2, "b", 'a'));
        assertEquals("Lexical error at line 2, column 3.  Encountered: \"\" (0), after : \"xyz\"",
                TokenMgrError.LexicalError(true, 0, 2, 3, "xyz", '\0'));
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError();
        assertEquals(null, error.getMessage());

        error = new TokenMgrError("Error message", 1);
        assertEquals("Error message", error.getMessage());
    }

    @Test
    public void testConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertEquals(0, error.errorCode);

        error = new TokenMgrError("Error message", 1);
        assertEquals(1, error.errorCode);
        assertEquals("Error message", error.getMessage());

        error = new TokenMgrError(true, 0, 2, 3, "xyz", '\0', 2);
        assertEquals(2, error.errorCode);
        assertEquals("Lexical error at line 2, column 3.  Encountered: \"\" (0), after : \"xyz\"", error.getMessage());
    }
}