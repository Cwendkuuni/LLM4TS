import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        assertEquals("Doctype", doctypeToken.tokenType());

        doctypeToken.reset();

        assertEquals("Doctype", doctypeToken.tokenType());
        assertNull(doctypeToken.asDoctype().getName());
        assertNull(doctypeToken.asDoctype().getPublicIdentifier());
        assertNull(doctypeToken.asDoctype().getSystemIdentifier());
        assertFalse(doctypeToken.asDoctype().isForceQuirks());
    }

    @org.junit.Test
    public void testDoctypeTokenWithData() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.asDoctype().getName().append("html");
        doctypeToken.asDoctype().getPublicIdentifier().append("public id");
        doctypeToken.asDoctype().getSystemIdentifier().append("system id");
        doctypeToken.asDoctype().setForceQuirks(true);

        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals("html", doctypeToken.asDoctype().getName());
        assertEquals("public id", doctypeToken.asDoctype().getPublicIdentifier());
        assertEquals("system id", doctypeToken.asDoctype().getSystemIdentifier());
        assertTrue(doctypeToken.asDoctype().isForceQuirks());

        doctypeToken.reset();

        assertEquals("Doctype", doctypeToken.tokenType());
        assertNull(doctypeToken.asDoctype().getName());
        assertNull(doctypeToken.asDoctype().getPublicIdentifier());
        assertNull(doctypeToken.asDoctype().getSystemIdentifier());
        assertFalse(doctypeToken.asDoctype().isForceQuirks());
    }

    @org.junit.Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        startTagToken.name("html");

        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("html", startTagToken.name());
        assertNull(startTagToken.asStartTag().getAttributes());
        assertFalse(startTagToken.asStartTag().isSelfClosing());

        startTagToken.newAttribute();
        startTagToken.asStartTag().appendAttributeName("class");
        startTagToken.asStartTag().appendAttributeValue("test");

        assertEquals("StartTag", startTagTagToken.tokenType());
        assertEquals("html", startTagToken.name());
        Attributes attributes = startTagToken.asStartTag().getAttributes();
        assertEquals(1, attributes.size());
        Attribute attribute = attributes.get("class");
        assertEquals("test", attribute.getValue());
        assertFalse(startTagToken.asStartTag().isSelfClosing());

        startTagToken.finaliseTag();

        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("html", startTagToken.name());
        attributes = startTagToken.asStartTag().getAttributes();
        assertEquals(1, attributes.size());
        attribute = attributes.get("class");
        assertEquals("test", attribute.getValue());
        assertFalse(startTagToken.asStartTag().isSelfClosing());

        doctypeToken.reset();
    }

    @org.junit.Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        endTagToken.name("html");

        assertEquals("EndTag", endTagToken.tokenType());
        assertEquals("html", endTagToken.name());

        doctypeToken.reset();
    }

    @org.junit.Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        commentToken.asComment().getData().append("This is a comment.");

        assertEquals("Comment", commentToken.tokenType());
        assertEquals("This is a comment.", commentToken.asComment().getData());

        doctypeToken.reset();
    }

    @org.junit.Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        characterToken.asCharacter().getData().append('A');

        assertEquals("Character", characterToken.tokenType());
        assertEquals('A', characterToken.asCharacter().getData().charAt(0));

        doctypeToken.reset();
    }

    @org.junit.Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();

        assertEquals("EOF", eofToken.tokenType());

        doctypeToken.reset();
    }
}