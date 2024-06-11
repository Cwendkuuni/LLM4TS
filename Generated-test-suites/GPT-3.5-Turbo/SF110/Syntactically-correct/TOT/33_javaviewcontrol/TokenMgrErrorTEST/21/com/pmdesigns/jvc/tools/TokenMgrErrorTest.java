import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TokenMgrErrorTest {
    private TokenMgrError tokenMgrError;

    @Before
    public void setUp() {
        tokenMgrError = new TokenMgrError();
    }

    @Test
    public void testAddEscapes() {
        String inputString = "This is a test string.";
        String expectedOutput = "This is a test string.";

        String actualOutput = tokenMgrError.addEscapes(inputString);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testLexicalError_EOFSeen() {
        boolean EOFSeen = true;
        int lexState = TokenMgrError.LEXICAL_ERROR;
        int errorLine = 10;
        int errorColumn = 5;
        String errorAfter = "test";
        char curChar = 'a';
        int reason = 0; // Assuming 0 represents LEXICAL_ERROR

        String expectedErrorMessage = "Lexical error at line 10, column 5.  Encountered: <EOF> after : \"test\"";
        TokenMgrError tokenMgrError = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);

        assertEquals(expectedErrorMessage, tokenMgrError.getMessage());
    }

    @Test
    public void testLexicalError_NonEOFSeen() {
        boolean EOFSeen = false;
        int lexState = TokenMgrError.STATIC_LEXER_ERROR;
        int errorLine = 5;
        int errorColumn = 2;
        String errorAfter = "example";
        char curChar = '!';
        int reason = 1; // Assuming 1 represents STATIC_LEXER_ERROR

        String expectedErrorMessage = "Lexical error at line 5, column 2.  Encountered: \"!\" (33), after : \"example\"";
        TokenMgrError tokenMgrError = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);

        assertEquals(expectedErrorMessage, tokenMgrError.getMessage());
    }

    @Test
    public void testGetMessage() {
        String inputMessage = "This is a sample message.";
        int reason = 2; // Assuming 2 represents INVALID_LEXICAL_STATE

        TokenMgrError tokenMgrError = new TokenMgrError(inputMessage, reason);

        assertEquals(inputMessage, tokenMgrError.getMessage());
    }

    @Test
    public void testConstructor() {
        String inputMessage = "This is a sample error message.";
        int reason = 3; // Assuming 3 represents LOOP_DETECTED

        TokenMgrError tokenMgrError = new TokenMgrError(inputMessage, reason);

        assertEquals(inputMessage, tokenMgrError.getMessage());
        assertEquals(reason, tokenMgrError.errorCode);
    }

}