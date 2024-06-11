package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.TokenType;

import java.util.Arrays;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        doctype.getPublicIdentifier().equals("public id");
        doctype.getSystemIdentifier().equals("system id");
        doctype.isForceQuirks();

        doctype.reset();

        assertNull(doctype.getName());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @org.junit.Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        doctype.getPublicIdentifier().equals("public id");
        doctype.getSystemIdentifier().equals("system id");
        doctype.isForceQuirks();

        String name = doctype.getName();
        assertEquals("html", name);
        String publicId = doctype.getPublicIdentifier();
        assertEquals("public id", publicId);
        String systemId = doctype.getSystemIdentifier();
        assertEquals("system id", systemId);
        boolean forceQuirks = doctype.isForceQuirks();
        assertTrue(forceQuirks);
    }

    @org.junit.Test
    public void testTagReset() {
        StartTag tag = new StartTag();
        tag.name().equals("tag");
        tag.getAttributes() == null;

        tag.reset();

        assertNull(tag.name());
        assertNull(tag.getAttributes());
    }

    @org.junit.Test
    public void testTagNewAttribute() {
        StartTag tag = new StartTag();

        tag.newAttribute();

        assertSame(tag, tag); // identity
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertNull(tag.attributes);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.selfClosing);
        assertTrue(tag.attributes == null);
    }

    @org.junit.Test
    public void testTagFinaliseTag() {
        StartTag tag = new StartTag();
        tag.tagName = "tag";
        tag.newAttribute();
        tag.newAttribute();

        tag.finaliseTag();

        assertSame(tag, tag); // identity
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertSame(tag.attributes, tag.attributes); // identity
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.selfClosing);
        assertTrue(tag.attributes != null);
    }

    @org.junit.Test
    public void testTagName() {
        StartTag tag = new StartTag();
        tag.name("Tag");

        assertEquals("Tag", tag.name());
        assertEquals("tag", tag.normalName());
    }

    @org.junit.Test
    public void testTagIsSelfClosing() {
        StartTag tag = new StartTag();
        tag.isSelfClosing(true);

        assertTrue(tag.isSelfClosing());

        tag.isSelfClosing(false);

        assertFalse(tag.isSelfClosing());
    }

    @org.junit.Test
    public void testTokenTypes() {
        Token token = new Doctype();

        assertTrue(token.isDoctype());
        assertTrue(token.isStartTag() == false);
        assertTrue(token.isEndTag() == false);
        assertTrue(token.isComment() == false);
        assertTrue(token.isCharacter() == false);
        assertTrue(token.isCData() == false);
        assertTrue(token.isEOF() == false);

        token = new StartTag();

        assertTrue(token.isDoctype() == false);
        assertTrue(token.isStartTag());
        assertTrue(token.isEndTag() == false);
        assertTrue(token.isComment() == false);
        assertTrue(token.isCharacter() == false);
        assertTrue(token.isCData() == false);
        assertTrue(token.isEOF() == false);

        token = new EndTag();

        assertTrue(token.isDoctype() == false);
        assertTrue(token.isStartTag() == false);
        assertTrue(token.isEndTag());
        assertTrue(token.isComment() == false);
        assertTrue(token.isCharacter() == false);
        assertTrue(token.isCData() == false);
        assertTrue(token.isEOF() == false);

        token = new Comment();

        assertTrue(token.isDoctype() == false);
        assertTrue(token.isStartTag() == false);
        assertTrue(token.isEndTag() == false);
        assertTrue(token.isComment());
        assertTrue(token.isCharacter() == false);
        assertTrue(token.isCData() == false);
        assertTrue(token.isEOF() == false);

        token = new Character();

        assertTrue(token.isDoctype() == false);
        assertTrue(token.isStartTag() == false);
        assertTrue(token.isEndTag() == false);
        assertTrue(token.isComment() == false);
        assertTrue(token.isCharacter());
        assertTrue(token.isCData() == false);
        assertTrue(token.isEOF() == false);

        token = new CData();

        assertTrue(token.isDoctype() == false);
        assertTrue(token.isStartTag() == false);
        assertTrue(token.isEndTag() == false);
        assertTrue(token.isComment() == false);
        assertTrue(token.isCharacter() == false);
        assertTrue(token.isCData());
        assertTrue(token.isEOF() == false);

        token = new EOF();

        assertTrue(token.isDoctype() == false);
        assertTrue(token.isStartTag() == false);
        assertTrue(token.isEndTag() == false);
        assertTrue(token.isComment() == false);
        assertTrue(token.isCharacter() == false);
        assertTrue(token.isCData() == false);
        assertTrue(token.isEOF());
    }

    @org.junit.Test
    public void testResetStringBuilder() {
        StringBuilder sb1 = new StringBuilder("test");
        Token.reset(sb1);
        assertSame(sb1, sb1); // identity
        assertEquals("", sb1.toString());

        StringBuilder sb2 = new StringBuilder();
        Token.reset(sb2);
        assertSame(sb2, sb2); // identity
        assertEquals("", sb2.toString());
    }

    @org.junit.Test
    public void testCharacterReset() {
        Character character = new Character();
        character.getData().equals("test");

        character.reset();

        assertNull(character.getData());
    }

    @org.junit.Test
    public void testCharacterGetters() {
        Character character = new Character();
        character.getData().equals("test");

        String data = character.getData();
        assertEquals("test", data);
        assertEquals("test", character.toString());
    }

    @org.junit.Test
    public void testCharacterData() {
        Character character = new Character();

        character.data("test");

        assertEquals("test", character.getData());
        assertEquals("test", character.toString());
    }

    @org.junit.Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.getData().equals("test");

        comment.reset();

        assertNull(comment.getData());
        comment.bogus = false;
    }

    @org.junit.Test
    public void testCommentGetters() {
        Comment comment = new Comment();
        comment.getData().equals("test");

        String data = comment.getData();
        assertEquals("test", data);
        assertEquals("<!--test-->", comment.toString());
    }

    @org.junit.Test
    public void testTokenList() {
        Token doctype = new Doctype();
        doctype.getName().equals("html");
        doctype.getPublicIdentifier().equals("public id");
        doctype.getSystemIdentifier().equals("system id");
        doctype.isForceQuirks();

        Token startTag = new StartTag();
        startTag.name("Tag");

        Token comment = new Comment();
        comment.getData().equals("test");

        Token endTag = new EndTag();
        endTag.name("Tag");

        Token character = new Character();
        character.getData().equals("test");

        Token cData = new CData();
        cData.getData().equals("test");

        Token eof = new EOF();

        Token[] tokens = {doctype, startTag, comment, endTag, character, cData, eof};

        assertEquals(7, tokens.length);
        assertEquals(TokenType.Doctype, tokens[0].tokenType());
        assertEquals(TokenType.StartTag, tokens[1].tokenType());
        assertEquals(TokenType.Comment, tokens[2].tokenType());
        assertEquals(TokenType.EndTag, tokens[3].tokenType());
        assertEquals(TokenType.Character, tokens[4].tokenType());
        assertEquals(TokenType.CData, tokens[5].tokenType());
        assertEquals(TokenType.EOF, tokens[6].tokenType());

        for (Token token : tokens) {
            assertEquals(token.getClass().getSimpleName(), token.tokenType());
        }
    }
}