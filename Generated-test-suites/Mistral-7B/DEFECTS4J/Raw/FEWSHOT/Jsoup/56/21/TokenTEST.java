package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Attributes;
import org.jsoup.parser.Token.BooleanAttribute;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.TokenType;

public class TokenTest {

    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();
        assertEquals(TokenType.Doctype, doctype.type);

        doctype.name("html");
        assertEquals("html", doctype.getName());

        doctype.publicIdentifier("public id");
        assertEquals("public id", doctype.getPublicIdentifier());

        doctype.systemIdentifier("system id");
        assertEquals("system id", doctype.getSystemIdentifier());

        doctype.isForceQuirks(true);
        assertTrue(doctype.isForceQuirks());

        Doctype resetDoctype = doctype.reset();
        assertEquals(TokenType.Doctype, resetDoctype.type);
        assertNull(resetDoctype.getName());
        assertNull(resetDoctype.getPublicIdentifier());
        assertNull(resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testTag() {
        Tag tag = new Tag();
        assertEquals(TokenType.Tag, tag.type);

        tag.name("div");
        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());

        tag.newAttribute();
        tag.appendAttributeName("id");
        tag.appendAttributeValue("testId");
        assertEquals(1, tag.getAttributes().size());
        assertEquals("testId", tag.getAttributes().get("id"));

        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("testClass");
        assertEquals(2, tag.getAttributes().size());
        assertEquals("testClass", tag.getAttributes().get("class"));

        tag.finaliseTag();

        Tag resetTag = tag.reset();
        assertEquals(TokenType.Tag, resetTag.type);
        assertNull(resetTag.tagName);
        assertNull(resetTag.normalName);
        assertNull(resetTag.getAttributes());
        assertFalse(resetTag.isSelfClosing());
    }

    @Test
    public void testStartTag() {
        StartTag startTag = new StartTag();
        assertEquals(TokenType.StartTag, startTag.type);

        startTag.name("p");
        assertEquals("p", startTag.name());
        assertEquals("p", startTag.normalName());

        startTag.newAttribute();
        startTag.appendAttributeName("id");
        startTag.appendAttributeValue("testId");
        assertEquals(1, startTag.getAttributes().size());
        assertEquals("testId", startTag.getAttributes().get("id"));

        StartTag resetStartTag = startTag.reset();
        assertEquals(TokenType.StartTag, resetStartTag.type);
        assertNull(resetStartTag.tagName);
        assertNull(resetStartTag.normalName);
        assertNull(resetStartTag.getAttributes());

        StartTag startTagWithAttributes = new StartTag();
        startTagWithAttributes.nameAttr("p", new Attributes().put("id", "testId").put("class", "testClass"));
        assertEquals("p", startTagWithAttributes.name());
        assertEquals("p", startTagWithAttributes.normalName());
        assertEquals(2, startTagWithAttributes.getAttributes().size());
        assertEquals("testId", startTagWithAttributes.getAttributes().get("id"));
        assertEquals("testClass", startTagWithAttributes.getAttributes().get("class"));
    }

    @Test
    public void testEndTag() {
        EndTag endTag = new EndTag();
        assertEquals(TokenType.EndTag, endTag.type);

        endTag.name("p");
        assertEquals("p", endTag.name());

        EndTag resetEndTag = endTag.reset();
        assertEquals(TokenType.EndTag, resetEndTag.type);
        assertNull(resetEndTag.tagName);
    }

    @Test
    public void testComment() {
        Comment comment = new Comment();
        assertEquals(TokenType.Comment, comment.type);

        comment.data("This is a comment.");
        assertEquals("This is a comment.", comment.getData());
        assertEquals("<!--This is a comment.-->", comment.toString());

        Comment resetComment = comment.reset();
        assertEquals(TokenType.Comment, resetComment.type);
        assertNull(resetComment.getData());
    }

    @Test
    public void testCharacter() {
        Character character = new Character();
        assertEquals(TokenType.Character, character.type);

        character.data("A");
        assertEquals('A', character.getData().charAt(0));
        assertEquals("A", character.getData());
        assertEquals("A", character.toString());

        Character resetCharacter = character.reset();
        assertEquals(TokenType.Character, resetCharacter.type);
        assertNull(resetCharacter.getData());
    }

    @Test
    public void testEOF() {
        EOF eof = new EOF();
        assertEquals(TokenType.EOF, eof.type);

        EOF resetEOF = eof.reset();
        assertEquals(TokenType.EOF, resetEOF.type);
    }
}