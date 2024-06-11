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
        assertEquals("\\u0000", TokenMgrError.addEscapes("\0"));
        assertEquals("\\u0020", TokenMgrError.addEscapes(" "));
        assertEquals("ABC", TokenMgrError.addEscapes("ABC"));
    }
    
    @Test
    public void testLexicalError() {
        boolean eofSeen = true;
        int lexState = 0;
        int errorLine = 10;
        int errorColumn = 5;
        String errorAfter = "after";
        char curChar = 'A';
        String expected = "Lexical error at line 10, column 5.  Encountered: <EOF> after : \"after\"";
        assertEquals(expected, TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
        
        eofSeen = false;
        lexState = 1;
        errorLine = 2;
        errorColumn = 3;
        errorAfter = "error";
        curChar = 'B';
        expected = "Lexical error at line 2, column 3.  Encountered: \"B\" (66), after : \"error\"";
        assertEquals(expected, TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
    }
    
    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("message", 1);
        assertEquals("message", error.getMessage());
    }
    
    @Test
    public void testConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertEquals(null, error.getMessage());
        
        error = new TokenMgrError("message", 1);
        assertEquals("message", error.getMessage());
        assertEquals(1, error.errorCode);
    }
    
    @Test
    public void testConstructorWithLexicalError() {
        boolean eofSeen = true;
        int lexState = 0;
        int errorLine = 10;
        int errorColumn = 5;
        String errorAfter = "after";
        char curChar = 'A';
        int reason = 1;
        String expected = "Lexical error at line 10, column 5.  Encountered: <EOF> after : \"after\"";
        
        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(expected, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
    
}