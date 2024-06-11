package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        assertEquals("\\u001F", TokenMgrError.addEscapes("\31"));
        assertEquals(" ", TokenMgrError.addEscapes(" "));
    }

    @Test
    public void testLexicalError() {
        String expected = "Lexical error at line 2, column 3.  Encountered: \"a\" (97), after : \"bc\"";
        String actual = TokenMgrError.LexicalError(false, 1, 2, 3, "bc", 'a');
        assertEquals(expected, actual);
        
        expected = "Lexical error at line 5, column 6.  Encountered: <EOF> , after : \"def\"";
        actual = TokenMgrError.LexicalError(true, 3, 5, 6, "def", '\0');
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMessage_DefaultConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertEquals(null, error.getMessage());
    }

    @Test
    public void testGetMessage_ParameterizedConstructor() {
        TokenMgrError error = new TokenMgrError("Error message", 2);
        assertEquals("Error message", error.getMessage());
    }

    @Test
    public void testTokenMgrError_ParameterizedConstructor() {
        TokenMgrError error = new TokenMgrError("Error message", 1);
        assertEquals("Error message", error.getMessage());
        assertEquals(1, error.errorCode);
    }

    @Test
    public void testTokenMgrError_LexicalError() {
        TokenMgrError error = new TokenMgrError(false, 2, 3, 4, "abc", 'd', 0);
        assertEquals("Lexical error at line 3, column 4.  Encountered: \"d\" (100), after : \"abc\"", error.getMessage());
        assertEquals(0, error.errorCode);
    }
}