package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String str = "\b";
        String expected = "\\b";
        String actual = TokenMgrError.addEscapes(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testLexicalError() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "";
        char curChar = 'a';
        String expected = "Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"\"";
        String actual = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Error message", 1);
        String expected = "Error message";
        String actual = error.getMessage();
        assertEquals(expected, actual);
    }

    @Test
    public void testTokenMgrErrorWithoutReason() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
    }

    @Test
    public void testTokenMgrErrorWithReason() {
        TokenMgrError error = new TokenMgrError("Error message", 1);
        String expected = "Error message";
        String actual = error.getMessage();
        assertEquals(expected, actual);
        assertEquals(1, error.errorCode);
    }

    @Test
    public void testTokenMgrErrorWithLexicalData() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "";
        char curChar = 'a';
        int reason = 1;
        String expected = "Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"\"";
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String actual = error.getMessage();
        assertEquals(expected, actual);
        assertEquals(reason, error.errorCode);
    }

}