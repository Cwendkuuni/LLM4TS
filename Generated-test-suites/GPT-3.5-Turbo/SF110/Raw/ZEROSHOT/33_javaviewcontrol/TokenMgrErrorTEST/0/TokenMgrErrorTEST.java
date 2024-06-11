
package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        // Test case 1
        assertEquals("\\b", TokenMgrError.addEscapes("\b"));
        // Test case 2
        assertEquals("\\t", TokenMgrError.addEscapes("\t"));
        // Test case 3
        assertEquals("\\n", TokenMgrError.addEscapes("\n"));
        // Test case 4
        assertEquals("\\f", TokenMgrError.addEscapes("\f"));
        // Test case 5
        assertEquals("\\r", TokenMgrError.addEscapes("\r"));
        // Test case 6
        assertEquals("\\\"", TokenMgrError.addEscapes("\""));
        // Test case 7
        assertEquals("\\'", TokenMgrError.addEscapes("'"));
        // Test case 8
        assertEquals("\\\\", TokenMgrError.addEscapes("\\"));
        // Test case 9
        assertEquals("\\u0010", TokenMgrError.addEscapes("\u0010"));
        // Test case 10
        assertEquals("A", TokenMgrError.addEscapes("A"));
    }

    @Test
    public void testLexicalError() {
        // Test case 1
        assertEquals("Lexical error at line 1, column 2.  Encountered: \"A\" (65), after : \"BCD\"",
                TokenMgrError.LexicalError(false, 1, 2, "BCD", 'A'));
        // Test case 2
        assertEquals("Lexical error at line 2, column 5.  Encountered: <EOF> after : \"EFG\"",
                TokenMgrError.LexicalError(true, 2, 5, "EFG", '\0'));
    }

    @Test
    public void testGetMessage() {
        // Test case 1
        TokenMgrError error1 = new TokenMgrError("Error Message 1", 0);
        assertEquals("Error Message 1", error1.getMessage());
        // Test case 2
        TokenMgrError error2 = new TokenMgrError("Error Message 2", 1);
        assertEquals("Error Message 2", error2.getMessage());
        // Test case 3
        TokenMgrError error3 = new TokenMgrError("Error Message 3", 2);
        assertEquals("Error Message 3", error3.getMessage());
    }

    @Test
    public void testTokenMgrError() {
        // Test case 1
        TokenMgrError error1 = new TokenMgrError();
        assertNull(error1.getMessage());
        assertEquals(0, error1.errorCode);
        // Test case 2
        TokenMgrError error2 = new TokenMgrError("Error Message", 3);
        assertEquals("Error Message", error2.getMessage());
        assertEquals(3, error2.errorCode);
    }

    @Test
    public void testTokenMgrErrorWithParams() {
        // Test case 1
        TokenMgrError error1 = new TokenMgrError(false, 1, 2, "BCD", 'A', 0);
        assertEquals("Lexical error at line 1, column 2.  Encountered: \"A\" (65), after : \"BCD\"", error1.getMessage());
        assertEquals(0, error1.errorCode);
        // Test case 2
        TokenMgrError error2 = new TokenMgrError(true, 2, 5, "EFG", '\0', 1);
        assertEquals("Lexical error at line 2, column 5.  Encountered: <EOF> after : \"EFG\"", error2.getMessage());
        assertEquals(1, error2.errorCode);
    }
}
