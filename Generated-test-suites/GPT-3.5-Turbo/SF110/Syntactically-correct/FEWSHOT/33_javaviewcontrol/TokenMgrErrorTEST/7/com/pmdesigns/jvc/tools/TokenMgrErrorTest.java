import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenMgrErrorTest {
    @Test
    public void testAddEscapes() {
        assertEquals("\\b", TokenMgrError.addEscapes("\b"));
        assertEquals("\\t", TokenMgrError.addEscapes("\t"));
        assertEquals("\\n", TokenMgrError.addEscapes("\n"));
        assertEquals("\\f", TokenMgrError.addEscapes("\f"));
        assertEquals("\\r", TokenMgrError.addEscapes("\r"));
        assertEquals("\\\"", TokenMgrError.addEscapes("\""));
        assertEquals("\\'", TokenMgrError.addEscapes("\'"));
        assertEquals("\\\\", TokenMgrError.addEscapes("\\"));
        assertEquals("\\u0041", TokenMgrError.addEscapes("A"));
        assertEquals("\\u00A9", TokenMgrError.addEscapes("©"));
    }
    
    @Test
    public void testLexicalError() {
        String expected1 = "Lexical error at line 10, column 20.  Encountered: <EOF> after : \"\"";
        String expected2 = "Lexical error at line 8, column 16.  Encountered: \"c\" (99), after : \"\"";
        
        assertEquals(expected1, TokenMgrError.LexicalError(true, 3, 10, 20, "", '\0'));
        assertEquals(expected2, TokenMgrError.LexicalError(false, 1, 8, 16, "", 'c'));
    }
    
    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("test message", 1);
        assertEquals("test message", error.getMessage());
    }
    
    @Test
    public void testConstructor() {
        TokenMgrError error1 = new TokenMgrError();
        assertEquals(0, error1.errorCode);
        
        TokenMgrError error2 = new TokenMgrError("test message", 2);
        assertEquals("test message", error2.getMessage());
        assertEquals(2, error2.errorCode);
        
        String expected1 = "Lexical error at line 4, column 8.  Encountered: <EOF> after : \"\"";
        TokenMgrError error3 = new TokenMgrError(true, 2, 4, 8, "", '\0', 0);
        assertEquals(expected1, error3.getMessage());
        assertEquals(0, error3.errorCode);
    }
}