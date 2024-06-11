import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    // Test case for default constructor
    @Test
    public void testDefaultConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
        assertEquals(0, error.errorCode);
    }

    // Test case for constructor with message and reason
    @Test
    public void testMessageAndReasonConstructor() {
        String message = "Test Error";
        int reason = TokenMgrError.LEXICAL_ERROR;
        
        TokenMgrError error = new TokenMgrError(message, reason);
        
        assertEquals(message, error.getMessage());
        assertEquals(reason, error.errorCode);
    }

    // Test case for constructor with all parameters
    @Test
    public void testAllParamConstructor() {
        boolean EOFSeen = true;
        int lexState = 1;
        int errorLine = 10;
        int errorColumn = 5;
        String errorAfter = "some string";
        char curChar = 'A';
        int reason = TokenMgrError.STATIC_LEXER_ERROR;
        
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        
        String expectedMessage = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expectedMessage, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
  
    // Test case for addEscapes method
    @Test
    public void testAddEscapes() {
        String input = "Hello\nWorld";
        String expectedOutput = "Hello\\nWorld";
        
        String output = TokenMgrError.addEscapes(input);
        
        assertEquals(expectedOutput, output);
    }
    
    // Test case for LexicalError method
    @Test
    public void testLexicalError() {
        boolean EOFSeen = true;
        int lexState = 1;
        int errorLine = 10;
        int errorColumn = 5;
        String errorAfter = "some string";
        char curChar = 'A';
        
        String expectedMessage = "Lexical error at line 10, column 5.  Encountered: <EOF>  after : \"some string\"";
        
        String output = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        
        assertEquals(expectedMessage, output);
    }
}