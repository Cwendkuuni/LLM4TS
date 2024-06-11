
package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test message", TokenMgrError.LEXICAL_ERROR);
        assertEquals("Test message", error.getMessage());
    }

    @Test
    public void testEmptyConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
    }

    @Test
    public void testConstructor() {
        TokenMgrError error = new TokenMgrError("Test message", TokenMgrError.LEXICAL_ERROR);
        assertEquals("Test message", error.getMessage());
        assertEquals(TokenMgrError.LEXICAL_ERROR, error.errorCode);
    }

    @Test
    public void testLexicalError() {
        TokenMgrError error = new TokenMgrError(true, 0, 1, 2, "Error", 'a', TokenMgrError.LEXICAL_ERROR);
        assertEquals("Lexical error at line 1, column 2.  Encountered: \"a\" (97), after : \"Error\"", error.getMessage());
        assertEquals(TokenMgrError.LEXICAL_ERROR, error.errorCode);
    }

}
