
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        // Test when given string is empty
        assertEquals("", TokenMgrError.addEscapes(""));
        
        // Test when given string contains special characters
        assertEquals("\\n\\r\\t\\b\\f\\\"\\\'\\\\", TokenMgrError.addEscapes("\n\r\t\b\f\"\'\\"));
        
        // Test when given string contains unicode characters
        assertEquals("\\u0000\\u001f\\u007f\\u0080\\u9999", TokenMgrError.addEscapes("\0\u001f\177\u0080\u9999"));
        
        // Test when given string contains non-special characters
        assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", TokenMgrError.addEscapes("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
    }
    
    @Test
    public void testLexicalError() {
        // Test when EOFSeen is true
        assertEquals("Lexical error at line 1, column 2.  Encountered: <EOF>  after : \"errorAfter\"", TokenMgrError.LexicalError(true, 1, 2, 4, "errorAfter", 'x'));
        
        // Test when EOFSeen is false
        assertEquals("Lexical error at line 1, column 2.  Encountered: \"'\" (26),  after : \"errorAfter\"", TokenMgrError.LexicalError(false, 1, 2, 4, "errorAfter", '\''));
    }
    
    @Test
    public void testGetMessage() {
        // Test when error message is not set
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
        
        // Test when error message is set
        error = new TokenMgrError("Test error", 1);
        assertEquals("Test error", error.getMessage());
    }
    
    @Test
    public void testTokenMgrError() {
        // Test when error message is not set
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
        assertEquals(0, error.errorCode);
        
        // Test when error message and reason are set
        error = new TokenMgrError("Test error", 1);
        assertEquals("Test error", error.getMessage());
        assertEquals(1, error.errorCode);
    }
    
    @Test
    public void testTokenMgrErrorWithParams() {
        // Test when EOFSeen is true
        TokenMgrError error = new TokenMgrError(true, 1, 2, 4, "errorAfter", 'x', 0);
        assertEquals("Lexical error at line 2, column 4.  Encountered: <EOF>  after : \"errorAfter\"", error.getMessage());
        assertEquals(0, error.errorCode);
        
        // Test when EOFSeen is false
        error = new TokenMgrError(false, 1, 2, 4, "errorAfter", '"', 1);
        assertEquals("Lexical error at line 2, column 4.  Encountered: \"\\\"\" (34),  after : \"errorAfter\"", error.getMessage());
        assertEquals(1, error.errorCode);
    }
}
