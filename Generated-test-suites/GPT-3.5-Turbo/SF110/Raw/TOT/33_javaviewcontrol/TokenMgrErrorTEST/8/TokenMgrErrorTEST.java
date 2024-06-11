import static org.junit.Assert.*;
import org.junit.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        // Typical use case - testing with a string that contains escape characters
        String str = "Testing addEscapes method with escape characters: \n \r \t \\ \" \'";
        String expected = "\"Testing addEscapes method with escape characters: \\n \\r \\t \\\\ \\\" \\'\"";
        String result = TokenMgrError.addEscapes(str);
        assertEquals(expected, result);
    
        // Edge case - testing with an empty string
        str = "";
        expected = "";
        result = TokenMgrError.addEscapes(str);
        assertEquals(expected, result);
        
        // Error scenario - testing with null string
        str = null;
        expected = "";
        result = TokenMgrError.addEscapes(str);
        assertEquals(expected, result);
    }
    
    @Test
    public void testLexicalError() {
        // Typical use case - testing with valid inputs
        boolean EOFSeen = false;
        int lexState = TokenMgrError.LEXICAL_ERROR;
        int errorLine = 10;
        int errorColumn = 5;
        String errorAfter = "Syntax error";
        char curChar = '/';
        String expected = "Lexical error at line 10, column 5.  Encountered: \"/\" (47), after : \"Syntax error\"";
        String result = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expected, result);
        
        // Edge case - testing with EOF seen
        EOFSeen = true;
        lexState = TokenMgrError.STATIC_LEXER_ERROR;
        errorLine = 1;
        errorColumn = 1;
        errorAfter = "";
        curChar = '\0';
        expected = "Lexical error at line 1, column 1.  Encountered: <EOF>  after : \"\"";
        result = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expected, result);
        
        // Error scenario - testing with invalid inputs
        EOFSeen = false;
        lexState = -1;
        errorLine = -1;
        errorColumn = -1;
        errorAfter = null;
        curChar = 'a';
        expected = "Lexical error at line -1, column -1.  Encountered: \"a\" (97), after : \"null\"";
        result = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetMessage() {
        // Typical use case - testing with a custom error message
        String message = "Error occurred during tokenizing";
        TokenMgrError error = new TokenMgrError(message, TokenMgrError.INVALID_LEXICAL_STATE);
        String expected = "Error occurred during tokenizing";
        String result = error.getMessage();
        assertEquals(expected, result);
    
        // Edge case - testing with a null message
        message = null;
        error = new TokenMgrError(message, TokenMgrError.STATIC_LEXER_ERROR);
        expected = null;
        result = error.getMessage();
        assertEquals(expected, result);
    }
    
    @Test
    public void testTokenMgrError() {
        // Typical use case - testing with a custom error message and valid reason
        String message = "Error occurred during tokenizing";
        int reason = TokenMgrError.LEXICAL_ERROR;
        TokenMgrError error = new TokenMgrError(message, reason);
        int expectedErrorCode = TokenMgrError.LEXICAL_ERROR;
        String expectedErrorMessage = "Error occurred during tokenizing";
        int resultErrorCode = error.errorCode;
        String resultErrorMessage = error.getMessage();
        assertEquals(expectedErrorCode, resultErrorCode);
        assertEquals(expectedErrorMessage, resultErrorMessage);
        
        // Edge case - testing with an empty error message and valid reason
        message = "";
        reason = TokenMgrError.INVALID_LEXICAL_STATE;
        error = new TokenMgrError(message, reason);
        expectedErrorCode = TokenMgrError.INVALID_LEXICAL_STATE;
        expectedErrorMessage = "";
        resultErrorCode = error.errorCode;
        resultErrorMessage = error.getMessage();
        assertEquals(expectedErrorCode, resultErrorCode);
        assertEquals(expectedErrorMessage, resultErrorMessage);
        
        // Error scenario - testing with null error message and invalid reason
        message = null;
        reason = -1;
        error = new TokenMgrError(message, reason);
        expectedErrorCode = -1;
        expectedErrorMessage = null;
        resultErrorCode = error.errorCode;
        resultErrorMessage = error.getMessage();
        assertEquals(expectedErrorCode, resultErrorCode);
        assertEquals(expectedErrorMessage, resultErrorMessage);
    }
    
    @Test
    public void testTokenMgrErrorWithChar() {
        // Typical use case - testing with valid inputs
        boolean EOFSeen = false;
        int lexState = TokenMgrError.STATIC_LEXER_ERROR;
        int errorLine = 1;
        int errorColumn = 4;
        String errorAfter = "/";
        char curChar = '/';
        int reason = TokenMgrError.LOOP_DETECTED;
        String expected = "Lexical error at line 1, column 4.  Encountered: \"/\" (47), after : \"/\"";
        TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        int expectedErrorCode = TokenMgrError.LOOP_DETECTED;
        String expectedErrorMessage = "Lexical error at line 1, column 4.  Encountered: \"/\" (47), after : \"/\"";
        int resultErrorCode = error.errorCode;
        String resultErrorMessage = error.getMessage();
        assertEquals(expectedErrorCode, resultErrorCode);
        assertEquals(expectedErrorMessage, resultErrorMessage);
        
        // Edge case - testing with EOF seen and empty error after string
        EOFSeen = true;
        lexState = TokenMgrError.INVALID_LEXICAL_STATE;
        errorLine = 10;
        errorColumn = 5;
        errorAfter = "";
        curChar = '\0';
        reason = TokenMgrError.STATIC_LEXER_ERROR;
        expected = "Lexical error at line 10, column 5.  Encountered: <EOF>  after : \"\"";
        error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        expectedErrorCode = TokenMgrError.STATIC_LEXER_ERROR;
        expectedErrorMessage = "Lexical error at line 10, column 5.  Encountered: <EOF>  after : \"\"";
        resultErrorCode = error.errorCode;
        resultErrorMessage = error.getMessage();
        assertEquals(expectedErrorCode, resultErrorCode);
        assertEquals(expectedErrorMessage, resultErrorMessage);
        
        // Error scenario - testing with invalid inputs
        EOFSeen = false;
        lexState = -1;
        errorLine = -1;
        errorColumn = -1;
        errorAfter = null;
        curChar = 'a';
        reason = -1;
        expected = "Lexical error at line -1, column -1.  Encountered: \"a\" (97), after : \"null\"";
        error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        expectedErrorCode = -1;
        expectedErrorMessage = "Lexical error at line -1, column -1.  Encountered: \"a\" (97), after : \"null\"";
        resultErrorCode = error.errorCode;
        resultErrorMessage = error.getMessage();
        assertEquals(expectedErrorCode, resultErrorCode);
        assertEquals(expectedErrorMessage, resultErrorMessage);
    }
}