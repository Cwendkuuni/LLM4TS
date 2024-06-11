package com.pmdesigns.jvc.tools;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenMgrErrorTest {
    
    @Test
    public void testAddEscapes() {
        TokenMgrError error = new TokenMgrError();
        String input = "\n\t\"\'\\abc";
        String expectedOutput = "\\n\\t\\\"\\\'\\\\abc";
        
        String result = TokenMgrError.addEscapes(input);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testLexicalError() {
        TokenMgrError error = new TokenMgrError();
        boolean EOFSeen = true;
        int lexState = 1;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "error";
        char curChar = 'a';
        String expectedOutput = "Lexical error at line 5, column 10.  Encountered: <EOF>  after : \"error\"";
        
        String result = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test message", 1);
        
        String result = error.getMessage();
        
        assertEquals("Test message", result);
    }
    
    @Test
    public void testTokenMgrError() {
        TokenMgrError error = new TokenMgrError();
        
        assertNotNull(error);
    }
    
    @Test
    public void testTokenMgrErrorWithReason() {
        TokenMgrError error = new TokenMgrError("Test message", 2);
        
        assertEquals(2, error.errorCode);
    }
    
    @Test
    public void testTokenMgrErrorWithDetails() {
        TokenMgrError error = new TokenMgrError(false, 1, 2, 3, "after", 'a', 2);
        String expectedOutput = "Lexical error at line 2, column 3.  Encountered: \"a\" (97), after : \"after\"";
        
        assertEquals(expectedOutput, error.getMessage());
        assertEquals(2, error.errorCode);
    }
  
}