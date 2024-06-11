import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token token = new Token.Doctype();
        assertEquals(TokenType.Doctype, token.type);

        Token resetToken = token.reset();
        assertEquals(TokenType.Doctype, resetToken.type);

        TokenType tokenType = token.tokenType();
        assertEquals("Doctype", tokenType.toString());

        String name = new Token.Doctype().getName();
        assertNull(name);

        String publicIdentifier = new Token.Doctype().getPublicIdentifier();
        assertNull(publicIdentifier);

        String systemIdentifier = new Token.Doctype().getSystemIdentifier();
        assertNull(systemIdentifier);

        Token doctypeTokenWithIdentifier = new Token.Doctype().name("html").publicIdentifier("_public").systemIdentifier("_system");
        assertEquals("Doctype", doctypeTokenWithIdentifier.tokenType().toString());
        assertEquals("html", doctypeTokenWithIdentifier.getName());
        assertEquals("_public", doctypeTokenWithIdentifier.getPublicIdentifier());
        assertEquals("_system", doctypeTokenWithIdentifier.getSystemIdentifier());
        assertEquals(true, doctypeTokenWithIdentifier.isDoctype());
    }

    @Test
    public void testStartTagToken() {
        Token token = new Token.StartTag();
        assertEquals(TokenType.StartTag, token.type);

        Token resetToken = token.reset();
        assertEquals(TokenType.StartTag, resetToken.type);

        token.name("div");
        assertEquals("div", token.name());
        assertEquals("div", token.normalName());
        assertEquals(true, token.isStartTag());

        token.selfClosing(true);
        assertTrue(token.isSelfClosing());

        token.newAttribute().name("class").value("test");
        assertEquals(2, token.getAttributes().size());
        assertEquals("class", token.getAttributes().keySet().toString());
        assertEquals("test", token.getAttributes().get("class").toString());

        token.finaliseTag();
        assertFalse(token.getAttributes() == null);
    }

    @Test
    public void testEndTagToken() {
        Token token = new Token.EndTag();
        assertEquals(TokenType.EndTag, token.type);

        Token resetToken = token.reset();
        assertEquals(TokenType.EndTag, resetToken.type);

        token.name("div");
        assertEquals("div", token.name());
        assertEquals("DIV", token.normalName());
        assertEquals(true, token.isEndTag());
    }

    @Test
    public void testCommentToken() {
        Token token = new Token.Comment();
        assertEquals(TokenType.Comment, token.type);

        String commentData = "This is a comment";
        Token resetToken = token.reset();
        token.getData(commentData);
        assertEquals(commentData, token.getData());

        assertEquals("<!--" + commentData + "-->", token.toString());
    }

    @Test
    public void testCharacterToken() {
        Token token = new Token.Character();
        assertEquals(TokenType.Character, token.type);

        String charData = "!";
        Token resetToken = token.reset();
        token.data(charData);
        assertEquals(charData, token.getData());

        assertEquals(charData, token.toString());
    }

    @Test
    public void testEOFToken() {
        Token token = new Token.EOF();
        assertEquals(TokenType.EOF, token.type);

        Token resetToken = token.reset();
        assertEquals(TokenType.EOF, resetToken.type);

        assertFalse(token.getData() != null);
        assertEquals("", token.toString());
    }
}