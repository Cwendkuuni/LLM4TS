import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Error message", 0);
        String message = error.getMessage();
        assertEquals("Error message", message);
    }

    @Test
    public void testTokenMgrErrorWithMessageAndReason() {
        TokenMgrError error = new TokenMgrError("Error message", 1);
        String message = error.getMessage();
        assertEquals("Error message", message);
        assertEquals(1, error.errorCode);
    }

    @Test
    public void testTokenMgrErrorWithLexicalError() {
        TokenMgrError error = new TokenMgrError(true, 0, 1, 2, "after", 'c', 0);
        String expectedMessage = "Lexical error at line 1, column 2.  Encountered: \"c\" (99), after : \"after\"";
        String message = error.getMessage();
        assertEquals(expectedMessage, message);
        assertEquals(0, error.errorCode);
    }

    @Test
    public void testTokenMgrErrorWithLexicalErrorEOFSeen() {
        TokenMgrError error = new TokenMgrError(true, 1, 3, 4, "after", 'd', 1);
        String expectedMessage = "Lexical error at line 3, column 4.  Encountered: <EOF>  after : \"after\"";
        String message = error.getMessage();
        assertEquals(expectedMessage, message);
        assertEquals(1, error.errorCode);
    }

    @Test
    public void testAddEscapes() {
        String str = "Test\\nString";
        String expectedStr = "Test\\nString";
        String result = TokenMgrError.addEscapes(str);
        assertEquals(expectedStr, result);
    }

    @Test
    public void testAddEscapesWithUnicodeCharacter() {
        String str = "\u00A9";
        String expectedStr = "\\u00A9";
        String result = TokenMgrError.addEscapes(str);
        assertEquals(expectedStr, result);
    }
}