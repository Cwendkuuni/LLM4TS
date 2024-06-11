package com.pmdesigns.jvc.tools;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage_DefaultConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
    }

    @Test
    public void testGetMessage_MessageReasonConstructor() {
        String message = "Test message";
        int reason = 0;
        TokenMgrError error = new TokenMgrError(message, reason);
        assertEquals(message, error.getMessage());
    }

    @Test
    public void testGetMessage_LexicalErrorConstructor() {
        boolean EOFSeen = true;
        int lexState = 1;
        int errorLine = 2;
        int errorColumn = 3;
        String errorAfter = "test";
        char curChar = 'a';
        int reason = 0;
        String expectedMessage = "Lexical error at line " + errorLine + ", column " + errorColumn +
                ".  Encountered: <EOF>  after : \"" + TokenMgrError.addEscapes(errorAfter) + "\"";
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(expectedMessage, error.getMessage());
    }

    @Test
    public void testErrorCode_DefaultConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertEquals(0, error.errorCode);
    }

    @Test
    public void testErrorCode_MessageReasonConstructor() {
        String message = "Test message";
        int reason = 1;
        TokenMgrError error = new TokenMgrError(message, reason);
        assertEquals(reason, error.errorCode);
    }

    @Test
    public void testErrorCode_LexicalErrorConstructor() {
        boolean EOFSeen = true;
        int lexState = 1;
        int errorLine = 2;
        int errorColumn = 3;
        String errorAfter = "test";
        char curChar = 'a';
        int reason = 2;
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(reason, error.errorCode);
    }
}