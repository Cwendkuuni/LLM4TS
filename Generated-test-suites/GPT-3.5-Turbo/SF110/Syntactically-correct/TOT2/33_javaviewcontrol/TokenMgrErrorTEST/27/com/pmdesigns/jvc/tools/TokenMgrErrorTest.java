package com.pmdesigns.jvc.tools;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "test\t\n1234";
        String output = TokenMgrError.addEscapes(input);
        assertEquals("test\\t\\n1234", output);
    }

    @Test
    public void testLexicalError() {
        String error = TokenMgrError.LexicalError(false, 1, 2, 3, "after", 'c');
        assertEquals("Lexical error at line 2, column 3.  Encountered: \"c\" (99), after : \"after\"", error);
    }

    @Test
    public void testGetMessage() {
        TokenMgrError tokenError = new TokenMgrError("Test Message", TokenMgrError.LEXICAL_ERROR);
        assertEquals("Test Message", tokenError.getMessage());
    }

    @Test
    public void testTokenMgrErrorDefaultConstructor() {
        TokenMgrError tokenError = new TokenMgrError();
        assertNotNull(tokenError);
    }

    @Test
    public void testTokenMgrErrorReasonConstructor() {
        TokenMgrError tokenError = new TokenMgrError("Test Message", TokenMgrError.LEXICAL_ERROR);
        assertEquals(TokenMgrError.LEXICAL_ERROR, tokenError.errorCode);
    }

    @Test
    public void testTokenMgrErrorFullConstructor() {
        TokenMgrError tokenError = new TokenMgrError(false, 1, 2, 3, "after", 'c', TokenMgrError.LEXICAL_ERROR);
        assertEquals("Lexical error at line 2, column 3.  Encountered: \"c\" (99), after : \"after\"", tokenError.getMessage());
        assertEquals(TokenMgrError.LEXICAL_ERROR, tokenError.errorCode);
    }
}