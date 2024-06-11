package com.pmdesigns.jvc.tools;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {
    
    @Test
    public void testAddEscapes() {
        String input = "Test\tString\n";
        String expected = "Test\\tString\\n";
        
        String result = TokenMgrError.addEscapes(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testLexicalError() {
        boolean EOFSeen = true;
        int lexState = 1;
        int errorLine = 2;
        int errorColumn = 5;
        String errorAfter = "error";
        char curChar = 'a';
        
        String expected = "Lexical error at line 2, column 5.  Encountered: <EOF>  after : \"error\"";
        
        String result = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetMessage() {
        String message = "Test Message";
        TokenMgrError tokenMgrError = new TokenMgrError(message, TokenMgrError.LEXICAL_ERROR);
        
        String result = tokenMgrError.getMessage();
        
        assertEquals(message, result);
    }
    
    @Test
    public void testConstructorWithReason() {
        String message = "Test Message";
        int reason = TokenMgrError.STATIC_LEXER_ERROR;
        
        TokenMgrError tokenMgrError = new TokenMgrError(message, reason);
        
        assertEquals(message, tokenMgrError.getMessage());
        assertEquals(reason, tokenMgrError.errorCode);
    }
    
    @Test
    public void testFullConstructor() {
        boolean EOFSeen = false;
        int lexState = 2;
        int errorLine = 3;
        int errorColumn = 6;
        String errorAfter = "another error";
        char curChar = 'b';
        int reason = TokenMgrError.INVALID_LEXICAL_STATE;
        
        String expected = "Lexical error at line 3, column 6.  Encountered: \"b\" (98),  after : \"another error\"";
        
        TokenMgrError tokenMgrError = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        
        assertEquals(expected, tokenMgrError.getMessage());
        assertEquals(reason, tokenMgrError.errorCode);
    }
}