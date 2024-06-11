package com.pmdesigns.jvc.tools;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String str = "TestString";
        String expected = "TestString";
        String actual = TokenMgrError.addEscapes(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testLexicalError() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 2;
        String errorAfter = "Test";
        char curChar = 'c';
        String expected = "Lexical error at line 1, column 2.  Encountered: \"c\" (99), after : \"Test\"";
        String actual = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("TestError", 0);
        String expected = "TestError";
        String actual = error.getMessage();
        assertEquals(expected, actual);
    }

    @Test
    public void testConstructor1() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
        assertEquals(0, error.errorCode);
    }

    @Test
    public void testConstructor2() {
        TokenMgrError error = new TokenMgrError("TestError", 1);
        assertEquals("TestError", error.getMessage());
        assertEquals(1, error.errorCode);
    }

    @Test
    public void testConstructor3() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 2;
        String errorAfter = "Test";
        char curChar = 'c';
        int reason = 3;
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String expected = "Lexical error at line 1, column 2.  Encountered: \"c\" (99), after : \"Test\"";
        assertEquals(expected, error.getMessage());
        assertEquals(reason, error.errorCode);
    }

}