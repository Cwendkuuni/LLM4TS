package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("test message", 0);
        assertEquals("test message", error.getMessage());
    }
    
    @Test
    public void testConstructorWithMessageAndReason() {
        TokenMgrError error = new TokenMgrError("test message", 1);
        assertEquals("test message", error.getMessage());
        assertEquals(1, error.errorCode);
    }
    
    @Test
    public void testConstructorWithLexicalError() {
        TokenMgrError error = new TokenMgrError(true, 0, 1, 2, "test", 'a', 2);
        assertEquals("Lexical error at line 1, column 2.  Encountered: \"a\" (97), after : \"test\"", error.getMessage());
        assertEquals(2, error.errorCode);
    }

}