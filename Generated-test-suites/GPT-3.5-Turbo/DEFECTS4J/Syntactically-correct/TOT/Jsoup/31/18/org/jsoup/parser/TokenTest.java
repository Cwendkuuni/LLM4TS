import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeTokenType() {
        Token.Doctype doctypeToken = new Token.Doctype();
        assertEquals(Token.TokenType.Doctype, doctypeToken.type);
    }

    @Test
    public void testDoctypeName() {
        Token.Doctype doctypeToken = new Token.Doctype();
        doctypeToken.name.append("html");
        assertEquals("html", doctypeToken.getName());
    }

    @Test
    public void testDoctypePublicIdentifier() {
        Token.Doctype doctypeToken = new Token.Doctype();
        doctypeToken.publicIdentifier.append("-//W3C//DTD HTML 4.01 Transitional//EN");
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctypeToken.getPublicIdentifier());
    }

    @Test
    public void testDoctypeSystemIdentifier() {
        Token.Doctype doctypeToken = new Token.Doctype();
        doctypeToken.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctypeToken.getSystemIdentifier());
    }

    @Test
    public void testDoctypeForceQuirks() {
        Token.Doctype doctypeToken = new Token.Doctype();
        assertFalse(doctypeToken.isForceQuirks());
    }

    @Test
    public void testStartTagName() {
        Token.StartTag startTagToken = new Token.StartTag();
        startTagToken.tagName = "div";
        assertEquals("div", startTagToken.name());
    }

    @Test
    public void testStartTagSelfClosing() {
        Token.StartTag startTagToken = new Token.StartTag();
        startTagToken.selfClosing = true;
        assertTrue(startTagToken.isSelfClosing());
    }

    @Test
    public void testStartTagAttributes() {
        Token.StartTag startTagToken = new Token.StartTag();
        startTagToken.attributes = new Attributes();
        startTagToken.attributes.put("id", "myId");
        assertNotNull(startTagToken.getAttributes());
    }

    @Test
    public void testEndTagName() {
        Token.EndTag endTagToken = new Token.EndTag();
        endTagToken.tagName = "p";
        assertEquals("p", endTagToken.name());
    }

    @Test
    public void testCommentData() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.data.append("This is a comment");
        assertEquals("This is a comment", commentToken.getData());
    }

    @Test
    public void testCharacterData() {
        Token.Character characterToken = new Token.Character("This is character data");
        assertEquals("This is character data", characterToken.getData());
    }

    @Test
    public void testEOFTokenType() {
        Token.EOF eofToken = new Token.EOF();
        assertEquals(Token.TokenType.EOF, eofToken.type);
    }
}