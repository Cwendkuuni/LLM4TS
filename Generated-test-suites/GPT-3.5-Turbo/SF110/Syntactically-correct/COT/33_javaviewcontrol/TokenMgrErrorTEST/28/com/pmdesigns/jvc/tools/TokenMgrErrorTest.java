
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {
    
    @Test
    public void testAddEscapes() {
        String input = "abc";
        String expectedResult = "abc";
        String result = TokenMgrError.addEscapes(input);
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testLexicalError() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "a";
        char curChar = 'b';
        String expectedResult = "Lexical error at line 1, column 1.  Encountered: \"b\" (98), after : \"a\"";
        String result = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Error message", 0);
        String expectedResult = "Error message";
        String result = error.getMessage();
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testTokenMgrError() {
        TokenMgrError error = new TokenMgrError();
        assertNotNull(error);
    }
    
    @Test
    public void testTokenMgrErrorWithMessageAndReason() {
        String message = "Error message";
        int reason = 0;
        TokenMgrError error = new TokenMgrError(message, reason);
        assertEquals(message, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
    
    @Test
    public void testTokenMgrErrorWithLexicalError() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "a";
        char curChar = 'b';
        int reason = 0;
        
        String expectedMessage = "Lexical error at line 1, column 1.  Encountered: \"b\" (98), after : \"a\"";
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(expectedMessage, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
}

