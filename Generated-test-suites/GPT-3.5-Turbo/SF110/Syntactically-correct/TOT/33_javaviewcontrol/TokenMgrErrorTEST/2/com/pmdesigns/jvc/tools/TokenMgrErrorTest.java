import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import com.pmdesigns.jvc.tools.TokenMgrError;

public class TokenMgrErrorTest {

    private static TokenMgrError tokenMgrError;

    @BeforeClass
    public static void setUp() {
        tokenMgrError = new TokenMgrError();
    }

    @Test
    public void testGetMessage_ValidMessage_ReturnsMessageString() {
        String message = "This is a test message";
        int reason = 0;
        TokenMgrError tokenMgrError = new TokenMgrError(message, reason);

        assertEquals(message, tokenMgrError.getMessage());
    }

    @Test
    public void testGetMessage_NoMessage_ReturnsNull() {
        TokenMgrError tokenMgrError = new TokenMgrError();

        assertEquals(null, tokenMgrError.getMessage());
    }

    @Test
    public void testTokenMgrError_AllParameters_ReturnsTokenMgrErrorInstance() {
        boolean EOFSeen = false;
        int lexState = 1;
        int errorLine = 10;
        int errorColumn = 5;
        String errorAfter = "error";
        char curChar = 'a';
        int reason = 1;

        TokenMgrError tokenMgrError = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);

        assertEquals("Lexical error at line 10, column 5.  Encountered: \"a\" (97), after : \"error\"", tokenMgrError.getMessage());
        assertEquals(reason, tokenMgrError.errorCode);
    }

    @Test
    public void testTokenMgrError_NoParameters_ReturnsTokenMgrErrorInstance() {
        TokenMgrError tokenMgrError = new TokenMgrError();

        assertEquals(null, tokenMgrError.getMessage());
        assertEquals(0, tokenMgrError.errorCode);
    }

    @Test
    public void testAddEscapes_ValidString_ReturnsEscapedString() {
        String input = "\t\n\r";
        String expectedOutput = "\\t\\n\\r";

        assertEquals(expectedOutput, TokenMgrError.addEscapes(input));
    }

    @Test
    public void testAddEscapes_EmptyString_ReturnsEmptyString() {
        String input = "";
        String expectedOutput = "";

        assertEquals(expectedOutput, TokenMgrError.addEscapes(input));
    }

    @Test
    public void testAddEscapes_InvalidCharacters_ReturnsEscapedCharacters() {
        String input = "\b\f\'\\";
        String expectedOutput = "\\b\\f\\'\\\\";

        assertEquals(expectedOutput, TokenMgrError.addEscapes(input));
    }

    @Test
    public void testAddEscapes_UnicodeCharacter_ReturnsEscapedUnicode() {
        String input = "\u00E9";
        String expectedOutput = "\\u00E9";

        assertEquals(expectedOutput, TokenMgrError.addEscapes(input));
    }

}