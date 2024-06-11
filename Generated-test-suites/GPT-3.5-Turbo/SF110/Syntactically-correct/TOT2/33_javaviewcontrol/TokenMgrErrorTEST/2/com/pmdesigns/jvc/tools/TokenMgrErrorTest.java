package com.pmdesigns.jvc.tools;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    private TokenMgrError tokenMgrError;

    @Before
    public void setUp() {
        tokenMgrError = new TokenMgrError();
    }
    
    @Test
    public void testAddEscapes() {
        String input = "abc\ndef";
        String expected = "abc\\ndef";
        assertEquals(expected, TokenMgrError.addEscapes(input));
    }
    
    @Test
    public void testLexicalError() {
        boolean EOFSeen = false;
        int lexState = 1;
        int errorLine = 2;
        int errorColumn = 3;
        String errorAfter = "abc";
        char curChar = 'd';
        
        String expected = "Lexical error at line 2, column 3.  Encountered: \"d\" (100), after : \"abc\"";
        
        assertEquals(expected, TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
    }
    
    @Test
    public void testGetMessage() {
        String message = "Test Message";
        tokenMgrError = new TokenMgrError(message, TokenMgrError.LEXICAL_ERROR);
        assertEquals(message, tokenMgrError.getMessage());
    }
    
    @Test
    public void testConstructor1() {
        String message = "Test Message";
        int reason = TokenMgrError.LEXICAL_ERROR;
        tokenMgrError = new TokenMgrError(message, reason);
        assertEquals(message, tokenMgrError.getMessage());
        assertEquals(reason, tokenMgrError.errorCode);
    }
    
    @Test
    public void testConstructor2() {
        boolean EOFSeen = true;
        int lexState = 2;
        int errorLine = 4;
        int errorColumn = 5;
        String errorAfter = "xyz";
        char curChar = 'a';
        int reason = TokenMgrError.STATIC_LEXER_ERROR;
        
        String expectedMessage = "Lexical error at line 4, column 5.  Encountered: \"<EOF> \", after : \"xyz\"";
        
        tokenMgrError = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(expectedMessage, tokenMgrError.getMessage());
        assertEquals(reason, tokenMgrError.errorCode);
    }
}