package com.pmdesigns.jvc.tools;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "Hello\tWorld";
        String expected = "Hello\\tWorld";
        assertEquals(expected, TokenMgrError.addEscapes(input));
    }

    @Test
    public void testLexicalError() {
        boolean eofSeen = true;
        int lexState = 1;
        int errorLine = 10;
        int errorColumn = 5;
        String errorAfter = "Error";
        char curChar = 'a';
        String expected = "Lexical error at line 10, column 5.  Encountered: <EOF> after : \"Error\"";
        assertEquals(expected, TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Error message", TokenMgrError.LEXICAL_ERROR);
        assertEquals("Error message", error.getMessage());
    }

    @Test
    public void testDefaultConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
    }

    @Test
    public void testParameterizedConstructor() {
        TokenMgrError error = new TokenMgrError("Error message", TokenMgrError.LEXICAL_ERROR);
        assertEquals("Error message", error.getMessage());
        assertEquals(TokenMgrError.LEXICAL_ERROR, error.errorCode);
    }

    @Test
    public void testComplexConstructor() {
        boolean eofSeen = false;
        int lexState = 2;
        int errorLine = 10;
        int errorColumn = 5;
        String errorAfter = "Error";
        char curChar = 'a';
        int reason = TokenMgrError.STATIC_LEXER_ERROR;
        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String expected = "Lexical error at line 10, column 5.  Encountered: \"a\" (97), after : \"Error\"";
        assertEquals(expected, error.getMessage());
        assertEquals(TokenMgrError.STATIC_LEXER_ERROR, error.errorCode);
    }
}