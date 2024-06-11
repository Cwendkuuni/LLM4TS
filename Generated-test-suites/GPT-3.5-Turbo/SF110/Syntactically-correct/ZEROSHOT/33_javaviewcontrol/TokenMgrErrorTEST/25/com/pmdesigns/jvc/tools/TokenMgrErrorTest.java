package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test Message", 1);
        assertEquals("Test Message", error.getMessage());
    }

    @Test
    public void testTokenMgrError() {
        TokenMgrError error = new TokenMgrError();
        assertNotNull(error);
    }

    @Test
    public void testTokenMgrErrorWithMessageAndReason() {
        TokenMgrError error = new TokenMgrError("Test Message", 1);
        assertEquals("Test Message", error.getMessage());
        assertEquals(1, error.errorCode);
    }

    @Test
    public void testTokenMgrErrorWithLexicalError() {
        TokenMgrError error = new TokenMgrError(true, 0, 1, 2, "Error", 'c', 3);
        String expected = "Lexical error at line 1, column 2.  Encountered: \"c\" (99), after : \"Error\"";
        assertEquals(expected, error.getMessage());
        assertEquals(3, error.errorCode);
    }

}