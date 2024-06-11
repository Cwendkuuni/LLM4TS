package org.jsoup.parser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.Token;
import org.jsoup.parser.Token.TokenType;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.EOF;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token().tokenType();
        assertEquals("Token", token);

        Doctype doctypeToken = new Doctype().tokenType();
        assertEquals(TokenType.Doctype, doctypeToken);

        StartTag startTagToken = new StartTag().tokenType();
        assertEquals(TokenType.StartTag, startTagToken);

        EndTag endTagToken = new EndTag().tokenType();
        assertEquals(TokenType.EndTag, endTagToken);

        Comment commentToken = new Comment().tokenType();
        assertEquals(TokenType.Comment, commentToken);

        Character characterToken = new Character().tokenType();
        assertEquals(TokenType.Character, characterToken);

        EOF eofToken = new EOF().tokenType();
        assertEquals(TokenType.EOF, eofToken);
    }

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        String name = "html";
        doctype.getName = name;
        assertEquals(name, doctype.getName());

        doctype.reset();
        assertNull(doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Doctype doctype = new Doctype();
        String publicIdentifier = "public-id";
        doctype.publicIdentifier = publicIdentifier;
        assertEquals(publicIdentifier, doctype.getPublicIdentifier());

        doctype.reset();
        assertNull(doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Doctype doctype = new Doctype();
        String systemIdentifier = "system-id";
        doctype.systemIdentifier = systemIdentifier;
        assertEquals(systemIdentifier, doctype.getSystemIdentifier());

        doctype.reset();
        assertNull(doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Doctype doctype = new Doctype();
        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());

        doctype.reset();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        StartTag tag = new StartTag();
        String tagName = "div";
        tag.tagName = tagName;
        assertEquals(tagName, tag.name());
        tag.reset();
        assertNull(tag.tagName);
    }

    @Test
    public void testTagName() {
        StartTag tag = new StartTag();
        tag.name("p");
        assertEquals("p", tag.name());
    }

    @Test
    public void testTagNormalName() {
        StartTag tag = new StartTag();
        tag.name("P");
        assertEquals("p", tag.normalName());
    }

    @Test
    public void testTagIsSelfClosing() {
        StartTag tag = new StartTag();
        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());

        tag.reset();
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        StartTag tag = new StartTag();
        Attributes attributes = new Attributes();
        tag.attributes = attributes;
        assertSame(attributes, tag.getAttributes());

        tag.reset();
        assertNull(tag.getAttributes());
    }

    @Test
    public void testTagNewAttribute() {
        StartTag tag = new StartTag();
        tag.newAttribute();
        assertFalse(tag.hasPendingAttributeValue);
    }

    @Test(expected = AssertionError.class)
    public void testTagNewAttributeWithAttributeName() {
        StartTag tag = new StartTag();
        String attributeName = "class";
        tag.newAttribute();
        tag.appendAttributeName(attributeName);
    }

    @Test
    public void testTagNewAttributeWithAttributeValue() {
        StartTag tag = new StartTag();
        tag.newAttribute();
        tag.appendAttributeValue("style");
        assertTrue(tag.hasPendingAttributeValue);
        assertEquals("style", tag.pendingAttributeValue.toString());
        tag.reset();
        assertFalse(tag.hasPendingAttributeValue);
    }

    @Test
    public void testTagNewAttributeWithEmptyAttributeValue() {
        StartTag tag = new StartTag();
        tag.newAttribute();
        tag.setEmptyAttributeValue();
        assertTrue(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        tag.reset();
        assertFalse(tag.hasEmptyAttributeValue);
    }

    @Test
    public void testTagFinaliseTag() {
        StartTag tag = new StartTag();
        tag.newAttribute();
        tag.finaliseTag();
        assertFalse(tag.hasPendingAttributeValue);
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        StringBuilder data = new StringBuilder("Test Comment");
        comment.data = data;
        assertEquals(data.toString(), comment.getData());

        comment.reset();
        assertNull(comment.getData());
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data = "Test Character";
        assertEquals(character.getData(), "Test Character");

        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testEOFReset() {
        EOF eof = new EOF();
        eof.reset();
        assertTrue(eof.isEOF());
    }
}