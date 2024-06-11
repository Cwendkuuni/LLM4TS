import static org.junit.Assert.*;
import org.junit.Test;

public class TokenMgrErrorTest {
    
    // Test case for addEscapes method
    @Test
    public void testAddEscapes() {
        String input = "\ttest\n";
        String expectedOutput = "\\ttest\\n";
        
        assertEquals(expectedOutput, TokenMgrError.addEscapes(input));
    }
    
    // Test case for LexicalError method
    @Test
    public void testLexicalError() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 2;
        String errorAfter = "test";
        char curChar = 'a';
        
        String expectedOutput = "Lexical error at line 1, column 2. Encountered: \"a\" (97), after : \"test\"";
        
        assertEquals(expectedOutput, TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
    }
    
    // Test case for getMessage method
    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test Message", 1);
        assertEquals("Test Message", error.getMessage());
    }
    
    // Test case for TokenMgrError constructor
    @Test
    public void testTokenMgrErrorConstructor() {
        TokenMgrError error = new TokenMgrError("Test Message", 1);
        assertEquals("Test Message", error.getMessage());
        assertEquals(1, error.errorCode);
    }
    
    // Test case for TokenMgrError constructor with further parameters
    @Test
    public void testTokenMgrErrorFullConstructor() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 2;
        String errorAfter = "test";
        char curChar = 'a';
        
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, 1);
        String expectedMessage = "Lexical error at line 1, column 2. Encountered: \"a\" (97), after : \"test\"";
        
        assertEquals(expectedMessage, error.getMessage());
        assertEquals(1, error.errorCode);
    }
}