
package com.pmdesigns.jvc.tools;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {
    
    // Test case for default constructor
    @Test
    public void testDefaultConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertEquals(null, error.getMessage());
        // Additional assertions if necessary
    }
    
    // Test case for constructor with message and reason
    @Test
    public void testConstructorWithMessageAndReason() {
        String message = "Test message";
        int reason = 0;
        TokenMgrError error = new TokenMgrError(message, reason);
        assertEquals(message, error.getMessage());
        assertEquals(reason, error.errorCode);
        // Additional assertions if necessary
    }
    
    // Test case for constructor with boolean, int, int, int, String, char, and reason
    @Test
    public void testConstructorWithParameters() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "Test";
        char curChar = 'a';
        int reason = 0;
        
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String expectedMessage = "Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"Test\"";
        assertEquals(expectedMessage, error.getMessage());
        assertEquals(reason, error.errorCode);
        // Additional assertions if necessary
    }

    // Test case for addEscapes method
    @Test
    public void testAddEscapes() {
        String input = "Hello\nWorld";
        String expectedOutput = "Hello\\nWorld";
        String actualOutput = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, actualOutput);
        // Additional assertions if necessary
    }

    // Test case for LexicalError method
    @Test
    public void testLexicalError() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "Test";
        char curChar = 'a';
        String expectedOutput = "Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"Test\"";
        String actualOutput = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expectedOutput, actualOutput);
        // Additional assertions if necessary
    }
}

