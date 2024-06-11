package com.pmdesigns.jvc.tools;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());

        TokenMgrError error2 = new TokenMgrError("This is an error", 1);
        assertEquals("This is an error", error2.getMessage());
    }

    @Test
    public void testConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertEquals(0, error.errorCode);

        TokenMgrError error2 = new TokenMgrError("This is an error", 1);
        assertEquals("This is an error", error2.getMessage());
        assertEquals(1, error2.errorCode);

        TokenMgrError error3 = new TokenMgrError(true, 0, 1, 2, "ErrorAfter", 'c', 2);
        assertEquals("Lexical error at line 1, column 2.  Encountered: \"c\" (99), after : \"ErrorAfter\"", error3.getMessage());
        assertEquals(2, error3.errorCode);
    }

    @Test
    public void testAddEscapes() {
        String str = "";
        assertEquals("", TokenMgrError.addEscapes(str));

        String str2 = "This is a string";
        assertEquals("This is a string", TokenMgrError.addEscapes(str2));

        String str3 = "This is a\nmultiline\nstring";
        assertEquals("This is a\\nmultiline\\nstring", TokenMgrError.addEscapes(str3));

        String str4 = "This is a\tstring";
        assertEquals("This is a\\tstring", TokenMgrError.addEscapes(str4));

        String str5 = "This is a\bstring";
        assertEquals("This is a\\bstring", TokenMgrError.addEscapes(str5));

        String str6 = "This is a\"string\"";
        assertEquals("This is a\\\"string\\\"", TokenMgrError.addEscapes(str6));

        String str7 = "This is a\'string\'";
        assertEquals("This is a\\'string\\'", TokenMgrError.addEscapes(str7));

        String str8 = "This is a\\string";
        assertEquals("This is a\\\\string", TokenMgrError.addEscapes(str8));

        String str9 = "This is a \u0014 utf-8 character";
        assertEquals("This is a \\u0014 utf-8 character", TokenMgrError.addEscapes(str9));
    }

    @Test
    public void testLexicalError() {
        String errorMessage = TokenMgrError.LexicalError(true, 0, 1, 2, "ErrorAfter", 'c');
        assertEquals("Lexical error at line 1, column 2.  Encountered: \"c\" (99), after : \"ErrorAfter\"", errorMessage);
    }
}