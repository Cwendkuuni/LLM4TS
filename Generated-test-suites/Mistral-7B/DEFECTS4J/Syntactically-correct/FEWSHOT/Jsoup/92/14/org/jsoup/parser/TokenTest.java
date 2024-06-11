package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName("html");
        doctype.setForceQuirks(true);
        assertEquals("html", doctype.getName());
        assertTrue(doctype.isForceQuirks());

        Doctype resetDoctype = doctype.reset();
        assertNull(resetDoctype.getName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
        assertEquals(TokenType.Doctype, doctype.tokenType());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.name("div");
        tag.attributes = new Attributes();
        tag.attributes.put("id", "myId");
        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());
        assertEquals("myId", tag.attributes().getValue("id"));
        assertEquals(1, tag.attributes().size());

        Tag resetTag = tag.reset();
        assertNull(resetTag.tagName);
        assertNull(resetTag.normalName);
        assertNull(resetTag.pendingAttributeName);
        assertNull(resetTag.pendingAttributeValue);
        assertNull(resetTag.pendingAttributeValueS);
        assertFalse(resetTag.hasEmptyAttributeValue);
        assertFalse(resetTag.hasPendingAttributeValue);
        assertFalse(resetTag.selfClosing);
        assertNull(resetTag.attributes);
        assertEquals(TokenType.StartTag, resetTag.tokenType());
    }

    @Test
    public void testTagNewAttribute() {
        Tag tag = new StartTag();
        tag.name("div");
        tag.newAttribute().name("class").value("foo");
        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());
        assertEquals("class", tag.pendingAttributeName);
        assertEquals("foo", tag.pendingAttributeValue.toString());
        assertEquals(1, tag.attributes().size());
        assertEquals("foo", tag.attributes().getValue("class"));

        tag.newAttribute().name("id").value("bar");
        assertEquals(2, tag.attributes().size());
        assertEquals("foo", tag.attributes().getValue("class"));
        assertEquals("bar", tag.attributes().getValue("id"));

        tag.newAttribute();
        assertEquals(2, tag.attributes().size());
    }

    @Test
    public void testTagFinaliseTag() {
        Tag tag = new StartTag();
        tag.name("div");
        tag.newAttribute().name("class").value("foo");
        tag.finaliseTag();
        assertEquals(TokenType.StartTag, tag.tokenType());
        assertFalse(tag.hasPendingAttributeValue);
    }

    @Test
    public void testTagToString() {
        StartTag tag = new StartTag().name("div").attributes(new Attributes().put("class", "foo").put("id", "bar"));
        assertEquals("<div class='foo' id='bar'>\n", tag.toString());
    }

    @Test
    public void testEndTagToString() {
        EndTag tag = new EndTag().name("div");
        assertEquals("</div>\n", tag.toString());
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.data("This is a comment");
        assertEquals("This is a comment", comment.getData());

        Comment resetComment = comment.reset();
        assertNull(resetComment.getData());
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character().data("A");
        assertEquals("A", character.getData());

        Character resetCharacter = character.reset();
        assertNull(resetCharacter.getData());
    }

    @Test
    public void testCDataReset() {
        CData cData = new CData("<!-- This is a CData -->");
        assertEquals("<!-- This is a CData -->", cData.toString());

        CData resetCData = cData.reset();
        assertNull(resetCData);
    }
}