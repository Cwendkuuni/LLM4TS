import static org.junit.Assert.*;
import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String str = "Test String";
        String expectedResult = "Test String";
        String actualResult = TokenMgrError.addEscapes(str);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testLexicalError() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "";
        char curChar = 'c';
        String expectedResult = "Lexical error at line 1, column 1.  Encountered: \"c\" (99), after : \"\"";
        String actualResult = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test Error Message", 0);
        String expectedResult = "Test Error Message";
        String actualResult = error.getMessage();
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
        
        error = new TokenMgrError("Test Error Message", 0);
        assertEquals("Test Error Message", error.getMessage());
        assertEquals(0, error.errorCode);
    }
    
    @Test
    public void testParameterizedConstructor() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "";
        char curChar = 'c';
        int reason = 0;
        
        String expectedErrorMessage = "Lexical error at line 1, column 1.  Encountered: \"c\" (99), after : \"\"";
        
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        
        assertEquals(expectedErrorMessage, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
}