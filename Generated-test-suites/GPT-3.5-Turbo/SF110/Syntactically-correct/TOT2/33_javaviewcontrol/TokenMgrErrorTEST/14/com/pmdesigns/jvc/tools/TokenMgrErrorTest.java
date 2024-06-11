package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "Test\nString";
        String expected = "Test\\nString";
        String result = TokenMgrError.addEscapes(input);
        assertEquals(expected, result);
    }

    @Test
    public void testLexicalError() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 5;
        String errorAfter = "Test";
        char curChar = 'A';
        String expected = "Lexical error at line 1, column 5.  Encountered: <EOF>  after : \"Test\"";
        String result = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expected, result);
    }

    @Test
    public void testGetMessage() {
        TokenMgrError tokenMgrError = new TokenMgrError("Error Message", 0);
        String expected = "Error Message";
        String result = tokenMgrError.getMessage();
        assertEquals(expected, result);
    }

    @Test
    public void testNoArgConstructor() {
        TokenMgrError tokenMgrError = new TokenMgrError();
        assertEquals(null, tokenMgrError.getMessage());
    }

    @Test
    public void testParamConstructor() {
        TokenMgrError tokenMgrError = new TokenMgrError("Error Message", 1);
        assertEquals("Error Message", tokenMgrError.getMessage());
    }

    @Test
    public void testFullConstructor() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 5;
        String errorAfter = "Test";
        char curChar = 'A';
        int reason = 2;

        TokenMgrError tokenMgrError = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String expected = "Lexical error at line 1, column 5.  Encountered: <EOF>  after : \"Test\"";
        assertEquals(expected, tokenMgrError.getMessage());
    }

}