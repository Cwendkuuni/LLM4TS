import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token token = new Token.Doctype();
        assertEquals(TokenType.Doctype, token.type);

        token.asDoctype().reset();
        Assert.assertNull(token.asDoctype().getName());
        Assert.assertNull(token.asDoctype().getPublicIdentifier());
        Assert.assertNull(token.asDoctype().getSystemIdentifier());
        Assert.assertFalse(token.asDoctype().isForceQuirks());

        token.asDoctype().getName("html");
        assertEquals("html", token.asDoctype().getName());
        assertEquals("html", token.tokenType());

        token.asDoctype().setForceQuirks(true);
        assertTrue(token.asDoctype().isForceQuirks());

        token.asDoctype().getSystemIdentifier().append("about:blank");
        assertEquals("about:blank", token.asDoctype().getSystemIdentifier().toString());
    }

    @Test
    public void testStartTagToken() {
        Token token = new Token.StartTag();
        assertEquals(TokenType.StartTag, token.type);

        token.asStartTag().reset();
        Assert.assertNull(token.asStartTag().tagName);
        Assert.assertNull(token.asStartTag().normalName);
        Assert.assertNull(token.asStartTag().pendingAttributeName);
        Assert.assertNull(token.asStartTag().pendingAttributeValue);
        Assert.assertFalse(token.asStartTag().hasPendingAttributeValue);
        Assert.assertFalse(token.asStartTag().selfClosing);
        Assert.assertNull(token.asStartTag().attributes);

        token.asStartTag().name("html");
        assertEquals("html", token.asStartTag().name());
        assertEquals("html", token.asStartTag().normalName());
        assertEquals("html", token.tokenType());

        token.asStartTag().nameAttr("class", "test");
        assertEquals("class", token.asStartTag().pendingAttributeName);
        Assert.assertNull(token.asStartTag().pendingAttributeValue);
        Assert.assertFalse(token.asStartTag().hasPendingAttributeValue);
        Assert.assertFalse(token.asStartTag().selfClosing);
        Attributes attributes = token.asStartTag().getAttributes();
        Attribute attribute = attributes.get("class");
        assertEquals("test", attribute.getValue());

        token.asStartTag().appendAttributeValue(" data-test");
        token.asStartTag().hasPendingAttributeValue = true;
        token.asStartTag().newAttribute();
        assertEquals("data-test", token.asStartTag().pendingAttributeName);
        assertEquals(" data-test", token.asStartTag().pendingAttributeValueS);
        assertTrue(token.asStartTag().hasPendingAttributeValue);
        Attribute attribute1 = attributes.get("data-test");
        assertEquals(" data-test", attribute1.getValue());

        token.asStartTag().setEmptyAttributeValue();
        Attributes attributes1 = token.asStartTag().getAttributes();
        Attribute attribute2 = attributes1.get("data-test");
        Assert.assertEquals("", attribute2.getValue());

        token.asStartTag().selfClosing = true;
        assertTrue(token.asStartTag().isSelfClosing());
    }

    @Test
    public void testEndTagToken() {
        Token token = new Token.EndTag();
        assertEquals(TokenType.EndTag, token.type);

        token.asEndTag().reset();
        Assert.assertNull(token.asEndTag().tagName);
        Assert.assertNull(token.asEndTag().normalName);
        Assert.assertFalse(token.asEndTag().selfClosing);

        token.asEndTag().name("html");
        assertEquals("html", token.asEndTag().name());
        assertEquals("html", token.asEndTag().normalName());
        assertEquals("html", token.tokenType());

        token.asEndTag().selfClosing = true;
        assertTrue(token.asEndTag().isSelfClosing());
    }

    @Test
    public void testCommentToken() {
        Token token = new Token.Comment();
        assertEquals(TokenType.Comment, token.type);

        token.asComment().reset();
        Assert.assertNull(token.asComment().getData());
        Assert.assertFalse(token.asComment().bogus);

        token.asComment().getData().append("<!-- comment -->");
        assertEquals("comment ", token.asComment().getData().toString());
        assertEquals("comment ", token.tokenType());

        token.asComment().bogus = true;
        assertTrue(token.asComment().isBogus());
    }

    @Test
    public void testCharacterToken() {
        Token token = new Token.Character();
        assertEquals(TokenType.Character, token.type);

        token.asCharacter().reset();
        Assert.assertNull(token.asCharacter().getData());

        token.asCharacter().data("a");
        assertEquals("a", token.asCharacter().getData());
        assertEquals("a", token.tokenType());
    }

    @Test
    public void testEOFToken() {
        Token token = new Token.EOF();
        assertEquals(TokenType.EOF, token.type);

        token.asEOF().reset();
    }
}