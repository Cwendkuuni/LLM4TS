package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test Message", 0);
        assertEquals("Test Message", error.getMessage());
    }

    @Test
    public void testTokenMgrErrorDefaultConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
        assertEquals(0, error.errorCode);
    }

    @Test
    public void testTokenMgrErrorConstructor() {
        TokenMgrError error = new TokenMgrError("Test Message", 1);
        assertEquals("Test Message", error.getMessage());
        assertEquals(1, error.errorCode);
    }

    @Test
    public void testTokenMgrErrorLexicalErrorConstructor() {
        TokenMgrError error = new TokenMgrError(true, 0, 1, 2, "Test", 'c', 3);
        assertEquals("Lexical error at line 1, column 2.  Encountered: \"c\" (99), after : \"Test\"", error.getMessage());
        assertEquals(3, error.errorCode);
    }

}