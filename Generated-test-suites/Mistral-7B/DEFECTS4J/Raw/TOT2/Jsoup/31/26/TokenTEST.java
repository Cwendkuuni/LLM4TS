import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {
    @Test
    public void testDoctypeCreation() {
        Token doctype = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctype.tokenType());
    }

    @Test
    public void testDoctypeName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        assertEquals("html", doctype.getName());
    }

    @Test
    public void testDoctypePublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier("_xml");
        assertEquals("_xml", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeSystemIdentifierEmpty() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier("");
        assertEquals("", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeSystemIdentifierWithValue() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier("http://example.com/");
        assertEquals("http://example.com/", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeForceQuirksTrue() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.isForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoctypeNameNull() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name(null);
    }
}