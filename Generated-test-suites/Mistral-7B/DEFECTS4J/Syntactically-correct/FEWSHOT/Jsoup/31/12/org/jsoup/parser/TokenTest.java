package org.jsoup.parser;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());
        assertEquals("", doctype.asDoctype().getName());
        assertNull(doctype.asDoctype().getPublicIdentifier());
        assertNull(doctype.asDoctype().getSystemIdentifier());
        assertFalse(doctype.asDoctype().isForceQuirks());

        Token doctypeWithName = new Token.Doctype();
        doctypeWithName.asDoctype().getName().append("html");
        assertEquals("Doctype", doctypeWithName.tokenType());
        assertEquals("html", doctypeWithName.asDoctype().getName());
        assertNull(doctypeWithName.asDoctype().getPublicIdentifier());
        assertNull(doctypeWithName.asDoctype().getSystemIdentifier());
        assertFalse(doctypeWithName.asDoctype().isForceQuirks());

        Token doctypeWithNameAndPI = new Token.Doctype();
        doctypeWithNameAndPI.asDoctype().getName().append("html");
        doctypeWithNameAndPI.asDoctype().getPublicIdentifier().append("PUBLIC");
        doctypeWithNameAndPI.asDoctype().getSystemIdentifier().append("-//W3C//DTD HTML 4.01//EN");
        assertFalse(doctypeWithNameAndPI.asDoctype().isForceQuirks());

        Token doctypeWithNameAndPIAndQuirks = new Token.Doctype();
        doctypeWithNameAndPIAndQuirks.asDoctype().getName().append("html");
        doctypeWithNameAndPIAndQuirks.asDoctype().getPublicIdentifier().append("PUBLIC");
        doctypeWithNameAndPIAndQuirks.asDoctype().getSystemIdentifier().append("-//W3C//DTD HTML 4.01//EN");
        doctypeWithNameAndPIAndQuirks.asDoctype().isForceQuirks() = true;
    }

    @Test
    public void testTag() {
        Token tag = new Token.StartTag();
        assertEquals("StartTag", tag.tokenType());
        assertNull(tag.asStartTag().tagName);
        assertFalse(tag.asStartTag().isSelfClosing());
        assertNull(tag.asStartTag().getAttributes());

        Token tagWithName = new Token.StartTag();
        tagWithName.asStartTag().tagName = "div";
        assertEquals("StartTag", tagWithName.tokenType());
        assertEquals("div", tagWithName.asStartTag().tagName);
        assertFalse(tagWithName.asStartTag().isSelfClosing());
        assertNull(tagWithName.asStartTag().getAttributes());

        Token tagWithNameAndAttributes = new Token.StartTag();
        tagWithNameAndAttributes.asStartTag().tagName = "input";
        tagWithNameAndAttributes.asStartTag().newAttribute();
        tagWithNameAndAttributes.asStartTag().appendAttributeName("type");
        tagWithNameAndAttributes.asStartTag().appendAttributeValue("text");
        tagWithNameAndAttributes.asStartTag().newAttribute();
        tagWithNameAndAttributes.asStartTag().appendAttributeName("name");
        tagWithNameAndAttributes.asStartTag().appendAttributeValue("username");
        assertEquals("StartTag", tagWithNameAndAttributes.tokenType());
        assertEquals("input", tagWithNameAndAttributes.asStartTag().tagName);
        assertFalse(tagWithNameAndAttributes.asStartTag().isSelfClosing());
        assertNotNull(tagWithNameAndAttributes.asStartTag().getAttributes());
        assertEquals("type", tagWithNameAndAttributes.asStartTag().getAttributes().getKey(0).getName());
        assertEquals("text", tagWithNameAndAttributes.asStartTag().getAttributes().getValue(0).getValue());
        assertEquals("name", tagWithNameAndAttributes.asStartTag().getAttributes().getKey(1).getName());
        assertEquals("username", tagWithNameAndAttributes.asStartTag().getAttributes().getValue(1).getValue());

        Token tagWithNameAndAttributesAndSelfClosing = new Token.StartTag();
        tagWithNameAndAttributesAndSelfClosing.asStartTag().tagName = "img";
        tagWithNameAndAttributesAndSelfClosing.asStartTag().newAttribute();
        tagWithNameAndAttributesAndSelfClosing.asStartTag().appendAttributeName("src");
        tagWithNameAndAttributesAndSelfClosing.asStartTag().appendAttributeValue("image.jpg");
        tagWithNameAndAttributesAndSelfClosing.asStartTag().selfClosing = true;
        assertEquals("StartTag", tagWithNameAndAttributesAndSelfClosing.tokenType());
        assertEquals("img", tagWithNameAndAttributesAndSelfClosing.asStartTag().tagName);
        assertTrue(tagWithNameAndAttributesAndSelfClosing.asStartTag().isSelfClosing());
        assertNull(tagWithNameAndAttributesAndSelfClosing.asStartTag().getAttributes());
    }

    @Test
    public void testEndTag() {
        Token tag = new Token.EndTag();
        assertEquals("EndTag", tag.tokenType());
        assertNull(tag.asEndTag().tagName);
        assertEquals("", tag.asEndTag().toString());

        Token tagWithName = new Token.EndTag();
        tagWithName.asEndTag().tagName = "div";
        assertEquals("EndTag", tagWithName.tokenType());
        assertEquals("div", tagWithName.asEndTag().tagName);
        assertEquals("</div>", tagWithName.asEndTag().toString());
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());
        assertNull(comment.asComment().getData());
        assertEquals("<!-- -->", comment.asComment().toString());

        Token commentWithData = new Token.Comment();
        commentWithData.asComment().getData().append("This is a comment");
        assertEquals("Comment", commentWithData.tokenType());
        assertEquals("This is a comment", commentWithData.asComment().getData().toString());
        assertEquals("<!--This is a comment-->", commentWithData.asComment().toString());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("ABC");
        assertEquals("Character", character.tokenType());
        assertEquals("ABC", character.getData());
        assertEquals("ABC", character.toString());

        Token characterWithNullData = new Token.Character(null);
        assertNull(characterWithNullData.getData());
        assertNull(characterWithNullData.toString());
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
        assertEquals("", eof.toString());
    }
}