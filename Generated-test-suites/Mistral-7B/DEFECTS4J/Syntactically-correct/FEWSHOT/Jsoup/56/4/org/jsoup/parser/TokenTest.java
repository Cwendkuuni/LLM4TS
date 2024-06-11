package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName("html");
        doctype.asDoctype().reset();
        assertNull(doctype.asDoctype().getName());
    }

    @Test
    public void testDoctypeGetValues() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName("html");
        doctype.asDoctype().getPublicIdentifier().append("public");
        doctype.asDoctype().setForceQuirks(true);

        assertEquals("html", doctype.asDoctype().getName());
        assertEquals("public", doctype.asDoctype().getPublicIdentifier().toString());
        assertTrue(doctype.asDoctype().isForceQuirks());

        doctype.reset();
        assertNull(doctype.asDoctype().getName());
        assertNull(doctype.asDoctype().getPublicIdentifier());
        assertFalse(doctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name("div");
        tag.asStartTag().attributes(new Attributes().put("class", "test"));
        tag.asStartTag().reset();

        assertNull(tag.asStartTag().tagName);
        assertNull(tag.asStartTag().attributes);
    }

    @Test
    public void testTagName() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name("div");

        assertEquals("div", tag.asStartTag().name());
    }

    @Test
    public void testTagNewAttribute() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name("img").attributes(new Attributes().put("src", "image.jpg").put("alt", "Alternate Text"));
        tag.asStartTag().newAttribute();
        tag.asStartTag().attributes().put("title", "Image Title");

        assertEquals("img", tag.asStartTag().tagName);
        assertEquals(3, tag.asStartTag().getAttributes().size());
        assertTrue(tag.asStartTag().getAttributes().containsKey("src"));
        assertTrue(tag.asStartTag().getAttributes().containsKey("alt"));
        assertTrue(tag.asStartTag().getAttributes().containsKey("title"));
        assertEquals("image.jpg", tag.asStartTag().getAttributes().get("src"));
        assertEquals("Alternate Text", tag.asStartTag().getAttributes().get("alt"));
        assertEquals("Image Title", tag.asStartTag().getAttributes().get("title"));
    }

    @Test
    public void testTagFinalizeTag() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name("p").attributes(new Attributes().put("class", "test"));
        tag.asStartTag().finaliseTag();

        assertNull(tag.asStartTag().pendingAttributeName);
        assertNull(tag.asStartTag().pendingAttributeValue);
        assertNull(tag.asStartTag().pendingAttributeValueS);
        assertFalse(tag.asStartTag().hasEmptyAttributeValue);
        assertFalse(tag.asStartTag().hasPendingAttributeValue);
    }

    @Test
    public void testTagNameAndAttributes() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name("p").nameAttr("id", "p1").attributes(new Attributes().put("class", "test").put("data-test", "123"));

        assertEquals("p", tag.asStartTag().tagName);
        assertEquals("p1", tag.asStartTag().getAttribute("id").getValue());
        assertEquals("test", tag.asStartTag().getAttribute("class").getValue());
        assertEquals("123", tag.asStartTag().getAttribute("data-test").getValue());
    }

    @Test
    public void testTagToString() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name("p").attributes(new Attributes().put("class", "test").put("data-test", "123"));

        assertEquals("<p class=\"test\" data-test=\"123\">", tag.toString());
    }

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        comment.asComment().getData().append("I'm a comment");
        comment.asComment().reset();

        assertNull(comment.asComment().getData());
        assertFalse(comment.asComment().isBogus());
    }

    @Test
    public void testCommentGetData() {
        Token comment = new Token.Comment();
        comment.asComment().getData().append("I'm a comment");

        assertEquals("I'm a comment", comment.asComment().getData().toString());
    }

    @Test
    public void testCommentToString() {
        Token comment = new Token.Comment();
        comment.asComment().getData().append("I'm a comment");

        assertEquals("<!--I'm a comment-->", comment.toString());
    }

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        character.asCharacter().data("Hello");
        character.asCharacter().reset();

        assertNull(character.asCharacter().getData());
    }

    @Test
    public void testCharactergetData() {
        Token character = new Token.Character();
        character.asCharacter().data("Hello");

        assertEquals("Hello", character.asCharacter().getData().toString());
    }

    @Test
    public void testCharacterToString() {
        Token character = new Token.Character();
        character.asCharacter().data("Hello");

        assertEquals("Hello", character.toString());
    }

    @Test
    public void testEOFReset() {
        Token eof = new Token.EOF();
        eof.reset();

        assertTrue(eof.isEOF());
    }
}