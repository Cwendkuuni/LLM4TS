package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.junit.*;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());
        doctype.name.append("html");
        doctype.pubSysKey = "PUBLIC";
        doctype.publicIdentifier.append("-//W3C//DTD XHTML 1.0 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        doctype.forceQuirks = false;

        assertEquals("html", doctype.getName());
        assertEquals("PUBLIC", doctype.getPubSysKey());
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token.StartTag startTag = new Token.StartTag();
        assertEquals("StartTag", startTag.tokenType());
        startTag.name("a");
        startTag.selfClosing = false;

        assertEquals("a", startTag.name());
        assertEquals("a", startTag.normalName());
        assertFalse(startTag.isSelfClosing());
    }

    @Test
    public void testEndTagToken() {
        Token.EndTag endTag = new Token.EndTag();
        assertEquals("EndTag", endTag.tokenType());
        endTag.name("p");

        assertEquals("p", endTag.name());
    }

    @Test
    public void testCommentToken() {
        Token.Comment comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());
        comment.data.append("This is a comment");
        comment.bogus = false;

        assertEquals("This is a comment", comment.getData());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterToken() {
        Token.Character character = new Token.Character();
        assertEquals("Character", character.tokenType());
        character.data("Hello World");

        assertEquals("Hello World", character.getData());
    }

    @Test
    public void testCDataToken() {
        Token.CData cdata = new Token.CData("Hello World");
        assertEquals("Character", cdata.tokenType());
        assertEquals("Hello World", cdata.getData());
    }

    @Test
    public void testEOFToken() {
        Token.EOF eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
    }

}