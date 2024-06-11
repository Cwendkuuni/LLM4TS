package com.pmdesigns.jvc.tools;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "Hello\nWorld";
        String expected = "Hello\\nWorld";
        assertEquals(expected, TokenMgrError.addEscapes(input));
    }

    @Test
    public void testLexicalError() {
        boolean EOFSeen = false;
        int lexState = 1;
        int errorLine = 10;
        int errorColumn = 5;
        String errorAfter = "token";
        char curChar = 'a';
        String expected = "Lexical error at line 10, column 5.  Encountered: \"a\" (97), after : \"token\"";
        assertEquals(expected, TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Error message", 1);
        assertEquals("Error message", error.getMessage());
    }

    @Test
    public void testConstructor1() {
        TokenMgrError error = new TokenMgrError();
        assertNotNull(error);
    }

    @Test
    public void testConstructor2() {
        TokenMgrError error = new TokenMgrError("Error message", 1);
        assertNotNull(error);
        assertEquals("Error message", error.getMessage());
        assertEquals(1, error.errorCode);
    }

    @Test
    public void testConstructor3() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 5;
        int errorColumn = 3;
        String errorAfter = "abc";
        char curChar = '3';
        int reason = 2;
        String expectedMessage = "Lexical error at line 5, column 3.  Encountered: \"3\" (51), after : \"abc\"";
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertNotNull(error);
        assertEquals(expectedMessage, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
}