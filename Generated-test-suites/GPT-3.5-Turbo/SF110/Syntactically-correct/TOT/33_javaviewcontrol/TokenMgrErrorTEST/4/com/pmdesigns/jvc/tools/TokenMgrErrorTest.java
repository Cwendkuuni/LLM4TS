
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "abc\ndef\tghi";
        String expectedOutput = "abc\\ndef\\tghi";
        String actualOutput = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAddEscapesWithSpecialCharacters() {
        String input = "\b\r\'\"";
        String expectedOutput = "\\b\\r\\'\\\"";
        String actualOutput = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAddEscapesWithUnicodeCharacter() {
        char unicodeChar = '\u038A'; // Greek Capital Letter Heta
        String input = String.valueOf(unicodeChar);
        String expectedOutput = "\\u038A";
        String actualOutput = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testLexicalErrorWithEOFSeen() {
        boolean EOFSeen = true;
        int lexState = 1;
        int errorLine = 2;
        int errorColumn = 6;
        String errorAfter = "xyz";
        char curChar = ' ';
        int reason = TokenMgrError.LEXICAL_ERROR;

        String expectedErrorMessage = "Lexical error at line 2, column 6.  Encountered: <EOF>  after : \"xyz\"";
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);

        assertEquals(expectedErrorMessage, error.getMessage());
        assertEquals(reason, error.errorCode);
    }

    @Test
    public void testLexicalErrorWithoutEOFSeen() {
        boolean EOFSeen = false;
        int lexState = 2;
        int errorLine = 4;
        int errorColumn = 5;
        String errorAfter = "mno";
        char curChar = 'a';
        int reason = TokenMgrError.STATIC_LEXER_ERROR;

        String expectedErrorMessage = "Lexical error at line 4, column 5.  Encountered: \"a\" (97),  after : \"mno\"";
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);

        assertEquals(expectedErrorMessage, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
}
