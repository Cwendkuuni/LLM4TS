import com.pmdesigns.jvc.tools.TokenMgrError;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TokenMgrErrorTest {

    @Test
    public void testEmptyConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
        assertEquals(0, error.errorCode);
    }
    
    @Test
    public void testConstructorWithMessageAndReason() {
        String message = "Test error message";
        int reason = TokenMgrError.LEXICAL_ERROR;
        TokenMgrError error = new TokenMgrError(message, reason);
        assertEquals(message, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
    
    @Test
    public void testConstructorWithCompleteParameter() {
        boolean eofSeen = true;
        int lexState = TokenMgrError.INVALID_LEXICAL_STATE;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "Some text";
        char curChar = 'c';
        int reason = TokenMgrError.LOOP_DETECTED;
        
        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String expectedMessage = "Lexical error at line 5, column 10.  Encountered: <EOF>  after : \"Some text\"";
        
        assertEquals(expectedMessage, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
    
    @Test
    public void testAddEscapes() {
        String input = "Test\test\"string";
        String expectedOutput = "Test\\test\\\"string";
        
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testLexicalError() {
        boolean eofSeen = false;
        int lexState = TokenMgrError.STATIC_LEXER_ERROR;
        int errorLine = 3;
        int errorColumn = 8;
        String errorAfter = "After text";
        char curChar = '@';
        
        String expectedError = "Lexical error at line 3, column 8.  Encountered: \"@\" (64), after : \"After text\"";
        
        String error = TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expectedError, error);
    }

}