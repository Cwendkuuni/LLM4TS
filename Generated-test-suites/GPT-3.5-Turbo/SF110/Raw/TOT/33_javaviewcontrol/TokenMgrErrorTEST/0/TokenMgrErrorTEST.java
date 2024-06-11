import static org.junit.Assert.*;
import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "This is a test";
        String expectedOutput = "This is a test";
        assertEquals(expectedOutput, TokenMgrError.addEscapes(input));
    }

    @Test
    public void testLexicalError_EOFSeen() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "";
        char curChar = ' ';
        String expectedOutput = "Lexical error at line 1, column 1.  Encountered: <EOF>  after : \"\"";
        assertEquals(expectedOutput, TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
    }

    @Test
    public void testLexicalError_NonEOFSeen() {
        boolean EOFSeen = false;
        int lexState = 1;
        int errorLine = 2;
        int errorColumn = 3;
        String errorAfter = "xyz";
        char curChar = 'x';
        String expectedOutput = "Lexical error at line 2, column 3.  Encountered: \"x\" (120), after : \"xyz\"";
        assertEquals(expectedOutput, TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
    }

    @Test
    public void testGetMessage_Default() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
    }

    @Test
    public void testGetMessage_Custom() {
        String message = "Custom error message";
        int reason = 0;
        TokenMgrError error = new TokenMgrError(message, reason);
        assertEquals(message, error.getMessage());
    }

    @Test
    public void testConstructor_Default() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
        assertEquals(0, error.errorCode);
    }

    @Test
    public void testConstructor_Custom() {
        String message = "Custom error message";
        int reason = 1;
        TokenMgrError error = new TokenMgrError(message, reason);
        assertEquals(message, error.getMessage());
        assertEquals(reason, error.errorCode);
    }

    @Test
    public void testConstructor_LexicalError() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "";
        char curChar = ' ';
        int reason = 2;
        String expectedMessage = "Lexical error at line 1, column 1.  Encountered: <EOF>  after : \"\"";
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(expectedMessage, error.getMessage());
        assertEquals(reason, error.errorCode);
    }

}