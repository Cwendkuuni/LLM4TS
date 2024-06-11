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

        assertEquals("\\u0041", TokenMgrError.addEscapes("A"));
        assertEquals("\\u0030\\u0031", TokenMgrError.addEscapes("01"));
        assertEquals("\\u001f", TokenMgrError.addEscapes("\u001f"));
    }
    
    @Test
    public void testLexicalError() {
        assertEquals("Lexical error at line 1, column 2.  Encountered: \"a\" (97), after : \"foo\"",
                TokenMgrError.LexicalError(false, 1, 2, 3, "foo", 'a'));
        
        assertEquals("Lexical error at line 2, column 3.  Encountered: <EOF> ",
                TokenMgrError.LexicalError(true, 2, 3, 4, "", ' '));
    }
    
    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Error message", 1);
        assertEquals("Error message", error.getMessage());
    }
    
    @Test
    public void testTokenMgrError() {
        TokenMgrError error = new TokenMgrError();
        assertEquals(null, error.getMessage());
        
        TokenMgrError error2 = new TokenMgrError("Error message", 2);
        assertEquals("Error message", error2.getMessage());
    }
    
    @Test
    public void testTokenMgrErrorWithReason() {
        TokenMgrError error = new TokenMgrError(false, 1, 2, 3, "foo", 'a', 0);
        assertEquals("Lexical error at line 2, column 3.  Encountered: \"a\" (97), after : \"foo\"",
                error.getMessage());
    }
}