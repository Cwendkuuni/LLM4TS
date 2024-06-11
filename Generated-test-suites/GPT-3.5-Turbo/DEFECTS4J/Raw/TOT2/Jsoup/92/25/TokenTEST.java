import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals(Token.TokenType.Doctype, token.type);
    }

    @Test
    public void testTokenTypeNegative() {
        Token token = new Token.Character();
        assertNotEquals(Token.TokenType.Doctype, token.type);
    }

    @Test
    public void testReset() {
        Token token = new Token.Doctype();
        token.reset();
        // Add assertions for token reset functionality
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        // Initialize and set the name of the doctype
        assertEquals("Expected doctype name", doctype.getName(), "Name");
    }

    @Test
    public void testDoctypeForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        // Set force quirks to true and check if the getter returns true
        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }

    // Add more test cases for other methods and scenarios

}