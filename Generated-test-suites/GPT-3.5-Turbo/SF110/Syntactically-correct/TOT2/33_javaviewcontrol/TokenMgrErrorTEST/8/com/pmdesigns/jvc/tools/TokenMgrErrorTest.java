package com.pmdesigns.jvc.tools;
import org.junit.Assert;
import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "Hello\nWorld";
        String expected = "Hello\\nWorld";
        String actual = TokenMgrError.addEscapes(input);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLexicalError() {
        boolean EOFSeen = false;
        int lexState = 1;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "token";
        char curChar = 'A';
        String expected = "Lexical error at line 5, column 10.  Encountered: \"A\" (65), after : \"token\"";
        String actual = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Error message", 0);
        String expected = "Error message";
        String actual = error.getMessage();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testConstructor1() {
        TokenMgrError error = new TokenMgrError();
        Assert.assertNotNull(error);
    }

    @Test
    public void testConstructor2() {
        TokenMgrError error = new TokenMgrError("Error message", 1);
        Assert.assertEquals("Error message", error.getMessage());
        Assert.assertEquals(1, error.errorCode);
    }

    @Test
    public void testConstructor3() {
        TokenMgrError error = new TokenMgrError(true, 2, 3, 4, "after", 'B', 3);
        Assert.assertNotNull(error);
    }

    // Additional test cases for edge cases and exception handling scenarios can be added here

}