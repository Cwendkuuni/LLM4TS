import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "Hello\nWorld";
        String expected = "Hello\\nWorld";
        assertEquals(expected, TokenMgrError.addEscapes(input));
    }

    @Test
    public void testLexicalError() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 2;
        String errorAfter = "after";
        char curChar = 'c';

        String expected = "Lexical error at line 1, column 2.  Encountered: <EOF>  after : \"after\"";
        assertEquals(expected, TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test Message", 10);
        assertEquals("Test Message", error.getMessage());
    }

    @Test
    public void testTokenMgrError1() {
        TokenMgrError error = new TokenMgrError("Test Message", 10);
        assertEquals("Test Message", error.getMessage());
        assertEquals(10, error.errorCode);
    }

    @Test
    public void testTokenMgrError2() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 2;
        String errorAfter = "after";
        char curChar = 'c';
        int reason = 10;

        String expected = "Lexical error at line 1, column 2.  Encountered: <EOF>  after : \"after\"";

        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(expected, error.getMessage());
        assertEquals(reason, error.errorCode);
    }

}


