package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokenMgrErrorTest {
    @Test
    public void testTokenMgrError_noArgs() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
    }
    
    @Test
    public void testTokenMgrError_withMessageAndReason() {
        TokenMgrError error = new TokenMgrError("Test Message", 0);
        assertEquals("Test Message", error.getMessage());
        assertEquals(0, error.errorCode);
    }
    
    @Test
    public void testTokenMgrError_withEOFSeen() {
        TokenMgrError error = new TokenMgrError(true, 0, 1, 2, "Test After", 'a', 0);
        assertEquals("Lexical error at line 1, column 2.  Encountered: \"a\" (97), after : \"Test After\"", error.getMessage());
        assertEquals(0, error.errorCode);
    }
    
    @Test
    public void testTokenMgrError_withoutEOFSeen() {
        TokenMgrError error = new TokenMgrError(false, 0, 1, 2, "Test After", 'a', 0);
        assertEquals("Lexical error at line 1, column 2.  Encountered: \"a\" (97), after : \"Test After\"", error.getMessage());
        assertEquals(0, error.errorCode);
    }
    
    @Test
    public void testTokenMgrError_withEmptyAfter() {
        TokenMgrError error = new TokenMgrError(true, 0, 1, 2, "", 'a', 0);
        assertEquals("Lexical error at line 1, column 2.  Encountered: \"a\" (97), after : \"\"", error.getMessage());
        assertEquals(0, error.errorCode);
    }
    
    @Test
    public void testAddEscapes_noSpecialCharacters() {
        String result = TokenMgrError.addEscapes("test");
        assertEquals("test", result);
    }
    
    @Test
    public void testAddEscapes_escapeSpecialCharacters() {
        String result = TokenMgrError.addEscapes("te\"st\ting\n");
        assertEquals("te\\\"st\\ting\\n", result);
    }
    
    @Test
    public void testLexicalError_withEOFSeen() {
        String result = TokenMgrError.LexicalError(true, 0, 1, 2, "Test After", 'a');
        assertEquals("Lexical error at line 1, column 2.  Encountered: \"a\" (97), after : \"Test After\"", result);
    }
    
    @Test
    public void testLexicalError_withoutEOFSeen() {
        String result = TokenMgrError.LexicalError(false, 0, 1, 2, "Test After", 'a');
        assertEquals("Lexical error at line 1, column 2.  Encountered: \"a\" (97), after : \"Test After\"", result);
    }
}