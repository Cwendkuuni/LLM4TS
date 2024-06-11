import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String result = TokenMgrError.addEscapes("\b\t\n\f\r\"\'\\");
        assertEquals("\\b\\t\\n\\f\\r\\\"\\\'\\\\", result);
    }
    
    @Test
    public void testLexicalError() {
        String result = TokenMgrError.LexicalError(false, 0, 1, 1, "errorAfter", 'a');
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"errorAfter\"", result);
    }
    
    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test message", TokenMgrError.LEXICAL_ERROR);
        assertEquals("Test message", error.getMessage());
    }
    
    @Test
    public void testTokenMgrErrorNoArgs() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
    }
    
    @Test
    public void testTokenMgrErrorWithArgs() {
        TokenMgrError error = new TokenMgrError(true, 0, 1, 1, "errorAfter", 'a', TokenMgrError.LEXICAL_ERROR);
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"errorAfter\"", error.getMessage());
    }
}