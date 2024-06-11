package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokenMgrErrorTest {
    @Test
    public void testAddEscapes() {
        // Test an empty string
        assertEquals("", TokenMgrError.addEscapes(""));

        // Test a string with escape characters
        assertEquals("\\b", TokenMgrError.addEscapes("\b"));
        assertEquals("\\t", TokenMgrError.addEscapes("\t"));
        assertEquals("\\n", TokenMgrError.addEscapes("\n"));
        assertEquals("\\f", TokenMgrError.addEscapes("\f"));
        assertEquals("\\r", TokenMgrError.addEscapes("\r"));
        assertEquals("\\\"", TokenMgrError.addEscapes("\""));
        assertEquals("\\'", TokenMgrError.addEscapes("'"));
        assertEquals("\\\\", TokenMgrError.addEscapes("\\"));
        
        // Test a string with unicode characters
        assertEquals("\\u0020", TokenMgrError.addEscapes(" "));
        assertEquals("\\u00A9", TokenMgrError.addEscapes("©"));
    }

    @Test
    public void testLexicalError() {
        // Test when EOFSeen is true
        assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF> after : \"\"",
                TokenMgrError.LexicalError(true, 0, 1, 1, "", ' '));

        // Test when EOFSeen is false
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\" (32), after : \"\"",
                TokenMgrError.LexicalError(false, 0, 1, 1, "", ' '));

        // Test when errorAfter is not empty and curChar is a printable character
        assertEquals("Lexical error at line 2, column 3.  Encountered: \"b\" (98), after : \"foo\"",
                TokenMgrError.LexicalError(false, 0, 2, 3, "foo", 'b'));

        // Test when errorAfter is not empty and curChar is a non-printable character
        assertEquals("Lexical error at line 3, column 2.  Encountered: \"\\n\" (10), after : \"\\n\"",
                TokenMgrError.LexicalError(false, 0, 3, 2, "\n", '\n'));
    }

    @Test
    public void testGetMessage() {
        // Test a TokenMgrError with no message
        TokenMgrError error1 = new TokenMgrError();
        assertNull(error1.getMessage());

        // Test a TokenMgrError with a message
        TokenMgrError error2 = new TokenMgrError("Error message", TokenMgrError.LEXICAL_ERROR);
        assertEquals("Error message", error2.getMessage());
    }

    @Test
    public void testTokenMgrError() {
        // Test a default TokenMgrError constructor
        TokenMgrError error1 = new TokenMgrError();
        assertNull(error1.getMessage());

        // Test a TokenMgrError constructor with a message and reason
        TokenMgrError error2 = new TokenMgrError("Error message", TokenMgrError.LEXICAL_ERROR);
        assertEquals("Error message", error2.getMessage());
        assertEquals(TokenMgrError.LEXICAL_ERROR, error2.errorCode);

        // Test a TokenMgrError constructor with all parameters
        TokenMgrError error3 = new TokenMgrError(false, 0, 5, 3, "foo", 'b', TokenMgrError.STATIC_LEXER_ERROR);
        assertEquals("Lexical error at line 5, column 3.  Encountered: \"b\" (98), after : \"foo\"",
                error3.getMessage());
        assertEquals(TokenMgrError.STATIC_LEXER_ERROR, error3.errorCode);
    }
}