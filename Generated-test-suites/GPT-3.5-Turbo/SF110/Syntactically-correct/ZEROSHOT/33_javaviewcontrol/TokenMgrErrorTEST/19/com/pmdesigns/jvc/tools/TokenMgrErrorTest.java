package com.pmdesigns.jvc.tools;

import org.junit.*;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test message", 0);
        String expected = "Test message";
        String actual = error.getMessage();
        assertEquals(expected, actual);
    }

    @Test
    public void testTokenMgrError() {
        TokenMgrError error = new TokenMgrError();
        assertNotNull(error);
    }

    @Test
    public void testTokenMgrErrorWithMessageAndReason() {
        TokenMgrError error = new TokenMgrError("Test message", 0);
        String expected = "Test message";
        String actual = error.getMessage();
        assertEquals(expected, actual);
        int expectedReason = 0;
        int actualReason = error.errorCode;
        assertEquals(expectedReason, actualReason);
    }

    @Test
    public void testTokenMgrErrorWithParameters() {
        TokenMgrError error = new TokenMgrError(true, 1, 2, 3, "Test", 'a', 0);
        String expectedMessage = "Lexical error at line 2, column 3.  Encountered: \"a\" (97), after : \"Test\"";
        String actualMessage = error.getMessage();
        assertEquals(expectedMessage, actualMessage);
        int expectedReason = 0;
        int actualReason = error.errorCode;
        assertEquals(expectedReason, actualReason);
    }
}