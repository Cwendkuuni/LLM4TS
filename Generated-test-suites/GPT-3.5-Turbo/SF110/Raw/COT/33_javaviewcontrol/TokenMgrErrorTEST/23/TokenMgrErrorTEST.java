
import org.junit.Test;

public class TokenMgrErrorTest {
    
    @Test
    public void testAddEscapes() {
        // Test a string with special characters
        String input = "Hello\tWorld";
        String expected = "Hello\\tWorld";
        String result = TokenMgrError.addEscapes(input);
        assertEquals(expected, result);
        
        // Test an empty string
        input = "";
        expected = "";
        result = TokenMgrError.addEscapes(input);
        assertEquals(expected, result);
        
        // Test a string without any special characters
        input = "Hello World";
        expected = "Hello World";
        result = TokenMgrError.addEscapes(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testLexicalError() {
        // Test with EOF seen
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 0;
        String errorAfter = "after";
        char curChar = 'a';
        int reason = TokenMgrError.LEXICAL_ERROR;
        
        String expected = "Lexical error at line 1, column 0.  Encountered: <EOF> after : \"after\"";
        String result = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expected, result);
        
        // Test without EOF seen
        EOFSeen = false;
        lexState = 1;
        errorLine = 2;
        errorColumn = 2;
        errorAfter = "after";
        curChar = 'b';
        reason = TokenMgrError.STATIC_LEXER_ERROR;
        
        expected = "Lexical error at line 2, column 2.  Encountered: \"b\" (98), after : \"after\"";
        result = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetMessage() {
        // Test getting the message without setting it
        TokenMgrError error = new TokenMgrError();
        String result = error.getMessage();
        assertNull(result);
        
        // Test getting the message after setting it
        String message = "Test error message";
        error = new TokenMgrError(message, TokenMgrError.LOOP_DETECTED);
        result = error.getMessage();
        assertEquals(message, result);
    }
    
    @Test
    public void testConstructor() {
        // Test constructor with no arguments
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
        assertEquals(0, error.errorCode);
        
        // Test constructor with message and reason arguments
        String message = "Test error message";
        error = new TokenMgrError(message, TokenMgrError.INVALID_LEXICAL_STATE);
        assertEquals(message, error.getMessage());
        assertEquals(TokenMgrError.INVALID_LEXICAL_STATE, error.errorCode);
    }
    
    @Test
    public void testConstructorWithEOF() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 0;
        String errorAfter = "after";
        char curChar = 'a';
        int reason = TokenMgrError.LOOP_DETECTED;
        
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String expected = "Lexical error at line 1, column 0.  Encountered: <EOF> after : \"after\"";
        assertEquals(expected, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
}