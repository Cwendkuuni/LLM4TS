package com.pmdesigns.jvc.tools;
import static org.junit.Assert.*;

import org.junit.Test;

public class TokenMgrErrorTest {
    
    @Test
    public void testAddEscapes() {
        assertEquals("Test Case for addEscapes", "\b\t\n\f\r\"\'\\", TokenMgrError.addEscapes("\b\t\n\f\r\"\'\\"));
    }
    
    @Test
    public void testLexicalError() {
        assertEquals("Test Case for LexicalError", "Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"test\"", TokenMgrError.LexicalError(false, 0, 1, 1, "test", 'a'));
    }
    
    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Error Message", 1);
        assertEquals("Test Case for getMessage", "Error Message", error.getMessage());
    }
    
    @Test
    public void testTokenMgrErrorWithoutReason() {
        TokenMgrError error = new TokenMgrError();
        assertNull("Test Case for TokenMgrError without reason", error.getMessage());
    }
    
    @Test
    public void testTokenMgrErrorWithReason() {
        TokenMgrError error = new TokenMgrError("Error Message", 1);
        assertEquals("Test Case for TokenMgrError with reason", "Error Message", error.getMessage());
        assertEquals("Test Case for TokenMgrError with reason", 1, error.errorCode);
    }
    
    @Test
    public void testTokenMgrErrorFull() {
        assertEquals("Test Case for TokenMgrError full constructor", "Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"test\"", 
                new TokenMgrError(true, 0, 1, 1, "test", 'a', 1).getMessage());
        assertEquals("Test Case for TokenMgrError full constructor", 1, new TokenMgrError(true, 0, 1, 1, "test", 'a', 1).errorCode);
    }

}