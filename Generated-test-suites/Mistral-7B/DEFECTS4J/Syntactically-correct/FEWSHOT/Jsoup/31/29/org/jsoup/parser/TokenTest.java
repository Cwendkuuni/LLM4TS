package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        Token doctypeWithName = new Token.Doctype().name("HTML").getSystemIdentifier("about:blank");
        Token doctypeWithNameForceQuirks = new Token.Doctype().name("HTML").getSystemIdentifier("about:blank").isForceQuirks(true);

        assertEquals(TokenType.Doctype, doctype.tokenType());
        assertEquals(TokenType.Doctype, doctypeWithName.tokenType());
        assertEquals(TokenType.Doctype, doctypeWithNameForceQuirks.tokenType());

        assertNull(doctype.getName());
        assertEquals("HTML", doctypeWithName.getName());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
        assertTrue(doctypeWithNameForceQuirks.isForceQuirks());
    }

    @Test
    public void testTag() {
        Token tag = new Token.StartTag();
        Token tagWithName = new Token.StartTag().name("div");
        Token tagWithNameAttributes = new Token.StartTag().name("div").attributes(new Attributes().put("class", "someClass"));
        Token tagWithNameSelfClosing = new Token.StartTag().name("img").selfClosing(true);

        assertEquals(TokenType.StartTag, tag.tokenType());
        assertEquals(TokenType.StartTag, tagWithName.tokenType());
        assertEquals(TokenType.StartTag, tagWithNameAttributes.tokenType());
        assertEquals(TokenType.StartTag, tagWithNameSelfClosing.tokenType());

        assertNull(tag.tagName());
        assertEquals("div", tagWithName.tagName());
        assertNull(tagWithNameAttributes.getAttributes());
        assertEquals("class", tagWithNameAttributes.pendingAttributeName());
        assertEquals("someClass", tagWithNameAttributes.getAttributes().getClass().getName());
        assertNull(tagWithNameAttributes.pendingAttributeValue());
        assertEquals("div", tagWithNameSelfClosing.tagName());
        assertTrue(tagWithNameSelfClosing.isSelfClosing());

        Element divElement = new Element("div");
        Attributes divAttributes = new Attributes();
        divAttributes.put("class", "someClass");
        Element divElementWithAttributes = new Element("div").attributes(divAttributes);

        Token tagWithElementAttributes = new Token.StartTag().name("div").attributes(divElement.attributes());

        assertEquals(tagWithNameAttributes.tagName(), tagWithElementAttributes.tagName());
        assertEquals(tagWithNameAttributes.getAttributes(), tagWithElementAttributes.getAttributes());
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        Token commentWithData = new Token.Comment().getData("This is a comment");

        assertEquals(TokenType.Comment, comment.tokenType());
        assertEquals(TokenType.Comment, commentWithData.tokenType());

        assertNull(comment.getData());
        assertEquals("This is a comment", commentWithData.getData());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("A");

        assertEquals(TokenType.Character, character.tokenType());
        assertEquals('A', character.getData().charAt(0));
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();

        assertEquals(TokenType.EOF, eof.tokenType());
    }
}