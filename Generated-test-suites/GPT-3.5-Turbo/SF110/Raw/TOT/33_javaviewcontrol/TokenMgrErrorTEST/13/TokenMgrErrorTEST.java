import static org.junit.Assert.*;
import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes_typicalInput() {
        String input = "This is a typical input";
        String expectedOutput = "This is a typical input";
        String actualOutput = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAddEscapes_specialCharacters() {
        String input = "This string contains special characters: \t\n\"\\'";
        String expectedOutput = "This string contains special characters: \\t\\n\\\"\\\\'";
        String actualOutput = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAddEscapes_unicodeCharacters() {
        String input = "This string contains unicode characters: \u00E0\u00E1\u00E2";
        String expectedOutput = "This string contains unicode characters: \\u00E0\\u00E1\\u00E2";
        String actualOutput = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testLexicalError_EOFSeen() {
        boolean EOFSeen = true;
        int lexState = 1;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "errorAfter";
        char curChar = 'a';
        int reason = 1;

        String expectedOutput = "Lexical error at line 5, column 10.  Encountered: <EOF> after : \"errorAfter\"";
        String actualOutput = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testLexicalError_NotEOFSeen() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 5;
        String errorAfter = "errorAfter";
        char curChar = '!';
        int reason = 2;

        String expectedOutput = "Lexical error at line 1, column 5.  Encountered: \"!\" (33), after : \"errorAfter\"";
        String actualOutput = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test error message", 3);

        String expectedOutput = "Test error message";
        String actualOutput = error.getMessage();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTokenMgrError_noReason() {
        TokenMgrError error = new TokenMgrError();

        assertNull(error.getMessage());
        assertEquals(0, error.errorCode);
    }

    @Test
    public void testTokenMgrError_withReason() {
        TokenMgrError error = new TokenMgrError("Test error", 2);

        String expectedOutput = "Test error";
        String actualOutput = error.getMessage();
        assertEquals(expectedOutput, actualOutput);
        assertEquals(2, error.errorCode);
    }

    @Test
    public void testTokenMgrError_withAllParameters() {
        boolean EOFSeen = true;
        int lexState = 1;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "errorAfter";
        char curChar = 'a';
        int reason = 1;

        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);

        String expectedOutput = "Lexical error at line 5, column 10.  Encountered: <EOF> after : \"errorAfter\"";
        String actualOutput = error.getMessage();
        assertEquals(expectedOutput, actualOutput);
        assertEquals(reason, error.errorCode);
    }
}