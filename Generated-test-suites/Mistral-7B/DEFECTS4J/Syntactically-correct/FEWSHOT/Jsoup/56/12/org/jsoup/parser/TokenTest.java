package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Attributes;
import org.jsoup.parser.Token.BooleanAttribute;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Doctype doctype = new Doctype();

        doctype.name("html");
        doctype.publicIdentifier("-//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier("http://www.w3.org/TR/html4/strict.dtd");
        doctype.forceQuirks(true);

        assertEquals("Doctype", doctype.tokenType());
        assertEquals("html", doctype.getName());
        assertEquals("--", doctype.getPublicIdentifier().substring(0, 2));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());

        Doctype resetDoctype = doctype.reset();
        assertNotSame(doctype, resetDoctype);
        assertEquals("", resetDoctype.getName());
        assertEquals("", resetDoctype.getPublicIdentifier());
        assertEquals("", resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        StartTag startTag = new StartTag();

        startTag.name("div");
        startTag.attributes(new Attributes().put("class", "container"));

        assertEquals("StartTag", startTag.tokenType());
        assertEquals("div", startTag.name());
        assertEquals("container", startTag.getAttributes().getValue("class"));

        StartTag resetStartTag = startTag.reset();
        assertNotSame(startTag, resetStartTag);
        assertEquals(null, resetStartTag.name());
        assertNull(resetStartTag.getAttributes());
    }

    @Test
    public void testEndTagToken() {
        EndTag endTag = new EndTag();

        endTag.name("div");

        assertEquals("EndTag", endTag.tokenType());
        assertEquals("div", endTag.name());

        EndTag resetEndTag = endTag.reset();
        assertNotSame(endTag, resetEndTag);
        assertEquals(null, resetEndTag.name());
    }

    @Test
    public void testCommentToken() {
        Comment comment = new Comment();

        comment.data("This is a comment.");

        assertEquals("Comment", comment.tokenType());
        assertEquals("This is a comment.", comment.getData());

        Comment resetComment = comment.reset();
        assertNotSame(comment, resetComment);
        assertEquals("", resetComment.getData());
    }

    @Test
    public void testCharacterToken() {
        Character character = new Character();

        character.data("A");

        assertEquals("Character", character.tokenType());
        assertEquals("A", character.getData());

        Character resetCharacter = character.reset();
        assertNotSame(character, resetCharacter);
        assertEquals(null, resetCharacter.getData());
    }

    @Test
    public void testBooleanAttribute() {
        Doctype doctype = new Doctype();
        BooleanAttribute booleanAttribute = new BooleanAttribute("quirksmode");

        doctype.attributes().put(booleanAttribute);

        assertTrue(doctype.asDoctype().getAttributes().getValue("quirksmode"));

        Doctype resetDoctype = doctype.reset();
        assertNotSame(doctype, resetDoctype);
        assertNull(resetDoctype.getAttributes());
    }

    @Test
    public void testAttributeWithValue() {
        StartTag startTag = new StartTag();
        Attribute attribute = new Attribute("class", "container");

        startTag.newAttribute();
        startTag.appendAttributeName("class");
        startTag.appendAttributeValue("container");

        assertEquals("container", startTag.getAttributes().getValue("class"));

        startTag.finaliseTag();

        assertEquals("StartTag", startTag.tokenType());
        assertEquals("container", startTag.getAttributes().getValue("class"));

        StartTag resetStartTag = startTag.reset();
        assertNotSame(startTag, resetStartTag);
        assertEquals(null, resetStartTag.getAttributes());
    }

    @Test
    public void testAttributeWithEmptyValue() {
        StartTag startTag = new StartTag();

        startTag.newAttribute();
        startTag.appendAttributeName("checked");
        startTag.setEmptyAttributeValue();

        assertTrue(startTag.getAttributes().getValue("checked").isEmpty());

        startTag.finaliseTag();

        assertEquals("StartTag", startTag.tokenType());
        assertTrue(startTag.getAttributes().getValue("checked").isEmpty());

        StartTag resetStartTag = startTag.reset();
        assertNotSame(startTag, resetStartTag);
        assertEquals(null, resetStartTag.getAttributes());
    }
}