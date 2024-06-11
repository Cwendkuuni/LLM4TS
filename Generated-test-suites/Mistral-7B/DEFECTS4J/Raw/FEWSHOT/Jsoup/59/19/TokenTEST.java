package org.jsoup.parser;

import static org.junit.Assert.*;

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

        doctype.pubSysKey("ps1");
        assertEquals("ps1", doctype.getPubSysKey());

        doctype.publicIdentifier("public id");
        assertEquals("public id", doctype.getPublicIdentifier());

        doctype.systemIdentifier("system id");
        assertEquals("system id", doctype.getSystemIdentifier());

        doctype.forceQuirks(true);
        assertTrue(doctype.isForceQuirks());

        Doctype resetDoctype = doctype.reset();
        assertSame(doctype, resetDoctype);
        assertEquals(TokenType.Doctype, resetDoctype.type);
        assertNull(resetDoctype.getName());
        assertNull(resetDoctype.getPubSysKey());
        assertNull(resetDoctype.getPublicIdentifier());
        assertNull(resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testTag() {
        StartTag tag = new StartTag();
        assertEquals(TokenType.StartTag, tag.type);

        tag.name("div");
        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());

        tag.selfClosing(true);
        assertTrue(tag.isSelfClosing());

        tag.attributes(new Attributes());
        assertNotNull(tag.getAttributes());
        tag.newAttribute();
        assertNull(tag.getAttributes());

        tag.attributes().put(new Attribute("id", "1"));
        Attribute attribute = tag.getAttributes().get("id");
        assertEquals("1", attribute.getValue());

        tag.finaliseTag();
        assertNull(tag.getAttributes());

        StartTag tagReset = tag.reset();
        assertSame(tag, tagReset);
        assertEquals(TokenType.StartTag, tagReset.type);
        assertNull(tagReset.name());
        assertNull(tagReset.normalName());
        assertFalse(tagReset.isSelfClosing());
        assertNull(tagReset.getAttributes());

        EndTag endTag = new EndTag();
        assertEquals(TokenType.EndTag, endTag.type);
        assertEquals("div", endTag.name());
        assertEquals("div", endTag.normalName());
        assertFalse(endTag.isSelfClosing());
        assertNull(endTag.getAttributes());

        EndTag endTagReset = endTag.reset();
        assertSame(endTag, endTagReset);
        assertEquals(TokenType.EndTag, endTagReset.type);
        assertNull(endTagReset.name());
        assertNull(endTagReset.normalName());
        assertFalse(endTagReset.isSelfClosing());
        assertNull(endTagReset.getAttributes());
    }

    @Test
    public void testComment() {
        Comment comment = new Comment();
        assertEquals(TokenType.Comment, comment.type);

        comment.data("This is a comment");
        assertEquals("This is a comment", comment.getData());

        Comment commentReset = comment.reset();
        assertSame(comment, commentReset);
        assertEquals(TokenType.Comment, commentReset.type);
        assertNull(commentReset.getData());
    }

    @Test
    public void testCharacter() {
        Character character = new Character();
        assertEquals(TokenType.Character, character.type);

        character.data("A");
        assertEquals("A", character.getData());

        Character characterReset = character.reset();
        assertSame(character, characterReset);
        assertEquals(TokenType.Character, characterReset.type);
        assertNull(characterReset.getData());
    }

    @Test
    public void testEOF() {
        EOF eof = new EOF();
        assertEquals(TokenType.EOF, eof.type);

        EOF eofReset = eof.reset();
        assertSame(eof, eofReset);
        assertEquals(TokenType.EOF, eofReset.type);
    }

    @Test
    public void testTokenTypes() {
        Token token = new Doctype();
        assertTrue(token.isDoctype());
        assertTrue(token.isDoctype()); // should not need to, but just to test constructor call doesn't reset type

        token = new StartTag();
        assertTrue(token.isStartTag());
        assertTrue(token.isStartTag());

        token = new EndTag();
        assertTrue(token.isEndTag());
        assertTrue(token.isEndTag());

        token = new Comment();
        assertTrue(token.isComment());
        assertTrue(token.isComment());

        token = new Character();
        assertTrue(token.isCharacter());
        assertTrue(token.isCharacter());

        token = new EOF();
        assertTrue(token.isEOF());
        assertTrue(token.isEOF());

        token = null;
        assertFalse(token.isDoctype());
        assertFalse(token.isDoctype());

        token = new Doctype();
        token.type = TokenType.StartTag;
        assertFalse(token.isDoctype());
        assertFalse(token.isDoctype());
    }
}