package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.LinkedList;

/**
 * Test class for Parser class.
 */
public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(html, "https://example.com");
        Document doc = parser.parse();

        Element root = doc.body();
        assertEquals("Hello World", root.getElementsByTag("h1").text());
        assertEquals("Test", doc.title().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Parser parser = new Parser(bodyHtml, "https://example.com");
        Document doc = parser.parseBodyFragment(bodyHtml, "https://example.com");

        Element root = doc.body();
        assertEquals("Hello World", root.getElementsByTag("h1").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><h1>Hello World</h1></div>"; // unknown tag is allowed to be self-closed in this case
        Parser parser = new Parser(bodyHtml, "https://example.com");
        Document doc = parser.parseBodyFragmentRelaxed(bodyHtml, "https://example.com");

        Element root = doc.body();
        assertEquals("Hello World", root.getElementsByTag("h1").text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        Parser parser = new Parser(null, "https://example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullBaseUri() {
        Parser parser = new Parser("<html></html>", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyBodyFragment() {
        Parser parser = new Parser("", "https://example.com");
    }

    @Test
    public void testParseStartTag() {
        Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "https://example.com");

        // parseStartTag() is not directly testable as it's a private method
        Document doc = parser.parse();

        Element root = doc.body();
        assertEquals("Hello World", root.getElementsByTag("h1").text());
    }

    @Test
    public void testParseEndTag() {
        Parser parser = new Parser("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "https://example.com");
        Document doc = parser.parse();

        Element root = doc.body();
        Element h1 = root.getElementsByTag("h1").get(0);
        parser.parseEndTag(); // parseEndTag() is not directly testable as it's a private method

        assertTrue(h1.hasAttr("id"));
        assertNull(h1.child(0));
    }

    @Test
    public void testParseComment() {
        Parser parser = new Parser("<html><!-- This is a comment --></html>", "https://example.com");
        Document doc = parser.parse();

        assertEquals(1, doc.body().child(0).childCount());
        assertEquals("This is a comment", doc.body().child(0).child(0).text());
    }

    @Test
    public void testParseXmlDecl() {
        Parser parser = new Parser("<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", "https://example.com");
        Document doc = parser.parse();

        assertEquals("http://www.w3.org/2001/XMLSchema-instance", doc.head().getElementsByTag("xml-decl").get(0).getAttribute("xmlns:xsi"));
    }

    @Test
    public void testParseTextNode() {
        Parser parser = new Parser("<html><body><p>This is a <textarea>test text</textarea> node.</p></body></html>", "https://example.com");
        Document doc = parser.parse();

        assertEquals(3, doc.body().childCount());
        assertEquals("This is a ", doc.body().child(0).text());
        assertEquals("test text", doc.body().child(1).child(0).text());
        assertEquals(" node.", doc.body().child(2).text());
    }

    @Test
    public void testParseCdata() {
        Parser parser = new Parser("<html><body><![CDATA[ <script>alert('Hello World');</script> ]]> </body></html>", "https://example.com");
        Document doc = parser.parse();

        assertEquals(1, doc.body().childCount());
        DataNode cdataNode = (DataNode) doc.body().child(0);
        assertEquals("<script>alert('Hello World');</script>", cdataNode.data());
    }

    @Test
    public void testParseAttribute() {
        Parser parser = new Parser("<html><body><div title='Test'></div></body></html>", "https://example.com");
        Document doc = parser.parse();

        Element div = doc.body().child(0);
        assertEquals("Test", div.getAttribute("title"));
    }

    @Test
    public void testParseEmptyElement() {
        Parser parser = new Parser("<img src=\"image.png\" />", "https://example.com");
        Document doc = parser.parse();

        Element img = doc.body().getElementsByTag("img").get(0);
        assertTrue(img.isEmpty());
    }

    @Test
    public void testParseInvalidTagName() {
        Parser parser = new Parser("<html><body><x></x></body></html>", "https://example.com");

        try {
            parser.parse();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseInvalidEndTag() {
        Parser parser = new Parser("<html><body><div><div></div></div></body></html>", "https://example.com");

        try {
            parser.parse();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseMixedContent() {
        Parser parser = new Parser("<html><body><p>This is a <b>test</b> <img src=\"image.png\" /> node.</p></body></html>", "https://example.com");
        Document doc = parser.parse();

        assertEquals(3, doc.body().childCount());
        TextNode textNode = (TextNode) doc.body().child(0);
        assertEquals("This is a ", textNode.text());
        Element bElement = doc.body().child(1);
        assertEquals("test", bElement.text());
        DataNode imgNode = (DataNode) doc.body().child(2);
        assertEquals("image.png", imgNode.data());
    }

    @Test
    public void testParseNestedTags() {
        Parser parser = new Parser("<html><body><p><b><i>This is a nested <a href=\"https://example.com\">link</a> tag.</i></b></p></body></html>", "https://example.com");
        Document doc = parser.parse();

        assertEquals(4, doc.body().childCount());
        TextNode textNode1 = (TextNode) doc.body().child(0);
        assertEquals("This is a ", textNode1.text());
        Element bElement = (Element) doc.body().child(1);
        assertEquals("This is a ", bElement.text());
        Element iElement = (Element) bElement.child(0);
        assertEquals("nested ", iElement.text());
        Element aElement = (Element) iElement.child(0);
        assertEquals("link", aElement.tagName());
        assertEquals("https://example.com", aElement.absUrl("href"));
        TextNode textNode2 = (TextNode) doc.body().child(3);
        assertEquals(" tag.", textNode2.text());
    }

    @Test
    public void testParseScriptTag() {
        Parser parser = new Parser("<html><body><script src=\"script.js\"></script></body></html>", "https://example.com");
        Document doc = parser.parse();

        DataNode scriptNode = (DataNode) doc.body().child(0);
        assertEquals("script.js", scriptNode.data());
    }

    @Test
    public void testParseStyleTag() {
        Parser parser = new Parser("<html><body><style type=\"text/css\">.test {color: red;}</style></body></html>", "https://example.com");
        Document doc = parser.parse();

        Element styleElement = doc.body().child(0);
        assertEquals("text/css", styleElement.attr("type"));
        Element styleSheet = styleElement.child(0);
        assertEquals("color: red;", styleSheet.data());
    }

    @Test
    public void testParseBaseTag() {
        Parser parser = new Parser("<html><head><base href=\"https://subdomain.example.com\"></base><body></body></head></html>", "https://example.com");
        Document doc = parser.parse();

        assertEquals("https://subdomain.example.com", doc.baseUri());
    }

    @Test
    public void testParseInvalidBaseTag() {
        Parser parser = new Parser("<html><head><base target=\"_blank\"></base><body></body></head></html>", "https://example.com");

        try {
            parser.parse();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}