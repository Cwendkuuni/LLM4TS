package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.EOF;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();

        // test getters
        assertEquals(TokenType.Doctype, doctype.type);
        assertNull(doctype.getName());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        doctype.getName().append("html");
        assertEquals("html", doctype.getName());

        doctype.getPublicIdentifier().append("Public");
        assertEquals("Public", doctype.getPublicIdentifier());

        doctype.getSystemIdentifier().append("system.html");
        assertEquals("system.html", doctype.getSystemIdentifier());

        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTag() {
        StartTag startTag = new StartTag("div");

        // test getters
        assertEquals(TokenType.StartTag, startTag.type);
        assertEquals("div", startTag.name());
        assertFalse(startTag.isSelfClosing());
        assertNull(startTag.getAttributes());

        // test setting attributes
        startTag.newAttribute();
        startTag.appendAttributeName("class");
        startTag.appendAttributeValue("box");
        assertEquals("class=box", startTag.toString());

        startTag.appendAttributeName("id");
        startTag.appendAttributeValue("container");
        assertEquals("class=box id=container", startTag.toString());

        startTag.finaliseTag();
        assertEquals("<div class=box id=container>", startTag.toString());

        // test self closing tag
        startTag = new StartTag("img").name("img").isSelfClosing(true);
        assertEquals("<img>", startTag.toString());

        // test end tag
        EndTag endTag = new EndTag("div");
        assertEquals("<div>", startTag.toString());
        assertEquals("/div>", endTag.toString());
    }

    @Test
    public void testComment() {
        Comment comment = new Comment();

        // test getters
        assertEquals(TokenType.Comment, comment.type);
        assertNull(comment.getData());

        // test setting data
        comment.getData().append("This is a comment");
        assertEquals("This is a comment", comment.getData().toString());
    }

    @Test
    public void testCharacter() {
        Character character = new Character("A");

        // test getters
        assertEquals(TokenType.Character, character.type);
        assertEquals("A", character.getData());
    }

    @Test
    public void testEOF() {
        EOF eof = new EOF();

        // test getters
        assertEquals(TokenType.EOF, eof.type);
    }
}