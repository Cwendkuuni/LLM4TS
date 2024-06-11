import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    private TokenMgrError tokenMgrError;
    
    @Before
    public void setUp() {
        tokenMgrError = new TokenMgrError();
    }
    
    @Test
    public void testGetMessage() {
        String expectedMessage = null;
        String actualMessage = tokenMgrError.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void testTokenMgrError() {
        assertNotNull(tokenMgrError);
    }
    
    @Test
    public void testTokenMgrErrorWithMessageAndReason() {
        String expectedMessage = "Test message";
        int expectedReason = 1;
        
        TokenMgrError error = new TokenMgrError(expectedMessage, expectedReason);
        String actualMessage = error.getMessage();
        int actualReason = error.errorCode;
        
        assertEquals(expectedMessage, actualMessage);
        assertEquals(expectedReason, actualReason);
    }
    
    @Test
    public void testTokenMgrErrorWithEOFSeenAndLexState() {
        boolean expectedEOFSeen = true;
        int expectedLexState = 2;
        int expectedErrorLine = 5;
        int expectedErrorColumn = 10;
        String expectedErrorAfter = "SomeError";
        char expectedCurChar = 'A';
        int expectedReason = 3;
        
        TokenMgrError error = new TokenMgrError(expectedEOFSeen, expectedLexState, expectedErrorLine, expectedErrorColumn, expectedErrorAfter, expectedCurChar, expectedReason);
        
        String actualMessage = error.getMessage();
        int actualErrorCode = error.errorCode;
        
        assertEquals("Lexical error at line 5, column 10.  Encountered: \"A\" (65), after : \"SomeError\"", actualMessage);
        assertEquals(expectedReason, actualErrorCode);
    }
}