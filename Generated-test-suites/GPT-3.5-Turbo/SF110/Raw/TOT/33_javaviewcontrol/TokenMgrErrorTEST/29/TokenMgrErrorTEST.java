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
    public void testAddEscapes_whenCalledWithEmptyString_thenReturnsEmptyString() {
        String result = tokenMgrError.addEscapes("");
        assertEquals("", result);
    }

    @Test
    public void testAddEscapes_whenCalledWithSpecialCharacters_thenEscapeAndReturnString() {
        String result = tokenMgrError.addEscapes("\"Hello\\nWorld\"");
        assertEquals("\\\"Hello\\\\nWorld\\\"", result);
    }

    @Test
    public void testLexicalError_whenCalledWithEOFSeenAsTrue_thenReturnErrorMessageWithEOF() {
        String result = tokenMgrError.LexicalError(true, 2, 5, 6, "errorAfter", 'a');
        assertEquals("Lexical error at line 5, column 6. Encountered: <EOF>  after : \"errorAfter\"", result);
    }

    @Test
    public void testLexicalError_whenCalledWithEOFSeenAsFalse_thenReturnErrorMessageWithCharacter() {
        String result = tokenMgrError.LexicalError(false, 1, 8, 4, "errorAfter", 'b');
        assertEquals("Lexical error at line 8, column 4. Encountered: \"b\" (98), after : \"errorAfter\"", result);
    }

    @Test
    public void testGetMessage_whenCalledWithNoError_thenReturnSuperMessage() {
        tokenMgrError = new TokenMgrError("Test Message", 0);
        String result = tokenMgrError.getMessage();
        assertEquals("Test Message", result);
    }

    @Test
    public void testTokenMgrError_withoutArguments() {
        tokenMgrError = new TokenMgrError();
        assertNull(tokenMgrError.getMessage());
        assertEquals(0, tokenMgrError.errorCode);
    }
    
    @Test
    public void testTokenMgrError_withMessageAndReason() {
        tokenMgrError = new TokenMgrError("Test Message", 1);
        assertEquals("Test Message", tokenMgrError.getMessage());
        assertEquals(1, tokenMgrError.errorCode);
    }
    
    @Test
    public void testTokenMgrError_withAllArguments() {
        tokenMgrError = new TokenMgrError(false, 2, 7, 3, "errorAfter", 'c', 2);
        String result = tokenMgrError.getMessage();
        assertEquals("Lexical error at line 7, column 3. Encountered: \"c\" (99), after : \"errorAfter\"", result);
        assertEquals(2, tokenMgrError.errorCode);
    }
}