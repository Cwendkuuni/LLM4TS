
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {
    
    @Test
    public void testAddEscapes_typicalCase() {
        String input = "Hello World";
        String expectedOutput = "Hello World";
        
        String result = TokenMgrError.addEscapes(input);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testAddEscapes_escapeBackslash() {
        String input = "This is a \\ test";
        String expectedOutput = "This is a \\\\ test";
        
        String result = TokenMgrError.addEscapes(input);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testAddEscapes_escapeNewLine() {
        String input = "This is a \n test";
        String expectedOutput = "This is a \\n test";
        
        String result = TokenMgrError.addEscapes(input);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testAddEscapes_escapeTab() {
        String input = "This is a \t test";
        String expectedOutput = "This is a \\t test";
        
        String result = TokenMgrError.addEscapes(input);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testAddEscapes_escapeDoubleQuote() {
        String input = "This is a \" test";
        String expectedOutput = "This is a \\\" test";
        
        String result = TokenMgrError.addEscapes(input);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testAddEscapes_escapeSingleQuote() {
        String input = "This is a ' test";
        String expectedOutput = "This is a \\' test";
        
        String result = TokenMgrError.addEscapes(input);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testAddEscapes_escapeCarriageReturn() {
        String input = "This is a \r test";
        String expectedOutput = "This is a \\r test";
        
        String result = TokenMgrError.addEscapes(input);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testAddEscapes_escapeNullCharacter() {
        String input = "This is a \0 test";
        String expectedOutput = "This is a \\0 test";
        
        String result = TokenMgrError.addEscapes(input);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testAddEscapes_escapeInvalidCharacter() {
        String input = "This is a ñ test";
        String expectedOutput = "This is a \\u00F1 test";
        
        String result = TokenMgrError.addEscapes(input);
        
        assertEquals(expectedOutput, result);
    }
    
    @Test
    public void testLexicalError_EOFSeen() {
        boolean EOFSeen = true;
        int lexState = 1;
        int errorLine = 10;
        int errorColumn = 5;
        String errorAfter = "abc";
        char curChar = '\0';
        int reason = TokenMgrError.LEXICAL_ERROR;
        
        String expectedMessage = "Lexical error at line 10, column 5.  Encountered: <EOF>  after : \"abc\"";
        
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        
        assertEquals(expectedMessage, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
    
    @Test
    public void testLexicalError_notEOFSeen() {
        boolean EOFSeen = false;
        int lexState = 2;
        int errorLine = 5;
        int errorColumn = 2;
        String errorAfter = "def";
        char curChar = 'g';
        int reason = TokenMgrError.INVALID_LEXICAL_STATE;
        
        String expectedMessage = "Lexical error at line 5, column 2.  Encountered: \"g\" (103), after : \"def\"";
        
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        
        assertEquals(expectedMessage, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
    
    @Test
    public void testConstructor_withoutMessage() {
        String expectedMessage = null;
        int reason = TokenMgrError.STATIC_LEXER_ERROR;
        
        TokenMgrError error = new TokenMgrError();
        
        assertEquals(expectedMessage, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
    
    @Test
    public void testConstructor_withMessage() {
        String message = "Error occurred";
        int reason = TokenMgrError.LOOP_DETECTED;
        
        TokenMgrError error = new TokenMgrError(message, reason);
        
        assertEquals(message, error.getMessage());
        assertEquals(reason, error.errorCode);
    }
    
}
