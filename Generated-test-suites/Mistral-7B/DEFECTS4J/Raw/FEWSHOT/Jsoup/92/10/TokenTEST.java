package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        Token resetDoctype = doctype.reset();

        assertEquals(TokenType.Doctype, resetDoctype.type);
        assertNull(resetDoctype.getName());
        assertNull(resetDoctype.getPubSysKey());
        assertNull(resetDoctype.getPublicIdentifier());
        assertNull(resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testDoctypeGetters() {
        Token doctype = new Token.Doctype();
        doctype.getName("html");
        doctype.getPubSysKey("pubsyskey");
        doctype.getPublicIdentifier("publicid");
        doctype.setSystemIdentifier("systemid");
        doctype.isForceQuirks(true);

        assertEquals("html", doctype.getName());
        assertEquals("pubsyskey", doctype.getPubSysKey());
        assertEquals("publicid", doctype.getPublicIdentifier());
        assertEquals("systemid", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.Tag();
        Token resetTag = tag.reset();

        assertNull(resetTag.tagName);
        assertNull(resetTag.normalName);
        assertNull(resetTag.pendingAttributeName);
        assertNull(resetTag.pendingAttributeValue);
        assertNull(resetTag.pendingAttributeValueS);
        assertFalse(resetTag.hasEmptyAttributeValue);
        assertFalse(resetTag.hasPendingAttributeValue);
        assertFalse(resetTag.selfClosing);
        assertNull(resetTag.attributes);
    }

    @Test
    public void testTagGetterSetter() {
        Token tag = new Token.Tag();
        tag.name("div");
        tag.normalName("div");
        tag.appendTagName("class");
        tag.appendAttributeName("id");
        tag.appendAttributeValue("test");
        tag.appendAttributeValue("test2");
        tag.setEmptyAttributeValue();
        tag.hasPendingAttributeValue(true);
        tag.selfClosing(true);
        tag.attributes(new Attributes());

        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());
        assertEquals("class", tag.pendingAttributeName);
        assertEquals("test test2", tag.pendingAttributeValue().toString());
        assertEquals("test", tag.getAttributes().get("class"));
        assertTrue(tag.hasEmptyAttributeValue);
        assertTrue(tag.hasPendingAttributeValue);
        assertTrue(tag.isSelfClosing());
        assertNotNull(tag.getAttributes());
    }

    @Test
    public void testTagToString() {
        Token tag = new Token.StartTag();
        tag.name("p");
        tag.attributes(new Attributes().put("class", "test").put("id", "test"));

        assertEquals("<p class=test id=test>", tag.toString());
    }

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        Token resetComment = comment.reset();

        assertEquals(TokenType.Comment, resetComment.type);
        assertNull(resetComment.getData());
        assertFalse(resetComment.bogus);
    }

    @Test
    public void testCommentGetterSetter() {
        Token comment = new Token.Comment();
        comment.getData("This is a comment");
        comment.bogus(true);

        assertEquals("This is a comment", comment.getData());
        assertTrue(comment.bogus);
    }

    @Test
    public void testCommentToString() {
        Token comment = new Token.Comment();
        comment.getData("<!-- This is a comment -->");

        assertEquals("<!--This is a comment -->", comment.toString());
    }

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        Token resetCharacter = character.reset();

        assertEquals(TokenType.Character, resetCharacter.type);
        assertNull(resetCharacter.getData());
    }

    @Test
    public void testCharacterGetterSetter() {
        Token character = new Token.Character();
        character.data("A");

        assertEquals("A", character.getData());
    }

    @Test
    public void testCharacterToString() {
        Token character = new Token.Character();
        character.data("A");

        assertEquals("A", character.toString());
    }

    @Test
    public void testCDataReset() {
        Token cdata = new Token.CData("<!--CDATA[-->\n<script>\nalert('Hello, world!');\n<!--]/CDATA-->");
        Token resetCdata = cdata.reset();

        assertEquals(TokenType.Character, resetCdata.type);
        assertNull(resetCdata.getData());
    }

    @Test
    public void testCDataGetterSetter() {
        Token cdata = new Token.CData("<![CDATA[\n<script>\nalert('Hello, world!');\n]]>");

        assertEquals("<![CDATA[\n<script>\nalert('Hello, world!');\n]]>", cdata.toString());
    }

    @Test
    public void testEOFReset() {
        Token eof = new Token.EOF();
        Token resetEof = eof.reset();

        assertEquals(TokenType.EOF, resetEof.type);
    }
}