package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    private Token.Doctype doctype;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    private Token.Character character;
    private Token.CData cdata;
    private Token.EOF eof;

    @Before
    public void setup() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
        cdata = new Token.CData("");
        eof = new Token.EOF();
    }

    @Test
    public void testResetDoctype() {
        doctype.name.append("name");
        doctype.pubSysKey = "key";
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        Token.Doctype resetDoctype = doctype.reset();

        assertEquals("", resetDoctype.getName());
        assertNull(resetDoctype.getPubSysKey());
        assertEquals("", resetDoctype.getPublicIdentifier());
        assertEquals("", resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testResetStartTag() {
        startTag.name("name");
        startTag.appendAttributeName("attr");
        startTag.appendAttributeValue("value");

        Token.StartTag resetStartTag = startTag.reset();

        assertEquals("", resetStartTag.name());
        assertNull(resetStartTag.normalName());
        assertNull(resetStartTag.getAttributes());
        assertFalse(resetStartTag.isSelfClosing());
    }

    @Test
    public void testResetEndTag() {
        endTag.name("name");

        Token.EndTag resetEndTag = endTag.reset();

        assertEquals("", resetEndTag.name());
        assertNull(resetEndTag.normalName());
    }

    @Test
    public void testResetComment() {
        comment.data.append("data");
        comment.bogus = true;

        Token.Comment resetComment = comment.reset();

        assertEquals("", resetComment.getData());
        assertFalse(resetComment.bogus);
    }

    @Test
    public void testResetCharacter() {
        character.data("data");

        Token.Character resetCharacter = character.reset();

        assertNull(resetCharacter.getData());
    }

    @Test
    public void testResetCData() {
        cdata.data("data");

        Token.CData resetCData = (Token.CData) cdata.reset();

        assertEquals("", resetCData.getData());
    }

    @Test
    public void testResetEOF() {
        Token.EOF resetEOF = eof.reset();
        assertSame(eof, resetEOF);
    }

    @Test
    public void testIsDoctype() {
        assertTrue(doctype.isDoctype());
        assertFalse(startTag.isDoctype());
        assertFalse(endTag.isDoctype());
        assertFalse(comment.isDoctype());
        assertFalse(character.isDoctype());
        assertFalse(cdata.isDoctype());
        assertFalse(eof.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        assertFalse(doctype.isStartTag());
        assertTrue(startTag.isStartTag());
        assertFalse(endTag.isStartTag());
        assertFalse(comment.isStartTag());
        assertFalse(character.isStartTag());
        assertFalse(cdata.isStartTag());
        assertFalse(eof.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        assertFalse(doctype.isEndTag());
        assertFalse(startTag.isEndTag());
        assertTrue(endTag.isEndTag());
        assertFalse(comment.isEndTag());
        assertFalse(character.isEndTag());
        assertFalse(cdata.isEndTag());
        assertFalse(eof.isEndTag());
    }

    @Test
    public void testIsComment() {
        assertFalse(doctype.isComment());
        assertFalse(startTag.isComment());
        assertFalse(endTag.isComment());
        assertTrue(comment.isComment());
        assertFalse(character.isComment());
        assertFalse(cdata.isComment());
        assertFalse(eof.isComment());
    }

    @Test
    public void testIsCharacter() {
        assertFalse(doctype.isCharacter());
        assertFalse(startTag.isCharacter());
        assertFalse(endTag.isCharacter());
        assertFalse(comment.isCharacter());
        assertTrue(character.isCharacter());
        assertFalse(cdata.isCharacter());
        assertFalse(eof.isCharacter());
    }

    @Test
    public void testIsCData() {
        assertFalse(doctype.isCData());
        assertFalse(startTag.isCData());
        assertFalse(endTag.isCData());
        assertFalse(comment.isCData());
        assertFalse(character.isCData());
        assertFalse(cdata.isCData());
        assertFalse(eof.isCData());
    }

    @Test
    public void testIsEOF() {
        assertFalse(doctype.isEOF());
        assertFalse(startTag.isEOF());
        assertFalse(endTag.isEOF());
        assertFalse(comment.isEOF());
        assertFalse(character.isEOF());
        assertFalse(cdata.isEOF());
        assertTrue(eof.isEOF());
    }
}