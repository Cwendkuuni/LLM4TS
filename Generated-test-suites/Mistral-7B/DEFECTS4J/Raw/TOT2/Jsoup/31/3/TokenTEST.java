import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {
    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());
        assertEquals(TokenType.Doctype, doctype.type);
    }

    // Edge case 1: Testing empty Doctype
    @Test
    public void testEmptyDoctype() {
        Token doctype = new Token.Doctype();
        doctype.name.append("");
        assertEquals("Doctype", doctype.tokenType());
        assertEquals(TokenType.Doctype, doctype.type);
    }

    // Edge case 2: Testing Doctype with name, publicIdentifier, systemIdentifier and forceQuirks
    @Test
    public void testDoctypeWithAttributes() {
        Token doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("MyCompany, Public, v1.0");
        doctype.systemIdentifier.append("/myapp/index.html");
        doctype.forceQuirks = true;
        assertEquals("Doctype", doctype.tokenType());
        assertEquals(TokenType.Doctype, doctype.type);
        assertEquals("html", doctype.getName());
        assertEquals("MyCompany, Public, v1.0", doctype.getPublicIdentifier());
        assertEquals("/myapp/index.html", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }
}