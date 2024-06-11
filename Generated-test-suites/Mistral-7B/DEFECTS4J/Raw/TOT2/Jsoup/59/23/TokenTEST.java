import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.assertEquals;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctypeToken.tokenType());
        assertEquals("Doctype", doctypeToken.getClass().getSimpleName());

        Token resetDoctypeToken = doctypeToken.reset();
        assertEquals(TokenType.Doctype, resetDoctypeToken.tokenType());
    }

    @Test
    public void testDoctypeResetName() {
        Token doctypeToken = new Token.Doctype();
        StringBuilder name = new StringBuilder("<!DOCTYPE html>");
        doctypeToken.name(name.toString());
        assertEquals("<!DOCTYPE html>", doctypeToken.getName());

        Token resetDoctypeToken = doctypeToken.reset();
        assertEquals("", resetDoctypeToken.getName());
    }

    @Test(expected = AssertionError.class)
    public void testDoctypeResetNullName() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.name(null);
        doctypeToken.reset();
    }

    @Test
    public void testDoctypeResetAttributes() {
        Token doctypeToken = new Token.Doctype();
        Attributes attributes = new Attributes();
        attributes.put("xmlns", "http://www.w3.org/2000/xmlns/");
        doctypeToken.attributes = attributes;

        Token resetDoctypeToken = doctypeToken.reset();
        assertNull(resetDoctypeToken.attributes);
    }
}