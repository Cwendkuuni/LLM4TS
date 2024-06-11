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
    public void testGetMessage() {
        assertNull(tokenMgrError.getMessage());
    }

    @Test
    public void testTokenMgrErrorWithoutMessage() {
        TokenMgrError tokenMgrError = new TokenMgrError();
        assertNull(tokenMgrError.getMessage());
        assertEquals(0, tokenMgrError.errorCode);
    }

    @Test
    public void testTokenMgrErrorWithMessageAndReason() {
        TokenMgrError tokenMgrError = new TokenMgrError("Test message", 1);
        assertEquals("Test message", tokenMgrError.getMessage());
        assertEquals(1, tokenMgrError.errorCode);
    }

    @Test
    public void testTokenMgrErrorWithLexicalError() {
        TokenMgrError tokenMgrError = new TokenMgrError(false, 1, 2, 3, "Test", 'a', 0);
        assertEquals("Lexical error at line 2, column 3.  Encountered: \"a\" (97), after : \"Test\"", tokenMgrError.getMessage());
        assertEquals(0, tokenMgrError.errorCode);
    }
}