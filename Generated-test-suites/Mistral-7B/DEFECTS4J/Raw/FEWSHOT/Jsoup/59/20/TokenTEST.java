package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.name("html");
        assertEquals("html", doctype.getName());
        doctype.reset();
        assertNull(doctype.getName());
    }

    @Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.name("html");
        assertEquals("html", doctype.getName());
        doctype.pubSysKey("DTD HTML 5");
        assertEquals("DTD HTML 5", doctype.getPubSysKey());
        doctype.publicIdentifier("HTML5 Public Identifier");
        assertEquals("HTML5 Public Identifier", doctype.getPublicIdentifier());
        doctype.systemIdentifier("/path/to/html5.dtd");
        assertEquals("/path/to/html5.dtd", doctype.getSystemIdentifier());
        doctype.forceQuirks(true);
        assertTrue(doctype.isForceQuirks());

        doctype.reset();
        assertNull(doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.name("div");
        assertEquals("div", tag.name());
        tag.reset();
        assertNull(tag.name());
    }

    @Test
    public void testTagNewAttribute() {
        Tag tag = new StartTag();
        tag.name("div");
        tag.newAttribute().name("class").value("red");
        assertEquals("class", tag.pendingAttributeName());
        assertEquals("red", tag.getAttributeValue());
        tag.newAttribute().name("id").value("myId");
        assertEquals("id", tag.pendingAttributeName());
        assertEquals("myId", tag.getAttributeValue());
        tag.attributes();
        assertEquals("class=red id=myId", tag.toString());
    }

    @Test
    public void testTagName() {
        Tag tag = new StartTag();
        tag.name("div");
        assertEquals("div", tag.name());
        tag.name("p");
        assertEquals("p", tag.name());
    }

    @Test
    public void testTagFinaliseTag() {
        Tag tag = new StartTag();
        tag.name("div");
        tag.newAttribute().name("class").value("red");
        tag.finaliseTag();
        assertEquals("div class=red", tag.toString());
    }

    @Test
    public void testTagSelfClosing() {
        Tag tag = new StartTag();
        tag.name("img").selfClosing(true);
        assertTrue(tag.isSelfClosing());

        tag.reset();
        tag.name("div");
        tag.selfClosing(true);
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagToString() {
        Tag tag = new StartTag();
        tag.name("div").newAttribute().name("class").value("red");
        assertEquals("<div class=red>", tag.toString());
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.data("This is a comment");
        assertEquals("This is a comment", comment.getData());
        comment.reset();
        assertNull(comment.getData());
    }

    @Test
    public void testCommentToString() {
        Comment comment = new Comment();
        comment.data("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data("A");
        assertEquals("A", character.getData());
        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testCharacterToString() {
        Character character = new Character();
        character.data("A");
        assertEquals("A", character.getData());
    }

    @Test
    public void testEOFReset() {
        EOF eof = new EOF();
        eof.reset();
    }

    @Test(expected = IllegalStateException.class)
    public void testTokenTypeMismatch() {
        StartTag tag = new StartTag();
        assertEquals(TokenType.StartTag, tag.type);

        Doctype doctype = new Doctype();
        assertEquals(TokenType.Doctype, doctype.type);

        tag.type = TokenType.Doctype;
    }

    @Test
    public void testTokenTypeEquals() {
        StartTag tagA = new StartTag();
        StartTag tagB = new StartTag();
        assertEquals(tagA.type, tagB.type);

        Doctype doctypeA = new Doctype();
        Doctype doctypeB = new Doctype();
        assertEquals(doctypeA.type, doctypeB.type);

        assertNotEquals(tagA.type, doctypeA.type);
        assertNotEquals(tagA.type, doctypeB.type);

        StartTag tagC = new StartTag();
        tagC.type(TokenType.Comment);
        assertSame(TokenType.Comment, tagC.type);
    }

    @Test
    public void testTokenTypeValues() {
        assertEquals(TokenType.Doctype, TokenType.valueOf("Doctype"));
        assertEquals(TokenType.StartTag, TokenType.valueOf("StartTag"));
        assertEquals(TokenType.EndTag, TokenType.valueOf("EndTag"));
        assertEquals(TokenType.Comment, TokenType.valueOf("Comment"));
        assertEquals(TokenType.Character, TokenType.valueOf("Character"));
        assertEquals(TokenType.EOF, TokenType.valueOf("EOF"));
    }

    // Add more test methods here for other Token methods and functionalities

}