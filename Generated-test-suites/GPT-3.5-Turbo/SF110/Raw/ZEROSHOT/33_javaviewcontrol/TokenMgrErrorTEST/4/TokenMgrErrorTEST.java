package com.pmdesigns.jvc.tools;

import org.junit.Assert;
import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError();
        Assert.assertNull(error.getMessage());
    }
    
    @Test
    public void testConstructor1() {
        String message = "Test Message";
        int reason = 1;
        TokenMgrError error = new TokenMgrError(message, reason);
        Assert.assertEquals(message, error.getMessage());
        Assert.assertEquals(reason, error.errorCode);
    }
    
    @Test
    public void testConstructor2() {
        boolean EOFSeen = true;
        int lexState = 1;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "Error After";
        char curChar = 'c';
        int reason = 1;
        
        String expected = "Lexical error at line " + errorLine + ", column " + errorColumn + ".  Encountered: <EOF> after : \"" + TokenMgrError.addEscapes(errorAfter) + "\"";
        
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        Assert.assertEquals(expected, error.getMessage());
        Assert.assertEquals(reason, error.errorCode);
    }
    
    @Test
    public void testAddEscapes() {
        String str = "Test String";
        String expected = "Test String";
        Assert.assertEquals(expected, TokenMgrError.addEscapes(str));
        
        str = "Test \n String";
        expected = "Test \\n String";
        Assert.assertEquals(expected, TokenMgrError.addEscapes(str));
        
        str = "\b\t\n\f\r\"\'\\";
        expected = "\\b\\t\\n\\f\\r\\\"\\\'\\\\";
        Assert.assertEquals(expected, TokenMgrError.addEscapes(str));
    }
}