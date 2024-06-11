package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenMgrErrorTest {
    @Test
    public void testAddEscapes() {
        assertEquals("\\b", TokenMgrError.addEscapes("\b"));
        assertEquals("\\t", TokenMgrError.addEscapes("\t"));
        assertEquals("\\n", TokenMgrError.addEscapes("\n"));
        assertEquals("\\f", TokenMgrError.addEscapes("\f"));
        assertEquals("\\r", TokenMgrError.addEscapes("\r"));
        assertEquals("\\\"", TokenMgrError.addEscapes("\""));
        assertEquals("\\'", TokenMgrError.addEscapes("'"));
        assertEquals("\\\\", TokenMgrError.addEscapes("\\"));
        assertEquals("\\u0000", TokenMgrError.addEscapes("\0"));
        assertEquals("\\u001f", TokenMgrError.addEscapes("\u001f"));
        assertEquals("a", TokenMgrError.addEscapes("a"));
        assertEquals("A", TokenMgrError.addEscapes("A"));
        assertEquals(" ", TokenMgrError.addEscapes(" "));
        assertEquals("~", TokenMgrError.addEscapes("~"));
    }

    @Test
    public void testLexicalError() {
        assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF> ", TokenMgrError.LexicalError(true, 0, 1, 1, "", '\0'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"A\" (65), after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", 'A'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\\t\" (9), after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", '\t'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\\\"\" (34), after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", '\"'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"'\" (39), after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", '\''));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\\\\\" (92), after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", '\\'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\\u0000\" (0), after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", '\0'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\\u001f\" (31), after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", '\u001f'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", 'a'));
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test Message", 0);
        assertEquals("Test Message", error.getMessage());
    }

    @Test
    public void testConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertEquals(0, error.errorCode);
        
        error = new TokenMgrError("Test Message", 1);
        assertEquals("Test Message", error.getMessage());
        assertEquals(1, error.errorCode);
        
    }

    @Test
    public void testFullConstructor() {
        TokenMgrError error = new TokenMgrError(true, 0, 1, 1, "", '\0', 2);
        assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF> ", error.getMessage());
        assertEquals(2, error.errorCode);
        
        error = new TokenMgrError(false, 0, 1, 1, "", 'A', 3);
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"A\" (65), after : \"\"", error.getMessage());
        assertEquals(3, error.errorCode);
    }
}