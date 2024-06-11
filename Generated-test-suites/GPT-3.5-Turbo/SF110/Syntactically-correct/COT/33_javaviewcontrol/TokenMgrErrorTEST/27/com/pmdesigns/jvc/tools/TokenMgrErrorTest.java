
package com.pmdesigns.jvc.tools;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {
    
    @Test
    public void testLexicalError() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "Error";
        char curChar = 'A';
        int reason = TokenMgrError.LEXICAL_ERROR;
        
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        
        String expectedMessage = "Lexical error at line 1, column 1. Encountered: \"A\" (65), after : \"Error\"";
        assertEquals(expectedMessage, error.getMessage());
        assertEquals(TokenMgrError.LEXICAL_ERROR, error.errorCode);
    }
    
    @Test
    public void testEmptyTokenMgrError() {
        TokenMgrError error = new TokenMgrError();
        
        assertEquals(null, error.getMessage());
        assertEquals(0, error.errorCode);
    }
    
    @Test
    public void testTokenMgrErrorWithMessageAndReason() {
        String message = "Error Message";
        int reason = TokenMgrError.STATIC_LEXER_ERROR;
        
        TokenMgrError error = new TokenMgrError(message, reason);
        
        assertEquals(message, error.getMessage());
        assertEquals(TokenMgrError.STATIC_LEXER_ERROR, error.errorCode);
    }
}
