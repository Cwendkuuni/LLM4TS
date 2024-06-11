import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {
    @Test
    public void testDoctypeToken() {
        // Create a new Doctype token
        Token doctypeToken = new Token.Doctype();

        // Check the type of the token
        assertEquals(TokenType.Doctype, doctypeToken.type);

        // Test reset method
        doctypeToken.reset();
        assertEquals(TokenType.Doctype, doctypeToken.type);

        // Test setting and getting name
        doctypeToken.name("HTML");
        assertEquals("HTML", doctypeToken.getName());

        // Test setting and getting public identifier
        doctypeToken.publicIdentifier("_public");
        assertEquals("_public", doctypeToken.getPublicIdentifier());

        // Test setting and getting system identifier
        doctypeToken.systemIdentifier("_system");
        assertEquals("_system", doctypeToken.getSystemIdentifier());

        // Test isForceQuirks method
        doctypeToken.isForceQuirks(true);
        assertTrue(doctypeToken.isForceQuirks());

        // Test reset method after setting forceQuirks
        doctypeToken.reset();
        assertFalse(doctypeToken.isForceQuirks());
    }
}