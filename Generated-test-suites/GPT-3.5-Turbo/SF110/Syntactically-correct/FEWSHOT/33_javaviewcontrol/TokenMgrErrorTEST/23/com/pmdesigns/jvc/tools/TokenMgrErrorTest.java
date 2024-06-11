package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

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
        assertEquals("\\u0020", TokenMgrError.addEscapes(" "));
        assertEquals("\\u007F", TokenMgrError.addEscapes("~"));
        assertEquals("\\u0080", TokenMgrError.addEscapes("€"));
    }
    
    @Test
    public void testLexicalError() {
        assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF> ", TokenMgrError.LexicalError(true, 0, 1, 1, "", '\0'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\\\"\" (34), after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", '\"'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\\t\" (9), after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", '\t'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\\n\" (10), after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", '\n'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\\f\" (12), after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", '\f'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\\r\" (13), after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", '\r'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\\\"\" (34), after : \"\\\"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "\"", '\"'));
    }
    
    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError();
        assertEquals(null, error.getMessage());
        
        error = new TokenMgrError("Error Message", 1);
        assertEquals("Error Message", error.getMessage());
    }
    
    @Test
    public void testConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertEquals(0, error.errorCode);
        
        error = new TokenMgrError("Error Message", 1);
        assertEquals("Error Message", error.getMessage());
        assertEquals(1, error.errorCode);
    }
    
    @Test
    public void testConstructorWithParams() {
        TokenMgrError error = new TokenMgrError(true, 0, 1, 1, "", '\0', 1);
        assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF> ", error.getMessage());
        assertEquals(1, error.errorCode);
        
        error = new TokenMgrError(false, 0, 1, 1, "", '\"', 0);
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\\\"\" (34), after : \"\"", error.getMessage());
        assertEquals(0, error.errorCode);
    }
}