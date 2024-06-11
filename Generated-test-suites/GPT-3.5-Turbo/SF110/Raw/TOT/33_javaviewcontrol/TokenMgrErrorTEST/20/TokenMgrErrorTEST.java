
package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        // Typical use case: input string contains escape characters
        String input = "Hello\tWorld\n";
        String expectedOutput = "Hello\\tWorld\\n";
        String actualOutput = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testLexicalError() {
        // Typical use case: no EOF seen
        boolean EOFSeen = false;
        int lexState = 1;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "abc";
        char curChar = 'd';
        int reason = 2;
        String expectedOutput = "Lexical error at line 5, column 10.  Encountered: \"d\" (100), after : \"abc\"";
        String actualOutput = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGetMessage() {
        // Typical use case: message is set in the constructor
        String message = "Test message";
        int reason = 1;
        TokenMgrError error = new TokenMgrError(message, reason);
        String expectedOutput = "Test message";
        String actualOutput = error.getMessage();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTokenMgrError1() {
        // Typical use case: constructor is called with message and reason
        String message = "Test message";
        int reason = 1;
        TokenMgrError error = new TokenMgrError(message, reason);
        String expectedOutput = "Test message";
        String actualOutput = error.getMessage();
        assertEquals(expectedOutput, actualOutput);
        assertEquals(reason, error.errorCode);
    }

    @Test
    public void testTokenMgrError2() {
        // Typical use case: constructor is called with all arguments
        boolean EOFSeen = true;
        int lexState = 2;
        int errorLine = 10;
        int errorColumn = 5;
        String errorAfter = "def";
        char curChar = 'e';
        int reason = 3;
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String expectedOutput = "Lexical error at line 10, column 5.  Encountered: <EOF> , after : \"def\"";
        String actualOutput = error.getMessage();
        assertEquals(expectedOutput, actualOutput);
        assertEquals(reason, error.errorCode);
    }

}
