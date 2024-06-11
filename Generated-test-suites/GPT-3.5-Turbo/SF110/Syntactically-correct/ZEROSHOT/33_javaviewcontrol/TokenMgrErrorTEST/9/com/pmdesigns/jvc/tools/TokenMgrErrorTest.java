import org.junit.Test;

import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String str = "\tThis is a test";
        String expected = "\\tThis is a test";
        String result = TokenMgrError.addEscapes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testLexicalError() {
        boolean eofSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "token";
        char curChar = 'a';
        String expected = "Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"token\"";
        String result = TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Error message", 1);
        String expected = "Error message";
        String result = error.getMessage();
        assertEquals(expected, result);
    }
    
    @Test
    public void testConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
        assertEquals(0, error.errorCode);
    }
    
    @Test
    public void testConstructorWithMessageAndReason() {
        String message = "Error message";
        int reason = 1;
        TokenMgrError error = new TokenMgrError(message, reason);
        assertEquals(message, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
    
    @Test
    public void testConstructorWithParams() {
        boolean eofSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "token";
        char curChar = 'a';
        int reason = 1;
        String expected = "Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"token\"";
        TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        assertEquals(expected, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
}