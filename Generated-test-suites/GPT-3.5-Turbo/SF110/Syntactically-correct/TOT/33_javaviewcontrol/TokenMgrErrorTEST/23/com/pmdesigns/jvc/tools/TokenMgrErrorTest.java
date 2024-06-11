import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {
    
    @Test
    public void testAddEscapes() {
        // Typical use case
        String input = "Hello, World!";
        String expectedOutput = "Hello, World!";
        assertEquals(expectedOutput, TokenMgrError.addEscapes(input));
        
        // Edge case: empty string
        input = "";
        expectedOutput = "";
        assertEquals(expectedOutput, TokenMgrError.addEscapes(input));
        
        // Edge case: string with special characters
        input = "\n\t\\\"";
        expectedOutput = "\\n\\t\\\\\\\"";
        assertEquals(expectedOutput, TokenMgrError.addEscapes(input));
        
        // Error scenario: string with unicode characters
        input = "\u00e9";
        expectedOutput = "\u00e9";
        assertEquals(expectedOutput, TokenMgrError.addEscapes(input));
    }
    
    @Test
    public void testLexicalError() {
        // Typical use case
        boolean eofSeen = false;
        int lexState = 1;
        int errorLine = 2;
        int errorColumn = 3;
        String errorAfter = "abc";
        char curChar = '!';
        String expectedOutput = "Lexical error at line 2, column 3. Encountered: \"!\" (33), after : \"abc\"";
        assertEquals(expectedOutput, TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
        
        // Error scenario: EOF seen
        eofSeen = true;
        expectedOutput = "Lexical error at line 2, column 3. Encountered: <EOF> , after : \"abc\"";
        assertEquals(expectedOutput, TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
    }
    
    @Test
    public void testGetMessage() {
        // Typical use case
        TokenMgrError error = new TokenMgrError("Error message", 0);
        String expectedOutput = "Error message";
        assertEquals(expectedOutput, error.getMessage());
        
        // Edge case: empty error message
        error = new TokenMgrError("", 0);
        expectedOutput = "";
        assertEquals(expectedOutput, error.getMessage());
    }
    
    @Test
    public void testTokenMgrError() {
        // Typical use case
        TokenMgrError error = new TokenMgrError("Error message", 1);
        String expectedOutput = "Error message";
        assertEquals(expectedOutput, error.getMessage());
        assertEquals(1, error.errorCode);
        
        // Error scenario: invalid error code
        error = new TokenMgrError("Error message", -1);
        expectedOutput = "Error message";
        assertEquals(expectedOutput, error.getMessage());
        assertEquals(-1, error.errorCode);
    }
    
    @Test
    public void testTokenMgrErrorWithChar() {
        // Typical use case
        boolean eofSeen = false;
        int lexState = 1;
        int errorLine = 2;
        int errorColumn = 3;
        String errorAfter = "abc";
        char curChar = '!';
        int reason = 1;
        String expectedOutput = "Lexical error at line 2, column 3. Encountered: \"!\" (33), after : \"abc\"";
        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(expectedOutput, error.getMessage());
        assertEquals(reason, error.errorCode);
        
        // Error scenario: EOF seen
        eofSeen = true;
        expectedOutput = "Lexical error at line 2, column 3. Encountered: <EOF> , after : \"abc\"";
        error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(expectedOutput, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
}