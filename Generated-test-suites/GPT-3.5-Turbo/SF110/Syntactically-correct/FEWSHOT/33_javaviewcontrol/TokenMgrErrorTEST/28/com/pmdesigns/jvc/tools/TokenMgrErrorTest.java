package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        assertEquals("\\u0020", TokenMgrError.addEscapes(" "));
        assertEquals("\\u0080", TokenMgrError.addEscapes("€"));
    }
    
    @Test
    public void testLexicalError() {
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"b\"", TokenMgrError.LexicalError(false, 0, 1, 1, "b", 'a'));
        assertEquals("Lexical error at line 5, column 2.  Encountered: \" \" (32), after : \"\'s\"", TokenMgrError.LexicalError(false, 0, 5, 2, "'s", ' '));
        assertEquals("Lexical error at line 10, column 5.  Encountered: \"\\u0080\" (8364), after : \"Hello\"", TokenMgrError.LexicalError(false, 0, 10, 5, "Hello", '€'));
        assertEquals("Lexical error at line 3, column 1.  Encountered: \"\\u0000\" (0), after : \"Hello\"", TokenMgrError.LexicalError(false, 0, 3, 1, "Hello", '\0'));
    }
    
    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
        
        error = new TokenMgrError("Error message", 1);
        assertEquals("Error message", error.getMessage());
    }
    
    @Test
    public void testConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertEquals(0, error.errorCode);
        
        error = new TokenMgrError("Error message", 1);
        assertEquals(1, error.errorCode);
    }
    
    @Test
    public void testConstructorWithLineAndColumn() {
        TokenMgrError error = new TokenMgrError(false, 0, 1, 2, "error", 'a', 2);
        assertEquals("Lexical error at line 1, column 2.  Encountered: \"a\" (97), after : \"error\"", error.getMessage());
        assertEquals(2, error.errorCode);
    }
}