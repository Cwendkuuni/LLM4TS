package com.pmdesigns.jvc.tools;

import org.junit.Assert;
import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError();
        Assert.assertNull(error.getMessage());

        error = new TokenMgrError("Test Message", TokenMgrError.LEXICAL_ERROR);
        Assert.assertEquals("Test Message", error.getMessage());
    }

    @Test
    public void testTokenMgrError() {
        TokenMgrError error = new TokenMgrError();
        Assert.assertEquals(TokenMgrError.LEXICAL_ERROR, error.errorCode);

        error = new TokenMgrError("Test Message", TokenMgrError.LEXICAL_ERROR);
        Assert.assertEquals(TokenMgrError.LEXICAL_ERROR, error.errorCode);
        Assert.assertEquals("Test Message", error.getMessage());

        error = new TokenMgrError(true, 0, 1, 2, "Error After", 'a', TokenMgrError.LEXICAL_ERROR);
        String expectedMessage = "Lexical error at line 1, column 2.  Encountered: \"a\" (97), after : \"Error After\"";
        Assert.assertEquals(TokenMgrError.LEXICAL_ERROR, error.errorCode);
        Assert.assertEquals(expectedMessage, error.getMessage());
    }
}