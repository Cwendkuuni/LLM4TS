package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());

        Token doctypeWithName = new Token.Doctype();
        doctypeWithName.asDoctype().name.append("html");
        assertEquals("Doctype[html]", doctypeWithName.tokenType());

        Token doctypeWithPublicIdentifier = new Token.Doctype();
        doctypeWithPublicIdentifier.asDoctype().publicIdentifier.append("_public");
        assertEquals("Doctype[html][_public]", doctypeWithPublicIdentifier.tokenType());

        Token doctypeWithSystemIdentifier = new Token.Doctype();
        doctypeWithSystemIdentifier.asDoctype().systemIdentifier.append("/path/to/file.html");
        assertEquals("Doctype[html][_public]/path/to/file.html", doctypeWithSystemIdentifier.tokenType());

        Token doctypeWithForceQuirks = new Token.Doctype();
        doctypeWithForceQuirks.asDoctype().forceQuirks = true;
        assertEquals("Doctype[html][_public]/path/to/file.html[forceQuirks=true]", doctypeWithForceQuirks.tokenType());
    }

    @Test
    public void testTag() {
        Token tag = new Token.StartTag();
        assertEquals("StartTag", tag.tokenType());

        Token tagWithName = new Token.StartTag();
        tagWithName.asStartTag().name.append("div");
        assertEquals("StartTag[div]", tagWithName.tokenType());

        // Test appending attributes
        Token tagWithAttributes = new Token.StartTag();
        tagWithAttributes.asStartTag().newAttribute();
        tagWithAttributes.asStartTag().appendAttributeName("class");
        tagWithAttributes.asStartTag().appendAttributeValue("container");
        tagWithAttributes.asStartTag().newAttribute();
        tagWithAttributes.asStartTag().appendAttributeName("id");
        tagWithAttributes.asStartTag().appendAttributeValue("main");
        assertEquals("StartTag[div][class='container'][id='main']", tagWithAttributes.tokenType());

        // Test self-closing tag
        Token selfClosingTag = new Token.StartTag();
        selfClosingTag.asStartTag().name.append("img");
        selfClosingTag.asStartTag().selfClosing = true;
        assertEquals("StartTag[img]", selfClosingTag.tokenType());

        // Test end tag
        Token endTag = new Token.EndTag();
        endTag.asEndTag().name.append("p");
        assertEquals("EndTag[p]", endTag.tokenType());
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());

        Token commentWithData = new Token.Comment();
        commentWithData.asComment().data.append("This is a comment.");
        assertEquals("Comment[This is a comment.]", commentWithData.tokenType());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("A");
        assertEquals("Character[A]", character.tokenType());

        Token characterWithMultipleChars = new Token.Character("ABC");
        assertEquals("Character[ABC]", characterWithMultipleChars.tokenType());
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
    }
}