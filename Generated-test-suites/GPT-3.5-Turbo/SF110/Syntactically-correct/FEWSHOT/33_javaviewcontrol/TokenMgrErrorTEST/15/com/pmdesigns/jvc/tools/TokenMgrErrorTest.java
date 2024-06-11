package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String str = "";
        assertEquals("", TokenMgrError.addEscapes(str));
        
        str = "hello";
        assertEquals("hello", TokenMgrError.addEscapes(str));
        
        str = "\0";
        assertEquals("\\0", TokenMgrError.addEscapes(str));
        
        str = "\b";
        assertEquals("\\b", TokenMgrError.addEscapes(str));
        
        str = "\t";
        assertEquals("\\t", TokenMgrError.addEscapes(str));
        
        str = "\n";
        assertEquals("\\n", TokenMgrError.addEscapes(str));
        
        str = "\f";
        assertEquals("\\f", TokenMgrError.addEscapes(str));
        
        str = "\r";
        assertEquals("\\r", TokenMgrError.addEscapes(str));
        
        str = "\"";
        assertEquals("\\\"", TokenMgrError.addEscapes(str));
        
        str = "\'";
        assertEquals("\\\'", TokenMgrError.addEscapes(str));
        
        str = "\\";
        assertEquals("\\\\", TokenMgrError.addEscapes(str));
        
        str = "€";
        assertEquals("\\u20ac", TokenMgrError.addEscapes(str));
        
        str = "ü";
        assertEquals("\\u00fc", TokenMgrError.addEscapes(str));
        
        str = "碁";
        assertEquals("\\u7941", TokenMgrError.addEscapes(str));
        
        str = "ಠ_ಠ";
        assertEquals("\\u0ca0_\\u0ca0", TokenMgrError.addEscapes(str));
    }

    @Test
    public void testLexicalError() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "";
        char curChar = 'A';
        assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF> after : \"\"", TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
        
        EOFSeen = false;
        lexState = 1;
        errorLine = 3;
        errorColumn = 5;
        errorAfter = "/";
        curChar = 'B';
        assertEquals("Lexical error at line 3, column 5.  Encountered: \"B\" (66), after : \"/\"", TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
        
        EOFSeen = false;
        lexState = 2;
        errorLine = 10;
        errorColumn = 2;
        errorAfter = "\'";
        curChar = 'C';
        assertEquals("Lexical error at line 10, column 2.  Encountered: \"C\" (67), after : \"\'\"", TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
    }
    
    @Test
    public void testGetMessage() {
    	TokenMgrError error = new TokenMgrError();
    	assertEquals(null, error.getMessage());
    	
    	error = new TokenMgrError("Error occurred", TokenMgrError.LEXICAL_ERROR);
    	assertEquals("Error occurred", error.getMessage());
    }

    @Test
    public void testTokenMgrError() {
    	TokenMgrError error = new TokenMgrError();
    	assertEquals(TokenMgrError.LEXICAL_ERROR, error.errorCode);
    	
    	error = new TokenMgrError("Error occurred", TokenMgrError.STATIC_LEXER_ERROR);
    	assertEquals("Error occurred", error.getMessage());
    	assertEquals(TokenMgrError.STATIC_LEXER_ERROR, error.errorCode);
    }

    @Test
    public void testTokenMgrErrorWithCurChar() {
    	boolean EOFSeen = true;
        int lexState = 3;
        int errorLine = 5;
        int errorColumn = 7;
        String errorAfter = ")";
        char curChar = 'D';
        
    	TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.INVALID_LEXICAL_STATE);
    	assertEquals("Lexical error at line 5, column 7.  Encountered: <EOF> after : \")\"", error.getMessage());
    	assertEquals(TokenMgrError.INVALID_LEXICAL_STATE, error.errorCode);
    }
    
}