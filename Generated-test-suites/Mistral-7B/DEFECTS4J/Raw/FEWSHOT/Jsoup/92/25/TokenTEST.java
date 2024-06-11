package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.CData;
import org.jsoup.parser.Token.EOF;

public class TokenTest {

    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();
        Doctype resetDoctype = doctype.reset();
        assertEquals(TokenType.Doctype, resetDoctype.type);
    }

    @Test
    public void testResetDoctype() {
        Doctype doctype = new Doctype();
        doctype.name("html");
        doctype.pubSysKey("DTD HTML 5");
        doctype.publicIdentifier("public \"-//W3C//DTD HTML 5//EN\"");
        doctype.systemIdentifier("/path/to/html5.dtd");
        doctype.forceQuirks(true);

        Doctype resetDoctype = doctype.reset();

        assertEquals(TokenType.Doctype, resetDoctype.type);
        assertNull(resetDoctype.name());
        assertNull(resetDoctype.pubSysKey());
        assertNull(resetDoctype.publicIdentifier());
        assertNull(resetDoctype.systemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testStartTag() {
        StartTag startTag = new StartTag();
        StartTag resetStartTag = startTag.reset();
        assertEquals(TokenType.StartTag, resetStartTag.type);
        assertNull(resetStartTag.tagName());
        assertNull(resetStartTag.normalName());
        assertNull(resetStartTag.pendingAttributeName());
        assertNull(resetStartTag.pendingAttributeValue());
        assertFalse(resetStartTag.selfClosing());
        assertNull(resetStartTag.attributes);
    }

    @Test
    public void testResetStartTag() {
        StartTag startTag = new StartTag();
        startTag.name("html");
        startTag.normalName("html");
        startTag.pendingAttributeName("class");
        startTag.pendingAttributeValue("test");
        startTag.selfClosing(true);
        startTag.attributes = new Attributes();
        startTag.attributes.put("id", "testID");

        StartTag resetStartTag = startTag.reset();

        assertEquals(TokenType.StartTag, resetStartTag.type);
        assertNull(resetStartTag.tagName());
        assertNull(resetStartTag.normalName());
        assertNull(resetStartTag.pendingAttributeName());
        assertNull(resetStartTag.pendingAttributeValue());
        assertFalse(resetStartTag.selfClosing());
        assertNull(resetStartTag.attributes);
    }

    @Test
    public void testEndTag() {
        EndTag endTag = new EndTag();
        EndTag resetEndTag = endTag.reset();
        assertEquals(TokenType.EndTag, resetEndTag.type);
        assertNull(resetEndTag.tagName());
        assertNull(resetEndTag.normalName());
        assertNull(resetEndTag.attributes);
    }

    @Test
    public void testResetEndTag() {
        EndTag endTag = new EndTag();
        endTag.tagName("html");
        endTag.normalName("html");
        endTag.attributes = new Attributes();
        endTag.attributes.put("id", "testID");

        EndTag resetEndTag = endTag.reset();

        assertEquals(TokenType.EndTag, resetEndTag.type);
        assertNull(resetEndTag.tagName());
        assertNull(resetEndTag.normalName());
        assertNull(resetEndTag.attributes);
    }

    @Test
    public void testComment() {
        Comment comment = new Comment();
        Comment resetComment = comment.reset();
        assertEquals(TokenType.Comment, resetComment.type);
        assertNull(resetComment.data());
        assertFalse(resetComment.bogus);
    }

    @Test
    public void testResetComment() {
        Comment comment = new Comment();
        comment.data("This is a comment");
        comment.bogus(true);

        Comment resetComment = comment.reset();

        assertEquals(TokenType.Comment, resetComment.type);
        assertNull(resetComment.data());
        assertFalse(resetComment.bogus);
    }

    @Test
    public void testCharacter() {
        Character character = new Character();
        Character resetCharacter = character.reset();
        assertEquals(TokenType.Character, resetCharacter.type);
        assertNull(resetCharacter.data());
    }

    @Test
    public void testResetCharacter() {
        Character character = new Character();
        character.data("A");

        Character resetCharacter = character.reset();

        assertEquals(TokenType.Character, resetCharacter.type);
        assertNull(resetCharacter.data());
    }

    @Test
    public void testCData() {
        CData cData = new CData("CDATA[This is CData]");
        CData resetCData = cData.reset();
        assertEquals(TokenType.CData, resetCData.type);
        assertNull(resetCData.data());
    }

    @Test
    public void testResetCData() {
        CData cData = new CData("CDATA[This is CData]");

        CData resetCData = cData.reset();

        assertEquals(TokenType.CData, resetCData.type);
        assertNull(resetCData.data());
    }

    @Test
    public void testEOF() {
        EOF eof = new EOF();
        EOF resetEof = eof.reset();
        assertEquals(TokenType.EOF, resetEof.type);
    }

    @Test
    public void testResetEOF() {
        EOF eof = new EOF();

        EOF resetEof = eof.reset();

        assertEquals(TokenType.EOF, resetEof.type);
    }
}