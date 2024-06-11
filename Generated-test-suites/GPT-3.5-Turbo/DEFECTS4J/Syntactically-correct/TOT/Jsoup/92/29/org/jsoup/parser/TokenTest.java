package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "pub";
        doctype.publicIdentifier.append("htmlpub");
        doctype.systemIdentifier.append("htmlsys");
        doctype.forceQuirks = true;

        assertEquals("Doctype", doctype.tokenType());
        assertEquals("html", doctype.getName());
        assertEquals("pub", doctype.getPubSysKey());
        assertEquals("htmlpub", doctype.getPublicIdentifier());
        assertEquals("htmlsys", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());

        // test reset
        doctype.reset();
        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.nameAttr("div", new Attributes());
        startTag.selfClosing = true;

        assertEquals("StartTag", startTag.tokenType());
        assertEquals("div", startTag.name());
        assertEquals("DIV", startTag.normalName());
        assertTrue(startTag.isSelfClosing());

        // test reset
        startTag.reset();
        assertNull(startTag.name());
        assertNull(startTag.normalName());
        assertFalse(startTag.isSelfClosing());
    }

    @Test
    public void testEndTagToken() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("p");

        assertEquals("EndTag", endTag.tokenType());
        assertEquals("p", endTag.name());

        // test reset
        endTag.reset();
        assertNull(endTag.name());
    }

    @Test
    public void testCommentToken() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;

        assertEquals("Comment", comment.tokenType());
        assertEquals("This is a comment", comment.getData());
        assertTrue(comment.bogus);

        // test reset
        comment.reset();
        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterToken() {
        Token.Character character = new Token.Character();
        character.data("some text");

        assertEquals("Character", character.tokenType());
        assertEquals("some text", character.getData());

        // test reset
        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testCDataToken() {
        Token.CData cdata = new Token.CData("some cdata");

        assertEquals("Character", cdata.tokenType());
        assertEquals("some cdata", cdata.getData());

        // test reset
        cdata.reset();
        assertNull(cdata.getData());
    }

    @Test
    public void testEOFToken() {
        Token.EOF eof = new Token.EOF();

        assertEquals("EOF", eof.tokenType());

        // test reset
        eof.reset();
        assertEquals(eof, eof.reset());
    }

}