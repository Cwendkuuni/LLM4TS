
package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes_NullInput_ReturnsEmptyString() {
        String result = TokenMgrError.addEscapes(null);
        assertEquals("", result);
    }

    @Test
    public void testAddEscapes_EmptyString_ReturnsEmptyString() {
        String result = TokenMgrError.addEscapes("");
        assertEquals("", result);
    }

    @Test
    public void testAddEscapes_StringWithSpecialCharacters_ReturnsEscapedString() {
        String input = "\b\t\n\f\r\"\'\\";
        String result = TokenMgrError.addEscapes(input);
        assertEquals("\\b\\t\\n\\f\\r\\\"\\\'\\\\", result);
    }

    @Test
    public void testAddEscapes_StringWithUnicodeCharacters_ReturnsEscapedString() {
        String input = "Testing unicode character: \u00E9";
        String result = TokenMgrError.addEscapes(input);
        assertEquals("Testing unicode character: \\u00E9", result);
    }

    @Test
    public void testLexicalError_EOFSeen_ReturnsCorrectErrorMessage() {
        String result = TokenMgrError.LexicalError(true, 1, 1, 1, "errorAfter", 'a');
        assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF> after : \"errorAfter\"", result);
    }

    @Test
    public void testLexicalError_NotEOFSeen_ReturnsCorrectErrorMessage() {
        String result = TokenMgrError.LexicalError(false, 1, 1, 1, "errorAfter", 'a');
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"errorAfter\"", result);
    }

    @Test
    public void testGetMessage_DefaultConstructor_ReturnsNull() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
    }

    @Test
    public void testGetMessage_ConstructorWithMessage_ReturnsCorrectMessage() {
        TokenMgrError error = new TokenMgrError("Error Message", 1);
        assertEquals("Error Message", error.getMessage());
    }

    @Test
    public void testConstructor_WithEOFSeen_ReturnsCorrectErrorMessage() {
        TokenMgrError error = new TokenMgrError(true, 1, 1, 1, "errorAfter", 'a', 2);
        assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF> after : \"errorAfter\"", error.getMessage());
        assertEquals(2, error.errorCode);
    }

    @Test
    public void testConstructor_WithoutEOFSeen_ReturnsCorrectErrorMessage() {
        TokenMgrError error = new TokenMgrError(false, 1, 1, 1, "errorAfter", 'a', 2);
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"errorAfter\"", error.getMessage());
        assertEquals(2, error.errorCode);
    }

}
