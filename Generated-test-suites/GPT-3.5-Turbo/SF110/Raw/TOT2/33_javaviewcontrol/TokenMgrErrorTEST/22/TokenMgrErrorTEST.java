import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "testing\t123\n";
        String expectedOutput = "testing\\t123\\n";
        String result = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testLexicalError() {
        boolean EOFSeen = false;
        int lexState = 1;
        int errorLine = 10;
        int errorColumn = 5;
        String errorAfter = "error";
        char curChar = 'a';
        String expectedResult = "Lexical error at line 10, column 5. Encountered: \"a\" (97), after : \"error\"";
        String result = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test message", TokenMgrError.LEXICAL_ERROR);
        assertEquals("Test message", error.getMessage());
    }

    // Edge case: errorCode initialization
    @Test
    public void testTokenMgrErrorConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertEquals(0, error.errorCode);
    }

    // Error scenario: errorCode passed in constructor
    @Test
    public void testTokenMgrErrorConstructorWithReason() {
        TokenMgrError error = new TokenMgrError("Test message", TokenMgrError.STATIC_LEXER_ERROR);
        assertEquals(TokenMgrError.STATIC_LEXER_ERROR, error.errorCode);
    }

    // Exception handling scenario
    @Test
    public void testTokenMgrErrorConstructorWithLexicalError() {
        boolean EOFSeen = true;
        int lexState = 2;
        int errorLine = 8;
        int errorColumn = 3;
        String errorAfter = "error";
        char curChar = '$';
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.INVALID_LEXICAL_STATE);
        assertEquals("Lexical error at line 8, column 3. Encountered: \"<EOF> \" after : \"error\"", error.getMessage());
        assertEquals(TokenMgrError.INVALID_LEXICAL_STATE, error.errorCode);
    }
}