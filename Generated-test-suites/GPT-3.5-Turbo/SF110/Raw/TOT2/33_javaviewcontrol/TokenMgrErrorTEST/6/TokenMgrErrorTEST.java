import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {
    
    @Test
    public void testAddEscapes() {
        String input = "Test\nString";
        String expectedOutput = "Test\\nString";
        String actualOutput = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testLexicalError() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "String";
        char curChar = 'S';
        int reason = TokenMgrError.LEXICAL_ERROR;
        
        String expectedOutput = "Lexical error at line 1, column 1. Encountered: <EOF> after : \"String\"";
        String actualOutput = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test Message", TokenMgrError.LEXICAL_ERROR);
        assertEquals("Test Message", error.getMessage());
    }
    
    @Test
    public void testTokenMgrError1() {
        TokenMgrError error = new TokenMgrError();
        assertEquals(null, error.getMessage());
    }
    
    @Test
    public void testTokenMgrError2() {
        TokenMgrError error = new TokenMgrError("Test Message", TokenMgrError.LEXICAL_ERROR);
        assertEquals("Test Message", error.getMessage());
        assertEquals(TokenMgrError.LEXICAL_ERROR, error.errorCode);
    }
    
    @Test
    public void testTokenMgrError3() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "String";
        char curChar = 'S';
        int reason = TokenMgrError.LEXICAL_ERROR;
        
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        
        String expectedMessage = "Lexical error at line 1, column 1. Encountered: <EOF> after : \"String\"";
        assertEquals(expectedMessage, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
}