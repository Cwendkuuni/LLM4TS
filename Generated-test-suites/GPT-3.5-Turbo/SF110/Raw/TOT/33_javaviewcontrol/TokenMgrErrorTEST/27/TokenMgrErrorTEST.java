import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.pmdesigns.jvc.tools.TokenMgrError;    

public class TokenMgrErrorTest {
    
    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test message", TokenMgrError.LEXICAL_ERROR);
        assertEquals("Test message", error.getMessage());
    }
    
    @Test
    public void testConstructor() {
        TokenMgrError error = new TokenMgrError("Test message", TokenMgrError.LEXICAL_ERROR);
        assertEquals("Test message", error.getMessage());
        assertEquals(TokenMgrError.LEXICAL_ERROR, error.errorCode);
    }
    
    @Test
    public void testLexicalError() {
        String errorString = TokenMgrError.LexicalError(false, 1, 5, 10, "Error after", 'a');
        String expected = "Lexical error at line 5, column 10.  Encountered: \"a\" (97), after : \"Error after\"";
        assertEquals(expected, errorString);
    }

    @Test
    public void testAddEscapes() {
        String input = "\"Test string \\t with \\n escapes\"";
        String expected = "\\\"Test string \\t with \\n escapes\\\"";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expected, output);
    }
}