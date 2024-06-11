package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        Token doctypeWithName = new Token.Doctype().name("html");
        Token doctypeWithNameAndPublicID = new Token.Doctype()
                .name("html")
                .publicIdentifier("_publicID");
        Token doctypeWithNamePublicIDSystemID = new Token.Doctype()
                .name("html")
                .publicIdentifier("_publicID")
                .systemIdentifier("_systemID");
        Token doctypeWithForceQuirks = new Token.Doctype().name("html").forceQuirks(true);

        assertEquals("Doctype", doctype.tokenType());
        assertEquals("Doctype", doctypeWithName.tokenType());
        assertEquals("Doctype", doctypeWithNameAndPublicID.tokenType());
        assertEquals("Doctype", doctypeWithNamePublicIDSystemID.tokenType());
        assertEquals("Doctype", doctypeWithForceQuirks.tokenType());

        assertNull(doctype.getName());
        assertEquals("html", doctypeWithName.getName());
        assertEquals("html", doctypeWithNameAndPublicID.getName());
        assertEquals("html", doctypeWithNamePublicIDSystemID.getName());
        assertFalse(doctype.isForceQuirks());
        assertTrue(doctypeWithForceQuirks.isForceQuirks());
    }

    @Test
    public void testTag() {
        Token tag = new Token.Tag();
        Token tagWithName = new Token.Tag().name("div");
        Token tagWithNameAndAttributes = new Token.Tag().name("div")
                .attributes(new Attributes().put("class", "class1").put("id", "id1"));

        assertEquals("Tag", tag.tokenType());
        assertEquals("Tag", tagWithName.tokenType());
        assertEquals("Tag", tagWithNameAndAttributes.tokenType());

        assertNull(tag.tagName);
        assertEquals("div", tagWithName.tagName);
        assertEquals("div", tagWithNameAndAttributes.tagName);

        assertFalse(tag.isSelfClosing());
        assertFalse(tagWithName.isSelfClosing());
        assertFalse(tagWithNameAndAttributes.isSelfClosing());

        assertNull(tag.getAttributes());
        assertNotNull(tagWithNameAndAttributes.getAttributes());
        assertEquals(2, tagWithNameAndAttributes.getAttributes().size());
        assertEquals("class1", tagWithNameAndAttributes.getAttributes().get("class"));
        assertEquals("id1", tagWithNameAndAttributes.getAttributes().get("id"));
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        Token commentWithData = new Token.Comment().data("This is a comment.");

        assertEquals("Comment", comment.tokenType());
        assertEquals("Comment", commentWithData.tokenType());

        assertNull(comment.getData());
        assertEquals("This is a comment.", commentWithData.getData());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("A");
        Token characterWithData = new Token.Character("ABC");

        assertEquals("Character", character.tokenType());
        assertEquals("Character", characterWithData.tokenType());

        assertEquals("A", character.getData());
        assertEquals("ABC", characterWithData.getData());
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();

        assertEquals("EOF", eof.tokenType());
        assertTrue(eof.isEOF());
    }
}