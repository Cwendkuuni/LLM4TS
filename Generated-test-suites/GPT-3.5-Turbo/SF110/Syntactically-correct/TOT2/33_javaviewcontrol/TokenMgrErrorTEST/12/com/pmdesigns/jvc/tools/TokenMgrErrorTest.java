
package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        // Test typical use case
        String input = "This is a test string";
        String expectedOutput = "This is a test string";
        assertEquals(TokenMgrError.addEscapes(input), expectedOutput);

        // Test edge case with special characters
        input = "Special characters: \n\t\0";
        expectedOutput = "Special characters: \n\t\0";
        assertEquals(TokenMgrError.addEscapes(input), expectedOutput);
    }

    @Test
    public void testLexicalError() {
        // Test typical use case
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 0;
        String errorAfter = "Error occurred after";
        char curChar = 'a';
        String expectedErrorMessage = "Lexical error at line 1, column 0. Encountered: \"a\" (97), after : \"Error occurred after\"";
        assertEquals(TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar), expectedErrorMessage);

        // Test error scenario with EOFSeen=true
        EOFSeen = true;
        expectedErrorMessage = "Lexical error at line 1, column 0. Encountered: <EOF> , after : \"Error occurred after\"";
        assertEquals(TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar), expectedErrorMessage);
    }

    @Test
    public void testGetMessage() {
        // Test typical use case
        TokenMgrError tokenMgrError = new TokenMgrError("Test Message", 0);
        assertEquals(tokenMgrError.getMessage(), "Test Message");
    }

    @Test
    public void testTokenMgrErrorConstructor() {
        // Test typical use case
        TokenMgrError tokenMgrError = new TokenMgrError("Test Message", 0);
        assertEquals(tokenMgrError.errorCode, 0);

        // Test edge case with different error code
        tokenMgrError = new TokenMgrError("Test Message", 1);
        assertEquals(tokenMgrError.errorCode, 1);
    }
}
