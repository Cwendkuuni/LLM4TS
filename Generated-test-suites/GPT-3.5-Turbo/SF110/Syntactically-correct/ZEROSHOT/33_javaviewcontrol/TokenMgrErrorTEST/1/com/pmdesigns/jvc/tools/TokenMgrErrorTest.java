import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    private TokenMgrError error;

    @Before
    public void setUp() {
        error = new TokenMgrError();
    }

    @Test
    public void testGetMessage() {
        assertNull(error.getMessage());
    }

    @Test
    public void testTokenMgrErrorEmptyConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
        assertEquals(0, error.errorCode);
    }

    @Test
    public void testTokenMgrErrorMessageReasonConstructor() {
        String message = "Test Message";
        int reason = 1;
        TokenMgrError error = new TokenMgrError(message, reason);
        assertEquals(message, error.getMessage());
        assertEquals(reason, error.errorCode);
    }

    @Test
    public void testTokenMgrErrorLexicalErrorConstructor() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "Test";
        char curChar = 'a';
        int reason = 2;

        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String expectedErrorMessage = "Lexical error at line " + errorLine + ", column " + errorColumn + ".  Encountered: " + (EOFSeen ? "<EOF> " : ("\"" + TokenMgrError.addEscapes(String.valueOf(curChar)) + "\"" + " (" + (int)curChar + "), ")) + "after : \"" + TokenMgrError.addEscapes(errorAfter) + "\"";
        assertEquals(expectedErrorMessage, error.getMessage());
        assertEquals(reason, error.errorCode);
    }

}