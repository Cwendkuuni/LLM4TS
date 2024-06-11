package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.parser.Token.*;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        assertEquals(TokenType.Doctype, doctype.type);

        Doctype resetDoctype = doctype.reset();
        assertTrue(resetDoctype == doctype);
        assertEquals(null, resetDoctype.getName());
        assertEquals(null, resetDoctype.pubSysKey);
        assertEquals(null, resetDoctype.publicIdentifier);
        assertEquals(null, resetDoctype.systemIdentifier);
        assertEquals(false, resetDoctype.forceQuirks);
        assertEquals(TokenType.Doctype, resetDoctype.type);
    }

    @Test
    public void testTagReset() {
        StartTag tag = new StartTag();
        tag.name("html").attributes(new Attributes().put("class", "test"));
        assertEquals("html", tag.name());
        assertEquals("html", tag.normalName());
        assertEquals("test", tag.attributes().get("class").getValue());
        assertEquals(TokenType.StartTag, tag.type);

        Tag resetTag = tag.reset();
        assertTrue(resetTag == tag);
        assertNull(resetTag.name());
        assertNull(resetTag.attributes());
        assertEquals(TokenType.StartTag, resetTag.type);
    }

    @Test
    public void testTagNewAttribute() {
        StartTag tag = new StartTag();
        tag.appendAttributeName("class").appendAttributeValue("test");
        assertEquals("test", tag.attributes().get("class").getValue());
        assertEquals(1, tag.attributes().size());

        tag.newAttribute();
        assertNull(tag.attributes());
        assertEquals(0, tag.attributes().size());
    }

    @Test
    public void testTagFinaliseTag() {
        StartTag tag = new StartTag();
        tag.appendAttributeName("class").appendAttributeValue("test");
        tag.finaliseTag();
        assertEquals("test", tag.attributes().get("class").getValue());
        assertEquals(1, tag.attributes().size());

        tag.newAttribute();
        tag.finaliseTag();
        assertNull(tag.attributes());
        assertEquals(0, tag.attributes().size());
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.getData().equals("This is a comment");
        assertEquals(TokenType.Comment, comment.type);

        Comment resetComment = comment.reset();
        assertTrue(resetComment == comment);
        assertEquals(null, resetComment.getData());
        assertEquals(false, resetComment.bogus);
        assertEquals(TokenType.Comment, resetComment.type);
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data("This is a character");
        assertEquals("This is a character", character.getData());
        assertEquals(TokenType.Character, character.type);

        Character resetCharacter = character.reset();
        assertTrue(resetCharacter == character);
        assertEquals(null, resetCharacter.getData());
        assertEquals(TokenType.Character, resetCharacter.type);
    }

    @Test
    public void testEOFReset() {
        EOF eof = new EOF();
        assertEquals(TokenType.EOF, eof.type);

        EOF resetEof = eof.reset();
        assertTrue(resetEof == eof);
        assertEquals(TokenType.EOF, resetEof.type);
    }
}