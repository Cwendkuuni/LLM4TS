
package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String result = TokenMgrError.addEscapes("\"Hello, World!\"");
        assertEquals("\\\"Hello, World!\\\"", result);
    }

    @Test
    public void testLexicalError() {
        String result = TokenMgrError.LexicalError(false, 1, 10, 5, "abc", 'a');
        assertEquals("Lexical error at line 10, column 5.  Encountered: \"a\" (97), after : \"abc\"", result);
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test Message", 0);
        assertEquals("Test Message", error.getMessage());
    }

    @Test
    public void testTokenMgrErrorConstructor() {
        TokenMgrError error = new TokenMgrError("Test Message", 1);
        assertEquals("Test Message", error.getMessage());
        assertEquals(1, error.errorCode);
    }

    @Test
    public void testTokenMgrErrorFullConstructor() {
        TokenMgrError error = new TokenMgrError(true, 2, 5, 3, "def", 'd', 2);
        assertEquals("Lexical error at line 5, column 3.  Encountered: \"d\" (100), after : \"def\"", error.getMessage());
        assertEquals(2, error.errorCode);
    }
}