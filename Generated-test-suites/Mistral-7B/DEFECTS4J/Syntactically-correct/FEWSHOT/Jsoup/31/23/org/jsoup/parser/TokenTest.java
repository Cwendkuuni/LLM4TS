package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        Token doctypeWithName = new Token.Doctype().name("html");
        Token doctypeWithNameAndPublicID = new Token.Doctype().name("html").publicIdentifier("_publicID");
        Token doctypeWithNamePublicIDSystemID = new Token.Doctype().name("html").publicIdentifier("_publicID").systemIdentifier("_systemID");
        Token doctypeWithNamePublicIDSystemIDForceQuirks = new Token.Doctype().name("html").publicIdentifier("_publicID").systemIdentifier("_systemID").forceQuirks(true);

        assertEquals("Doctype", doctype.tokenType());
        assertEquals("Doctype", doctypeWithName.tokenType());
        assertEquals("html", doctypeWithName.getName());
        assertEquals("html", doctypeWithName.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("_publicID", doctypeWithNamePublicID.getPublicIdentifier());
        assertEquals("_systemID", doctypeWithNamePublicIDSystemID.getSystemIdentifier());
        assertTrue(doctypeWithNamePublicIDSystemIDForceQuirks.isForceQuirks());
    }

    @Test
    public void testTag() {
        Token tag = new Token.Tag();
        Token tagWithName = new Token.Tag().name("div");
        Token tagWithNameAndAttributes = new Token.Tag().name("div").newAttribute().name("class").value("class1 class2");

        assertEquals("Tag", tag.tokenType());
        assertEquals("Tag", tagWithName.tokenType());
        assertEquals("div", tagWithName.name());
        assertEquals("div", tagWithName.name());
        assertNull(tag.getAttributes());
        assertNotNull(tagWithNameAndAttributes.getAttributes());
        assertEquals("class1 class2", tagWithNameAndAttributes.getAttributes().toString());
    }

    @Test
    public void testStartTag() {
        Token startTag = new Token.StartTag();
        Token startTagWithName = new Token.StartTag().name("div");
        Token startTagWithNameAndAttributes = new Token.StartTag().name("div").newAttribute().name("class").value("class1 class2");

        assertEquals("StartTag", startTag.tokenType());
        assertEquals("StartTag", startTagWithName.tokenType());
        assertEquals("div", startTagWithName.name());
        assertEquals("div", startTagWithName.name());
        assertNull(startTag.getAttributes());
        assertNotNull(startTagWithNameAndAttributes.getAttributes());
        assertEquals("div", startTagWithName.toString());
        assertEquals("<div class=\"class1 class2\">", startTagWithNameAndAttributes.toString());
    }

    @Test
    public void testEndTag() {
        Token endTag = new Token.EndTag();
        Token endTagWithName = new Token.EndTag().name("div");

        assertEquals("EndTag", endTag.tokenType());
        assertEquals("EndTag", endTagWithName.tokenType());
        assertEquals("div", endTagWithName.name());
        assertEquals("div", endTagWithName.name());
        assertEquals("</div>", endTagWithName.toString());
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        Token commentWithData = new Token.Comment().data("This is a comment");

        assertEquals("Comment", comment.tokenType());
        assertEquals("Comment", commentWithData.tokenType());
        assertEquals("", comment.getData());
        assertEquals("This is a comment", commentWithData.getData());
        assertEquals("<!-- -->", comment.toString());
        assertEquals("<!-- This is a comment -->", commentWithData.toString());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("Hello World!");
        Token characterWithData = new Token.Character("Hello World!");

        assertEquals("Character", character.tokenType());
        assertEquals("Character", characterWithData.tokenType());
        assertEquals("Hello World!", character.getData());
        assertEquals("Hello World!", characterWithData.getData());
        assertEquals("Hello World!", character.toString());
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();

        assertEquals("EOF", eof.tokenType());
        assertNull(eof.toString());
    }
}