
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "Hello\tWorld";
        String expected = "Hello\\tWorld";
        String result = TokenMgrError.addEscapes(input);
        assertEquals(expected, result);
    }

    @Test
    public void testLexicalError() {
        boolean eofSeen = false;
        int lexState = 1;
        int errorLine = 2;
        int errorColumn = 3;
        String errorAfter = "abc";
        char curChar = 'd';
        String expected = "Lexical error at line 2, column 3.  Encountered: \"d\" (100), after : \"abc\"";
        String result = TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expected, result);
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Error Message", 1);
        String expected = "Error Message";
        String result = error.getMessage();
        assertEquals(expected, result);
    }

    @Test
    public void testConstructorWithoutParameters() {
        TokenMgrError error = new TokenMgrError();
        assertNotNull(error);
    }

    @Test
    public void testConstructorWithMessageAndReason() {
        String message = "Error Message";
        int reason = 1;
        TokenMgrError error = new TokenMgrError(message, reason);
        assertNotNull(error);
        assertEquals(message, error.getMessage());
        assertEquals(reason, error.errorCode);
    }

    @Test
    public void testConstructorWithAllParameters() {
        boolean eofSeen = false;
        int lexState = 1;
        int errorLine = 2;
        int errorColumn = 3;
        String errorAfter = "abc";
        char curChar = 'd';
        int reason = 1;
        String expectedMessage = "Lexical error at line 2, column 3.  Encountered: \"d\" (100), after : \"abc\"";
        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertNotNull(error);
        assertEquals(expectedMessage, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
}

