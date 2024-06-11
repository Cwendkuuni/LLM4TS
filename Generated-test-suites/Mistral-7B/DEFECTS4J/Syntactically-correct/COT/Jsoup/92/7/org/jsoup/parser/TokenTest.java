package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.Token.*;
import org.jsoup.parser.TokenType;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Doctype();
        assertEquals(TokenType.Doctype, token.tokenType());

        token = new StartTag();
        assertEquals(TokenType.StartTag, token.tokenType());

        token = new EndTag();
        assertEquals(TokenType.EndTag, token.tokenType());

        token = new Comment();
        assertEquals(TokenType.Comment, token.tokenType());

        token = new Character();
        assertEquals(TokenType.Character, token.tokenType());

        token = new CData();
        assertTrue(token instanceof Character);

        token = new EOF();
        assertEquals(TokenType.EOF, token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        assertEquals(TokenType.Doctype, doctype.tokenType());

        doctype.getName().append("html");
        assertEquals("html", doctype.getName().toString());

        doctype.reset();
        assertNull(doctype.getName());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.getName().append("html");
        doctype.setForceQuirks(true);

        assertEquals("html", doctype.getName().toString());
        assertEquals(false, doctype.isForceQuirks());

        doctype.setForceQuirks(true);
        assertEquals(true, doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);

        tag = new EndTag();
        assertEquals(TokenType.EndTag, tag.tokenType());

        tag.reset();
        assertEquals(TokenType.EndTag, tag.tokenType());
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @Test
    public void testTagAttributeHandling() {
        StartTag tag = new StartTag();

        tag.name("html").appendTagName("body").appendAttributeName("id").appendAttributeValue("test");
        assertEquals("html", tag.tagName);
        assertEquals("body", tag.normalName);
        assertEquals("body", tag.name());
        assertEquals("id", tag.pendingAttributeName);
        assertEquals("test", tag.pendingAttributeValue.toString());
        assertEquals("test", tag.getAttributes().get("id"));
        assertTrue(tag.hasPendingAttributeValue);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.selfClosing);

        tag.newAttribute();
        assertEquals(1, tag.getAttributes().size());

        tag.appendAttributeValue("class").appendAttributeValue("test2");
        tag.newAttribute();
        assertEquals(2, tag.getAttributes().size());

        tag.finaliseTag();
        assertEquals(2, tag.getAttributes().size());

        tag.reset();
        assertEquals(0, tag.getAttributes().size());
    }

    @Test
    public void testTagAttributeEmptyValue() {
        StartTag tag = new StartTag();

        tag.name("html").appendTagName("body").appendAttributeName("class");
        tag.setEmptyAttributeValue();

        assertEquals("body", tag.normalName);
        assertEquals("class", tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertEquals("class", tag.getAttributes().keySet().iterator().next().toString());
        assertEquals("", tag.getAttributes().get("class"));
        assertTrue(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);

        tag.newAttribute();
        assertEquals(1, tag.getAttributes().size());

        tag.finaliseTag();
        assertEquals(1, tag.getAttributes().size());
    }

    @Test
    public void testTagAttributeMultipleValues() {
        StartTag tag = new StartTag();

        tag.name("html").appendTagName("body").appendAttributeName("class");
        tag.appendAttributeValue("test1");
        tag.appendAttributeValue("test2");

        assertEquals("body", tag.normalName);
        assertEquals("class", tag.pendingAttributeName);
        assertEquals("test1 test2", tag.pendingAttributeValueS);
        assertEquals("test1 test2", tag.getAttributes().get("class"));
        assertEquals(1, tag.getAttributes().size());

        tag.newAttribute();
        assertEquals(1, tag.getAttributes().size());
    }

    @Test
    public void testTagSelfClosing() {
        StartTag tag = new StartTag();

        tag.name("img").appendTagName("").appendAttributeName("src").appendAttributeValue("");
        tag.isSelfClosing = true;

        assertEquals("img", tag.tagName);
        assertEquals("img", tag.normalName);
        assertEquals("img", tag.name());
        assertEquals("src", tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertEquals("src", tag.getAttributes().keySet().iterator().next().toString());
        assertEquals("", tag.getAttributes().get("src"));
        assertTrue(tag.hasPendingAttributeValue);
        assertTrue(tag.isSelfClosing);
        assertNull(tag.attributes);

        tag.newAttribute();
        assertNull(tag.getAttributes());

        tag.finaliseTag();
        assertNull(tag.getAttributes());
    }

    @Test(expected = AssertionError.class)
    public void testTagIllegalAttributeName() {
        StartTag tag = new StartTag();

        tag.name("html").appendTagName("body").appendAttributeName("\u0000"); // null byte
        tag.appendAttributeValue("test");

        tag.newAttribute();
    }

    @Test(expected = AssertionError.class)
    public void testTagIllegalAttributeValue() {
        StartTag tag = new StartTag();

        tag.name("html").appendTagName("body").appendAttributeName("class");
        tag.appendAttributeValue("\u0000"); // null byte

        tag.newAttribute();
    }
}