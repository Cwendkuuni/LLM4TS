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
        assertEquals("\\\'", TokenMgrError.addEscapes("\'"));
        assertEquals("\\\\", TokenMgrError.addEscapes("\\"));
        assertEquals("\\u0000", TokenMgrError.addEscapes("\0"));
        assertEquals("test", TokenMgrError.addEscapes("test"));
    }

    @Test
    public void testLexicalError() {
        assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF> ", TokenMgrError.LexicalError(true, 0, 1, 1, "", ''));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", 'a'));
    }

    @Test
    public void testGetMessage() {
        TokenMgrError tokenMgrError = new TokenMgrError("Test message", 0);
        assertEquals("Test message", tokenMgrError.getMessage());
    }

    @Test
    public void testConstructors() {
        TokenMgrError tokenMgrError = new TokenMgrError();
        assertEquals(0, tokenMgrError.errorCode);
        
        TokenMgrError tokenMgrErrorWithMessageAndReason = new TokenMgrError("Test message", 1);
        assertEquals("Test message", tokenMgrErrorWithMessageAndReason.getMessage());
        assertEquals(1, tokenMgrErrorWithMessageAndReason.errorCode);
        
        TokenMgrError tokenMgrErrorWithParams = new TokenMgrError(true, 0, 1, 1, "", 'a', 2);
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"\"", tokenMgrErrorWithParams.getMessage());
        assertEquals(2, tokenMgrErrorWithParams.errorCode);
    }
}