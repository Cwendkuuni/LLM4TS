
package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test Error", 0);
        assertEquals("Test Error", error.getMessage());
    }

    @Test
    public void testDefaultConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
        assertEquals(0, error.errorCode);
    }

    @Test
    public void testConstructorWithMessageAndReason() {
        TokenMgrError error = new TokenMgrError("Test Error", 1);
        assertEquals("Test Error", error.getMessage());
        assertEquals(1, error.errorCode);
    }

    @Test
    public void testConstructorWithParams() {
        TokenMgrError error = new TokenMgrError(true, 1, 2, 3, "Test", 'a', 2);
        assertEquals("Lexical error at line 2, column 3.  Encountered: \"a\" (97), after : \"Test\"", error.getMessage());
        assertEquals(2, error.errorCode);
    }

}
