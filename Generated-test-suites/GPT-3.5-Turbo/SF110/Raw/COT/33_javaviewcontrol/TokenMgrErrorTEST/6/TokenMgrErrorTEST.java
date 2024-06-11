import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError();
        assertEquals(null, error.getMessage());
        
        TokenMgrError error2 = new TokenMgrError("Test message", 1);
        assertEquals("Test message", error2.getMessage());
    }
    
    @Test
    public void testConstructor() {
        TokenMgrError error = new TokenMgrError();
        assertEquals(0, error.errorCode);
        
        TokenMgrError error2 = new TokenMgrError("Test message", 1);
        assertEquals("Test message", error2.getMessage());
        assertEquals(1, error2.errorCode);
        
        TokenMgrError error3 = new TokenMgrError(true, 3, 4, 5, "Test after", 'a', 2);
        assertEquals("Lexical error at line 4, column 5.  Encountered: \"a\" (97), after : \"Test after\"", error3.getMessage());
        assertEquals(2, error3.errorCode);
    }
}