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
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        assertEquals("html", doctype.getName());
    }

    @Test
    public void testStartTagSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.selfClosing = true;
        assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testCommentData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data("Hello World");
        assertEquals("Hello World", character.getData());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }
}