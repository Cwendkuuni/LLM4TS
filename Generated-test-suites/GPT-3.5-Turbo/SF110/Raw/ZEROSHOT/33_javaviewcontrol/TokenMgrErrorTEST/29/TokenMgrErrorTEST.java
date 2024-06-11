package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());

        TokenMgrError errorWithMessage = new TokenMgrError("Error message", 0);
        assertEquals("Error message", errorWithMessage.getMessage());
    }

    @Test
    public void testConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertEquals(0, error.errorCode);

        TokenMgrError errorWithCode = new TokenMgrError("Error message", 1);
        assertEquals(1, errorWithCode.errorCode);
        assertEquals("Error message", errorWithCode.getMessage());
    }

    @Test
    public void testLexicalError() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 2;
        String errorAfter = "after";
        char curChar = 'a';
        int reason = 0;

        String expected = "Lexical error at line 1, column 2. Encountered: <EOF>  after : \"after\"";
        String result = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expected, result);

        char curChar2 = '\'';
        String expected2 = "Lexical error at line 1, column 2. Encountered: \"'\" (39), after : \"after\"";
        String result2 = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar2);
        assertEquals(expected2, result2);
    }

}