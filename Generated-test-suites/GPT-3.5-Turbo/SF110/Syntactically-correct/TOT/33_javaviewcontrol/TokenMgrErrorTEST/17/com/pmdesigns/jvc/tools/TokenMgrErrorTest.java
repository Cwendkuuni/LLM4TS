import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test message", 0);
        assertEquals("Test message", error.getMessage());
    }
    
    @Test
    public void testConstructor() {
        TokenMgrError error = new TokenMgrError("Test message", 1);
        assertEquals("Test message", error.getMessage());
        assertEquals(1, error.errorCode);
    }
    
    @Test
    public void testLexicalError() {
        String expectedMessage = "Lexical error at line 3, column 2.  Encountered: \"a\" (97), after : \"bc\"";
        String actualMessage = TokenMgrError.LexicalError(false, 1, 3, 2, "bc", 'a');
        assertEquals(expectedMessage, actualMessage);
    }
}