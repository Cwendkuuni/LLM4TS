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
        assertEquals("\\'", TokenMgrError.addEscapes("\'"));
        assertEquals("\\\\", TokenMgrError.addEscapes("\\"));
        assertEquals("\\u0020", TokenMgrError.addEscapes("\u0020"));
    }
    
    @Test
    public void testLexicalError() {
        assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF> after : \"\"", TokenMgrError.LexicalError(true, 0, 1, 1, "", ' '));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\\r\" (13), after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", '\r'));
    }
    
    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError();
        assertEquals(null, error.getMessage());
        
        String message = "Test Message";
        error = new TokenMgrError(message, 1);
        assertEquals(message, error.getMessage());
    }    
}