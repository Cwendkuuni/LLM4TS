package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());

        Token doctypeWithName = new Token.Doctype();
        doctypeWithName.asDoctype().name("HTML");
        assertEquals("Doctype", doctypeWithName.tokenType());
        assertEquals("HTML", doctypeWithName.asDoctype().getName());

        Token doctypeWithPublicIdentifier = new Token.Doctype();
        doctypeWithPublicIdentifier.asDoctype().name("HTML");
        doctypeWithPublicIdentifier.asDoctype().publicIdentifier().append("PUBLIC");
        assertEquals("Doctype", doctypeWithPublicIdentifier.tokenType());
        assertEquals("HTML", doctypeWithPublicIdentifier.asDoctype().getName());
        assertEquals("PUBLIC", doctypeWithPublicIdentifier.asDoctype().getPublicIdentifier());

        Token doctypeWithSystemIdentifier = new Token.Doctype();
        doctypeWithSystemIdentifier.asDoctype().name("HTML");
        doctypeWithSystemIdentifier.asDoctype().publicIdentifier().append("PUBLIC");
        doctypeWithSystemIdentifier.asDoctype().systemIdentifier().append("http://example.com");
        assertEquals("Doctype", doctypeWithSystemIdentifier.tokenType());
        assertEquals("HTML", doctypeWithSystemIdentifier.asDoctype().getName());
        assertEquals("PUBLIC", doctypeWithSystemIdentifier.asDoctype().getPublicIdentifier());
        assertEquals("http://example.com", doctypeWithSystemIdentifier.asDoctype().getSystemIdentifier());

        Token doctypeWithForceQuirks = new Token.Doctype();
        doctypeWithForceQuirks.asDoctype().name("HTML");
        doctypeWithForceQuirks.asDoctype().publicIdentifier().append("PUBLIC");
        doctypeWithForceQuirks.asDoctype().systemIdentifier().append("http://example.com");
        doctypeWithForceQuirks.asDoctype().isForceQuirks(true);
        assertEquals("Doctype", doctypeWithForceQuirks.tokenType());
        assertEquals("HTML", doctypeWithForceQuirks.asDoctype().getName());
        assertEquals("PUBLIC", doctypeWithForceQuirks.asDoctype().getPublicIdentifier());
        assertEquals("http://example.com", doctypeWithForceQuirks.asDoctype().getSystemIdentifier());
        assertTrue(doctypeWithForceQuirks.asDoctype().isForceQuirks());

        Token docType = new Token.Doctype();
        docType.asDoctype().name("HTML");
        docType.asDoctype().publicIdentifier().append("PUBLIC");
        docType.asDoctype().systemIdentifier().append("http://example.com");
        docType.asDoctype().isForceQuirks(true);
        assertSame(docType, doctypeWithForceQuirks);
    }

    @Test
    public void testTag() {
        Token tag = new Token.StartTag();
        assertEquals("StartTag", tag.tokenType());

        Token tagWithName = new Token.StartTag();
        tagWithName.asStartTag().name("div");
        assertEquals("StartTag", tagWithName.tokenType());
        assertEquals("div", tagWithName.asStartTag().name());

        Token tagWithAttributes = new Token.StartTag();
        tagWithAttributes.asStartTag().name("a");
        tagWithAttributes.asStartTag().attributes().put(new Attribute("href", "http://example.com"));
        assertEquals("StartTag", tagWithAttributes.tokenType());
        assertEquals("a", tagWithAttributes.asStartTag().name());
        assertEquals(1, tagWithAttributes.asStartTag().getAttributes().size());
        assertEquals("href", tagWithAttributes.asStartTag().getAttributes().get("href").getName());
        assertEquals("http://example.com", tagWithAttributes.asStartTag().getAttributes().get("href").getValue());

        Token tagWithSelfClosing = new Token.StartTag();
        tagWithSelfClosing.asStartTag().name("img").selfClosing(true);
        assertEquals("StartTag", tagWithSelfClosing.tokenType());
        assertEquals("img", tagWithSelfClosing.asStartTag().name());
        assertTrue(tagWithSelfClosing.isSelfClosing());

        Token tagWithAttributesAndSelfClosing = new Token.StartTag();
        tagWithAttributesAndSelfClosing.asStartTag().name("input").selfClosing(true);
        tagWithAttributesAndSelfClosing.asStartTag().attributes().put(new Attribute("type", "text"));
        assertEquals("StartTag", tagWithAttributesAndSelfClosing.tokenType());
        assertEquals("input", tagWithAttributesAndSelfClosing.asStartTag().name());
        assertTrue(tagWithAttributesAndSelfClosing.isSelfClosing());
        assertEquals(1, tagWithAttributesAndSelfClosing.asStartTag().getAttributes().size());
        assertEquals("type", tagWithAttributesAndSelfClosing.asStartTag().getAttributes().get("type").getName());
        assertEquals("text", tagWithAttributesAndSelfClosing.asStartTag().getAttributes().get("type").getValue());

        Token tag1 = new Token.StartTag();
        tag1.asStartTag().name("div");
        Token tag2 = new Token.StartTag();
        tag2.asStartTag().name("p");
        tag2.asStartTag().attributes().put(new Attribute("class", "foo"));
        assertEquals(2, tag1.asStartTag().getNextSibling().tokenTypeCount());
        assertEquals("StartTag", tag1.asStartTag().getNextSibling(0).tokenType());
        assertEquals("div", tag1.asStartTag().getNextSibling(0).asStartTag().name());
        assertEquals("StartTag", tag1.asStartTag().getNextSibling(1).tokenType());
        assertEquals("p", tag1.asStartTag().getNextSibling(1).asStartTag().name());
        assertEquals("p", tag2.asStartTag().name());
        assertEquals("foo", tag2.asStartTag().getAttributes().get("class").getValue());

        Token tag = new Token.StartTag();
        tag.asStartTag().name("div");
        tag.asStartTag().attributes().put(new Attribute("class", "foo"));
        tag.asStartTag().attributes().put(new Attribute("id", "bar"));
        Element element = new Element("div");
        element.classes().add("foo");
        element.id("bar");
        assertSame(tag, element);
    }

    @Test
    public void testEndTag() {
        Token tag = new Token.EndTag();
        assertEquals("EndTag", tag.tokenType());

        Token tagWithName = new Token.EndTag();
        tagWithName.asEndTag().name("p");
        assertEquals("EndTag", tagWithName.tokenType());
        assertEquals("p", tagWithName.asEndTag().name());

        Token tagWithNameAndParent = new Token.EndTag();
        tagWithNameAndParent.asEndTag().name("p");
        tagWithNameAndParent.asEndTag().parent(new Token.StartTag().asStartTag().name("div"));
        assertEquals("EndTag", tagWithNameAndParent.tokenType());
        assertEquals("p", tagWithNameAndParent.asEndTag().name());
        assertEquals("div", tagWithNameAndParent.asEndTag().getParent().asStartTag().name());

        Token tag1 = new Token.StartTag().asStartTag().name("div");
        Token tag2 = new Token.EndTag().asEndTag().name("div");
        assertEquals(0, tag1.tokenTypeCount());
        assertEquals(1, tag2.tokenTypeCount());
        assertEquals(tag1, tag2.getPreviousSibling());

        Token tag = new Token.EndTag();
        tag.asEndTag().name("div");
        Token parentTag = new Token.StartTag().asStartTag().name("body");
        parentTag.asStartTag().attributes().put(new Attribute("id", "content"));
        tag.asEndTag().parent(parentTag);
        assertEquals("EndTag", tag.tokenType());
        assertEquals("div", tag.asEndTag().name());
        assertEquals("body", tag.asEndTag().getParent().asStartTag().name());
        assertEquals("content", parentTag.asStartTag().getAttributes().get("id").getValue());
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());

        Token commentWithData = new Token.Comment();
        commentWithData.asComment().data().append("This is a comment.");
        assertEquals("Comment", commentWithData.tokenType());
        assertEquals("This is a comment.", commentWithData.asComment().getData());

        Token comment = new Token.Comment();
        comment.asComment().data().append("--! [CDATA[");
        comment.asComment().data().append("<![CDATA[");
        comment.asComment().data().append("<html>");
        comment.asComment().data().append("</html>");
        comment.asComment().data().append("]]>");
        comment.asComment().data().append("--! ]]>");
        assertEquals("Comment", comment.tokenType());
        assertEquals("<![CDATA[<html>]]>", comment.asComment().getData());

        Token comment1 = new Token.Comment();
        comment1.asComment().data().append("--! [CDATA[");
        Token comment2 = new Token.Comment();
        comment2.asComment().data().append("<!--");
        Token comment3 = new Token.Comment();
        comment3.asComment().data().append("-->");
        Token comment4 = new Token.Comment();
        comment4.asComment().data().append("--! ]]>");
        assertEquals(4, comment1.tokenTypeCount());
        assertEquals(TokenType.Comment, comment1.tokenTypeAt(0).tokenType());
        assertEquals(comment1, comment1.tokenTypeAt(0));
        assertEquals(TokenType.Comment, comment1.tokenTypeAt(1).tokenType());
        assertEquals(comment2, comment1.tokenTypeAt(1));
        assertEquals(TokenType.Comment, comment1.tokenTypeAt(2).tokenType());
        assertEquals(comment3, comment1.tokenTypeAt(2));
        assertEquals(TokenType.Comment, comment1.tokenTypeAt(3).tokenType());
        assertEquals(comment4, comment1.tokenTypeAt(3));

        Token comment = new Token.Comment();
        comment.asComment().data().append("--! [CDATA[");
        comment.asComment().data().append("<![CDATA[");
        comment.asComment().data().append("<html>");
        comment.asComment().data().append("</html>");
        comment.asComment().data().append("]]>");
        comment.asComment().data().append("--! ]]>");
        Token commentWithData = new Token.Comment();
        commentWithData.asComment().data("This is a comment.");
        assertEquals(2, comment.tokenTypeCount());
        assertEquals(TokenType.Comment, comment.tokenTypeAt(0).tokenType());
        assertEquals(comment, comment.tokenTypeAt(0));
        assertEquals(TokenType.Comment, comment.tokenTypeAt(1).tokenType());
        assertEquals(commentWithData, comment.tokenTypeAt(1));
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("&lt;");
        assertEquals("Character", character.tokenType());
        assertEquals("&lt;", character.getData());

        Token character = new Token.Character('\u00A9');
        assertEquals("Character", character.tokenType());
        assertEquals("\u00A9", character.getData());

        Token character1 = new Token.Character('&');
        Token character2 = new Token.Character('l');
        Token character3 = new Token.Character('t;');
        assertEquals(3, character1.tokenTypeCount());
        assertEquals(TokenType.Character, character1.tokenTypeAt(0).tokenType());
        assertEquals(character1, character1.tokenTypeAt(0));
        assertEquals(TokenType.Character, character1.tokenTypeAt(1).tokenType());
        assertEquals(character2, character1.tokenTypeAt(1));
        assertEquals(TokenType.Character, character1.tokenTypeAt(2).tokenType());
        assertEquals(character3, character1.tokenTypeAt(2));
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());

        Token eof = new Token();
        assertEquals("EOF", eof.tokenType());

        Token startTag = new Token.StartTag().asStartTag().name("div");
        Token eof = new Token.EOF();
        assertEquals(1, startTag.tokenTypeCount());
        assertEquals(TokenType.StartTag, startTag.tokenTypeAt(0).tokenType());
        assertEquals(startTag, startTag.tokenTypeAt(0));
        assertEquals(0, eof.tokenTypeCount());
        assertNull(eof.getData());
    }
}