package com.pmdesigns.jvc.tools;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "\n";
        String expectedOutput = "\\n";
        assertEquals(expectedOutput, TokenMgrError.addEscapes(input));
    }

    @Test
    public void testLexicalError() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "";
        char curChar = 'a';
        String expectedOutput = "Lexical error at line 1, column 1.  Encountered: <EOF>  after : \"\"";
        assertEquals(expectedOutput, TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
    }

    @Test
    public void testGetMessage() {
        String message = "Test message";
        TokenMgrError tokenMgrError = new TokenMgrError(message, TokenMgrError.LEXICAL_ERROR);
        assertEquals(message, tokenMgrError.getMessage());
    }

    @Test
    public void testTokenMgrErrorConstructor() {
        TokenMgrError tokenMgrError = new TokenMgrError();
        assertNotNull(tokenMgrError);
    }

    @Test
    public void testTokenMgrErrorConstructorWithReason() {
        String message = "Test message";
        int reason = TokenMgrError.LEXICAL_ERROR;
        TokenMgrError tokenMgrError = new TokenMgrError(message, reason);
        assertEquals(message, tokenMgrError.getMessage());
        assertEquals(reason, tokenMgrError.errorCode);
    }

    @Test
    public void testTokenMgrErrorConstructorWithDetails() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "";
        char curChar = 'a';
        int reason = TokenMgrError.LEXICAL_ERROR;
        String expectedMessage = "Lexical error at line 1, column 1.  Encountered: <EOF>  after : \"\"";
        TokenMgrError tokenMgrError = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(expectedMessage, tokenMgrError.getMessage());
        assertEquals(reason, tokenMgrError.errorCode);
    }
}