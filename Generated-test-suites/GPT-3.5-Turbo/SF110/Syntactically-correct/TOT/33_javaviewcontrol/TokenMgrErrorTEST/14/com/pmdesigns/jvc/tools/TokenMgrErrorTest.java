import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes_typicalCase() {
        String input = "Hello, World!";
        String expectedOutput = "Hello, World!";
        
        String actualOutput = TokenMgrError.addEscapes(input);
        
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testAddEscapes_specialCharacters() {
        String input = "Hello\n\tWorld!";
        String expectedOutput = "Hello\\n\\tWorld!";
        
        String actualOutput = TokenMgrError.addEscapes(input);
        
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testAddEscapes_unicodeCharacter() {
        String input = "©";
        String expectedOutput = "\u00a9";
        
        String actualOutput = TokenMgrError.addEscapes(input);
        
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testLexicalError_EOFSeen() {
        boolean EOFSeen = true;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 5;
        String errorAfter = "Hello";
        char curChar = '\0';
        int reason = TokenMgrError.LEXICAL_ERROR;
        
        String expectedOutput = "Lexical error at line 1, column 5.  Encountered: <EOF> after : \"Hello\"";
        
        String actualOutput = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testLexicalError_characterOutOfRange() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 2;
        int errorColumn = 3;
        String errorAfter = "World";
        char curChar = '\u0128'; // Character out of range
        int reason = TokenMgrError.LEXICAL_ERROR;
        
        String expectedOutput = "Lexical error at line 2, column 3.  Encountered: \"\\u0128\" (296), after : \"World\"";
        
        String actualOutput = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testGetMessage_typicalCase() {
        String message = "Error occurred";
        int reason = TokenMgrError.LEXICAL_ERROR;
        
        TokenMgrError error = new TokenMgrError(message, reason);
        
        assertEquals(message, error.getMessage());
    }
    
    @Test
    public void testGetMessage_emptyMessage() {
        String message = "";
        int reason = TokenMgrError.LEXICAL_ERROR;
        
        TokenMgrError error = new TokenMgrError(message, reason);
        
        assertEquals(message, error.getMessage());
    }
    
    @Test
    public void testConstructor_typicalCase() {
        String message = "Error occurred";
        int reason = TokenMgrError.LEXICAL_ERROR;
        
        TokenMgrError error = new TokenMgrError(message, reason);
        
        assertEquals(message, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
    
    @Test
    public void testConstructor_emptyMessage() {
        String message = "";
        int reason = TokenMgrError.LEXICAL_ERROR;
        
        TokenMgrError error = new TokenMgrError(message, reason);
        
        assertEquals(message, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
    
    @Test
    public void testConstructor_LexicalError() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 3;
        int errorColumn = 4;
        String errorAfter = "Java";
        char curChar = '.';
        int reason = TokenMgrError.LEXICAL_ERROR;
        
        String expectedMessage = "Lexical error at line 3, column 4.  Encountered: \".\" (46), after : \"Java\"";
        
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        
        assertEquals(expectedMessage, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
}