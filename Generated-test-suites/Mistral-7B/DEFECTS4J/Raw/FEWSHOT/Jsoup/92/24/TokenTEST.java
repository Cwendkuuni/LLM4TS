package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token token = new Doctype();
        Doctype doctype = (Doctype) token.reset();

        assertNull(doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testDoctypeValues() {
        Doctype doctype = new Doctype().name("HTML").pubSysKey("pUB SYS KEY").publicIdentifier("PUBLIC ID").systemIdentifier("SYS ID");

        assertEquals("HTML", doctype.getName());
        assertEquals("pUB SYS KEY", doctype.getPubSysKey());
        assertEquals("PUBLIC ID", doctype.getPublicIdentifier());
        assertEquals("SYS ID", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token token = new StartTag();
        StartTag tag = (StartTag) token.reset();

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
    public void testTagValues() {
        StartTag tag = new StartTag().name("div").normalName("div").selfClosing(true).attributes(new Attributes().put("class", "class1"));

        assertEquals("div", tag.tagName);
        assertEquals("div", tag.normalName);
        assertTrue(tag.isSelfClosing());
        assertEquals("class=class1", tag.toString());
    }

    @Test
    public void testTagNewAttribute() {
        StartTag tag = new StartTag().name("input").newAttribute().name("type").value("text");

        assertEquals("input", tag.tagName);
        assertEquals("input", tag.normalName);
        assertFalse(tag.isSelfClosing());
        assertEquals("type=\"text\"", tag.toString());
    }

    @Test
    public void testTagFinaliseTag() {
        StartTag tag = new StartTag().name("p").newAttribute().name("class").value("class1").finaliseTag();

        assertEquals("p", tag.tagName);
        assertEquals("p", tag.normalName);
        assertFalse(tag.isSelfClosing());
        assertEquals("<p class=\"class1\">", tag.toString());
    }

    @Test
    public void testTagValueAppender() {
        StartTag tag = new StartTag();

        tag.appendTagName("div");
        tag.appendTagName("id");
        tag.appendAttributeName("class");
        tag.appendAttributeValue("class1");

        assertEquals("divid", tag.tagName);
        assertEquals("div", tag.normalName);
        assertFalse(tag.isSelfClosing());
        assertEquals("<div class=\"class1\">", tag.toString());
    }

    @Test
    public void testCommentReset() {
        Token token = new Comment();
        Comment comment = (Comment) token.reset();

        assertNull(comment.getData());
        assertFalse(comment.isBogus());
    }

    @Test
    public void testCommentValues() {
        Comment comment = new Comment().data("This is a comment");

        assertEquals("This is a comment", comment.getData());
        assertFalse(comment.isBogus());
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testCharacterReset() {
        Token token = new Character();
        Character character = (Character) token.reset();

        assertNull(character.getData());
    }

    @Test
    public void testCharacterValues() {
        Character character = new Character().data("Hello, World!");

        assertEquals("Hello, World!", character.getData());
        assertEquals("Hello, World!", character.toString());
    }

    @Test
    public void testCDataReset() {
        Token token = new CData();
        CData cdata = (CData) token.reset();

        assertNull(cdata.getData());
    }

    @Test
    public void testCDataValues() {
        CData cdata = new CData().data("<![CDATA[Hello, World!]]>");

        assertEquals("<![CDATA[Hello, World!]]>", cdata.getData());
        assertEquals("<![CDATA[Hello, World!]]>", cdata.toString());
    }

    @Test
    public void testEndTagReset() {
        Token token = new EndTag();
        EndTag endTag = (EndTag) token.reset();

        assertNull(endTag.tagName);
        assertNull(endTag.normalName);

    }

    @Test
    public void testEOFReset() {
        Token token = new EOF();
        EOF eof = (EOF) token.reset();

        assertTrue(eof.isEOF());
    }
}