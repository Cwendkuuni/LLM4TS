import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenMgrErrorTest {
    
    @Test
    public void testAddEscapes() {
        String input = "\\n";
        String expected = "\\\\n";
        String result = TokenMgrError.addEscapes(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testLexicalError() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "token";
        char curChar = 'a';
        String expected = "Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"token\"";
        String result = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expected, result);
    }
    
    @Test
    public void testgetMessage() {
        TokenMgrError tokenError = new TokenMgrError("Test Error", 1);
        String expected = "Test Error";
        String result = tokenError.getMessage();
        assertEquals(expected, result);
    }
    
    @Test
    public void testTokenMgrErrorConstructor() {
        TokenMgrError tokenError = new TokenMgrError("Test Error", 1);
        int expected = 1;
        int result = tokenError.errorCode;
        assertEquals(expected, result);
    }
    
    @Test
    public void testTokenMgrErrorFullConstructor() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "token";
        char curChar = 'a';
        int reason = 1;
        
        TokenMgrError tokenError = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String expectedMessage = "Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"token\"";
        int expectedCode = 1;
        
        assertEquals(expectedMessage, tokenError.getMessage());
        assertEquals(expectedCode, tokenError.errorCode);
    }
}