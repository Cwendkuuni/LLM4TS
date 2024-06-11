package com.pmdesigns.jvc.tools;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {

    private TokenMgrError tokenMgrError;

    @Before
    public void setUp() {
        tokenMgrError = new TokenMgrError();
    }

    @Test
    public void testGetMessage() {
        tokenMgrError = new TokenMgrError("Test Message", 0);
        assertEquals("Test Message", tokenMgrError.getMessage());
    }

    @Test
    public void testTokenMgrError() {
        tokenMgrError = new TokenMgrError();
        assertEquals(0, tokenMgrError.errorCode);
    }

    @Test
    public void testTokenMgrErrorWithMessageAndReason() {
        tokenMgrError = new TokenMgrError("Test Message", 1);
        assertEquals("Test Message", tokenMgrError.getMessage());
        assertEquals(1, tokenMgrError.errorCode);
    }

    @Test
    public void testTokenMgrErrorWithLexicalError() {
        tokenMgrError = new TokenMgrError(false, 1, 10, 5, "TestAfter", 'A', 0);
        String expectedMessage = "Lexical error at line 10, column 5.  Encountered: \"A\" (65), after : \"TestAfter\"";
        assertEquals(expectedMessage, tokenMgrError.getMessage());
        assertEquals(0, tokenMgrError.errorCode);
    }
}