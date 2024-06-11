package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError();
        String message = error.getMessage();
        assertNull(message);
    }

    @Test
    public void testConstructorWithMessageAndReason() {
        String message = "Test error message";
        int reason = 0;
        TokenMgrError error = new TokenMgrError(message, reason);
        assertEquals(message, error.getMessage());
        assertEquals(reason, error.errorCode);
    }

    @Test
    public void testConstructorWithLexicalError() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "test";
        char curChar = 'c';
        int reason = 0;

        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String expectedMessage = "Lexical error at line 1, column 1.  Encountered: <EOF> after : \"test\"";
        assertEquals(expectedMessage, error.getMessage());
        assertEquals(reason, error.errorCode);
    }

}