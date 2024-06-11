package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype docType = new Doctype();
        docType.name("html");
        docType.pubSysKey("_pub-sys-key_");
        docType.publicIdentifier("_public-identifier_");
        docType.systemIdentifier("_system-identifier_");
        docType.forceQuirks(true);

        Doctype resetDocType = docType.reset();

        assertEquals(TokenType.Doctype, resetDocType.type);
        assertNull(resetDocType.getName());
        assertNull(resetDocType.getPubSysKey());
        assertNull(resetDocType.getPublicIdentifier());
        assertNull(resetDoctype.getSystemIdentifier());
        assertFalse(resetDocType.isForceQuirks());
    }

    @Test
    public void testDoctypeMethods() {
        Doctype docType = new Doctype();

        docType.name("html");
        assertEquals("html", docType.getName());

        docType.pubSysKey("_pub-sys-key_");
        assertEquals("_pub-sys-key_", docType.getPubSysKey());

        docType.publicIdentifier("_public-identifier_");
        assertEquals("_public-identifier_", docType.getPublicIdentifier());

        docType.systemIdentifier("_system-identifier_");
        assertEquals("_system-identifier_", docType.getSystemIdentifier());

        docType.forceQuirks(true);
        assertTrue(docType.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.name("div");
        tag.attributes = new Attributes();
        tag.attributes.put("id", "testId");

        Tag resetTag = tag.reset();

        assertEquals(TokenType.StartTag, resetTag.type);
        assertNull(resetTag.tagName);
        assertNull(resetTag.normalName);
        assertNull(resetTag.pendingAttributeName);
        assertNull(resetTag.pendingAttributeValue);
        assertNull(resetTag.pendingAttributeValueS);
        assertFalse(resetTag.hasEmptyAttributeValue);
        assertFalse(resetTag.hasPendingAttributeValue);
        assertFalse(resetTag.selfClosing);
        assertNull(resetTag.attributes);
    }

    @Test
    public void testTagMethods() {
        StartTag startTag = new StartTag();

        startTag.name("div");
        assertEquals("div", startTag.name());

        startTag.name("DIV");
        assertEquals("DIV", startTag.name());

        startTag.name("Div");
        assertEquals("div", startTag.name()); // lowercase

        startTag.newAttribute();
        startTag.attributes = new Attributes();
        startTag.attributes.put("id", "testId");
        assertEquals("testId", startTag.attributes.get("id"));

        startTag.finaliseTag();
        assertSame(startTag.attributes, startTag.getAttributes());
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.data("This is a comment");

        Comment resetComment = comment.reset();

        assertEquals(TokenType.Comment, resetComment.type);
        assertNull(resetComment.getData());
        assertFalse(resetComment.isBogus());
    }

    @Test
    public void testCommentMethods() {
        Comment comment = new Comment();

        comment.data("This is a comment");
        assertEquals("This is a comment", comment.getData());

        comment.bogus(true);
        assertTrue(comment.isBogus());
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data("A");

        Character resetCharacter = character.reset();

        assertEquals(TokenType.Character, resetCharacter.type);
        assertNull(resetCharacter.getData());
    }

    @Test
    public void testCharacterMethods() {
        Character character = new Character();

        character.data("A");
        assertEquals("A", character.getData());

        character.data("ABC");
        assertEquals("ABC", character.getData());

        character.data("ABC\u00E9");
        assertEquals("ABC\u00E9", character.getData());
    }

    @Test
    public void testTokenTypes() {
        Token token = new Doctype();
        assertEquals(TokenType.Doctype, token.type);

        token = new StartTag();
        assertEquals(TokenType.StartTag, token.type);

        token = new EndTag();
        assertEquals(TokenType.EndTag, token.type);

        token = new Comment();
        assertEquals(TokenType.Comment, token.type);

        token = new Character();
        assertEquals(TokenType.Character, token.type);

        token = new EOF();
        assertEquals(TokenType.EOF, token.type);
    }

    @Test
    public void testTokenTypeEnums() {
        TokenType tokenType = TokenType.Doctype;
        assertEquals(TokenType.Doctype, tokenType);

        tokenType = TokenType.StartTag;
        assertEquals(TokenType.StartTag, tokenType);

        tokenType = TokenType.EndTag;
        assertEquals(TokenType.EndTag, tokenType);

        tokenType = TokenType.Comment;
        assertEquals(TokenType.Comment, tokenType);

        tokenType = TokenType.Character;
        assertEquals(TokenType.Character, tokenType);

        tokenType = TokenType.EOF;
        assertEquals(TokenType.EOF, tokenType);

        Map<TokenType, String> tokenTypeToStringMap = new HashMap<>();
        tokenTypeToStringMap.put(TokenType.Doctype, "Doctype");
        tokenTypeToStringMap.put(TokenType.StartTag, "StartTag");
        tokenTypeToStringMap.put(TokenType.EndTag, "EndTag");
        tokenTypeToStringMap.put(TokenType.Comment, "Comment");
        tokenTypeToStringMap.put(TokenType.Character, "Character");
        tokenTypeToStringMap.put(TokenType.EOF, "EOF");

        for (TokenType tokenTypeEnum : TokenType.values()) {
            assertEquals(tokenTypeToStringMap.get(tokenTypeEnum), tokenTypeEnum.toString());
        }
    }
}