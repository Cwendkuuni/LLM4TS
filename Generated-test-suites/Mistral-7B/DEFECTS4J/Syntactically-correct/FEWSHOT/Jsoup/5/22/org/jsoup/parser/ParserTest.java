package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

public class ParserTest {

    @Test
    public void testParseEmptyHtml() {
        Document doc = Parser.parse("<html></html>", "http://example.com");
        assertTrue(doc.body().childElementCount() == 0);
        assertTrue(doc.outputSettings().prettyPrint());
    }

    @Test
    public void testParseHtmlWithBody() {
        Document doc = Parser.parse("<html><body><p>Hello World</p></body></html>", "http://example.com");

        assertEquals(1, doc.childNodeCount());
        assertEquals(htmlTag, doc.tagName());

        Element body = doc.body();
        assertEquals(1, body.childNodeCount());
        assertEquals(bodyTag, body.tagName());

        Element p = body.firstChild();
        assertEquals(1, p.childNodeCount());
        assertEquals(tagName("p"), p.tagName());

        TextNode textNode = (TextNode) p.child(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseHtmlWithComments() {
        Document doc = Parser.parse("<html><!-- This is a comment --><body><comment><!-- -->This is another comment<!-- --></comment></body></html>", "http://example.com");

        assertEquals(1, doc.childNodeCount());
        assertEquals(htmlTag, doc.tagName());

        Element body = doc.body();
        assertEquals(1, body.childNodeCount());
        assertEquals(bodyTag, body.tagName());

        Comment comment = (Comment) body.child(0);
        assertEquals("This is a comment", comment.text());

        Element comment2 = body.child(1);
        assertEquals(1, comment2.childNodeCount());
        assertEquals(tagName("comment"), comment2.tagName());

        TextNode textNode = (TextNode) comment2.child(0);
        assertEquals("This is another comment", textNode.text());
    }

    @Test
    public void testParseHtmlWithCdata() {
        Document doc = Parser.parse("<html><![CDATA[ <p>Hello World</p> ]]> </html>", "http://example.com");

        assertEquals(1, doc.childNodeCount());
        assertEquals(htmlTag, doc.tagName());

        TextNode cdataNode = (TextNode) doc.child(0);
        assertEquals("<p>Hello World</p>", cdataNode.text());
    }

    @Test
    public void testParseHtmlWithXmlDeclaration() {
        Document doc = Parser.parse("<html xmlns:foo=\"http://example.com\" xml:lang=\"en\">Hello World</html>", "http://example.com");

        XmlDeclaration xmlDeclaration = doc.xml();
        assertEquals("xmlns:foo=\"http://example.com\" xml:lang=\"en\"", xmlDeclaration.xmlDecl().data());
    }

    @Test
    public void testParseHtmlWithAttributes() {
        Document doc = Parser.parse("<html <title>My Title</title>><body <class=\"myClass\"><p class=\"anotherClass\">Hello World</p></body></html>", "http://example.com");

        Element title = doc.title();
        assertEquals("My Title", title.text());
        Attribute titleAttribute = title.tag().attr("title");
        assertEquals("My Title", titleAttribute.value());

        Element body = doc.body();
        Attribute classAttribute = body.tag().attr("class");
        assertEquals("myClass", classAttribute.value());

        Element p = body.firstChild();
        assertEquals("Hello World", p.text());
        Attribute anotherClassAttribute = p.tag().attr("class");
        assertEquals("anotherClass", anotherClassAttribute.value());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>Hello World</p>", "http://example.com");

        assertEquals(1, doc.childNodeCount());
        assertEquals(htmlTag, doc.tagName());

        Element body = doc.body();
        assertEquals(1, body.childNodeCount());
        assertEquals(bodyTag, body.tagName());

        Element p = body.firstChild();
        assertEquals(1, p.childNodeCount());
        assertEquals(tagName("p"), p.tagName());

        TextNode textNode = (TextNode) p.child(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseBodyFragmentWithAttributes() {
        Document doc = Parser.parseBodyFragment("<p class=\"myClass\" id=\"myId\">Hello World</p>", "http://example.com");

        Element p = doc.body().firstChild();
        assertEquals("Hello World", p.text());
        Attribute classAttribute = p.tag().attr("class");
        assertEquals("myClass", classAttribute.value());
        Attribute idAttribute = p.tag().attr("id");
        assertEquals("myId", idAttribute.value());
    }

    @Test
    public void testParseBodyFragmentWithImplicitParents() {
        Document doc = Parser.parseBodyFragment("<img src=\"image.jpg\" />", "http://example.com");

        Element img = doc.body().firstChild();
        assertEquals("img", img.tagName());
        assertEquals("image.jpg", img.absUrl("src"));

        Element imgParent = img.parent();
        assertEquals("body", imgParent.tagName());
    }

    @Test
    public void testParseBodyFragmentWithRelaxedParsing() {
        Document doc = Parser.parseBodyFragmentRelaxed("<div><img src=\"image.jpg\"></div>", "http://example.com");

        Element div = doc.body().firstChild();
        assertEquals("div", div.tagName());

        Element img = div.firstChild();
        assertEquals("img", img.tagName());
        assertEquals("image.jpg", img.absUrl("src"));
    }

    private Tag tagName(String name) {
        try {
            return Tag.valueOf(name);
        } catch (IllegalArgumentException e) {
            return Tag.valueOf(name.toUpperCase());
        }
    }
}