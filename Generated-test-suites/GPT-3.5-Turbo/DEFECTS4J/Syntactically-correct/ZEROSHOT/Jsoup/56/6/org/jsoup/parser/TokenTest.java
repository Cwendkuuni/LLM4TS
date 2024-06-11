package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.name.append("test");
        doctype.publicIdentifier.append("test");
        doctype.systemIdentifier.append("test");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testDoctypeGetName() {
        Doctype doctype = new Doctype();
        doctype.name.append("test");

        assertEquals("test", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Doctype doctype = new Doctype();
        doctype.publicIdentifier.append("test");

        assertEquals("test", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Doctype doctype = new Doctype();
        doctype.systemIdentifier.append("test");

        assertEquals("test", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Doctype doctype = new Doctype();
        doctype.forceQuirks = true;

        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        StartTag startTag = new StartTag();
        startTag.tagName = "test";
        startTag.normalName = "test";
        startTag.pendingAttributeName = "test";
        startTag.pendingAttributeValue.append("test");
        startTag.pendingAttributeValueS = "test";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        startTag.reset();

        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }

    @Test
    public void testStartTagNameAttr() {
        StartTag startTag = new StartTag();
        Attributes attributes = new Attributes();
        startTag.nameAttr("test", attributes);

        assertEquals("test", startTag.tagName);
        assertEquals(attributes, startTag.attributes);
        assertEquals("test", startTag.normalName);
    }

    @Test
    public void testStartTagToStringWithAttributes() {
        StartTag startTag = new StartTag();
        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        startTag.tagName = "test";
        startTag.attributes = attributes;

        assertEquals("<test attr1=\"value1\" attr2=\"value2\">", startTag.toString());
    }

    @Test
    public void testStartTagToStringWithoutAttributes() {
        StartTag startTag = new StartTag();
        startTag.tagName = "test";
        startTag.attributes = null;

        assertEquals("<test>", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        EndTag endTag = new EndTag();
        endTag.tagName = "test";

        assertEquals("</test>", endTag.toString());
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.data.append("test");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCommentGetData() {
        Comment comment = new Comment();
        comment.data.append("test");

        assertEquals("test", comment.getData());
    }

    @Test
    public void testCommentToString() {
        Comment comment = new Comment();
        comment.data.append("test");

        assertEquals("<!--test-->", comment.toString());
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data = "test";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testCharacterData() {
        Character character = new Character();
        character.data("test");

        assertEquals("test", character.getData());
    }

    @Test
    public void testCharacterToString() {
        Character character = new Character();
        character.data = "test";

        assertEquals("test", character.toString());
    }

    @Test
    public void testEOFReset() {
        EOF eof = new EOF();

        eof.reset();

        assertNotNull(eof);
    }

    @Test
    public void testIsDoctype() {
        Token token = new Doctype();
        assertTrue(token.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token token = new Doctype();
        Doctype doctype = token.asDoctype();
        assertNotNull(doctype);
    }

    @Test
    public void testIsStartTag() {
        Token token = new StartTag();
        assertTrue(token.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token token = new StartTag();
        StartTag startTag = token.asStartTag();
        assertNotNull(startTag);
    }

    @Test
    public void testIsEndTag() {
        Token token = new EndTag();
        assertTrue(token.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token token = new EndTag();
        EndTag endTag = token.asEndTag();
        assertNotNull(endTag);
    }

    @Test
    public void testIsComment() {
        Token token = new Comment();
        assertTrue(token.isComment());
    }

    @Test
    public void testAsComment() {
        Token token = new Comment();
        Comment comment = token.asComment();
        assertNotNull(comment);
    }

    @Test
    public void testIsCharacter() {
        Token token = new Character();
        assertTrue(token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token token = new Character();
        Character character = token.asCharacter();
        assertNotNull(character);
    }

    @Test
    public void testIsEOF() {
        Token token = new EOF();
        assertTrue(token.isEOF());
    }

    @Test
    public void testTokenTypes() {
        assertEquals(Token.TokenType.Doctype, Token.TokenType.valueOf("Doctype"));
        assertEquals(Token.TokenType.StartTag, Token.TokenType.valueOf("StartTag"));
        assertEquals(Token.TokenType.EndTag, Token.TokenType.valueOf("EndTag"));
        assertEquals(Token.TokenType.Comment, Token.TokenType.valueOf("Comment"));
        assertEquals(Token.TokenType.Character, Token.TokenType.valueOf("Character"));
        assertEquals(Token.TokenType.EOF, Token.TokenType.valueOf("EOF"));
    }
}