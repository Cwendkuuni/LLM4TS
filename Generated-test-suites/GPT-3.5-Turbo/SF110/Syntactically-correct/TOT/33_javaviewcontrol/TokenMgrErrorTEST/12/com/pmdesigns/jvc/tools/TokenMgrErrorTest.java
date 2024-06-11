import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class TokenMgrErrorTest {
    
    @Test
    public void testAddEscapes_validString_returnEscapedString() {
        String input = "Hello, \nWorld!";
        String expectedOutput = "Hello, \\nWorld!";
        
        String result = TokenMgrError.addEscapes(input);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testLexicalError_validParameters_returnErrorMessage() {
        boolean EOFSeen = false;
        int lexState = 1;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "abc";
        char curChar = 'd';
        
        String expectedErrorMessage = "Lexical error at line 5, column 10. Encountered: \"d\" (100), after : \"abc\"";
        
        String result = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        
        assertEquals(expectedErrorMessage, result);
    }
    
    @Test
    public void testGetMessage_validReason_returnMessage() {
        String message = "Error occurred.";
        int reason = TokenMgrError.LEXICAL_ERROR;
        
        TokenMgrError error = new TokenMgrError(message, reason);
        
        String result = error.getMessage();
        
        assertEquals(message, result);
    }
    
    @Test
    public void testConstructor_noParameters_createInstance() {
        TokenMgrError error = new TokenMgrError();
        
        assertNotNull(error);
    }
    
    @Test
    public void testConstructor_validParameters_createInstance() {
        String message = "Error occurred.";
        int reason = TokenMgrError.LEXICAL_ERROR;
        
        TokenMgrError error = new TokenMgrError(message, reason);
        
        assertNotNull(error);
        assertEquals(message, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
    
    @Test
    public void testConstructor_validParameters_createInstanceWithErrorMessage() {
        boolean EOFSeen = false;
        int lexState = 1;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "abc";
        char curChar = 'd';
        int reason = TokenMgrError.LEXICAL_ERROR;
        
        String expectedErrorMessage = "Lexical error at line 5, column 10. Encountered: \"d\" (100), after : \"abc\"";
        
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        
        assertNotNull(error);
        assertEquals(expectedErrorMessage, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
}