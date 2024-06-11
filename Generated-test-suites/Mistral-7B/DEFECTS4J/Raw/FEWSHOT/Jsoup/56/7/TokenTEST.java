package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctype = new Token.Doctype();
        Token resetDoctype = doctype.reset();
        assertEquals(TokenType.Doctype, resetDoctype.type);

        doctype.name("html");
        Token namedDoctype = doctype.reset();
        assertEquals("html", namedDoctype.getName());

        doctype.publicIdentifier("XHTML 1.0");
        namedDoctype = doctype.reset();
        assertEquals("XHTML 1.0", namedDoctype.getPublicIdentifier());

        doctype.systemIdentifier("/path/to/myfile.html");
        namedDoctype = doctype.reset();
        assertEquals("/path/to/myfile.html", namedDoctype.getSystemIdentifier());

        doctype.forceQuirks(true);
        namedDoctype = doctype.reset();
        assertTrue(namedDoctype.isForceQuirks());
    }

    @Test
    public void testTagToken() {
        Token tag = new Token.Tag();
        Token resetTag = tag.reset();
        assertEquals(TokenType.Tag, resetTag.type);

        tag.name("p");
        Token namedTag = tag.reset();
        assertEquals("p", namedTag.getName());

        tag.normalName("paragraph");
        namedTag = tag.reset();
        assertEquals("paragraph", namedTag.normalName());

        tag.selfClosing(true);
        namedTag = tag.reset();
        assertTrue(namedTag.isSelfClosing());

        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("red");
        namedTag = tag.reset();
        Attributes attributes = namedTag.getAttributes();
        Element element = new Element("p").addClass("red");
        assertEquals(attributes, element.attributes());

        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.setEmptyAttributeValue();
        namedTag = tag.reset();
        attributes = namedTag.getAttributes();
        assertEquals("", attributes.get("class"));

        tag.finaliseTag();
        namedTag = tag.reset();
        assertNull(namedTag.getAttributes());
    }

    @Test
    public void testCommentToken() {
        Token comment = new Token.Comment();
        Token resetComment = comment.reset();
        assertEquals(TokenType.Comment, resetComment.type);

        comment.data("This is a comment");
        Token namedComment = comment.reset();
        assertEquals("This is a comment", namedComment.getData());

        namedComment = comment.reset();
        assertNull(namedComment.getData());
    }

    @Test
    public void testCharacterToken() {
        Token character = new Token.Character();
        Token resetCharacter = character.reset();
        assertEquals(TokenType.Character, resetCharacter.type);

        character.data("A");
        Token namedCharacter = character.reset();
        assertEquals("A", namedCharacter.getData());

        namedCharacter = character.reset();
        assertNull(namedCharacter.getData());
    }

    @Test
    public void testEOFToken() {
        Token eof = new Token.EOF();
        Token resetEof = eof.reset();
        assertEquals(TokenType.EOF, resetEof.type);

        namedEof = eof.reset();
        assertNull(namedEof);
    }
}