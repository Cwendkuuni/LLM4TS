package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("testName");
        assertEquals("testName", doctype.getName());
    }

    @Test
    public void testTagSetName() {
        Token.Tag tag = new Token.StartTag();
        tag.name("testName");
        assertEquals("testName", tag.name());
    }

    @Test
    public void testTagIsSelfClosing() {
        Token.Tag tag = new Token.StartTag();
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagAppendAttributeValue() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeValue("value1");
        assertEquals("value1", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("testData");
        assertEquals("testData", comment.getData());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("testData");
        assertEquals("testData", character.getData());
    }

    @Test
    public void testEOFGetType() {
        Token.EOF eof = new Token.EOF();
        assertEquals(Token.TokenType.EOF, eof.type);
    }

    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token.StartTag tag = new Token.StartTag();
        assertTrue(tag.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        assertTrue(endTag.isEndTag());
    }

    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();
        assertTrue(comment.isComment());
    }

    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character("testData");
        assertTrue(character.isCharacter());
    }

    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }

}