package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        assertEquals("\\u0100", TokenMgrError.addEscapes("\u0100"));
    }

    @Test
    public void testLexicalError() {
        String expected = "Lexical error at line 1, column 1.  Encountered:  (0), after : \"\"";
        assertEquals(expected, TokenMgrError.LexicalError(false, 0, 1, 1, "", (char) 0));

        expected = "Lexical error at line 2, column 3.  Encountered: \"a\" (97), after : \"bc\"";
        assertEquals(expected, TokenMgrError.LexicalError(false, 0, 2, 3, "bc", 'a'));
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error1 = new TokenMgrError();
        assertNotNull(error1.getMessage());

        TokenMgrError error2 = new TokenMgrError("Test Message", 0);
        assertEquals("Test Message", error2.getMessage());
    }

    @Test
    public void testConstructorWithoutMessage() {
        TokenMgrError error1 = new TokenMgrError();
        assertEquals(0, error1.errorCode);

        TokenMgrError error2 = new TokenMgrError("Test Message", 1);
        assertEquals(1, error2.errorCode);
    }

    @Test
    public void testConstructorWithMessage() {
        String expected = "Lexical error at line 1, column 1.  Encountered:  (0), after : \"\"";
        TokenMgrError error1 = new TokenMgrError(false, 0, 1, 1, "", (char) 0, 0);
        assertEquals(expected, error1.getMessage());
        assertEquals(0, error1.errorCode);

        expected = "Lexical error at line 2, column 3.  Encountered: \"a\" (97), after : \"bc\"";
        TokenMgrError error2 = new TokenMgrError(false, 0, 2, 3, "bc", 'a', 1);
        assertEquals(expected, error2.getMessage());
        assertEquals(1, error2.errorCode);
    }
}