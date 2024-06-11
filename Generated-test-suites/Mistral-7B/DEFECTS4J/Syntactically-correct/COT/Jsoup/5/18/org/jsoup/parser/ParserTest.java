package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.LinkedList;

/**
 * Test cases for Parser class.
 */
public class ParserTest {
    // Test cases for static methods

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Title</title></head><body><div>Test Content</div></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.body();
        assertEquals("Test Content", root.child(0).text());
        assertEquals("Test Title", doc.title().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>Test Content</div>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
        Element root = doc.body();
        assertEquals("Test Content", root.child(0).text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div>Test Content</div>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://test.com");
        Element root = doc.body();
        assertEquals("Test Content", root.child(0).text());

        // Test for unknown self-closed tag
        String bodyHtml2 = "<test></test>";
        Document doc2 = Parser.parseBodyFragmentRelaxed(bodyHtml2, "http://test.com");
        Element root2 = doc2.body();
        assertTrue(root2.child(0).tag().isSelfClosing());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        Parser.parse(null, "http://test.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullBaseUri() {
        String html = "<html><head><title>Test Title</title></head><body><div>Test Content</div></body></html>";
        Parser.parse(html, null);
    }

    // Test cases for private methods

    @Test
    public void testParseStartTag() {
        Parser parser = new Parser("", "", false);
        parser.parseStartTag();
        Element root = parser.stack.getLast();
        assertEquals(Tag.valueOf("html"), root.tag());
    }

    @Test
    public void testParseEndTag() {
        Parser parser = new Parser("<html><body><div></div></body></html>", "", false);
        parser.parseEndTag();
        Element parent = parser.stack.getLast();
        assertEquals(Tag.valueOf("body"), parent.tag());
        assertEquals(Tag.valueOf("div"), parent.child(0).tag());
    }

    @Test
    public void testParseComment() {
        Parser parser = new Parser("<html><!-- Test Comment --></html>", "", false);
        parser.parseComment();
        assertEquals(Comment.class, parser.stack.getLast().getClass());
    }

    @Test
    public void testParseXmlDecl() {
        Parser parser = new Parser("<html xmlns=\"http://test.com\" xml:lang=\"en\"></html>", "", false);
        parser.parseXmlDecl();
        XmlDeclaration xmlDeclaration = (XmlDeclaration) parser.stack.getLast();
        assertEquals("http://test.com", xmlDeclaration.xmlns());
        assertEquals("en", xmlDeclaration.xmlLang());
    }

    @Test
    public void testParseTextNode() {
        Parser parser = new Parser("", "", false);
        parser.parseTextNode();
        TextNode textNode = (TextNode) parser.stack.getLast();
        assertNotNull(textNode.text());
    }

    @Test
    public void testParseCdata() {
        Parser parser = new Parser("<html><![CDATA[Test CData]]></html>", "", false);
        parser.parseCdata();
        TextNode textNode = (TextNode) parser.stack.getLast();
        assertEquals("<![CDATA[Test CData]]>", textNode.text());
    }

    @Test
    public void testParseInvalidHtml() {
        Parser parser = new Parser("", "", false);
        parser.parseStartTag(); // consume <
        parser.parseEndTag(); // consume >
        parser.parse(); // consume unexpected character
        assertFalse(parser.stack.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseUnknownTag() {
        Parser parser = new Parser("<html><test></test></html>", "", false);
        parser.parseStartTag();
        parser.parseEndTag();
        parser.parse();
        assertEquals(0, parser.stack.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingEndTag() {
        Parser parser = new Parser("<html><head></body></html>", "", false);
        parser.parse();
        assertEquals(1, parser.stack.size());
        assertNull(parser.stack.getLast().child(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidAttributeValue() {
        Parser parser = new Parser("<html <test='<script>alert(\"Hello\")'>></html>", "", false);
        parser.parseStartTag();
        parser.parseAttribute();
        parser.parseEndTag();
        parser.parse();
        assertEquals(1, parser.stack.size());
    }

    // Test cases for edge cases and exception handling

    @Test
    public void testParseEmptyHtml() {
        Parser parser = new Parser("", "", false);
        Document doc = parser.parse();
        assertEquals(1, doc.numChildren());
        assertEquals(Tag.valueOf("html"), doc.child(0).tag());
        assertTrue(doc.body().isEmpty());
    }

    @Test
    public void testParseHtmlWithBaseTag() {
        String html = "<html><head><base href=\"/test/\"><title>Test Title</title></head><body><div>Test Content</div></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        assertEquals("http://test.com/test/", doc.baseUri());
    }

    @Test
    public void testParseHtmlWithEmptyBody() {
        String html = "<html><head><title>Test Title</title></head><body></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        assertEquals(1, doc.numChildren());
        assertTrue(doc.body().isEmpty());
    }

    @Test
    public void testParseHtmlWithSelfClosedTags() {
        String html = "<html><head><title>Test Title</title></head><body><img src=\"image.png\" /></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Image img = (Image) doc.body().child(0);
        assertEquals("image.png", img.src());
        assertTrue(img.isEmpty());
    }

    @Test
    public void testParseHtmlWithImplicitParents() {
        String html = "<html><head><title>Test Title</title></head><body><script src=\"script.js\"></script></body></html>";
        Document doc = Parser.parseBodyFragmentRelaxed(html, "http://test.com");
        Element root = doc.body();
        assertEquals(1, root.numChildren());
        Script script = (Script) root.child(0);
        assertEquals("script.js", script.src());
    }

    @Test
    public void testParseHtmlWithInvalidAncestor() {
        String html = "<html><head><title>Test Title</title></head><body><ul><li><script src=\"script.js\"></script></li></ul></body></html>";
        Document doc = Parser.parseBodyFragmentRelaxed(html, "http://test.com");
        Element ul = doc.body().child(0);
        assertEquals(1, ul.numChildren());
        Script script = (Script) ul.child(0);
        assertEquals("script.js", script.src());

        // Check that the script was properly wrapped in a li tag
        assertEquals("li", ul.tag().name());
        ListItem li = (ListItem) ul.child(0);
        assertEquals(script, li.child(0));
    }

    @Test
    public void testParseHtmlWithImplicitParentInHead() {
        String html = "<html><head><title>Test Title</title><script src=\"script.js\"></script></head><body></body></html>";
        Document doc = Parser.parseBodyFragmentRelaxed(html, "http://test.com");
        Element head = doc.head();
        assertEquals(1, head.numChildren());
        Script script = (Script) head.child(0);
        assertEquals("script.js", script.src());

        // Check that the script was not added to the body
        Element body = doc.body();
        assertTrue(body.isEmpty());
    }

    @Test
    public void testParseHtmlWithImplicitParentInBody() {
        String html = "<html><head><title>Test Title</title></head><body><script src=\"script.js\"></script></body></html>";
        Document doc = Parser.parseBodyFragmentRelaxed(html, "http://test.com");
        Element body = doc.body();
        assertEquals(1, body.numChildren());
        Script script = (Script) body.child(0);
        assertEquals("script.js", script.src());

        // Check that the script was properly wrapped in a script tag
        assertEquals("script", script.tag().name());
    }

    @Test
    public void testParseHtmlWithMissingEndTag() {
        String html = "<html><head><title>Test Title</title></head><body><div>";
        try {
            Parser.parseBodyFragmentRelaxed(html, "http://test.com");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Check stack state
        Parser parser = new Parser(html, "http://test.com", true);
        assertEquals(1, parser.stack.size());
        Element div = parser.stack.getLast();
        assertEquals(Tag.valueOf("body"), div.parent().tag());
        assertTrue(div.isEmpty());
    }
}