import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "test\tinput\n";
        String expectedOutput = "test\\tinput\\n";
        String actualOutput = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testLexicalError() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "error";
        char curChar = 'a';

        String expectedMessage = "Lexical error at line 1, column 1. Encountered: <EOF> after: \"error\"";
        String actualMessage = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessage() {
        String message = "Test message";
        int reason = 0;
        TokenMgrError error = new TokenMgrError(message, reason);

        String expectedMessage = "Test message";
        String actualMessage = error.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testConstructor() {
        String message = "Test message";
        int reason = 0;
        TokenMgrError error = new TokenMgrError(message, reason);

        assertNotNull(error);
        assertEquals(message, error.getMessage());
        assertEquals(reason, error.errorCode);
    }

    @Test
    public void testFullConstructor() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "error";
        char curChar = 'a';
        int reason = 0;

        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);

        assertNotNull(error);
        assertEquals("Lexical error at line 1, column 1. Encountered: <EOF> after: \"error\"", error.getMessage());
        assertEquals(reason, error.errorCode);
    }
}

