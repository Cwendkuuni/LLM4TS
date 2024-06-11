import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        // Given: A new Doctype token
        Token doctypeToken = new Token.Doctype();

        // When: Checking the token type
        TokenType expectedType = TokenType.Doctype;
        TokenType actualType = doctypeToken.type;

        // Then: The token types should match
        assertEquals(expectedType, actualType);

        // Given: Setting values for the Doctype token
        doctypeToken.name.append("html");
        doctypeToken.publicIdentifier.append("HTML5");
        doctypeToken.systemIdentifier.append("http://www.w3.org/TR/html5/");
        doctypeToken.forceQuirks = true;

        // When: Getting the values from the Doctype token
        String name = doctypeToken.getName();
        String publicIdentifier = doctypeToken.getPublicIdentifier();
        String systemIdentifier = doctypeToken.getSystemIdentifier();
        boolean forceQuirks = doctypeToken.isForceQuirks();

        // Then: The values should match the ones set earlier
        assertEquals("html", name);
        assertEquals("HTML5", publicIdentifier);
        assertEquals("http://www.w3.org/TR/html5/", systemIdentifier);
        assertTrue(forceQuirks);

        // Given: Resetting the Doctype token
        doctypeToken.reset();

        // Then: The token should be reset to its initial state
        assertFalse(doctypeToken.isForceQuirks());
        assertEquals(0, doctypeToken.name.length());
        assertEquals(0, doctypeToken.publicIdentifier.length());
        assertEquals(0, doctypeToken.systemIdentifier.length());
    }
}