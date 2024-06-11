import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.helper.Validate;

public class TokenTest {

    @Test
    public void testDoctypeConstructor() {
        Token token = new Token.Doctype();
        assertEquals(TokenType.Doctype, token.type);
        assertNull(token.getName());
        assertNull(token.getPublicIdentifier());
        assertNull(token.getSystemIdentifier());
        assertFalse(token.isForceQuirks());
    }

    @Test
    public void testDoctypeSettersAndGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName().append("html");
        assertEquals("html", doctype.getName());
        doctype.getPublicIdentifier().append("public");
        assertEquals("public", doctype.getPublicIdentifier());
        doctype.getSystemIdentifier().append("/index.html");
        assertEquals("/index.html", doctype.getSystemIdentifier());
        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    @Test(expected = AssertionError.class)
    public void testDoctypeNegativeName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName().append("-html");
    }

    @Test
    public void testTagConstructor() {
        Token token = new Token.Tag();
        assertEquals(TokenType.Tag, token.type);
        assertNull(token.tagName);
        assertNull(token.pendingAttributeName);
        assertNull(token.pendingAttributeValue);
        assertFalse(token.selfClosing);
        assertNull(token.attributes);
    }

    @Test
    public void testTagSettersAndGetters() {
        Token.Tag tag = new Token.Tag();
        tag.name("div");
        assertEquals("div", tag.name());
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("red");
        tag.newAttribute();
        tag.appendAttributeName("id");
        tag.appendAttributeValue("box");
        assertEquals("class=red id=box", tag.toString());
        tag.finaliseTag();
        assertEquals(2, tag.getAttributes().size());
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testTagConstructorWithNameAndAttributes() {
        Token.Tag tag = new Token.Tag("p", new Attributes());
        assertEquals("p", tag.name());
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertEquals(0, tag.getAttributes().size());
    }

    @Test
    public void testTagToString() {
        Token.StartTag startTag = new Token.StartTag("div");
        assertEquals("<div>", startTag.toString());
        Token.StartTag startTagWithAttributes = new Token.StartTag("p", new Attributes().put("class", "red").put("id", "box"));
        assertEquals("<p class=\"red\" id=\"box\"></p>", startTagWithAttributes.toString());
    }

    @Test
    public void testTokenIsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
        Token tag = new Token.Tag();
        assertFalse(tag.isDoctype());
    }

    @Test
    public void testTokenIsStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());
        Token doctype = new Token.Doctype();
        assertFalse(doctype.isStartTag());
    }

    @Test
    public void testTokenIsEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.isEndTag());
        Token startTag = new Token.StartTag();
        assertFalse(startTag.isEndTag());
    }

    @Test
    public void testTokenIsComment() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());
        Token tag = new Token.Tag();
        assertFalse(tag.isComment());
    }

    @Test
    public void testTokenIsCharacter() {
        Token token = new Token.Character("a");
        assertTrue(token.isCharacter());
        Token doctype = new Token.Doctype();
        assertFalse(doctype.isCharacter());
    }

    @Test
    public void testTokenIsEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());
        Token tag = new Token.Tag();
        assertFalse(tag.isEOF());
    }
}