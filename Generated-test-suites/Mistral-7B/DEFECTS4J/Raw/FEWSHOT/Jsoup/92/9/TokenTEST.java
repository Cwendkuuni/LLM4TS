package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.jsoup.helper.Validate.isFalse;

import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.TokenType;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();

        doctype.name("html");
        assertEquals("html", doctype.getName());
        assertEquals("HTML", doctype.tokenType());

        doctype.pubSysKey("DTD HTML 5");
        assertEquals("DTD HTML 5", doctype.getPubSysKey());

        doctype.publicIdentifier("PUBLIC");
        assertEquals("PUBLIC", doctype.getPublicIdentifier());

        doctype.systemIdentifier("system.html");
        assertEquals("system.html", doctype.getSystemIdentifier());

        doctype.forceQuirks(true);
        assertTrue(doctype.isForceQuirks());

        Doctype resetDoctype = doctype.reset();
        assertSame(doctype, resetDoctype);
    }

    @Test
    public void testTag() {
        StartTag tag = new StartTag();

        tag.name("p");
        assertEquals("p", tag.name());
        assertEquals("p", tag.normalName());

        tag.selfClosing(true);
        assertTrue(tag.isSelfClosing());

        tag.attributes(new Attributes());
        assertNull(tag.getAttributes());

        tag.newAttribute();
        tag.appendAttributeName("class").appendAttributeValue("foo");
        assertEquals("class", tag.pendingAttributeName());
        assertEquals("foo", tag.getAttributes().getClass());
        assertEquals("foo", tag.getAttributes().getValue("class"));

        tag.finaliseTag();
        assertNull(tag.pendingAttributeName());
        assertNull(tag.getAttributes().getClass());

        tag.reset();
        assertNull(tag.getAttributes());

        tag.name("div").attributes(new Attributes().put("class", "bar"));
        assertEquals("div", tag.name());
        assertEquals("DIV", tag.tokenType());
        assertEquals("div", tag.normalName());
        assertEquals("bar", tag.getAttributes().getValue("class"));

        Doctype doctype = new Doctype();
        tag.attributes(doctype.getAttributes());
        assertSame(doctype, tag.getAttributes());
    }

    @Test
    public void testComment() {
        Comment comment = new Comment();

        comment.appendAttributeName("data").appendAttributeValue("-- Hello, world!");
        assertEquals("-- Hello, world!", comment.getData());

        comment.reset();
        assertNull(comment.getData());

        comment.bogus(true);
        assertTrue(comment.isBogus());

        Comment resetComment = comment.reset();
        assertSame(comment, resetComment);
    }

    @Test
    public void testCharacter() {
        Character character = new Character();

        character.data("abc");
        assertEquals("abc", character.getData());

        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testCData() {
        CData cData = new CData("[CDATA[ abc ]]");

        assertEquals("[CDATA[ abc ]]", cData.toString());

        cData.reset();
        assertNull(cData.getData());
    }

    @Test
    public void testTokenType() {
        assertEquals("Doctype", TokenType.Doctype.toString());
        assertEquals("StartTag", TokenType.StartTag.toString());
        assertEquals("EndTag", TokenType.EndTag.toString());
        assertEquals("Comment", TokenType.Comment.toString());
        assertEquals("Character", TokenType.Character.toString());
        assertEquals("EOF", TokenType.EOF.toString());

        assertFalse(TokenType.Doctype.isSelfClosing());
        assertFalse(TokenType.StartTag.isSelfClosing());
        assertFalse(TokenType.EndTag.isSelfClosing());
        assertFalse(TokenType.Comment.isSelfClosing());
        assertFalse(TokenType.Character.isSelfClosing());
        assertFalse(TokenType.EOF.isSelfClosing());
    }
}