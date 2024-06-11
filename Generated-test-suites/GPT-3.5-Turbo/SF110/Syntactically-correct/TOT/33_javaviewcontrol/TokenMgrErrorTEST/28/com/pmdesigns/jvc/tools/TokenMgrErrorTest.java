import static org.junit.Assert.*;
import org.junit.Test;

public class TokenMgrErrorTest {
    
    @Test
    public void testAddEscapes() {
        // Typical use case: input contains escape characters
        String input = "abc\n123\tdef";
        String expectedOutput = "abc\\n123\\tdef";
        assertEquals(expectedOutput, TokenMgrError.addEscapes(input));
        
        // Edge case: input is empty string
        input = "";
        expectedOutput = "";
        assertEquals(expectedOutput, TokenMgrError.addEscapes(input));
        
        // Edge case: input contains only escape characters
        input = "\n\t\r";
        expectedOutput = "\\n\\t\\r";
        assertEquals(expectedOutput, TokenMgrError.addEscapes(input));
        
        // Error scenario: input contains special characters
        input = "abc\"123'xyz\\";
        expectedOutput = "abc\\\"123\\'xyz\\\\";
        assertEquals(expectedOutput, TokenMgrError.addEscapes(input));
    }
    
    @Test
    public void testLexicalError() {
        // Typical use case: EOF not seen
        boolean EOFSeen = false;
        int lexState = 1;
        int errorLine = 2;
        int errorColumn = 5;
        String errorAfter = "xyz";
        char curChar = 'a';
        String expectedOutput = "Lexical error at line 2, column 5.  Encountered: \"a\" (97), after : \"xyz\"";
        assertEquals(expectedOutput, TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
        
        // Typical use case: EOF seen
        EOFSeen = true;
        lexState = 0;
        errorLine = 1;
        errorColumn = 1;
        errorAfter = "";
        curChar = ' ';
        expectedOutput = "Lexical error at line 1, column 1.  Encountered: <EOF>  after : \"\"";
        assertEquals(expectedOutput, TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
        
        // Edge case: errorAfter is empty
        EOFSeen = false;
        lexState = 2;
        errorLine = 3;
        errorColumn = 2;
        errorAfter = "";
        curChar = '!';
        expectedOutput = "Lexical error at line 3, column 2.  Encountered: \"!\" (33), after : \"\"";
        assertEquals(expectedOutput, TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
        
        // Error scenario: curChar is a non-printable character
        EOFSeen = false;
        lexState = 3;
        errorLine = 4;
        errorColumn = 6;
        errorAfter = "abc";
        curChar = '\b';
        expectedOutput = "Lexical error at line 4, column 6.  Encountered: \"\\b\" (8), after : \"abc\"";
        assertEquals(expectedOutput, TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
    }
    
    @Test
    public void testGetMessage() {
        // Typical use case: message is not null
        TokenMgrError error = new TokenMgrError("Test error", 1);
        String expectedOutput = "Test error";
        assertEquals(expectedOutput, error.getMessage());
        
        // Edge case: message is null
        error = new TokenMgrError(null, 2);
        expectedOutput = null;
        assertNull(error.getMessage());
    }
    
    @Test
    public void testConstructor() {
        // Typical use case: reason is valid
        String message = "Test error";
        int reason = 3;
        TokenMgrError error = new TokenMgrError(message, reason);
        assertEquals(message, error.getMessage());
        assertEquals(reason, error.errorCode);
        
        // Edge case: reason is minimum value
        message = "";
        reason = Integer.MIN_VALUE;
        error = new TokenMgrError(message, reason);
        assertEquals(message, error.getMessage());
        assertEquals(reason, error.errorCode);
        
        // Error scenario: reason is maximum value + 1
        message = "Overflow error";
        reason = Integer.MAX_VALUE + 1;
        try {
            error = new TokenMgrError(message, reason);
            fail("Should throw exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid reason code", e.getMessage());
        }
    }
    
    @Test
    public void testFullConstructor() {
        // Typical use case: EOF not seen, reason is valid
        boolean EOFSeen = false;
        int lexState = 1;
        int errorLine = 2;
        int errorColumn = 5;
        String errorAfter = "xyz";
        char curChar = 'a';
        int reason = 4;
        String expectedMessage = "Lexical error at line 2, column 5.  Encountered: \"a\" (97), after : \"xyz\"";
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(expectedMessage, error.getMessage());
        assertEquals(reason, error.errorCode);
        
        // Typical use case: EOF seen, reason is valid
        EOFSeen = true;
        lexState = 0;
        errorLine = 1;
        errorColumn = 1;
        errorAfter = "";
        curChar = ' ';
        reason = 5;
        expectedMessage = "Lexical error at line 1, column 1.  Encountered: <EOF>  after : \"\"";
        error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(expectedMessage, error.getMessage());
        assertEquals(reason, error.errorCode);
        
        // Edge case: errorAfter is empty, reason is minimum value
        EOFSeen = false;
        lexState = 2;
        errorLine = 3;
        errorColumn = 2;
        errorAfter = "";
        curChar = '!';
        reason = Integer.MIN_VALUE;
        expectedMessage = "Lexical error at line 3, column 2.  Encountered: \"!\" (33), after : \"\"";
        error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(expectedMessage, error.getMessage());
        assertEquals(reason, error.errorCode);
        
        // Error scenario: curChar is a non-printable character, reason is maximum value + 1
        EOFSeen = false;
        lexState = 3;
        errorLine = 4;
        errorColumn = 6;
        errorAfter = "abc";
        curChar = '\b';
        reason = Integer.MAX_VALUE + 1;
        try {
            error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
            fail("Should throw exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid reason code", e.getMessage());
        }
    }
}