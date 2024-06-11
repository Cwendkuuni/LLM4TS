import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String result = TokenMgrError.addEscapes("This is a test");
        assertEquals("This is a test", result);
    }

    @Test
    public void testLexicalError() {
        String result = TokenMgrError.LexicalError(true, 1, 2, 3, "error", 'a');
        assertEquals("Lexical error at line 2, column 3.  Encountered: \"a\" (97), after : \"error\"", result);
    }

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test Message", TokenMgrError.LEXICAL_ERROR);
        assertEquals("Test Message", error.getMessage());
    }
}