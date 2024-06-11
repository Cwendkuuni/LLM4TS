package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Token.*;
import org.jsoup.parser.TokenType;

import java.util.Arrays;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html"); // initialize name
        Doctype resetDoctype = doctype.reset();
        assertNotSame(doctype, resetDoctype);
        assertTrue(resetDoctype.getName().isEmpty());
    }

    @org.junit.Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.getName().append("html");
        doctype.getPubSysKey("publicId");
        doctype.getPublicIdentifier().append("publicId");
        doctype.setSystemIdentifier("systemId");
        doctype.setForceQuirks(true);

        assertEquals("html", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("publicId", doctype.getPublicIdentifier());
        assertEquals("systemId", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testDoctypeGettersInvalidState() {
        Doctype doctype = new Doctype();
        doctype.getName().append("html");
        doctype.reset(); // should reset all values
        doctype.getPublicIdentifier();
    }

    @org.junit.Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.nameAttr("tagName", new Attributes());
        Tag resetTag = tag.reset();
        assertNotSame(tag, resetTag);
        assertTrue(resetTag.tagName == null);
    }

    @org.junit.Test
    public void testTagGetters() {
        Tag tag = new StartTag();
        tag.nameAttr("tagName", new Attributes());
        tag.appendTagName("tagName");
        tag.name("tagName");
        assertTrue(tag.isSelfClosing());
        tag.selfClosing = false;
        Attributes attributes = tag.getAttributes();
        assertNotNull(attributes);
        attributes.put("attributeName", "attributeValue");
        assertEquals("tagName", tag.name());
        assertEquals("tagName", tag.normalName());
        assertEquals("tagName", tag.toString());
        assertEquals(attributes, tag.getAttributes());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testTagGettersInvalidState() {
        Tag tag = new StartTag();
        tag.nameAttr("tagName", new Attributes());
        tag.reset(); // should reset all values
        tag.getAttributes();
    }

    @org.junit.Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.getData().append("commentData");
        Comment resetComment = comment.reset();
        assertNotSame(comment, resetComment);
        assertTrue(resetComment.getData().isEmpty());
    }

    @org.junit.Test
    public void testCommentGetters() {
        Comment comment = new Comment();
        comment.getData().append("commentData");
        assertEquals("commentData", comment.getData());
        assertEquals("<!--commentData-->", comment.toString());
    }

    @org.junit.Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data("characterData");
        Character resetCharacter = character.reset();
        assertNotSame(character, resetCharacter);
        assertNull(resetCharacter.getData());
    }

    @org.junit.Test
    public void testCharacterGetters() {
        Character character = new Character();
        character.data("characterData");
        assertEquals("characterData", character.getData());
    }

    @org.junit.Test
    public void testTokenTypes() {
        Token token = new Doctype();
        assertTrue(token.isDoctype());
        token = new StartTag();
        assertTrue(token.isStartTag());
        token = new EndTag();
        assertTrue(token.isEndTag());
        token = new Comment();
        assertTrue(token.isComment());
        token = new Character();
        assertTrue(token.isCharacter());
        token = new CData();
        assertTrue(token.isCData());
        token = new EOF();
        assertTrue(token.isEOF());
    }

    @org.junit.Test
    public void testTokenTypeStringRepresentation() {
        Token token = new Doctype();
        assertEquals(TokenType.Doctype.name(), token.tokenType());
        token = new StartTag();
        assertEquals(TokenType.StartTag.name(), token.tokenType());
        token = new EndTag();
        assertEquals(TokenType.EndTag.name(), token.tokenType());
        token = new Comment();
        assertEquals(TokenType.Comment.name(), token.tokenType());
        token = new Character();
        assertEquals(TokenType.Character.name(), token.tokenType());
        token = new EOF();
        assertEquals(TokenType.EOF.name(), token.tokenType());
    }

    @org.junit.Test
    public void testTokenTypeEnumValues() {
        Arrays.stream(TokenType.values()).forEach(tokenType -> {
            Token token = tokenType.isDoctype() ? new Doctype() : tokenType.isStartTag() ? new StartTag() : new EndTag();
            assertEquals(tokenType, token.tokenType());
        });
    }
}