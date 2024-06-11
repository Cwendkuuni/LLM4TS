package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "teststring\\n";
        String expectedOutput = "teststring\\n";
        String actualOutput = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testLexicalError() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 10;
        int errorColumn = 5;
        String errorAfter = "error";
        char curChar = 'a';
        String expectedOutput = "Lexical error at line 10, column 5.  Encountered: \"a (97), after : \"error\"";
        String actualOutput = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGetMessage() {
        TokenMgrError tokenMgrError = new TokenMgrError("Test Message", 1);
        String expectedOutput = "Test Message";
        String actualOutput = tokenMgrError.getMessage();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTokenMgrErrorConstructor1() {
        TokenMgrError tokenMgrError = new TokenMgrError();
        assertNotNull(tokenMgrError);
    }

    @Test
    public void testTokenMgrErrorConstructor2() {
        TokenMgrError tokenMgrError = new TokenMgrError("Test Message", 1);
        assertNotNull(tokenMgrError);
        assertEquals("Test Message", tokenMgrError.getMessage());
        assertEquals(1, tokenMgrError.errorCode);
    }

    @Test
    public void testTokenMgrErrorConstructor3() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 10;
        int errorColumn = 5;
        String errorAfter = "error";
        char curChar = 'a';
        int reason = 1;
        TokenMgrError tokenMgrError = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertNotNull(tokenMgrError);
        assertEquals("Lexical error at line 10, column 5.  Encountered: \"a (97), after : \"error\"", tokenMgrError.getMessage());
        assertEquals(reason, tokenMgrError.errorCode);
    }    
}