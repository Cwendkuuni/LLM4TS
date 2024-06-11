
package com.pmdesigns.jvc.tools;

import org.junit.Assert;
import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "\b\t\n\f\r\"\'\\";
        String expectedOutput = "\\b\\t\\n\\f\\r\\\"\\\'\\\\";
        String actualOutput = TokenMgrError.addEscapes(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAddEscapesWithUnicode() {
        String input = "\u1234";
        String expectedOutput = "\\u1234";
        String actualOutput = TokenMgrError.addEscapes(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testLexicalErrorWithEOF() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "abc";
        char curChar = 'z';
        int reason = TokenMgrError.LEXICAL_ERROR;

        String expectedOutput = "Lexical error at line 5, column 10. Encountered: <EOF>  after : \"abc\"";
        String actualOutput = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testLexicalErrorWithoutEOF() {
        boolean EOFSeen = false;
        int lexState = 1;
        int errorLine = 2;
        int errorColumn = 3;
        String errorAfter = "def";
        char curChar = 'y';
        int reason = TokenMgrError.STATIC_LEXER_ERROR;

        String expectedOutput = "Lexical error at line 2, column 3. Encountered: \"y\" (121), after : \"def\"";
        String actualOutput = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGetMessage() {
        String message = "Test Message";
        int reason = TokenMgrError.INVALID_LEXICAL_STATE;

        TokenMgrError tokenMgrError = new TokenMgrError(message, reason);

        String expectedOutput = message;
        String actualOutput = tokenMgrError.getMessage();
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testConstructorWithMessageAndReason() {
        String message = "Test Message";
        int reason = TokenMgrError.LOOP_DETECTED;

        TokenMgrError tokenMgrError = new TokenMgrError(message, reason);

        String expectedOutput = message;
        String actualOutput = tokenMgrError.getMessage();
        Assert.assertEquals(expectedOutput, actualOutput);
        Assert.assertEquals(reason, tokenMgrError.errorCode);
    }

    @Test
    public void testConstructorWithLexicalError() {
        boolean EOFSeen = true;
        int lexState = 3;
        int errorLine = 6;
        int errorColumn = 11;
        String errorAfter = "ghi";
        char curChar = 'x';
        int reason = TokenMgrError.LEXICAL_ERROR;

        TokenMgrError tokenMgrError = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);

        String expectedOutput = "Lexical error at line 6, column 11. Encountered: <EOF>  after : \"ghi\"";
        String actualOutput = tokenMgrError.getMessage();
        Assert.assertEquals(expectedOutput, actualOutput);
        Assert.assertEquals(reason, tokenMgrError.errorCode);
    }
}
