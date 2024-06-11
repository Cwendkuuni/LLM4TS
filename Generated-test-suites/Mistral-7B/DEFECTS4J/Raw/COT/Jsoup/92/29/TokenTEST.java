package org.jsoup.parser.test;

import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

import java.util.Arrays;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName().equals("doctype"); // check initial state

        doctype.reset();
        assertTrue(doctype.getType() == TokenType.Doctype);
        assertTrue(doctype.getName().isEmpty());
        assertTrue(doctype.getPubSysKey() == null);
        assertTrue(doctype.getPublicIdentifier().isEmpty());
        assertTrue(doctype.getSystemIdentifier().isEmpty());
        assertFalse(doctype.isForceQuirks());
    }

    @org.junit.Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName().append("html");
        doctype.getPubSysKey() = "pubsys";
        doctype.getPublicIdentifier().append("iso-8859-1");
        doctype.setForceQuirks(true);

        assertEquals("doctype", doctype.tokenType());
        assertEquals("html", doctype.getName());
        assertEquals("pubsys", doctype.getPubSysKey());
        assertEquals("iso-8859-1", doctype.getPublicIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testDoctypeResetAfterGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName().append("html");
        doctype.getPubSysKey() = "pubsys";
        doctype.getPublicIdentifier().append("iso-8859-1");
        doctype.setForceQuirks(true);

        assertEquals("doctype", doctype.tokenType());
        assertEquals("html", doctype.getName());
        assertEquals("pubsys", doctype.getPubSysKey());
        assertEquals("iso-8859-1", doctype.getPublicIdentifier());
        assertTrue(doctype.isForceQuirks());

        doctype.reset();

        fail("Doctype reset should have cleared all fields");
    }

    @org.junit.Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "tag";
        tag.newAttribute();
        tag.appendAttributeName("attr").appendAttributeValue("value");

        tag.reset();
        assertTrue(tag.getType() == TokenType.StartTag);
        assertNull(tag.tagName);
        assertNull(tag.getAttributes());
        assertFalse(tag.isSelfClosing());
    }

    @org.junit.Test
    public void testTagNameAndNormalName() {
        Token.StartTag tag = new Token.StartTag();
        tag.name("tagName");

        assertEquals("tagName", tag.name());
        assertEquals("tagname", tag.normalName());
    }

    @org.junit.Test
    public void testTagAppenders() {
        Token.StartTag tag = new Token.StartTag();
        tag.appendTagName("tag").appendTagName("name");

        assertEquals("tagname", tag.normalName());
        assertEquals("tagtagname", tag.name());
    }

    @org.junit.Test
    public void testTagAttributes() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute();
        tag.appendAttributeName("attr1").appendAttributeValue("value1");
        tag.newAttribute();
        tag.appendAttributeName("attr2").appendAttributeValue("value2");

        Attributes attributes = tag.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("attr1", attributes.getKey("attr1"));
        assertEquals("value1", attributes.getValue("attr1"));
        assertEquals("attr2", attributes.getKey("attr2"));
        assertEquals("value2", attributes.getValue("attr2"));
    }

    @org.junit.Test
    public void testTagSelfClosing() {
        Token.StartTag tag = new Token.StartTag();
        tag.isSelfClosing(true);
        assertTrue(tag.isSelfClosing());

        tag.reset();
        assertFalse(tag.isSelfClosing());
    }

    @org.junit.Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.getData().append("comment");

        comment.reset();
        assertTrue(comment.getType() == TokenType.Comment);
        assertTrue(comment.getData().isEmpty());
        assertFalse(comment.isBogus());
    }

    @org.junit.Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.getData().append("a");

        character.reset();
        assertTrue(character.getType() == TokenType.Character);
        assertNull(character.getData());
    }

    @org.junit.Test
    public void testCDataReset() {
        Token.CData cdata = new Token.CData("[cdatadata]");

        cdata.reset();
        assertTrue(cdata.getType() == TokenType.CData);
        assertNull(cdata.getData());
    }

    @org.junit.Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();
        assertTrue(eof.getType() == TokenType.EOF);
    }

}