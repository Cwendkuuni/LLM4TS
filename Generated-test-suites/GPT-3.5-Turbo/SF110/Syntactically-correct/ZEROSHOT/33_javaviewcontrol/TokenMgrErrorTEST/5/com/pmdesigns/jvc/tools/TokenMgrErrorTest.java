package com.pmdesigns.jvc.tools;

import org.junit.Assert;
import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError();
        String message = error.getMessage();
        Assert.assertNull(message);
    }

    @Test
    public void testTokenMgrErrorWithMessageAndReason() {
        String message = "Lexical error at line 1, column 1.  Encountered: <EOF>  after : \"\"";
        int reason = 0;
        TokenMgrError error = new TokenMgrError(message, reason);
        Assert.assertEquals(message, error.getMessage());
        Assert.assertEquals(reason, error.errorCode);
    }

    @Test
    public void testTokenMgrErrorWithParams() {
        boolean EOFSeen = true;
        int lexState = 1;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "error";
        char curChar = 'a';
        int reason = 2;
        String expectedMessage = "Lexical error at line 5, column 10.  Encountered: \"a\" (97), after : \"error\"";
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        Assert.assertEquals(expectedMessage, error.getMessage());
        Assert.assertEquals(reason, error.errorCode);
    }
}