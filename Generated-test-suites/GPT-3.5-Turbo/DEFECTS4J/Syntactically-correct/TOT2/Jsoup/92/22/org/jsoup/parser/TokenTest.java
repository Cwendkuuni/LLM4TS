package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals(Token.TokenType.Doctype, token.type);
    }

    @Test
    public void testTokenTypeEndTag() {
        Token token = new Token.EndTag();
        assertEquals(Token.TokenType.EndTag, token.type);
    }

    @Test
    public void testTokenTypeCharacter() {
        Token token = new Token.Character();
        assertEquals(Token.TokenType.Character, token.type);
    }

    @Test
    public void testTokenReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("test");
        doctype.reset();
        assertEquals("", doctype.name.toString());
    }

    @Test
    public void testDoctypeForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagNameNormal() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "TITLE";
        assertEquals("title", startTag.normalName());
    }

    @Test
    public void testTagAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeName("href");
        startTag.appendAttributeValue("http://www.example.com");
        startTag.newAttribute();
        assertEquals("http://www.example.com", startTag.getAttributes().get("href"));
    }

    @Test
    public void testCommentBogus() {
        Token.Comment comment = new Token.Comment();
        comment.bogus = true;
        assertTrue(comment.bogus);
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data("text");
        assertEquals("text", character.getData());
    }

    @Test
    public void testCharacterCDATA() {
        Token.CData cdata = new Token.CData("data");
        assertEquals("<![CDATA[data]]>", cdata.toString());
    }

    @Test
    public void testTokenTypeEOF() {
        Token token = new Token.EOF();
        assertEquals(Token.TokenType.EOF, token.type);
    }

}