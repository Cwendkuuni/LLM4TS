package com.pmdesigns.jvc.tools;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenMgrErrorTest {
    
    @Test
    public void testAddEscapes() {
        String input = "Test\ting\nquotes\"escape'";
        String expectedOutput = "Test\\ting\\nquotes\\\"escape\\'";
        String actualOutput = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testLexicalError() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 0;
        String errorAfter = "error after";
        char curChar = 'a';
        String expectedOutput = "Lexical error at line 1, column 0.  Encountered: <EOF> after : \"error after\"";
        String actualOutput = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test message", TokenMgrError.LEXICAL_ERROR);
        assertEquals("Test message", error.getMessage());
    }
    
    @Test
    public void testConstructor1() {
        TokenMgrError error = new TokenMgrError();
        assertNotNull(error);
    }
    
    @Test
    public void testConstructor2() {
        TokenMgrError error = new TokenMgrError("Test message", TokenMgrError.LEXICAL_ERROR);
        assertEquals(TokenMgrError.LEXICAL_ERROR, error.errorCode);
    }
    
    @Test
    public void testConstructor3() {
        TokenMgrError error = new TokenMgrError(true, 0, 1, 0, "error after", 'b', TokenMgrError.LEXICAL_ERROR);
        assertNotNull(error);
    }
}