
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage() {
        // Test case 1: Check if getMessage returns the correct message
        TokenMgrError error = new TokenMgrError("Test Message", 0);
        assertEquals("Test Message", error.getMessage());
    }

    @Test
    public void testTokenMgrError() {
        // Test case 2: Check if the default constructor initializes the errorCode to 0
        TokenMgrError error = new TokenMgrError();
        assertEquals(0, error.errorCode);

        // Test case 3: Check if the constructor sets the errorCode correctly
        TokenMgrError error2 = new TokenMgrError("Test Message", 1);
        assertEquals(1, error2.errorCode);
        assertEquals("Test Message", error2.getMessage());
    }

    @Test
    public void testLexicalError() {
        // Test case 4: Check if LexicalError returns the correct error message
        String expectedError = "Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"\"";
        String actualError = TokenMgrError.LexicalError(false, 0, 1, 1, "", 'a');
        assertEquals(expectedError, actualError);
    }
}

