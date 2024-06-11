package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head></head><body><div><p>foo</p></div></body></html>";
        Document parsed = Parser.parse(html, "http://example.com");

        Element root = parsed.body();
        assertEquals(htmlTag, root.tag());
        assertEquals("http://example.com", root.baseUri());

        Element htmlElement = parsed.head();
        assertEquals(htmlTag, htmlElement.tag());
        assertNull(htmlElement.htmlText());

        Element bodyElement = parsed.body();
        assertEquals(bodyTag, bodyElement.tag());
        assertNull(bodyElement.htmlText());

        Element divElement = bodyElement.child(0);
        assertEquals(divTag, divElement.tag());
        assertNull(divElement.htmlText());

        Element pElement = divElement.child(0);
        assertEquals(pTag, pElement.tag());
        assertEquals("foo", pElement.text());

        assertEquals(3, parsed.numChildren());
        assertEquals(3, root.numChildren());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>foo</p></div>";
        Document parsed = Parser.parseBodyFragment(bodyHtml, "http://example.com");

        Element root = parsed;
        assertEquals(bodyTag, root.tag());
        assertEquals("http://example.com", root.baseUri());

        Element divElement = root.child(0);
        assertEquals(divTag, divElement.tag());
        assertNull(divElement.htmlText());

        Element pElement = divElement.child(0);
        assertEquals(pTag, pElement.tag());
        assertEquals("foo", pElement.text());

        assertEquals(2, parsed.numChildren());
        assertEquals(2, root.numChildren());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>foo</p>";
        Document parsed = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");

        Element root = parsed;
        assertEquals(bodyTag, root.tag());
        assertEquals("http://example.com", root.baseUri());

        Element divElement = root.child(0);
        assertEquals(divTag, divElement.tag());
        assertNull(divElement.htmlText());

        Element pElement = divElement.child(0);
        assertEquals(pTag, pElement.tag());
        assertEquals("foo", pElement.text());

        assertEquals(2, parsed.numChildren());
        assertEquals(2, root.numChildren());

        // check that unclosed tag is not closed by the parser
        Element unclosedTagElement = new Element(new Tag("unclosed"), "http://example.com");
        root.appendChild(unclosedTagElement);
        assertEquals(3, parsed.numChildren());
        assertEquals(3, root.numChildren());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- this is a comment --><body></body></html>";
        Document parsed = Parser.parse(html, "http://example.com");

        Element root = parsed.body();
        Comment comment = root.child(0);
        assertEquals(Comment.class, comment.getClass());
        assertEquals(" this is a comment ", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<html xmlns:foo=\"http://example.com/foo\" xml:lang=\"en\">";
        Document parsed = Parser.parse(html, "http://example.com");

        Element root = parsed;
        XmlDeclaration xmlDeclaration = root.child(0);
        assertEquals(XmlDeclaration.class, xmlDeclaration.getClass());
        assertTrue(xmlDeclaration.isStandalone());
        assertEquals("http://example.com/foo", xmlDeclaration.namespaceUri());
        assertEquals("en", xmlDeclaration.xmlLang());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><div><p>foo</p></div></body></html>";
        Document parsed = Parser.parse(html, "http://example.com");

        Element root = parsed.body();
        Element divElement = root.child(0);
        assertEquals(divTag, divElement.tag());
        assertEquals(1, divElement.numChildren());

        Element pElement = divElement.child(0);
        assertEquals(pTag, pElement.tag());
        assertEquals("foo", pElement.text());

        assertEquals("</div>", divElement.tagName());
        assertSame(divElement, divElement.parent());

        assertEquals("</body>", root.tagName());
        assertSame(root, root.parent());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><div><p>foo</p></div></body></html>";
        Document parsed = Parser.parse(html, "http://example.com");

        Element root = parsed.body();
        Element divElement = new Element(new Tag("div"), "http://example.com");
        root.appendChild(divElement);

        Element pElement = new Element(new Tag("p"), "http://example.com");
        divElement.appendChild(pElement);

        pElement.text("foo");

        assertEquals(2, parsed.numChildren());
        assertEquals(2, root.numChildren());

        assertEquals("<div>", divElement.tagName());
        assertSame(root, divElement.parent());

        assertEquals("<p>", pElement.tagName());
        assertSame(divElement, pElement.parent());
    }

    @Test
    public void testParseAttribute() {
        String html = "<img src=\"image.png\" alt=\"image\" title=\"title\">";
        Document parsed = Parser.parse(html, "http://example.com");
        Element imgElement = parsed.select("img").first();

        assertEquals(3, imgElement.numAttrs());

        Attribute srcAttribute = imgElement.attr("src");
        assertEquals("image.png", srcAttribute.value());

        Attribute altAttribute = imgElement.attr("alt");
        assertEquals("image", altAttribute.value());

        Attribute titleAttribute = imgElement.attr("title");
        assertEquals("title", titleAttribute.value());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body><p>foo bar baz</p></body></html>";
        Document parsed = Parser.parse(html, "http://example.com");

        Element root = parsed.body();
        Element pElement = root.child(0);
        TextNode textNode = pElement.text();

        assertEquals("foo bar baz", textNode.text());
        assertEquals(3, textNode.numTextNodes());
    }

    @Test
    public void testParseCdata() {
        String html = "<html><body><script><![CDATA[alert('hello world');]]></script></body></html>";
        Document parsed = Parser.parse(html, "http://example.com");

        Element root = parsed.body();
        Element scriptElement = root.child(0);
        DataNode dataNode = scriptElement.child(0);

        assertEquals("alert('hello world');", dataNode.text());

        assertEquals("<script>", scriptElement.tagName());
        assertSame(root, scriptElement.parent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtml() {
        String html = "<html><body><script><![CDATA[alert";
        Parser.parse(html, "http://example.com");
    }

    @Test
    public void testParsingBaseTag() {
        String html = "<html><head><base href=\"/some/path\"><body></body></html>";
        Document parsed = Parser.parse(html, "http://example.com");

        assertEquals("/some/path", parsed.baseUri());
    }
}