package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.helper.Constants.Whitespace.SPACE;

import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Token.*;
import org.jsoup.parser.TokenType;

import java.util.Arrays;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeToken() {
        Doctype doctype = new Doctype();
        assertEquals("Doctype", doctype.tokenType());

        doctype.name("html");
        assertEquals("html", doctype.getName());

        doctype.pubSysKey("PUBLIC");
        doctype.publicIdentifier("-//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier("/HTML/DTD/HTML1.dtd");
        doctype.forceQuirks(true);

        assertEquals("PUBLIC", doctype.getPubSysKey());
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
        assertEquals("/HTML/DTD/HTML1.dtd", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());

        Doctype resetDoctype = doctype.reset();
        assertFalse(doctype.isForceQuirks());
        assertEquals("", doctype.getName());
        assertEquals(null, doctype.getPubSysKey());
        assertEquals(null, doctype.getPublicIdentifier());
        assertEquals(null, doctype.getSystemIdentifier());

        assertEquals(doctype, resetDoctype);
    }

    @org.junit.Test
    public void testTagToken() {
        Tag tag = new StartTag();
        assertEquals("StartTag", tag.tokenType().name());

        tag.name("html");
        tag.normalName("html");
        tag.selfClosing(false);

        assertEquals("html", tag.name());
        assertEquals("html", tag.normalName());
        assertFalse(tag.isSelfClosing());

        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("test");

        Attribute attribute = tag.getAttributes();
        assertEquals("class", attribute.keySet().iterator().next());
        assertEquals("test", attribute.get("class"));

        tag.newAttribute();
        tag.appendAttributeName("id");
        tag.appendAttributeValue("test2");

        assertTrue(tag.hasPendingAttributeValue());
        assertEquals("test2", tag.pendingAttributeValueS);

        tag.finaliseTag();
        assertEquals("class=test id=test2", tag.toString());

        tag = new EndTag();
        assertEquals("EndTag", tag.tokenType().name());

        tag.name("html");
        tag.normalName("html");

        assertEquals("html", tag.name());
        assertEquals("html", tag.normalName());

        tag.finaliseTag();
        assertEquals("</html>", tag.toString());
    }

    @org.junit.Test
    public void testCommentToken() {
        Comment comment = new Comment();
        assertEquals("Comment", comment.tokenType().name());

        comment.data("This is a comment");

        assertEquals("This is a comment", comment.getData());
        assertEquals("<!--This is a comment-->", comment.toString());

        Comment resetComment = comment.reset();
        assertEquals("", resetComment.getData());
        assertEquals("", resetComment.toString());

        assertEquals(comment, resetComment);
    }

    @org.junit.Test
    public void testCharacterToken() {
        Character character = new Character();
        assertEquals("Character", character.tokenType().name());

        character.data("Hello World!");

        assertEquals("Hello World!", character.getData());
        assertEquals("Hello World!", character.toString());

        Character resetCharacter = character.reset();
        assertEquals(null, resetCharacter.getData());
        assertEquals(null, resetCharacter.toString());

        assertEquals(character, resetCharacter);
    }

    @org.junit.Test
    public void testCDataToken() {
        CData cdata = new CData("<![CDATA[Hello World!]]>");
        assertEquals("CData", cdata.tokenType().name());

        assertEquals("<![CDATA[Hello World!]]>", cdata.toString());

        CData resetCdata = cdata.reset();
        assertEquals(null, resetCdata.getData());
        assertEquals("", resetCdata.toString());

        assertEquals(cdata, resetCdata);
    }

    @org.junit.Test
    public void testEOFToken() {
        EOF eof = new EOF();
        assertEquals("EOF", eof.tokenType().name());

        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
        assertFalse(eof.isCData());

        assertTrue(eof.isEOF());

        EOF resetEOF = eof.reset();
        assertTrue(eof.isEOF());

        assertEquals(eof, resetEOF);
    }

    @org.junit.Test
    public void testTokenTypeValues() {
        assertEquals(TokenType.values().length, 6);
        TokenType[] tokenTypes = TokenType.values();

        Arrays.stream(tokenTypes).forEach(tokenType -> {
            assertTrue(tokenType.name().length() > 0);
            assertTrue(tokenType.name().equals(tokenType.toString()));
        });

        assertTrue(TokenType.Character.isInstance(new Character()));
        assertTrue(TokenType.Comment.isInstance(new Comment()));
        assertTrue(TokenType.Doctype.isInstance(new Doctype()));
        assertTrue(TokenType.EndTag.isInstance(new EndTag()));
        assertTrue(TokenType.StartTag.isInstance(new StartTag()));
        assertTrue(TokenType.EOF.isInstance(new EOF()));
    }
}