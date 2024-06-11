package com.pmdesigns.jvc.tools;

import org.junit.Assert;
import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test Message", TokenMgrError.LEXICAL_ERROR);
        Assert.assertEquals("Test Message", error.getMessage());
    }

    @Test
    public void testConstructor() {
        TokenMgrError error = new TokenMgrError("Test Message", TokenMgrError.LEXICAL_ERROR);
        Assert.assertEquals("Test Message", error.getMessage());
        Assert.assertEquals(TokenMgrError.LEXICAL_ERROR, error.errorCode);
    }

    @Test
    public void testLexicalError() {
        String expectedError = "Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"\"";
        TokenMgrError error = new TokenMgrError(false, 0, 1, 1, "", 'a', TokenMgrError.LEXICAL_ERROR);
        Assert.assertEquals(expectedError, error.getMessage());
        Assert.assertEquals(TokenMgrError.LEXICAL_ERROR, error.errorCode);
    }

}