import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());
        assertTrue(doctype instanceof Token.Doctype);

        doctype.reset();
        assertNull(doctype.getName());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        doctype.getName("html");
        assertEquals("html", doctype.getName());
        assertEquals("html", doctype.normalName());

        doctype.getPublicIdentifier().append("PUBLIC");
        doctype.getPublicIdentifier().append(" -//W3C//DTD XHTML 1.0 Strict//EN");
        assertEquals("PUBLIC -//W3C//DTD XHTML 1.0 Strict//EN", doctype.getPublicIdentifier().toString());

        doctype.getSystemIdentifier().append("/path/to/xhtml.dtd");
        assertEquals("/path/to/xhtml.dtd", doctype.getSystemIdentifier().toString());

        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token startTag = new Token.StartTag();
        assertEquals("StartTag", startTag.tokenType());
        assertTrue(startTag instanceof Token.StartTag);

        startTag.reset();
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.attributes);
        assertFalse(startTag.isSelfClosing());

        startTag.name("html").attributes(new Attributes().put("xmlns:foo", "bar"));
        assertEquals("html", startTag.tagName);
        assertEquals("html", startTag.normalName);
        assertEquals("xmlns:foo", startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertNull(startTag.getAttributes());
        assertFalse(startTag.isSelfClosing());

        startTag.appendAttributeName("class").appendAttributeValue("foo");
        assertFalse(startTag.hasPendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertEquals("class=foo", startTag.toString());

        startTag.newAttribute();
        assertEquals("class", startTag.pendingAttributeName);
        assertEquals("foo", startTag.attributes.getValue("class"));
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
    }

    @Test
    public void testEndTagToken() {
        Token endTag = new Token.EndTag();
        assertEquals("EndTag", endTag.tokenType());
        assertTrue(endTag instanceof Token.EndTag);

        endTag.reset();
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.getAttributes());
        assertFalse(endTag.isSelfClosing());

        endTag.name("html");
        assertEquals("html", endTag.tagName);
        assertEquals("html", endTag.normalName);
        assertNull(endTag.getAttributes());
        assertFalse(endTag.isSelfClosing());

        endTag.finaliseTag();
        assertNull(endTag.pendingAttributeName);
        assertNull(endTag.pendingAttributeValue);
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertNull(endTag.getAttributes());
    }

    @Test(expected = AssertionError.class)
    public void testCommentToken() {
        Token comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());
        assertTrue(comment instanceof Token.Comment);

        comment.reset();
        assertNull(comment.getData());
        assertFalse(comment.isForceQuirks());

        comment.getData().append("--hello world");
        assertEquals("--hello world", comment.getData().toString());
        assertFalse(comment.isForceQuirks());

        comment.reset();
        comment.getData().append("--hello world--");
        assertEquals("--hello world--", comment.getData().toString());
        assertTrue(comment.isForceQuirks());
    }

    @Test
    public void testCharacterToken() {
        Token character = new Token.Character();
        assertEquals("Character", character.tokenType());
        assertTrue(character instanceof Token.Character);

        character.reset();
        assertNull(character.getData());

        character.data("Hello, World!");
        assertEquals("Hello, World!", character.getData());

        character.reset();
        character.data("\u00A9");
        assertEquals("©", character.getData());
    }

    @Test
    public void testEOFToken() {
        Token eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
        assertTrue(eof instanceof Token.EOF);

        eof.reset();
    }
}