package com.pmdesigns.jvc.tools;

import org.junit.Assert;
import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage() {
        TokenMgrError tokenMgrError = new TokenMgrError();
        Assert.assertNull(tokenMgrError.getMessage());
    }

    @Test
    public void testTokenMgrErrorStringInt() {
        String message = "Test Message";
        int reason = TokenMgrError.LEXICAL_ERROR;

        TokenMgrError tokenMgrError = new TokenMgrError(message, reason);

        Assert.assertEquals(message, tokenMgrError.getMessage());
        Assert.assertEquals(reason, tokenMgrError.errorCode);
    }

    @Test
    public void testLexicalError() {
        boolean EOFSeen = true;
        int lexState = 2;
        int errorLine = 1;
        int errorColumn = 2;
        String errorAfter = "Test Error";
        char curChar = 'a';
        int reason = TokenMgrError.STATIC_LEXER_ERROR;

        String expected = "Lexical error at line 1, column 2. Encountered: <EOF> after : \"Test Error\"";

        Assert.assertEquals(expected, TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
    }
}