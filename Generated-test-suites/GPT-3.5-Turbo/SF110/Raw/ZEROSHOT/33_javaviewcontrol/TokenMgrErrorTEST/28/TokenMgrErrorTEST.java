package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        // Test case 1: Empty string
        String result = TokenMgrError.addEscapes("");
        assertEquals("", result);

        // Test case 2: String with special characters
        result = TokenMgrError.addEscapes("\\test\n");
        assertEquals("\\test\\n", result);

        // Test case 3: String with unicode characters
        result = TokenMgrError.addEscapes("hello\u005Cu00E9");
        assertEquals("hello\\u00E9", result);
    }

    @Test
    public void testLexicalError() {
        // Test case 1: EOFSeen = true
        String result = TokenMgrError.LexicalError(true, 0, 1, 2, "error", 'a');
        assertEquals("Lexical error at line 1, column 2.  Encountered: <EOF> after : \"error\"", result);

        // Test case 2: EOFSeen = false
        result = TokenMgrError.LexicalError(false, 1, 3, 4, "error", 'b');
        assertEquals("Lexical error at line 3, column 4.  Encountered: \"b\" (98), after : \"error\"", result);
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("message", 2);
        String result = error.getMessage();
        assertEquals("message", result);
    }

    @Test
    public void testConstructor() {
        // Test case 1: No arguments
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
        assertEquals(0, error.errorCode);

        // Test case 2: With message and reason
        error = new TokenMgrError("message", 3);
        assertEquals("message", error.getMessage());
        assertEquals(3, error.errorCode);

        // Test case 3: With all arguments
        error = new TokenMgrError(false, 2, 1, 4, "error", 'c', 1);
        assertEquals("Lexical error at line 1, column 4.  Encountered: \"c\" (99), after : \"error\"", error.getMessage());
        assertEquals(1, error.errorCode);
    }
}