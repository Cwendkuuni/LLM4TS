package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;

import java.util.Arrays;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;

        Doctype resetDoctype = doctype.reset();
        assertFalse(doctype.isForceQuirks());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertEquals("", doctype.getName());
        assertEquals(TokenType.Doctype, resetDoctype.tokenType());
    }

    @Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;

        assertEquals("html", doctype.getName());
        assertEquals("pubSysKey", doctype.getPubSysKey());
        assertEquals("publicIdentifier", doctype.getPublicIdentifier());
        assertEquals("systemIdentifier", doctype.getSystemIdentifier());
        assertEquals(true, doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.tagName = "div";
        tag.attributes = new Attributes();
        tag.attributes.put("id", "id1");

        Tag resetTag = tag.reset();
        assertNull(tag.attributes);
        assertEquals(false, tag.isSelfClosing());
        assertEquals("", tag.tagName);
        assertEquals(TokenType.StartTag, resetTag.tokenType());
    }

    @Test
    public void testTagGetters() {
        StartTag tag = new StartTag();
        tag.tagName = "div";
        tag.normalName = "div";
        tag.attributes = new Attributes();
        tag.attributes.put("id", "id1");
        tag.attributes.put("class", "class1");

        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());
        assertEquals("id1", tag.attributes().getValue("id"));
        assertEquals("class1", tag.attributes().getValue("class"));
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTag_newAttribute() {
        StartTag tag = new StartTag();
        tag.tagName = "div";
        tag.normalName = "div";

        tag.newAttribute();
        tag.appendAttributeName("id");
        tag.appendAttributeValue("id1");
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("class1");
        tag.newAttribute();

        assertEquals("id1", tag.attributes().getValue("id"));
        assertEquals("class1", tag.attributes().getValue("class"));
        assertEquals("id", tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertEquals(null, tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTag_finaliseTag() {
        StartTag tag = new StartTag();
        tag.tagName = "div";
        tag.normalName = "div";

        tag.newAttribute();
        tag.appendAttributeName("id");
        tag.appendAttributeValue("id1");
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("class1");
        tag.finaliseTag();

        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());
        assertEquals("id1", tag.attributes().getValue("id"));
        assertEquals("class1", tag.attributes().getValue("class"));
        assertEquals(null, tag.pendingAttributeName);
        assertEquals(null, tag.pendingAttributeValue);
        assertEquals(null, tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data("data");

        Character resetCharacter = character.reset();
        assertEquals(null, character.getData());
        assertEquals(TokenType.Character, resetCharacter.tokenType());
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.data.append("data");

        Comment resetComment = comment.reset();
        assertEquals(null, comment.getData());
        assertEquals(TokenType.Comment, resetComment.tokenType());
    }

    @Test
    public void testTokenTypeIdentification() {
        Doctype doctype = new Doctype();
        StartTag startTag = new StartTag();
        EndTag endTag = new EndTag();
        Comment comment = new Comment();
        Character character = new Character();
        CData cData = new CData("data");
        EOF eof = new EOF();

        assertTrue(doctype.isDoctype());
        assertTrue(startTag.isStartTag());
        assertTrue(endTag.isEndTag());
        assertTrue(comment.isComment());
        assertTrue(character.isCharacter());
        assertTrue(cData.isCData());
        assertTrue(eof.isEOF());
    }
}