package com.pmdesigns.jvc.tools;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "abc\n\tdef";
        String expectedOutput = "abc\\n\\tdef";
        String actualOutput = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testLexicalError() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 5;
        String errorAfter = "abc";
        char curChar = 'd';
        String expectedOutput = "Lexical error at line 1, column 5. Encountered: \"d\" (100), after : \"abc\"";
        String actualOutput = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGetMessage() {
        TokenMgrError tokenMgrError = new TokenMgrError("test message", 1);
        assertEquals("test message", tokenMgrError.getMessage());
    }

    @Test
    public void testTokenMgrErrorConstructorWithoutParams() {
        TokenMgrError tokenMgrError = new TokenMgrError();
        assertNotNull(tokenMgrError);
    }

    @Test
    public void testTokenMgrErrorConstructorWithParams() {
        TokenMgrError tokenMgrError = new TokenMgrError(false, 0, 1, 5, "abc", 'd', 1);
        assertNotNull(tokenMgrError);
    }

}