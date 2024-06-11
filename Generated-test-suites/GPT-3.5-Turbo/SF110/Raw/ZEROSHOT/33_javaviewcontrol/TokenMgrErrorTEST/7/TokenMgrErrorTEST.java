package com.pmdesigns.jvc.tools;

import org.junit.Assert;
import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test Message", 1);
        String message = error.getMessage();
        Assert.assertEquals("Test Message", message);
    }

    @Test
    public void testTokenMgrErrorNoArgs() {
        TokenMgrError error = new TokenMgrError();
        Assert.assertNull(error.getMessage());
    }

    @Test
    public void testTokenMgrError() {
        TokenMgrError error = new TokenMgrError("Test Message", 1);
        Assert.assertEquals("Test Message", error.getMessage());
        Assert.assertEquals(1, error.errorCode);
    }

    @Test
    public void testTokenMgrErrorWithLexicalError() {
        TokenMgrError error = new TokenMgrError(true, 0, 1, 1, "Error", 'a', 1);
        Assert.assertEquals("Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"Error\"", error.getMessage());
        Assert.assertEquals(1, error.errorCode);
    }

}