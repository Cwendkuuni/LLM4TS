package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {
    
    @Test
    public void testAddEscapes() {
        assertEquals("", TokenMgrError.addEscapes(""));
        assertEquals("\\n", TokenMgrError.addEscapes("\n"));
        assertEquals("\\t", TokenMgrError.addEscapes("\t"));
        assertEquals("\\r", TokenMgrError.addEscapes("\r"));
        assertEquals("\\\"", TokenMgrError.addEscapes("\""));
        assertEquals("\'", TokenMgrError.addEscapes("'"));
        assertEquals("\\\\", TokenMgrError.addEscapes("\\"));
        assertEquals("\\u003F", TokenMgrError.addEscapes("?"));
    }

    @Test
    public void testLexicalError() {
        assertEquals("Lexical error at line 2, column 3.  Encountered: \"a\" (97), after : \"bc\"", TokenMgrError.LexicalError(false, 0, 2, 3, "bc", 'a'));
        assertEquals("Lexical error at line 5, column 6.  Encountered: <EOF> , after : \"def\"", TokenMgrError.LexicalError(true, 1, 5, 6, "def", '\0'));
    }

    @Test
    public void testGetMessage() {
        TokenMgrError tokenMgrError = new TokenMgrError("Test Message", 1);
        assertEquals("Test Message", tokenMgrError.getMessage());
    }

    @Test
    public void testConstructor1() {
        TokenMgrError tokenMgrError = new TokenMgrError();
        assertNull(tokenMgrError.getMessage());
        assertEquals(0, tokenMgrError.errorCode);
    }

    @Test
    public void testConstructor2() {
        TokenMgrError tokenMgrError = new TokenMgrError("Test Message", 2);
        assertEquals("Test Message", tokenMgrError.getMessage());
        assertEquals(2, tokenMgrError.errorCode);
    }

    @Test
    public void testConstructor3() {
        TokenMgrError tokenMgrError = new TokenMgrError(false, 2, 4, 5, "ghi", 'j', 3);
        assertEquals("Lexical error at line 4, column 5.  Encountered: \"j\" (106), after : \"ghi\"", tokenMgrError.getMessage());
        assertEquals(3, tokenMgrError.errorCode);
    }
}