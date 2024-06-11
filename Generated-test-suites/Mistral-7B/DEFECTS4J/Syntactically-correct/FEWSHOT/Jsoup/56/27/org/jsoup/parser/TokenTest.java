package org.jsoup.parser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.AttributeSet;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.TokenType;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.name("html");
        assertEquals("html", doctype.getName());
        assertEquals(TokenType.Doctype, doctype.type);
        doctype.reset();
        assertNull(doctype.getName());
        assertSame(TokenType.Doctype, doctype.type);
    }

    @Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.name("html");
        doctype.publicIdentifier("_publicId_");
        doctype.systemIdentifier("_systemId_");
        doctype.isForceQuirks(true);

        assertEquals("html", doctype.getName());
        assertEquals("_publicId_", doctype.getPublicIdentifier());
        assertEquals("_systemId_", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.name("div");
        tag.reset();
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @Test
    public void testTagNewAttribute() {
        Tag tag = new StartTag();
        tag.name("div");
        tag.newAttribute().name("class").value("red-green");
        assertEquals("red-green", tag.getAttributes().getValue("class"));
    }

    @Test
    public void testTagFinaliseTag() {
        Tag tag = new StartTag();
        tag.name("div");
        tag.newAttribute().name("class").value("red-green");
        tag.finaliseTag();
        assertNull(tag.pendingAttributeName);
        assertFalse(tag.hasPendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
    }

    @Test
    public void testTagNameAndNormalName() {
        Tag tag = new StartTag();
        tag.name("DIV");
        tag.name("div");
        assertEquals("DIV", tag.name());
        assertEquals("div", tag.normalName());
    }

    @Test
    public void testTagSelfClosing() {
        Tag tag = new StartTag();
        tag.name("img").selfClosing(true);
        assertTrue(tag.isSelfClosing());

        tag = new StartTag();
        tag.name("div");
        tag.selfClosing(true);
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data("A");
        assertEquals("A", character.getData());
        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.data("This is a comment");
        assertEquals("This is a comment", comment.getData());
        comment.reset();
        assertNull(comment.getData());
    }

    @Test
    public void testCommentBogus() {
        Comment comment = new Comment();
        comment.bogus(true);
        assertTrue(comment.isBogus());
        comment.reset();
        assertFalse(comment.isBogus());
    }

    @Test
    public void testEOFReset() {
        EOF eof = new EOF();
        eof.reset();
        assertSame(TokenType.EOF, eof.type);
    }

    @Test
    public void testTokenTypeIdentification() {
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

        token = new EOF();
        assertTrue(token.isEOF());
    }

    @Test
    public void testTokenTypeCasting() {
        Token token = new Doctype();
        assertEquals(TokenType.Doctype, token.tokenType());

        token = new StartTag();
        assertEquals(TokenType.StartTag, token.tokenType());

        token = new EndTag();
        assertEquals(TokenType.EndTag, token.tokenType());

        token = new Comment();
        assertEquals(TokenType.Comment, token.tokenType());

        token = new Character();
        assertEquals(TokenType.Character, token.tokenType());

        token = new EOF();
        assertEquals(TokenType.EOF, token.tokenType());
    }

    @Test
    public void testAttributeSetGetters() {
        AttributeSet attributes = new AttributeSet();
        attributes.put(new Attribute("class", "red-green"));
        attributes.put(new Attribute("id", "myId"));
        attributes.put(new BooleanAttribute("disabled", true));

        assertEquals("red-green", attributes.getValue("class"));
        assertEquals("myId", attributes.getValue("id"));
        assertTrue(attributes.getValue("disabled"));
    }

    @Test
    public void testAttributeSetEquals() {
        AttributeSet attributes1 = new AttributeSet();
        attributes1.put(new Attribute("class", "red-green"));
        attributes1.put(new Attribute("id", "myId"));
        attributes1.put(new BooleanAttribute("disabled", true));

        AttributeSet attributes2 = new AttributeSet();
        attributes2.put(new Attribute("class", "red-green"));
        attributes2.put(new Attribute("id", "myId"));
        attributes2.put(new BooleanAttribute("disabled", true));

        assertEquals(attributes1, attributes2);
    }
}