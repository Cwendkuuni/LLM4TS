package com.pmdesigns.jvc.tools;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        // Test typical use case
        String inputStr = "This is a test \\ string";
        String expectedOutput = "This is a test \\\\ string";
        assertEquals(expectedOutput, TokenMgrError.addEscapes(inputStr));

        // Test edge case: empty string
        inputStr = "";
        expectedOutput = "";
        assertEquals(expectedOutput, TokenMgrError.addEscapes(inputStr));
    }

    @Test
    public void testLexicalError() {
        // Test typical use case
        boolean EOFSeen = false;
        int lexState = 1;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "some text";
        char curChar = 'A';
        String expectedOutput = "Lexical error at line 5, column 10. Encountered: \"A\" (65), after : \"some text\"";
        assertEquals(expectedOutput, TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));

        // Test edge case: EOF seen
        EOFSeen = true;
        expectedOutput = "Lexical error at line 5, column 10. Encountered: <EOF> after : \"some text\"";
        assertEquals(expectedOutput, TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
    }

    @Test
    public void testGetMessage() {
        // Test typical use case
        String message = "This is an error message";
        TokenMgrError tokenError = new TokenMgrError(message, 0);
        assertEquals(message, tokenError.getMessage());

        // Test edge case: Empty message
        message = "";
        tokenError = new TokenMgrError(message, 1);
        assertEquals(message, tokenError.getMessage());
    }

    @Test
    public void testTokenMgrError() {
        // Test typical use case
        String message = "This is an error message";
        int reason = 2;
        TokenMgrError tokenError = new TokenMgrError(message, reason);
        assertEquals(message, tokenError.getMessage());
        assertEquals(reason, tokenError.errorCode);

        // Test edge case: Empty message
        message = "";
        reason = 0;
        tokenError = new TokenMgrError(message, reason);
        assertEquals(message, tokenError.getMessage());
        assertEquals(reason, tokenError.errorCode);
    }

    @Test
    public void testTokenMgrErrorWithParams() {
        // Test typical use case
        boolean EOFSeen = false;
        int lexState = 1;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "some text";
        char curChar = 'A';
        int reason = 3;
        String expectedMessage = "Lexical error at line 5, column 10. Encountered: \"A\" (65), after : \"some text\"";
        TokenMgrError tokenError = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(expectedMessage, tokenError.getMessage());
        assertEquals(reason, tokenError.errorCode);

        // Test edge case: EOF seen
        EOFSeen = true;
        expectedMessage = "Lexical error at line 5, column 10. Encountered: <EOF> after : \"some text\"";
        tokenError = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(expectedMessage, tokenError.getMessage());
        assertEquals(reason, tokenError.errorCode);
    }
}