import org.junit.Test;
import org.jsoup.parser.Token;
import static org.junit.Assert.assertEquals;

public class TokenTest {
    @Test
    public void testTokenType() {
        Token token = new Token.Character("a");
        assertEquals("Character", token.tokenType());
    }

    @Test
    public void testIsDoctype() {
        Token doctypeToken = new Token.Doctype();
        assertTrue(doctypeToken.isDoctype());
        Token characterToken = new Token.Character("a");
        assertFalse(characterToken.isDoctype());
    }

    // Test other method isX() and isX() methods similarly.
}