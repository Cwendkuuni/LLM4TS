package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.name("html");
        doctype.pubSysKey("_pub-sys-key_");
        doctype.publicIdentifier("PUBLIC []");
        doctype.systemIdentifier("system-identifier.html");
        doctype.forceQuirks(true);

        Doctype resetDoctype = doctype.reset();

        assertEquals(Doctype.class, resetDoctype.getClass());
        assertFalse(doctype.isForceQuirks());
        assertNull(doctype.pubSysKey);
        assertNull(doctype.publicIdentifier);
        assertNull(doctype.systemIdentifier);
        assertEquals("", doctype.getName());
    }

    @Test
    public void testTagReset() {
        StartTag startTag = new StartTag();
        startTag.name("div");

        Tag resetTag = startTag.reset();

        assertEquals(StartTag.class, resetTag.getClass());
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }

    @Test
    public void testTagNewAttribute() {
        StartTag startTag = new StartTag();
        startTag.name("div");

        startTag.newAttribute().name("class").value("test");

        Attribute att = startTag.attributes.get("class");
        assertEquals("test", att.getValue());

        startTag.newAttribute().name("id").value("testId");
        Attribute idAtt = startTag.attributes.get("id");
        assertEquals("testId", idAtt.getValue());
    }

    @Test
    public void testTagFinaliseTag() {
        StartTag startTag = new StartTag();
        startTag.name("div");

        startTag.newAttribute().name("class").value("test");
        startTag.finaliseTag();

        assertFalse(startTag.hasPendingAttributeValue);
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
    }

    @Test
    public void testTokenIsDoctype() {
        Doctype doctype = new Doctype();
        assertTrue(doctype.isDoctype());
    }

    @Test
    public void testTokenIsStartTag() {
        StartTag startTag = new StartTag();
        assertFalse(startTag.isStartTag());
        startTag.name("div");
        assertTrue(startTag.isStartTag());
    }

    @Test
    public void testTokenIsEndTag() {
        EndTag endTag = new EndTag();
        assertFalse(endTag.isEndTag());
        endTag.name("div");
        assertTrue(endTag.isEndTag());
    }

    @Test
    public void testTokenIsComment() {
        Comment comment = new Comment();
        assertFalse(comment.isComment());
        comment.data("This is a comment.");
        assertTrue(comment.isComment());
    }

    @Test
    public void testTokenIsCharacter() {
        Character character = new Character();
        assertFalse(character.isCharacter());
        character.data("A character.");
        assertTrue(character.isCharacter());
    }

    @Test
    public void testTokenIsEOF() {
        EOF eof = new EOF();
        assertFalse(eof.isEOF());
        assertTrue(eof.isEOF());
    }

}