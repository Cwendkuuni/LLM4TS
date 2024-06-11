package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testResetDoctype() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName().equals("doctype");
        doctype.reset();
        assertNull(doctype.asDoctype().getName());
    }

    @Test
    public void testDoctypeGetters() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName("html");
        assertEquals("html", doctype.asDoctype().getName());
        doctype.asDoctype().setPubSysKey("foo");
        assertEquals("foo", doctype.asDoctype().getPubSysKey());
        doctype.asDoctype().getPublicIdentifier().append("public");
        assertEquals("public", doctype.asDoctype().getPublicIdentifier().toString());
        doctype.asDoctype().getSystemIdentifier().append("system");
        assertEquals("system", doctype.asDoctype().getSystemIdentifier().toString());
        doctype.asDoctype().isForceQuirks();
        assertFalse(doctype.asDoctype().isForceQuirks());
        doctype.asDoctype().isForceQuirks(true);
        assertTrue(doctype.asDoctype().isForceQuirks());
        doctype.reset();
        assertNull(doctype.asDoctype().getName());
        assertNull(doctype.asDoctype().getPubSysKey());
        Assert.assertEquals("", doctype.asDoctype().getPublicIdentifier().toString());
        Assert.assertEquals("", doctype.asDoctype().getSystemIdentifier().toString());
        assertFalse(doctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagGetters() {
        Token tag = new Token.Tag();
        tag.asTag().name("tag");
        tag.asTag().name("Tag"); // test case insensitive
        assertEquals("tag", tag.asTag().name());
        assertEquals("Tag", tag.asTag().normalName());
        assertFalse(tag.asTag().isSelfClosing());
        tag.asTag().isSelfClosing(true);
        assertTrue(tag.asTag().isSelfClosing());
        tag.reset();
        assertNull(tag.asTag().name());
        assertNull(tag.asTag().normalName());
        assertFalse(tag.asTag().isSelfClosing());
    }

    @Test
    public void testStartTagGetters() {
        Token startTag = new Token.StartTag();
        startTag.asStartTag().name("tag");
        startTag.asStartTag().attributes(new Attributes());
        assertEquals("tag", startTag.asStartTag().name());
        Attributes attributes = startTag.asStartTag().getAttributes();
        attributes.put(new Attribute("attr", "value"));
        assertEquals("attr", attributes.get("attr").getName());
        assertEquals("value", attributes.get("attr").getValue());
        startTag.reset();
        assertNull(startTag.asStartTag().name());
        assertNull(startTag.asStartTag().getAttributes());
    }

    @Test
    public void testEndTagGetters() {
        Token endTag = new Token.EndTag();
        endTag.asEndTag().name("tag");
        assertEquals("tag", endTag.asEndTag().name());
        endTag.reset();
        assertNull(endTag.asEndTag().name());
    }

    @Test
    public void testCommentGetters() {
        Token comment = new Token.Comment();
        comment.asComment().getData().append("comment");
        assertEquals("comment", comment.asComment().getData().toString());
        comment.reset();
        assertNull(comment.asComment().getData());
    }

    @Test
    public void testCharacterGetters() {
        Token character = new Token.Character();
        character.asCharacter().data("char");
        assertEquals("char", character.asCharacter().getData());
        character.reset();
        assertNull(character.asCharacter().getData());
    }

    @Test
    public void testTokenTypeGetters() {
        Token doctype = new Token.Doctype();
        Token startTag = new Token.StartTag();
        Token endTag = new Token.EndTag();
        Token comment = new Token.Comment();
        Token character = new Token.Character();
        Token eof = new Token.EOF();

        assertFalse(doctype.isDoctype());
        assertTrue(doctype.asDoctype().isDoctype());

        assertFalse(startTag.isStartTag());
        assertTrue(startTag.asStartTag().isStartTag());

        assertFalse(endTag.isEndTag());
        assertTrue(endTag.asEndTag().isEndTag());

        assertFalse(comment.isComment());
        assertTrue(comment.asComment().isComment());

        assertFalse(character.isCharacter());
        assertTrue(character.asCharacter().isCharacter());

        assertFalse(eof.isEOF());
        assertTrue(eof.asEOF().isEOF());

        assertFalse(doctype.isEndTag());
        assertFalse(doctype.isComment());
        assertFalse(doctype.isCharacter());
        assertFalse(doctype.isEOF());

        assertFalse(startTag.isDoctype());
        assertFalse(startTag.isEndTag());
        assertFalse(startTag.isComment());
        assertFalse(startTag.isCharacter());
        assertFalse(startTag.isEOF());

        assertFalse(endTag.isDoctype());
        assertFalse(endTag.isStartTag());
        assertFalse(endTag.isComment());
        assertFalse(endTag.isCharacter());
        assertFalse(endTag.isEOF());

        assertFalse(comment.isDoctype());
        assertFalse(comment.isStartTag());
        assertFalse(comment.isEndTag());
        assertFalse(comment.isCharacter());
        assertFalse(comment.isEOF());

        assertFalse(character.isDoctype());
        assertFalse(character.isStartTag());
        assertFalse(character.isEndTag());
        assertFalse(character.isComment());
        assertFalse(character.isEOF());

        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
    }
}