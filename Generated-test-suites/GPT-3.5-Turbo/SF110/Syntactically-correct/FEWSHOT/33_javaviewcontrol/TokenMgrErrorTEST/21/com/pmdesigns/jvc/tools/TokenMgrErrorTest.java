package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenMgrErrorTest {
    
    @Test
    public void testAddEscapes() {
        String input = "";
        assertEquals("", TokenMgrError.addEscapes(input));
        
        input = "abc";
        assertEquals("abc", TokenMgrError.addEscapes(input));
        
        input = "\0";
        assertEquals("\\0", TokenMgrError.addEscapes(input));
        
        input = "\b";
        assertEquals("\\b", TokenMgrError.addEscapes(input));
        
        input = "\t";
        assertEquals("\\t", TokenMgrError.addEscapes(input));
        
        input = "\n";
        assertEquals("\\n", TokenMgrError.addEscapes(input));
        
        input = "\f";
        assertEquals("\\f", TokenMgrError.addEscapes(input));
        
        input = "\r";
        assertEquals("\\r", TokenMgrError.addEscapes(input));
        
        input = "\"";
        assertEquals("\\\"", TokenMgrError.addEscapes(input));
        
        input = "'";
        assertEquals("\\'", TokenMgrError.addEscapes(input));
        
        input = "\\";
        assertEquals("\\\\", TokenMgrError.addEscapes(input));
        
        input = "\u0097";
        assertEquals("\\u0097", TokenMgrError.addEscapes(input));
    }
    
    @Test
    public void testLexicalError() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "";
        char curChar = 'a';
        
        String expected = "Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"\"";
        assertEquals(expected, TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
        
        EOFSeen = true;
        lexState = 1;
        errorLine = 2;
        errorColumn = 3;
        errorAfter = "error";
        curChar = '\0';
        
        expected = "Lexical error at line 2, column 3.  Encountered: <EOF>  after : \"error\"";
        assertEquals(expected, TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
    }
    
    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test Message", 0);
        assertEquals("Test Message", error.getMessage());
        
        error = new TokenMgrError();
        assertEquals(null, error.getMessage());
    }
    
    @Test
    public void testConstructor() {
        TokenMgrError error = new TokenMgrError("Test Message", 0);
        assertEquals("Test Message", error.getMessage());
        assertEquals(0, error.errorCode);
        
        error = new TokenMgrError();
        assertEquals(null, error.getMessage());
        assertEquals(0, error.errorCode);
        
        boolean EOFSeen = true;
        int lexState = 2;
        int errorLine = 3;
        int errorColumn = 4;
        String errorAfter = "error";
        char curChar = '\n';
        int reason = 1;
        
        error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals("Lexical error at line 3, column 4.  Encountered: <EOF>  after : \"error\"", error.getMessage());
        assertEquals(reason, error.errorCode);
    }
}