package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.junit.Test;

public class TokenTest {
    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html"); // assert initial state

        doctype.reset();
        doctype.getName().equals("");
        doctype.forceQuirks = false;

        doctype.getName("html5");
        doctype.forceQuirks = true;

        doctype.reset();
        doctype.getName().equals("");
        doctype.forceQuirks = false;
    }

    @Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.getName("html");

        assertEquals("html", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        doctype.setName("html5");

        assertEquals("html5", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        StartTag tag = new StartTag();
        tag.name("html").attributes(new Attributes());

        tag.reset();
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        tag.hasEmptyAttributeValue = false;
        tag.hasPendingAttributeValue = false;
        tag.selfClosing = false;
        assertNull(tag.attributes);
    }

    @Test
    public void testTagName() {
        StartTag tag = new StartTag();
        tag.name("html");
        tag.name("body");

        assertEquals("html", tag.name());
        assertEquals("body", tag.normalName());
    }

    @Test
    public void testTagNewAttribute() {
        StartTag tag = new StartTag();

        tag.newAttribute().put("class", "foo");
        tag.newAttribute().put("id", "bar");
        tag.newAttribute().put("data-myprop", "value");

        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.hasEmptyAttributeValue);
        assertEquals(3, tag.getAttributes().size());

        assertEquals("foo", tag.getAttributes().get("class"));
        assertEquals("bar", tag.getAttributes().get("id"));
        assertEquals("value", tag.getAttributes().get("data-myprop"));
    }

    @Test
    public void testTagFinaliseTag() {
        StartTag tag = new StartTag();

        tag.newAttribute().put("class", "foo");
        tag.newAttribute().put("id", "bar");
        tag.finaliseTag();

        assertTrue(tag.hasPendingAttributeValue);
        assertEquals(1, tag.getAttributes().size());
        assertEquals("foo bar", tag.getAttributes().get("class").getValue());
    }

    @Test
    public void testTagToString() {
        StartTag tag = new StartTag();

        tag.name("html").attributes(new Attributes().put("class", "foo").put("id", "bar"));
        assertEquals("<html class=\"foo\" id=\"bar\">", tag.toString());
    }

    @Test
    public void testTagResetStartTag() {
        StartTag tag = new StartTag();
        tag.name("html").attributes(new Attributes().put("class", "foo").put("id", "bar"));

        tag.reset();
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        tag.hasEmptyAttributeValue = false;
        tag.hasPendingAttributeValue = false;
        tag.selfClosing = false;
        assertNull(tag.attributes);
    }

    @Test
    public void testTagResetEndTag() {
        EndTag tag = new EndTag();

        tag.reset();
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        tag.hasEmptyAttributeValue = false;
        tag.hasPendingAttributeValue = false;
        tag.selfClosing = false;
        assertNull(tag.attributes);
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.getData().equals("comment data");

        comment.reset();
        assertNull(comment.getData());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCommentGetters() {
        Comment comment = new Comment();
        comment.getData().equals("comment data");

        assertEquals("comment data", comment.getData());
        assertFalse(comment.bogus);

        comment.bogus = true;

        assertEquals("comment data", comment.getData());
        assertTrue(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.getData().equals("character data");

        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testCharacterGetters() {
        Character character = new Character();
        character.data("character data");

        assertEquals("character data", character.getData());
        assertEquals("character data", character.toString());
    }

    @Test
    public void testTagDataTypeCheck() {
        Token tag = new StartTag();
        tag.type = TokenType.EndTag;

        assertFalse(tag.isStartTag());
        assertFalse(tag.isEndTag());
        assertFalse(tag.isComment());
        assertFalse(tag.isCharacter());
        assertFalse(tag.isDoctype());
        assertFalse(tag.isEOF());

        tag = new EndTag();

        assertFalse(tag.isStartTag());
        assertTrue(tag.isEndTag());
        assertFalse(tag.isComment());
        assertFalse(tag.isCharacter());
        assertFalse(tag.isDoctype());
        assertFalse(tag.isEOF());

        tag = new Comment();

        assertFalse(tag.isStartTag());
        assertFalse(tag.isEndTag());
        assertTrue(tag.isComment());
        assertFalse(tag.isCharacter());
        assertFalse(tag.isDoctype());
        assertFalse(tag.isEOF());

        tag = new Character();

        assertFalse(tag.isStartTag());
        assertFalse(tag.isEndTag());
        assertFalse(tag.isComment());
        assertTrue(tag.isCharacter());
        assertFalse(tag.isDoctype());
        assertFalse(tag.isEOF());

        tag = new Doctype();

        assertFalse(tag.isStartTag());
        assertFalse(tag.isEndTag());
        assertFalse(tag.isComment());
        assertFalse(tag.isCharacter());
        assertTrue(tag.isDoctype());
        assertFalse(tag.isEOF());

        tag = new EOF();

        assertFalse(tag.isStartTag());
        assertFalse(tag.isEndTag());
        assertFalse(tag.isComment());
        assertFalse(tag.isCharacter());
        assertFalse(tag.isDoctype());
        assertTrue(tag.isEOF());
    }

    @Test
    public void testTagTypeCasts() {
        Token token = new StartTag();

        assertSame(TokenType.StartTag, token.type);
        assertSame(StartTag.class, token.getClass());
        assertSame(StartTag.class, token.asStartTag());
        assertFalse(token.isDoctype());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());

        token = new EndTag();
        assertSame(TokenType.EndTag, token.type);
        assertSame(EndTag.class, token.getClass());
        assertSame(EndTag.class, token.asEndTag());
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());

        token = new Comment();
        assertSame(TokenType.Comment, token.type);
        assertSame(Comment.class, token.getClass());
        assertSame(Comment.class, token.asComment());
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());

        token = new Character();
        assertSame(TokenType.Character, token.type);
        assertSame(Character.class, token.getClass());
        assertSame(Character.class, token.asCharacter());
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isEOF());

        token = new Doctype();
        assertSame(TokenType.Doctype, token.type);
        assertSame(Doctype.class, token.getClass());
        assertSame(Doctype.class, token.asDoctype());
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());

        token = new EOF();
        assertSame(TokenType.EOF, token.type);
        assertSame(EOF.class, token.getClass());
        assertSame(EOF.class, token.asEOF());
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertTrue(token.isEOF());
    }
}