import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "Hello, world!";
        String expected = "Hello, world!";
        String actual = TokenMgrError.addEscapes(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testLexicalError() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = " ";
        char curChar = '\0';
        String expected = "Lexical error at line 1, column 1.  Encountered: <EOF>  after : \" \"";
        String actual = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test Message", 0);
        String expected = "Test Message";
        String actual = error.getMessage();
        assertEquals(expected, actual);
    }

    @Test
    public void testConstructor1() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
        assertEquals(0, error.errorCode);
    }

    @Test
    public void testConstructor2() {
        TokenMgrError error = new TokenMgrError("Test Message", 1);
        String expected = "Test Message";
        String actual = error.getMessage();
        assertEquals(expected, actual);
        assertEquals(1, error.errorCode);
    }

    @Test
    public void testConstructor3() {
        boolean EOFSeen = false;
        int lexState = 1;
        int errorLine = 2;
        int errorColumn = 3;
        String errorAfter = "error";
        char curChar = 'a';
        int reason = 2;
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String expected = "Lexical error at line 2, column 3.  Encountered: \"a\" (97), after : \"error\"";
        String actual = error.getMessage();
        assertEquals(expected, actual);
        assertEquals(reason, error.errorCode);
    }

}