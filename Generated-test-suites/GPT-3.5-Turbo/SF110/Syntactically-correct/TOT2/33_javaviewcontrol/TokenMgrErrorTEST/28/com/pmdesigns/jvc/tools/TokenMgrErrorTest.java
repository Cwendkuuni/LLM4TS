package com.pmdesigns.jvc.tools;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {
    
    @Test
    public void testAddEscapes() {
        // Typical use case
        assertEquals("\\n", TokenMgrError.addEscapes("\n"));
        
        // Edge case: empty string
        assertEquals("", TokenMgrError.addEscapes(""));
        
        // Edge case: special characters
        assertEquals("\\t\\r\\\"", TokenMgrError.addEscapes("\t\r\""));
    }
    
    @Test
    public void testLexicalError() {
        // Typical use case
        assertEquals("Lexical error at line 1, column 2.  Encountered: \"a\" (97), after : \"b\"", TokenMgrError.LexicalError(false, 1, 1, 2, "b", 'a'));
        
        // Edge case: EOF seen
        assertEquals("Lexical error at line 3, column 4.  Encountered: <EOF> , after : \"d\"", TokenMgrError.LexicalError(true, 3, 3, 4, "d", ' '));
    }
    
    @Test
    public void testGetMessage() {
        // Typical use case
        TokenMgrError error = new TokenMgrError("Test error message", 0);
        assertEquals("Test error message", error.getMessage());
        
        // Edge case: empty message
        TokenMgrError emptyError = new TokenMgrError("", 0);
        assertEquals("", emptyError.getMessage());
    }
    
    @Test
    public void testTokenMgrError() {
        // Typical use case
        TokenMgrError error = new TokenMgrError("Test error message", 1);
        assertEquals("Test error message", error.getMessage());
        assertEquals(1, error.errorCode);
    }
    
    @Test
    public void testTokenMgrErrorWithDetails() {
        // Typical use case
        TokenMgrError error = new TokenMgrError(false, 1, 2, 3, "abc", 'd', 4);
        assertNotNull(error);
    }
}