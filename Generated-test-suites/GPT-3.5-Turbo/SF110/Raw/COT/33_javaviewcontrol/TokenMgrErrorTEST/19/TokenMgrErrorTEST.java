
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("test message", 0);
        assertEquals("test message", error.getMessage());
    }

    @Test
    public void testTokenMgrErrorEmptyConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
        assertEquals(0, error.errorCode);
    }
    
    @Test
    public void testTokenMgrErrorConstructor() {
        TokenMgrError error = new TokenMgrError("test message", 1);
        assertEquals("test message", error.getMessage());
        assertEquals(1, error.errorCode);
    }
    
    @Test
    public void testTokenMgrErrorConstructorWithLexicalError() {
        TokenMgrError error = new TokenMgrError(false, 0, 1, 1, "errorAfter", 'a', 2);
        assertEquals("Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"errorAfter\"", error.getMessage());
        assertEquals(2, error.errorCode);
    }
}
