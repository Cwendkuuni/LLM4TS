package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName().equals("doctype");

        Token resetDoctype = doctype.reset();
        resetDoctype.asDoctype().getName() == null;
    }

    @Test
    public void testDoctypeGetName() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName("html");
        assertEquals("html", doctype.asDoctype().getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().setPublicIdentifier("_public");
        assertEquals("_public", doctype.asDoctype().getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().setSystemIdentifier("/path/to/file.html");
        assertEquals("/path/to/file.html", doctype.asDoctype().getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().setForceQuirks(true);
        assertTrue(doctype.asDoctype().isForceQuirks());
    }

    // Add more test cases for methods in Doctype class as needed

    // StartTag tests follow

    // ...

    // EndTag tests follow

    // ...

    // Comment tests follow

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        comment.asComment().getData().equals("--comment--");

        Token resetComment = comment.reset();
        resetComment.asComment().getData() == null;
    }

    @Test
    public void testCommentgetData() {
        Token comment = new Token.Comment();
        comment.asComment().getData("--comment--");
        assertEquals("--comment--", comment.asComment().getData());
    }

    @Test
    public void testCommenttoString() {
        Token comment = new Token.Comment();
        comment.asComment().getData("--comment--");
        assertEquals("<!----comment---->", comment.toString());
    }

    // Character tests follow

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        character.asCharacter().getData().equals("c");

        Token resetCharacter = character.reset();
        resetCharacter.asCharacter().getData() == null;
    }

    @Test
    public void testCharactergetData() {
        Token character = new Token.Character();
        character.asCharacter().getData("c");
        assertEquals("c", character.asCharacter().getData());
    }

    @Test
    public void testCharactertoString() {
        Token character = new Token.Character();
        character.asCharacter().getData("c");
        assertEquals("c", character.toString());
    }

    // EOF tests follow

    @Test
    public void testEOFReset() {
        Token eof = new Token.EOF();

        Token resetEof = eof.reset();
        assertNull(resetEof);
    }

    // Add more test cases for methods in other classes as needed
}