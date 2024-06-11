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
        assertEquals("\\u0000", TokenMgrError.addEscapes("\u0000"));
        assertEquals("\\u0001", TokenMgrError.addEscapes("\u0001"));
        // Add more test cases here
    }

    @Test
    public void testLexicalError() {
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"b\"",
                TokenMgrError.LexicalError(false, 0, 1, 1, "b", 'a'));
        assertEquals("Lexical error at line 2, column 3.  Encountered: <EOF> , after : \"test\"",
                TokenMgrError.LexicalError(true, 1, 2, 3, "test", '\0'));
        // Add more test cases here
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test Error", 1);
        assertEquals("Test Error", error.getMessage());
        // Add more test cases here
    }

    @Test
    public void testConstructor1() {
        TokenMgrError error = new TokenMgrError();
        // Add assertions for constructor here
    }

    @Test
    public void testConstructor2() {
        TokenMgrError error = new TokenMgrError("Test Error", 2);
        assertEquals("Test Error", error.getMessage());
        // Add more test cases here
    }

    @Test
    public void testConstructor3() {
        TokenMgrError error = new TokenMgrError(false, 1, 3, 4, "something", 'c', 3);
        assertEquals("Lexical error at line 3, column 4.  Encountered: \"c\" (99), after : \"something\"",
                error.getMessage());
        // Add more test cases here
    }
}