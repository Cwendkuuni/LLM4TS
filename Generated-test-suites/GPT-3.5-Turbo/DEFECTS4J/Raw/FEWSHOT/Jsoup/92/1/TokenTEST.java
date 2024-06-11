package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.junit.Test;

public class TokenTest {
    @Test
    public void testTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.type = Token.TokenType.Doctype;
        assertEquals(Token.TokenType.Doctype, doctype.type);

        Token.StartTag startTag = new Token.StartTag();
        startTag.type = Token.TokenType.StartTag;
        assertEquals(Token.TokenType.StartTag, startTag.type);

        Token.EndTag endTag = new Token.EndTag();
        endTag.type = Token.TokenType.EndTag;
        assertEquals(Token.TokenType.EndTag, endTag.type);

        Token.Comment comment = new Token.Comment();
        comment.type = Token.TokenType.Comment;
        assertEquals(Token.TokenType.Comment, comment.type);

        Token.Character character = new Token.Character();
        character.type = Token.TokenType.Character;
        assertEquals(Token.TokenType.Character, character.type);

        Token.EOF eof = new Token.EOF();
        eof.type = Token.TokenType.EOF;
        assertEquals(Token.TokenType.EOF, eof.type);
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.nodeName = "html";
        doctype.publicIdentifier = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        doctype.systemIdentifier = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.nodeName);
        assertEquals("", doctype.publicIdentifier);
        assertEquals("", doctype.systemIdentifier);
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");

        assertEquals("html", doctype.getName());
    }

    @Test
    public void testDoctypeGetPubSysKey() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.pubSysKey = "-//W3C//DTD XHTML 1.0 Transitional//EN";

        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", doctype.getPubSysKey());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("-//W3C//DTD XHTML 1.0 Transitional//EN");

        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");

        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;

        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.attributes = new Attributes();
        startTag.selfClosing = true;

        startTag.reset();

        assertNull(startTag.tagName);
        assertNull(startTag.attributes);
        assertFalse(startTag.selfClosing);
    }

    @Test
    public void testStartTagNameAttr() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "span";
        startTag.attributes = new Attributes();

        startTag.nameAttr("div", new Attributes());

        assertEquals("div", startTag.tagName);
        assertNotNull(startTag.attributes);
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.attributes = new Attributes();
        startTag.attributes.put("id", "test");

        assertEquals("<div id=\"test\">", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";

        assertEquals("</div>", endTag.toString());
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Test comment");

        comment.reset();

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Test comment");

        assertEquals("Test comment", comment.getData());
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "Test character";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data = "Test character";

        assertEquals("Test character", character.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assertEquals(eof, eof.reset());
    }
}