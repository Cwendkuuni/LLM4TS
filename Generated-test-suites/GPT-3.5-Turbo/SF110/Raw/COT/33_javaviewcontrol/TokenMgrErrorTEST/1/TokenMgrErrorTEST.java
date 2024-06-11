import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testConstructorWithEmptyMessageAndReason() {
        TokenMgrError error = new TokenMgrError();
        assertEquals(null, error.getMessage());
        assertEquals(0, error.errorCode);
    }

    @Test
    public void testConstructorWithMessageAndReason() {
        TokenMgrError error = new TokenMgrError("Test error message", 1);
        assertEquals("Test error message", error.getMessage());
        assertEquals(1, error.errorCode);
    }

    @Test
    public void testConstructorWithAllParameters() {
        TokenMgrError error = new TokenMgrError(true, 0, 1, 2, "errorAfter", 'c', 3);
        String expectedMessage = "Lexical error at line 1, column 2.  Encountered: 'c' (99), after : \"errorAfter\"";
        assertEquals(expectedMessage, error.getMessage());
        assertEquals(3, error.errorCode);
    }

    @Test
    public void testAddEscapesWithEmptyString() {
        String output = TokenMgrError.addEscapes("");
        assertEquals("", output);
    }

    @Test
    public void testAddEscapesWithSpecialCharacters() {
        String input = "\b\t\n\f\r\"\'\\";
        String expectedOutput = "\\b\\t\\n\\f\\r\\\"\\\'\\\\";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testAddEscapesWithNonPrintableCharacters() {
        String input = "\0";
        String expectedOutput = "\\u0000";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testLexicalError() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 2;
        String errorAfter = "errorAfter";
        char curChar = 'c';
        
        String expectedMessage = "Lexical error at line 1, column 2.  Encountered: <EOF> after : \"errorAfter\"";
        String output = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        
        assertEquals(expectedMessage, output);
    }

}