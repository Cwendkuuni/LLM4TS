
package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {
    
    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
    }
    
    @Test
    public void testConstructorNoParams() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
        assertEquals(0, error.errorCode);
    }
    
    @Test
    public void testConstructorWithMessageAndReason() {
        String message = "Test error message";
        int reason = 1;
        TokenMgrError error = new TokenMgrError(message, reason);
        assertEquals(message, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
    
    @Test
    public void testConstructorWithLexerParams() {
        boolean EOFSeen = false;
        int lexState = 1;
        int errorLine = 10;
        int errorColumn = 5;
        String errorAfter = "testing";
        char curChar = 'a';
        int reason = 2;
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        
        String expectedMessage = "Lexical error at line " + errorLine + ", column " + errorColumn + ".  Encountered: " + "\"" + TokenMgrError.addEscapes(String.valueOf(curChar)) + "\"" + " (" + (int) curChar + "), " + "after : \"" + TokenMgrError.addEscapes(errorAfter) + "\"";
        assertEquals(expectedMessage, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
    
}
