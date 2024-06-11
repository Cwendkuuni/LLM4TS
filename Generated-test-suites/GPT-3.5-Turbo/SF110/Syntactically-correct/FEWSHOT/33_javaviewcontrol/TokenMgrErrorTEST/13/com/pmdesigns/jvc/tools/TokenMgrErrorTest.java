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
        assertEquals("\\\'", TokenMgrError.addEscapes("\'"));
        assertEquals("\\\\", TokenMgrError.addEscapes("\\"));
        assertEquals("\\u0000", TokenMgrError.addEscapes("\u0000"));
    }
    
    @Test
    public void testLexicalError() {
        assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF> ", TokenMgrError.LexicalError(true, 0, 1, 1, "", '\u0000'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\\b\" (8), after : \"test\"", TokenMgrError.LexicalError(false, 0, 1, 1, "test", '\b'));
    }
    
    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError();
        assertEquals(null, error.getMessage());
        error = new TokenMgrError("Error", 1);
        assertEquals("Error", error.getMessage());
    }
    
    @Test
    public void testConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertEquals(0, error.errorCode);
        error = new TokenMgrError("Error", 1);
        assertEquals("Error", error.getMessage());
        assertEquals(1, error.errorCode);
        error = new TokenMgrError(false, 0, 1, 1, "test", '\b', 2);
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\\b\" (8), after : \"test\"", error.getMessage());
        assertEquals(2, error.errorCode);
    }
}