import static org.junit.Assert.*;
import org.junit.Test;
import com.pmdesigns.jvc.tools.TokenMgrError;

public class TokenMgrErrorTest {

    @Test
    public void testTokenMgrError() {
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
    }

    @Test
    public void testTokenMgrErrorWithMessageAndReason() {
        TokenMgrError error = new TokenMgrError("Test Message", 0);
        assertEquals("Test Message", error.getMessage());
        assertEquals(0, error.errorCode);
    }

    @Test
    public void testTokenMgrErrorWithLexicalError() {
        TokenMgrError error = new TokenMgrError(true, 1, 2, 3, "Error After", 'a', 0);
        assertEquals("Lexical error at line 2, column 3.  Encountered: \"a\" (97), after : \"Error After\"", error.getMessage());
        assertEquals(0, error.errorCode);
    }

}