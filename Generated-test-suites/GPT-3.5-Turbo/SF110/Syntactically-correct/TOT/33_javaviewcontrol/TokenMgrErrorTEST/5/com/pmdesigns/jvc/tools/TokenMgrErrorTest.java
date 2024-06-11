
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String str = "Hello";
        String expected = "Hello";
        assertEquals(expected, TokenMgrError.addEscapes(str));
    }

    @Test
    public void testLexicalError() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "test";
        char curChar = 'a';
        String expected = "Lexical error at line 1, column 1.  Encountered: <EOF> after : \"test\"";
        assertEquals(expected, TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
    }

    @Test
    public void testGetMessage() {
        TokenMgrError tokenMgrError = new TokenMgrError("Sample error message", 1);
        String expected = "Sample error message";
        assertEquals(expected, tokenMgrError.getMessage());
    }

    @Test
    public void testConstructor1() {
        TokenMgrError tokenMgrError = new TokenMgrError();
        String expectedMessage = null;
        int expectedReason = 0;
        assertEquals(expectedMessage, tokenMgrError.getMessage());
        assertEquals(expectedReason, tokenMgrError.errorCode);
    }

    @Test
    public void testConstructor2() {
        String message = "Sample error message";
        int reason = 1;
        TokenMgrError tokenMgrError = new TokenMgrError(message, reason);
        assertEquals(message, tokenMgrError.getMessage());
        assertEquals(reason, tokenMgrError.errorCode);
    }

    @Test
    public void testConstructor3() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "test";
        char curChar = 'a';
        int reason = 1;
        String expectedMessage = "Lexical error at line 1, column 1.  Encountered: <EOF> after : \"test\"";
        TokenMgrError tokenMgrError = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(expectedMessage, tokenMgrError.getMessage());
        assertEquals(reason, tokenMgrError.errorCode);
    }
    
}

