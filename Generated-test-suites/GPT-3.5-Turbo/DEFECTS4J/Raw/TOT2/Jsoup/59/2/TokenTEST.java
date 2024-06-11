package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        doctype.reset();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = new Attributes();
        startTag.nameAttr("div", attributes);
        assertNotNull(startTag.getAttributes());
    }

    @Test
    public void testEndTagName() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("span");
        assertEquals("span", endTag.name());
    }

    @Test
    public void testCommentData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Hello");
        assertEquals("Hello", comment.getData());
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data("Lorem ipsum");
        assertEquals("Lorem ipsum", character.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        assertNotNull(eof.reset());
    }

    @Test
    public void testAsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
    }

    @Test
    public void testAsStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());
    }

    @Test
    public void testAsEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.isEndTag());
    }

    @Test
    public void testAsComment() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());
    }

    @Test
    public void testAsCharacter() {
        Token token = new Token.Character();
        assertTrue(token.isCharacter());
    }

    @Test
    public void testAsEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());
    }
}