package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;

import java.util.Arrays;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        doctype.setForceQuirks(true);

        Doctype resetDoctype = doctype.reset();
        ResetCheck(doctype, resetDoctype);
    }

    @Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        doctype.setForceQuirks(true);

        assertEquals("html", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        doctype.setPubSysKey("_pubSysKey_");
        assertEquals("_pubSysKey_", doctype.getPubSysKey());

        doctype.setPublicIdentifier("_publicIdentifier_");
        assertEquals("_publicIdentifier_", doctype.getPublicIdentifier());

        doctype.setSystemIdentifier("_systemIdentifier_");
        assertEquals("_systemIdentifier_", doctype.getSystemIdentifier());

        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.name().equals("tag");

        Tag resetTag = tag.reset();
        ResetCheck(tag, resetTag);
    }

    @Test
    public void testTagGetters() {
        StartTag tag = new StartTag();
        tag.name().equals("tag");

        assertEquals("tag", tag.name());
        assertEquals("tag", tag.normalName());
        assertNull(tag.getAttributes());
        assertFalse(tag.isSelfClosing());

        Attributes attributes = new Attributes();
        attributes.put("attr1", "val1");
        attributes.put("attr2", "val2");

        tag.newAttribute();
        tag.appendAttributeName("attr1");
        tag.appendAttributeValue("val1");
        tag.newAttribute();
        tag.appendAttributeName("attr2");
        tag.appendAttributeValue("val2");

        assertEquals("tag", tag.name());
        assertEquals("tag", tag.normalName());
        assertSame(attributes, tag.getAttributes());
        assertFalse(tag.isSelfClosing());

        tag.finaliseTag();
        assertEquals("tag", tag.name());
        assertEquals("tag", tag.normalName());
        assertSame(attributes, tag.getAttributes());
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagIsSelfClosing() {
        StartTag tag = new StartTag();
        tag.name().equals("img");
        tag.selfClosing = true;

        assertTrue(tag.isSelfClosing());

        tag.selfClosing = false;
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.getData().equals("comment");

        Comment resetComment = comment.reset();
        ResetCheck(comment, resetComment);
    }

    @Test
    public void testCommentGetters() {
        Comment comment = new Comment();
        comment.getData().equals("comment");

        assertEquals("comment", comment.getData());
        assertFalse(comment.isBogus());

        comment.setBogus(true);
        assertTrue(comment.isBogus());
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.getData().equals("char");

        Character resetCharacter = character.reset();
        ResetCheck(character, resetCharacter);
    }

    @Test
    public void testCharacterGetters() {
        Character character = new Character();
        character.getData().equals("char");

        assertEquals("char", character.getData());

        character.data("newChar");
        assertEquals("newChar", character.getData());
    }

    @Test
    public void testCDataReset() {
        CData cData = new CData("cdata");

        CData resetCData = cData.reset();
        ResetCheck(cData, resetCData);
    }

    @Test
    public void testCDataGetters() {
        CData cData = new CData("cdata");

        assertEquals("cdata", cData.getData());
    }

    @Test
    public void testTokenTypeGetters() {
        Character character = new Character();
        character.type = TokenType.StartTag;

        assertFalse(character.isDoctype());
        assertFalse(character.isStartTag());
        assertFalse(character.isEndTag());
        assertFalse(character.isComment());
        assertFalse(character.isCharacter());
        assertFalse(character.isCData());
        assertFalse(character.isEOF());

        StartTag startTag = new StartTag();
        startTag.type = TokenType.Doctype;

        assertFalse(startTag.isDoctype());
        assertFalse(startTag.isStartTag());
        assertFalse(startTag.isEndTag());
        assertFalse(startTag.isComment());
        assertFalse(startTag.isCharacter());
        assertFalse(startTag.isCData());
        assertFalse(startTag.isEOF());

        Doctype doctype = new Doctype();
        doctype.type = TokenType.Doctype;

        assertTrue(doctype.isDoctype());
        assertFalse(doctype.isStartTag());
        assertFalse(doctype.isEndTag());
        assertFalse(doctype.isComment());
        assertFalse(doctype.isCharacter());
        assertFalse(doctype.isCData());
        assertFalse(doctype.isEOF());

        EndTag endTag = new EndTag();
        endTag.type = TokenType.EndTag;

        assertFalse(endTag.isDoctype());
        assertFalse(endTag.isStartTag());
        assertTrue(endTag.isEndTag());
        assertFalse(endTag.isComment());
        assertFalse(endTag.isCharacter());
        assertFalse(endTag.isCData());
        assertFalse(endTag.isEOF());

        Comment comment = new Comment();
        comment.type = TokenType.Comment;

        assertFalse(comment.isDoctype());
        assertFalse(comment.isStartTag());
        assertFalse(comment.isEndTag());
        assertTrue(comment.isComment());
        assertFalse(comment.isCharacter());
        assertFalse(comment.isCData());
        assertFalse(comment.isEOF());

        Character char1 = new Character();
        char1.type = TokenType.Character;

        assertFalse(char1.isDoctype());
        assertFalse(char1.isStartTag());
        assertFalse(char1.isEndTag());
        assertFalse(char1.isComment());
        assertTrue(char1.isCharacter());
        assertFalse(char1.isCData());
        assertFalse(char1.isEOF());

        CData cdata = new CData("cdata");
        cdata.type = TokenType.CData;

        assertFalse(cdata.isDoctype());
        assertFalse(cdata.isStartTag());
        assertFalse(cdata.isEndTag());
        assertFalse(cdata.isComment());
        assertFalse(cdata.isCharacter());
        assertTrue(cdata.isCData());
        assertFalse(cdata.isEOF());

        EOF eof = new EOF();
        eof.type = TokenType.EOF;

        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
        assertFalse(eof.isCData());
        assertTrue(eof.isEOF());
    }

    private void ResetCheck(Token token1, Token token2) {
        assertEquals(token1.getClass(), token2.getClass());
        assertEquals(token1.type, token2.type);
        assertNull(token1.tagName);
        assertNull(token1.normalName);
        assertNull(token1.pendingAttributeName);
        reset(token1.pendingAttributeValue);
        assertNull(token1.pendingAttributeValueS);
        assertFalse(token1.hasEmptyAttributeValue);
        assertFalse(token1.hasPendingAttributeValue);
        assertFalse(token1.selfClosing);
        assertNull(token1.attributes);
    }
}