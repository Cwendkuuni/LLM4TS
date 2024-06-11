package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Tag;
import org.jsoup.parser.Token.TokenType;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.name("html");
        String name = doctype.getName();
        assertEquals("html", name);

        doctype.reset();
        assertNull(doctype.getName());
    }

    @Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.name("html");

        assertEquals(TokenType.Doctype, doctype.type);

        assertEquals("html", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        StartTag startTag = new StartTag();
        startTag.name("div");

        assertEquals("div", startTag.name());
        assertEquals("div", startTag.normalName());
        assertFalse(startTag.isSelfClosing());
        assertNull(startTag.getAttributes());

        startTag.reset();

        assertNull(startTag.name());
        assertNull(startTag.normalName());
        assertFalse(startTag.isSelfClosing());
        assertNull(startTag.getAttributes());
    }

    @Test
    public void testStartTagNewAttribute() {
        StartTag startTag = new StartTag();
        startTag.name("div");

        startTag.appendAttributeName("class");
        startTag.appendAttributeValue("red");

        assertEquals(1, startTag.getAttributes().size());
        Attribute attribute = startTag.getAttributes().get("class");
        assertEquals("red", attribute.getValue());

        startTag.reset();

        assertEquals(0, startTag.getAttributes().size());
    }

    @Test
    public void testStartTagFinaliseTag() {
        StartTag startTag = new StartTag();
        startTag.name("div");

        startTag.appendAttributeName("class");
        startTag.appendAttributeValue("red");

        startTag.finaliseTag();

        assertEquals(1, startTag.getAttributes().size());
        Attribute attribute = startTag.getAttributes().get("class");
        assertEquals("red", attribute.getValue());

        assertSame(startTag, attribute.getParentNode());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();

        tag.reset();

        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertNull(tag.hasPendingAttributeValue);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @Test
    public void testTokenTypes() {
        Token token = new Doctype();
        assertEquals(TokenType.Doctype, token.type);
        token = new StartTag();
        assertEquals(TokenType.StartTag, token.type);
        token = new EndTag();
        assertEquals(TokenType.EndTag, token.type);
        token = new Comment();
        assertEquals(TokenType.Comment, token.type);
        token = new Character();
        assertEquals(TokenType.Character, token.type);
        token = new EOF();
        assertEquals(TokenType.EOF, token.type);
    }
}