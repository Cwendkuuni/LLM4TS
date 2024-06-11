import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Character();
        assertEquals("Character", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        doctype.reset();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        assertTrue(doctype.getName().equals("html"));
        assertTrue(doctype.getPublicIdentifier().equals("-//W3C//DTD HTML 4.01//EN"));
        assertTrue(doctype.getSystemIdentifier().equals("http://www.w3.org/TR/html4/strict.dtd"));
    }

    // Additional test cases for other methods in the Token class can be added here

}