
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {
    
    @Test
    public void testAddEscapes() {
        String str = "abc\tdef\nghi\rjkl\"mno\'pqr\\stu";
        String expected = "abc\\tdef\\nghi\\rjkl\\\"mno\\\'pqr\\\\stu";
        assertEquals(expected, TokenMgrError.addEscapes(str));
    }
    
    @Test
    public void testLexicalError() {
        boolean EOFSeen = true;
        int lexState = 1;
        int errorLine = 2;
        int errorColumn = 3;
        String errorAfter = "xyz";
        char curChar = 'a';
        String expected = "Lexical error at line 2, column 3.  Encountered: <EOF> after : \"xyz\"";
        assertEquals(expected, TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
    }
    
    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test", 0);
        assertEquals("Test", error.getMessage());
    }
    
    @Test
    public void testEmptyConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
        assertEquals(0, error.errorCode);
    }
    
    @Test
    public void testStringIntConstructor() {
        TokenMgrError error = new TokenMgrError("Test", 1);
        assertEquals("Test", error.getMessage());
        assertEquals(1, error.errorCode);
    }
    
    @Test
    public void testFullConstructor() {
        boolean EOFSeen = true;
        int lexState = 1;
        int errorLine = 2;
        int errorColumn = 3;
        String errorAfter = "xyz";
        char curChar = 'a';
        int reason = 1;
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String expected = "Lexical error at line 2, column 3.  Encountered: <EOF> after : \"xyz\"";
        assertEquals(expected, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
}
