package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokenMgrErrorTest {
    
    @Test
    public void testAddEscapes() {
        assertEquals("", TokenMgrError.addEscapes(""));
        assertEquals("\\b", TokenMgrError.addEscapes("\b"));
        assertEquals("\\t", TokenMgrError.addEscapes("\t"));
        assertEquals("\\n", TokenMgrError.addEscapes("\n"));
        assertEquals("\\f", TokenMgrError.addEscapes("\f"));
        assertEquals("\\r", TokenMgrError.addEscapes("\r"));
        assertEquals("\\\"", TokenMgrError.addEscapes("\""));
        assertEquals("\\'", TokenMgrError.addEscapes("\'"));
        assertEquals("\\\\", TokenMgrError.addEscapes("\\"));
        assertEquals("\\u0000", TokenMgrError.addEscapes("\0"));
        assertEquals("\\u0019", TokenMgrError.addEscapes("\u0019"));
        assertEquals("abc", TokenMgrError.addEscapes("abc"));
    }
    
    @Test
    public void testLexicalError() {
        assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF>  after : \"\"", TokenMgrError.LexicalError(true, 0, 1, 1, "", '\0'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"a\" (97),  after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", 'a'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\b\" (8),  after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", '\b'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\t\" (9),  after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", '\t'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\n\" (10),  after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", '\n'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\f\" (12),  after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", '\f'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\r\" (13),  after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", '\r'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\\\"\" (34),  after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", '\"'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\'\" (39),  after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", '\''));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\\\\\" (92),  after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", '\\'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\\u0000\" (0),  after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", '\0'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"\\u0019\" (25),  after : \"\"", TokenMgrError.LexicalError(false, 0, 1, 1, "", '\u0019'));
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"a\" (97), abc", TokenMgrError.LexicalError(false, 0, 1, 1, "abc", 'a'));
    }
    
    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("");
        assertEquals("", error.getMessage());
        
        error = new TokenMgrError("Error Message", 0);
        assertEquals("Error Message", error.getMessage());
    }
    
    @Test
    public void testTokenMgrError() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
        
        error = new TokenMgrError("Error Message", 0);
        assertEquals("Error Message", error.getMessage());
        assertEquals(0, error.errorCode);
    }
    
    @Test
    public void testTokenMgrErrorWithDetails() {
        TokenMgrError error = new TokenMgrError(true, 0, 1, 1, "", '\0', 1);
        assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF>  after : \"\"", error.getMessage());
        assertEquals(1, error.errorCode);
    }
}