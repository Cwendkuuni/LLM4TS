package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

public class TokenTest {

    // Test for Doctype token
    @Test
    public void testDoctypeToken() {
        Token doctype = new Token.Doctype();
        Token resetDoctype = doctype.reset();
        assertEquals(TokenType.Doctype, resetDoctype.type);

        doctype.name("html");
        Token namedDoctype = doctype.reset();
        assertEquals("html", namedDoctype.getName());

        doctype.pubSysKey("DTD HTML 4.01");
        namedDoctype = doctype.reset();
        assertEquals("DTD HTML 4.01", namedDoctype.getPubSysKey());

        doctype.publicIdentifier("public \"-//W3C//DTD HTML 4.01//EN\"");
        namedDoctype = doctype.reset();
        assertEquals("public \"-//W3C//DTD HTML 4.01//EN\"", namedDoctype.getPublicIdentifier());

        doctype.systemIdentifier("/path/to/html.dtd");
        namedDoctype = doctype.reset();
        assertEquals("/path/to/html.dtd", namedDoctype.getSystemIdentifier());

        doctype.forceQuirks(true);
        namedDoctype = doctype.reset();
        assertTrue(namedDoctype.isForceQuirks());
    }

    // Test for Tag token (StartTag and EndTag)
    @Test
    public void testTagToken() {
        Token tag = new Token.StartTag();
        Token resetTag = tag.reset();
        assertEquals(TokenType.StartTag, resetTag.type);

        tag.name("div");
        Token namedTag = tag.reset();
        assertEquals("div", namedTag.name());

        tag.name("p");
        namedTag = tag.reset();
        assertEquals("p", namedTag.name());

        tag.attributes = new Attributes();
        tag.attributes.put(new Attribute("class", "foo"));
        tag.attributes.put(new Attribute("id", "bar"));
        namedTag = tag.reset();
        Attributes attributes = namedTag.getAttributes();
        Assert.assertEquals(2, attributes.size());
        assertTrue(attributes.hasAttributeValue("class", "foo"));
        assertTrue(attributes.hasAttributeValue("id", "bar"));

        tag = new Token.EndTag();
        resetTag = tag.reset();
        assertEquals(TokenType.EndTag, resetTag.type);

        tag.name("div");
        namedTag = tag.reset();
        assertEquals("div", namedTag.name());
    }

    // Test for Comment token
    @Test
    public void testCommentToken() {
        Token comment = new Token.Comment();
        Token resetComment = comment.reset();
        assertEquals(TokenType.Comment, resetComment.type);

        comment.data("This is a comment");
        Token namedComment = comment.reset();
        assertEquals("This is a comment", namedComment.getData());

        comment.bogus = true;
        namedComment = comment.reset();
        assertTrue(namedComment.isBogus());
    }

    // Test for Character token
    @Test
    public void testCharacterToken() {
        Token character = new Token.Character();
        Token resetCharacter = character.reset();
        assertEquals(TokenType.Character, resetCharacter.type);

        character.data("A character");
        Token namedCharacter = character.reset();
        assertEquals("A character", namedCharacter.getData());
    }

    // Test for EOF token
    @Test
    public void testEOFToken() {
        Token eof = new Token.EOF();
        Token resetEof = eof.reset();
        assertEquals(TokenType.EOF, resetEof.type);
    }
}