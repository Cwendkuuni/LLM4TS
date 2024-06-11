package com.pmdesigns.jvc.tools;

import org.junit.*;

public class TokenMgrErrorTest {

    @Test
    public void testMessage() {
        TokenMgrError error = new TokenMgrError("Test message", TokenMgrError.LEXICAL_ERROR);
        Assert.assertEquals("Test message", error.getMessage());
    }
    
    @Test
    public void testErrorCode() {
        TokenMgrError error = new TokenMgrError("Test message", TokenMgrError.LEXICAL_ERROR);
        Assert.assertEquals(TokenMgrError.LEXICAL_ERROR, error.errorCode);
    }
    
    @Test
    public void testLexicalError() {
        String expectedErrorMessage = "Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"\"";
        TokenMgrError error = new TokenMgrError(false, 0, 1, 1, "", 'a', TokenMgrError.LEXICAL_ERROR);
        Assert.assertEquals(expectedErrorMessage, error.getMessage());
        Assert.assertEquals(TokenMgrError.LEXICAL_ERROR, error.errorCode);
    }
}