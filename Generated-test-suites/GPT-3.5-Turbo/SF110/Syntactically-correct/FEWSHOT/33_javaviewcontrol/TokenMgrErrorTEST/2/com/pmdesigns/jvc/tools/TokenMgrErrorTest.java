package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        // Test case with empty string
        String result = TokenMgrError.addEscapes("");
        assertEquals("", result);

        // Test case with non-escape characters
        result = TokenMgrError.addEscapes("abcdefg");
        assertEquals("abcdefg", result);

        // Test case with escape characters
        result = TokenMgrError.addEscapes("abc\tdef\nghi\"");
        assertEquals("abc\\tdef\\nghi\\\"", result);

        // Test case with special characters
        result = TokenMgrError.addEscapes("abc\0def\bghi\fjkl\'mno\\pqr");
        assertEquals("abc\\u0000def\\bghi\\fjkl\\'mno\\\\pqr", result);
    }

    @Test
    public void testLexicalError() {
        // Test case with EOF seen
        String result = TokenMgrError.LexicalError(true, 1, 2, 3, "abc", 'd');
        assertEquals("Lexical error at line 2, column 3.  Encountered: <EOF>  after : \"abc\"", result);

        // Test case without EOF seen
        result = TokenMgrError.LexicalError(false, 4, 5, 6, "def", 'g');
        assertEquals("Lexical error at line 5, column 6.  Encountered: \"g\" (103), after : \"def\"", result);
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test message", 0);
        assertEquals("Test message", error.getMessage());
    }

    @Test
    public void testTokenMgrError() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
    }

    @Test
    public void testTokenMgrErrorWithMessageAndReason() {
        TokenMgrError error = new TokenMgrError("Test message", 1);
        assertEquals("Test message", error.getMessage());
        assertEquals(1, error.errorCode);
    }

    @Test
    public void testTokenMgrErrorWithAllParameters() {
        TokenMgrError error = new TokenMgrError(false, 2, 3, 4, "abc", 'd', 5);
        String expectedMessage = "Lexical error at line 3, column 4.  Encountered: \"d\" (100), after : \"abc\"";
        assertEquals(expectedMessage, error.getMessage());
        assertEquals(5, error.errorCode);
    }
}