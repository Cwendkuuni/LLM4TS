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
        doctype.getName().equals("html");
        assertEquals("html", doctype.getName());
        doctype.reset();
        assertNull(doctype.getName());
    }

    @Test
    public void testDoctypeSetName() {
        Doctype doctype = new Doctype();
        doctype.name("html5");
        assertEquals("html5", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Doctype doctype = new Doctype();
        doctype.getPublicIdentifier().equals("Public Identifier");
        doctype.getPublicIdentifier().equals("Public Identifier");
        doctype.reset();
        assertNull(doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeSetPublicIdentifier() {
        Doctype doctype = new Doctype();
        doctype.getPublicIdentifier().equals("");
        doctype.setPublicIdentifier("Public Identifier");
        assertEquals("Public Identifier", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Doctype doctype = new Doctype();
        doctype.getSystemIdentifier().equals("");
        doctype.setSystemIdentifier("system.html");
        assertEquals("system.html", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeSetSystemIdentifier() {
        Doctype doctype = new Doctype();
        doctype.getSystemIdentifier().equals("");
        doctype.setSystemIdentifier("system.html");
        assertEquals("system.html", doctype.getSystemIdentifier());
        doctype.reset();
        assertNull(doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Doctype doctype = new Doctype();
        assertFalse(doctype.isForceQuirks());
        doctype.isForceQuirks();
        doctype.reset();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.tagName = "div";
        tag.normalName = "div";
        tag.newAttribute();
        tag.attributes.put(new Attribute("class", "class1 class2"));
        assertEquals("div", tag.name());
        assertEquals("div", tag.name());
        tag.reset();
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.attributes);
    }

    @Test
    public void testTagName() {
        Tag tag = new StartTag();
        tag.name("div");
        assertEquals("div", tag.name());
        tag.name("span");
        assertEquals("span", tag.name());
    }

    @Test
    public void testTagNormalName() {
        Tag tag = new StartTag();
        tag.name("DIV");
        tag.normalName = "div";
        assertEquals("DIV", tag.name());
        assertEquals("div", tag.normalName());
    }

    @Test
    public void testTagIsSelfClosing() {
        Tag tag = new StartTag();
        assertFalse(tag.isSelfClosing());
        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());
        tag.reset();
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        Tag tag = new StartTag();
        tag.newAttribute();
        tag.attributes.put(new Attribute("class", "class1 class2"));
        Attributes attributes = tag.getAttributes();
        assertEquals("class1 class2", attributes.toString());
        tag.reset();
        assertNull(tag.getAttributes());
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.getData().equals("");
        assertEquals("", comment.getData());
        comment.reset();
        assertNull(comment.getData());
    }

    @Test
    public void testCommentSetData() {
        Comment comment = new Comment();
        comment.getData().equals("");
        comment.setData("Comment Data");
        assertEquals("Comment Data", comment.getData());
    }

    @Test
    public void testCommentToString() {
        Comment comment = new Comment();
        comment.getData().equals("");
        comment.setData("Comment Data");
        assertEquals("<!--Comment Data-->", comment.toString());
        comment.reset();
        assertEquals("<!---->", comment.toString());
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.getData().equals("");
        assertEquals("", character.getData());
        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testCharacterSetData() {
        Character character = new Character();
        character.getData().equals("");
        character.data("A");
        assertEquals("A", character.getData());
    }

    @Test
    public void testCharacterToString() {
        Character character = new Character();
        character.data("A");
        assertEquals("A", character.toString());
        character.reset();
        assertNull(character.toString());
    }

    @Test
    public void testEOFReset() {
        EOF eof = new EOF();
        eof.reset();
        assertSame(eof, eof.reset());
    }

    @Test
    public void testTokenTypes() {
        Token token = new Doctype();
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
        token = new StartTag();
        assertFalse(token.isDoctype());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
        token = new EndTag();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
        token = new Comment();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
        token = new Character();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
        token = new EOF();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertTrue(token.isEOF());
    }
}