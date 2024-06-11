import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    private TokenMgrError tokenMgrError;

    @Before
    public void setUp() {
        tokenMgrError = new TokenMgrError();
    }

    @Test
    public void testAddEscapes() {
        String str = "Hello World!";
        String expected = "Hello World!";
        String result = tokenMgrError.addEscapes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testAddEscapesWithSpecialCharacters() {
        String str = "Hello\tWorld\n";
        String expected = "Hello\\tWorld\\n";
        String result = tokenMgrError.addEscapes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testAddEscapesWithUnicodeCharacter() {
        String str = "Hello\u03A9World";
        String expected = "Hello\\u03A9World";
        String result = tokenMgrError.addEscapes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testLexicalErrorWithoutEOF() {
        boolean EOFSeen = false;
        int lexState = 1;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "Hello";
        char curChar = 'W';
        int reason = TokenMgrError.LEXICAL_ERROR;
        String expected = "Lexical error at line 5, column 10.  Encountered: \"W\" (87), after : \"Hello\"";
        String result = tokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expected, result);
    }

    @Test
    public void testLexicalErrorWithEOF() {
        boolean EOFSeen = true;
        int lexState = 1;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "Hello";
        char curChar = 'W';
        int reason = TokenMgrError.LEXICAL_ERROR;
        String expected = "Lexical error at line 5, column 10.  Encountered: <EOF> , after : \"Hello\"";
        String result = tokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expected, result);
    }

    @Test
    public void testGetMessage() {
        String message = "Error occurred.";
        tokenMgrError = new TokenMgrError(message, TokenMgrError.LEXICAL_ERROR);
        String expected = message;
        String result = tokenMgrError.getMessage();
        assertEquals(expected, result);
    }

    @Test
    public void testConstructorWithMessageAndReason() {
        String message = "Error occurred.";
        int reason = TokenMgrError.LEXICAL_ERROR;
        tokenMgrError = new TokenMgrError(message, reason);
        String expected = message;
        String result = tokenMgrError.getMessage();
        assertEquals(expected, result);
        assertEquals(reason, tokenMgrError.errorCode);
    }

    @Test
    public void testConstructorWithOtherParameters() {
        boolean EOFSeen = false;
        int lexState = 1;
        int errorLine = 5;
        int errorColumn = 10;
        String errorAfter = "Hello";
        char curChar = 'W';
        int reason = TokenMgrError.LEXICAL_ERROR;
        String expected = "Lexical error at line 5, column 10.  Encountered: \"W\" (87), after : \"Hello\"";
        
        tokenMgrError = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String result = tokenMgrError.getMessage();
        assertEquals(expected, result);
        assertEquals(reason, tokenMgrError.errorCode);
    }
}