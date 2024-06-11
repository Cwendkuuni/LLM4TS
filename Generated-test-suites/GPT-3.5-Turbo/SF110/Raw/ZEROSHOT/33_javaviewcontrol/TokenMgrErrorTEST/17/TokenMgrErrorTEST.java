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
    public void testConstructor1() {
        String message = "Test Message";
        int reason = 0;
        TokenMgrError error = new TokenMgrError(message, reason);
        assertEquals(message, error.getMessage());
        assertEquals(reason, error.errorCode);
    }

    @Test
    public void testConstructor2() {
        boolean EOFSeen = false;
        int lexState = 1;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "ErrorAfter";
        char curChar = 'a';
        int reason = 2;

        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String expectedMessage = "Lexical error at line " + errorLine + ", column " + errorColumn + ".  Encountered: " + "\"" + TokenMgrError.addEscapes(String.valueOf(curChar)) + "\"" + " (" + (int)curChar + "), " + "after : \"" + TokenMgrError.addEscapes(errorAfter) + "\"";
        assertEquals(expectedMessage, error.getMessage());
        assertEquals(reason, error.errorCode);
    }

    @Test
    public void testAddEscapes() {
        String str = "Test String";
        String escapedStr = TokenMgrError.addEscapes(str);

        String expectedStr = "Test String";
        assertEquals(expectedStr, escapedStr);
    }
}