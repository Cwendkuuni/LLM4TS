import org.junit.Test;
import static org.junit.Assert.*;
import com.pmdesigns.jvc.tools.TokenMgrError;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "test\nstring\twith\r\tescapes";
        String expectedOutput = "test\\nstring\\twith\\r\\tescapes";
        assertEquals(expectedOutput, TokenMgrError.addEscapes(input));
    }

    @Test
    public void testLexicalError() {
        boolean EOFSeen = true;
        int lexState = 1;
        int errorLine = 10;
        int errorColumn = 5;
        String errorAfter = "test";
        char curChar = 'a';
        String expectedOutput = "Lexical error at line 10, column 5.  Encountered: <EOF> after : \"test\"";
        assertEquals(expectedOutput, TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test message", 1);
        assertEquals("Test message", error.getMessage());
    }

    @Test
    public void testTokenMgrError() {
        TokenMgrError error = new TokenMgrError();
        assertNotNull(error);
    }

    @Test
    public void testTokenMgrErrorWithReason() {
        TokenMgrError error = new TokenMgrError("Test error", 2);
        assertEquals(2, error.errorCode);
    }

    @Test
    public void testTokenMgrErrorWithLexicalInfo() {
        boolean EOFSeen = false;
        int lexState = 2;
        int errorLine = 5;
        int errorColumn = 3;
        String errorAfter = "abc";
        char curChar = 'b';
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, 3);
        assertEquals("Lexical error at line 5, column 3.  Encountered: \"b\" (98), after : \"abc\"", error.getMessage());
        assertEquals(3, error.errorCode);
    }
}