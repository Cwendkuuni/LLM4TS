package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        doctype.reset();
        assertNull(doctype.getName());
    }

    @Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        doctype.setName("html5");
        assertTrue(doctype.isDoctype());
        assertEquals("html5", doctype.getName());
        assertEquals("html5", doctype.normalName());
        assertFalse(doctype.isStartTag());
        assertFalse(doctype.isEndTag());
        assertFalse(doctype.isComment());
        assertFalse(doctype.isCharacter());
        assertFalse(doctype.isEOF());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.name().equals("div");
        tag.reset();
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
    public void testTagGetters() {
        Tag tag = new StartTag();
        tag.name().equals("div");
        tag.name("p");
        tag.selfClosing = true;
        tag.newAttribute();
        tag.newAttribute();
        tag.newAttribute().setValue("class", "myclass");
        tag.newAttribute().setValue("id", "myid");
        tag.finaliseTag();
        assertFalse(tag.isDoctype());
        assertFalse(tag.isEndTag());
        assertFalse(tag.isComment());
        assertFalse(tag.isCharacter());
        assertFalse(tag.isEOF());
        assertTrue(tag.isStartTag());
        assertEquals("p", tag.name());
        assertEquals("p", tag.normalName());
        assertTrue(tag.isSelfClosing());
        assertNotNull(tag.attributes);
        assertEquals("class", tag.attributes.keySet().iterator().next().toString());
        assertEquals("myclass", tag.attributes.get("class").getValue());
        assertEquals("id", tag.attributes.keySet().iterator().next().toString());
        assertEquals("myid", tag.attributes.get("id").getValue());
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.getData().equals("");
        comment.reset();
        assertNull(comment.getData());
    }

    @Test
    public void testCommentGetters() {
        Comment comment = new Comment();
        comment.getData().equals("<!-- This is a test comment -->");
        assertTrue(comment.isComment());
        assertEquals("<!-- This is a test comment -->", comment.toString());
        assertNull(comment.tagName);
        assertNull(comment.normalName);
        assertNull(comment.pendingAttributeName);
        assertNull(comment.pendingAttributeValue);
        assertNull(comment.pendingAttributeValueS);
        assertFalse(comment.hasEmptyAttributeValue);
        assertFalse(comment.hasPendingAttributeValue);
        assertFalse(comment.selfClosing);
        assertNull(comment.attributes);
        assertEquals("This is a test comment", comment.getData());
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.getData().equals("");
        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testCharacterGetters() {
        Character character = new Character();
        character.getData().equals("!");
        assertTrue(character.isCharacter());
        assertEquals("!", character.getData());
        assertNull(character.tagName);
        assertNull(character.normalName);
        assertNull(character.pendingAttributeName);
        assertNull(character.pendingAttributeValue);
        assertNull(character.pendingAttributeValueS);
        assertFalse(character.hasEmptyAttributeValue);
        assertFalse(character.hasPendingAttributeValue);
        assertFalse(character.selfClosing);
        assertNull(character.attributes);
    }

    @Test
    public void testEOFReset() {
        EOF eof = new EOF();
        eof.reset();
        assertTrue(eof.isEOF());
    }
}