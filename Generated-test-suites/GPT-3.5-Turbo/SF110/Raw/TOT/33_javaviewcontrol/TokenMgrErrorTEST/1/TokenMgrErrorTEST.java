import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String str = "abc";
        String result = TokenMgrError.addEscapes(str);
        assertEquals("abc", result);
    }
    
    @Test
    public void testLexicalError() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "abc";
        char curChar = 'a';
        String result = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF>  after : \"abc\"", result);
    }
    
    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Error message", 0);
        String result = error.getMessage();
        assertEquals("Error message", result);
    }
    
    @Test
    public void testTokenMgrError() {
        TokenMgrError error = new TokenMgrError();
        String result = error.getMessage();
        assertNull(result);
    }
    
    @Test
    public void testTokenMgrErrorWithMessageAndReason() {
        String message = "Error message";
        int reason = 1;
        TokenMgrError error = new TokenMgrError(message, reason);
        String resultMessage = error.getMessage();
        int resultReason = error.errorCode;
        assertEquals("Error message", resultMessage);
        assertEquals(1, resultReason);
    }
    
    @Test
    public void testTokenMgrErrorWithAllParameters() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "abc";
        char curChar = 'a';
        int reason = 1;
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String resultMessage = error.getMessage();
        int resultErrorCode = error.errorCode;
        assertEquals("Lexical error at line 1, column 1.  Encountered: <EOF>  after : \"abc\"", resultMessage);
        assertEquals(1, resultErrorCode);
    }
}