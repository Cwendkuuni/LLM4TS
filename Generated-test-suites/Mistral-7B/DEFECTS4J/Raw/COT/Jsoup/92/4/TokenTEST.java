package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.CData;
import org.jsoup.parser.Token.EOF;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @org.junit.Test
    public void testDoctype() {
        Doctype doctype = new Doctype();
        assertEquals("Doctype", doctype.tokenType());

        Doctype resetDoctype = doctype.reset();
        assertNotSame(doctype, resetDoctype);

        doctype.name("html");
        assertEquals("html", doctype.getName());

        doctype.pubSysKey("DTD XHTML 1.0");
        assertEquals("DTD XHTML 1.0", doctype.getPubSysKey());

        doctype.publicIdentifier("XHTML-1.0");
        assertEquals("XHTML-1.0", doctype.getPublicIdentifier());

        doctype.systemIdentifier("/path/to/xhtml10.dtd");
        assertEquals("/path/to/xhtml10.dtd", doctype.getSystemIdentifier());

        doctype.isForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    @org.junit.Test
    public void testStartTag() {
        StartTag startTag = new StartTag();
        assertEquals("StartTag", startTag.tokenType().name());

        StartTag resetStartTag = startTag.reset();
        assertNotSame(startTag, resetStartTag);

        startTag.name("div");
        assertEquals("div", startTag.name());

        startTag.selfClosing(true);
        assertTrue(startTag.isSelfClosing());

        startTag.attributes(new org.jsoup.nodes.Attributes().put("id", "myID").put("class", "myClass"));
        assertNotNull(startTag.getAttributes());

        startTag.newAttribute();
        startTag.appendAttributeName("title");
        startTag.appendAttributeValue("My Title");
        assertEquals("title", startTag.pendingAttributeName);
        assertTrue(startTag.hasPendingAttributeValue);
        assertEquals("My Title", startTag.pendingAttributeValue.toString());

        startTag.newAttribute();
        startTag.appendAttributeName("style");
        startTag.appendAttributeValue("color: red;");
        assertEquals("style", startTag.pendingAttributeName);
        assertFalse(startTag.hasPendingAttributeValue);
        assertEquals("color: red;", startTag.pendingAttributeValueS);

        startTag.finaliseTag();

        assertEquals("<div id=\"myID\" class=\"myClass\" title=\"My Title\" style=\"color: red;\"></div>", startTag.toString());
    }

    @org.junit.Test
    public void testEndTag() {
        EndTag endTag = new EndTag();
        assertEquals("EndTag", endTag.tokenType().name());

        EndTag resetEndTag = endTag.reset();
        assertNotSame(endTag, resetEndTag);

        endTag.name("div");
        assertEquals("div", endTag.name());

        assertEquals("</div>", endTag.toString());
    }

    @org.junit.Test
    public void testComment() {
        Comment comment = new Comment();
        assertEquals("Comment", comment.tokenType().name());

        Comment resetComment = comment.reset();
        assertNotSame(comment, resetComment);

        comment.data("This is a comment");
        assertEquals("This is a comment", comment.getData());

        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @org.junit.Test
    public void testCharacter() {
        Character character = new Character();
        assertEquals("Character", character.tokenType().name());

        Character resetCharacter = character.reset();
        assertNotSame(character, resetCharacter);

        character.data("Hello World!");
        assertEquals("Hello World!", character.getData());

        assertEquals("Hello World!", character.toString());
    }

    @org.junit.Test
    public void testCData() {
        CData cData = new CData("<![CDATA[Hello World!]]>");
        assertEquals("CData", cData.tokenType().name());

        CData resetCData = cData.reset();
        assertNotSame(cData, resetCData);

        assertEquals("<![CDATA[Hello World!]]>", cData.toString());
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testEOF() {
        EOF eof = new EOF();
        assertEquals("EOF", eof.tokenType().name());

        EOF resetEOF = eof.reset();
        assertNotSame(eof, resetEOF);

        // EOF cannot be instantiated or reset, causing AssertionError
    }

    @org.junit.Test
    public void testTokenTypes() {
        TokenType[] tokenTypes = TokenType.values();
        for (TokenType tokenType : tokenTypes) {
            assertEquals(tokenType.name(), tokenType.toString());
        }

        assertTrue(TokenType.Doctype.isInstance(new Doctype()));
        assertTrue(TokenType.StartTag.isInstance(new StartTag()));
        assertTrue(TokenType.EndTag.isInstance(new EndTag()));
        assertTrue(TokenType.Comment.isInstance(new Comment()));
        assertTrue(TokenType.Character.isInstance(new Character()));
        assertTrue(TokenType.EOF.isInstance(new EOF()));
    }
}