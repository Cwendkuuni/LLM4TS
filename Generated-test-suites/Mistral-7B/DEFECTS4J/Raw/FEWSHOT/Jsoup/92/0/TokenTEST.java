package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.jsoup.internal.Normalizer.lowerCase;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName = "html";
        doctype.asDoctype().pubSysKey = "pubSysKey";
        doctype.asDoctype().publicIdentifier.append("publicId");
        doctype.asDoctype().systemIdentifier.append("systemId");
        doctype.asDoctype().forceQuirks = true;

        Token resetDoctype = doctype.reset();
        assertEquals(doctype.getClass(), resetDoctype.getClass());
        assertNull(resetDoctype.asDoctype().getName);
        assertNull(resetDoctype.asDoctype().pubSysKey);
        Token.reset(resetDoctype.asDoctype().publicIdentifier);
        assertNull(resetDoctype.asDoctype().publicIdentifier);
        assertNull(resetDoctype.asDoctype().systemIdentifier);
        assertFalse(resetDoctype.asDoctype().forceQuirks);
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.StartTag();
        tag.tagName = "div";
        tag.normalName = lowerCase("div");
        tag.pendingAttributeName = "class";
        tag.hasPendingAttributeValue = true;
        tag.pendingAttributeValue.append("red");
        tag.attributes = new Attributes();
        tag.attributes.put("id", "elementId");

        Token resetTag = tag.reset();
        assertEquals(tag.getClass(), resetTag.getClass());
        assertNull(resetTag.tagName);
        assertNull(resetTag.normalName);
        assertNull(resetTag.pendingAttributeName);
        Token.reset(resetTag.pendingAttributeValue);
        assertFalse(resetTag.hasPendingAttributeValue);
        assertNull(resetTag.attributes);
    }

    @Test
    public void testTagNewAttribute() {
        Token tag = new Token.StartTag();
        tag.tagName = "div";
        tag.newAttribute();
        tag.pendingAttributeName = "class";
        tag.pendingAttributeValue.append("red");
        tag.newAttribute();
        tag.pendingAttributeName = "id";
        tag.pendingAttributeValue.append("elementId");
        tag.newAttribute();
        tag.newAttribute();

        Attributes attributes = tag.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("class", attributes.keyAt(0));
        assertEquals("red", attributes.valueAt(0));
        assertEquals("id", attributes.keyAt(1));
        assertEquals("elementId", attributes.valueAt(1));
    }

    @Test
    public void testTagFinaliseTag() {
        Token tag = new Token.StartTag();
        tag.tagName = "div";
        tag.newAttribute();
        tag.pendingAttributeName = "class";
        tag.pendingAttributeValue.append("red");
        tag.newAttribute();
        tag.pendingAttributeName = null;
        tag.finaliseTag();

        Attributes attributes = tag.getAttributes();
        assertEquals(1, attributes.size());
        assertEquals("class", attributes.keyAt(0));
        assertEquals("red", attributes.valueAt(0));
    }

    @Test
    public void testTagName() {
        Token tag = new Token.StartTag();
        tag.tagName = "div";
        tag.name("p");

        assertEquals("div", tag.tagName);
        assertEquals("p", tag.normalName);
    }

    @Test
    public void testTagIsSelfClosing() {
        Token tag1 = new Token.StartTag();
        tag1.tagName = "img";
        tag1.selfClosing = false;

        Token tag2 = new Token.StartTag();
        tag2.tagName = "img";
        tag2.selfClosing = true;

        assertEquals(false, tag1.isSelfClosing());
        assertEquals(true, tag2.isSelfClosing());
    }

    @Test
    public void testTokenType() {
        Token tag = new Token.StartTag();
        assertEquals(TokenType.StartTag, tag.tokenType());

        Token comment = new Token.Comment();
        assertEquals(TokenType.Comment, comment.tokenType());

        Token character = new Token.Character();
        assertEquals(TokenType.Character, character.tokenType());

        Token doctype = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctype.tokenType());

        Token eof = new Token.EOF();
        assertEquals(TokenType.EOF, eof.tokenType());
    }
}