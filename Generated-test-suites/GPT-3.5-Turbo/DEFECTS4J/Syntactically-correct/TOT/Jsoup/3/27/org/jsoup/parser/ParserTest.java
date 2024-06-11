package org.jsoup.parser;

import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    private Parser parser;
    
    @Before
    public void setUp() {
        parser = new Parser("<html><head><title>Test Page</title></head><body><h1>Test Heading</h1><p>Test Paragraph</p></body></html>", "http://example.com");
    }

    @Test
    public void testParse() {
        Document doc = parser.parse();

        // Check if the document has the correct structure
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.title());
        assertNotNull(doc.body());
        assertEquals("Test Page", doc.title().text());
        assertEquals("Test Heading", doc.body().getElementsByTag("h1").text());
        assertEquals("Test Paragraph", doc.body().getElementsByTag("p").text());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<div>Test Fragment</div>", "http://example.com");

        // Check if the fragment is correctly parsed into the body
        assertNotNull(doc);
        assertNotNull(doc.body());
        assertEquals("Test Fragment", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragment("<h1>Test Heading</h1><p>Test Paragraph</p>", "http://example.com");

        // Check if the fragment is correctly parsed into the body and no implicit tags are created
        assertNotNull(doc);
        assertNotNull(doc.body());
        assertEquals("Test Heading", doc.body().getElementsByTag("h1").text());
        assertEquals("Test Paragraph", doc.body().getElementsByTag("p").text());
    }

    @Test
    public void testParseComment() {
        parser = new Parser("<!-- Test Comment -->", "http://example.com");
        Comment comment = (Comment) parser.parse().childNode(0);

        // Check if the comment is correctly parsed
        assertNotNull(comment);
        assertEquals(" Test Comment ", comment.getData());
    }

    @Test
    public void testParseCdata() {
        parser = new Parser("<![CDATA[Test Data]]>", "http://example.com");
        TextNode cdata = (TextNode) parser.parse().childNode(0);

        // Check if the CDATA section is correctly parsed
        assertNotNull(cdata);
        assertEquals("Test Data", cdata.text());
    }

    @Test
    public void testParseXmlDecl() {
        parser = new Parser("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "http://example.com");
        XmlDeclaration xmlDecl = (XmlDeclaration) parser.parse().childNode(0);

        // Check if the XML declaration is correctly parsed
        assertNotNull(xmlDecl);
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDecl.getWholeDeclaration());
    }

    @Test
    public void testParseEndTag() {
        parser = new Parser("</html>", "http://example.com");
        parser.parse();

        // Check if the stack is correctly popped to close the html tag
        assertTrue(parser.stack.isEmpty());
    }

    @Test
    public void testParseStartTag() {
        parser = new Parser("<div class=\"test\">Test Div</div>", "http://example.com");
        Document doc = parser.parse();

        // Check if the div element is correctly parsed and added to the body
        assertNotNull(doc);
        assertNotNull(doc.body());
        assertEquals("Test Div", doc.body().getElementsByClass("test").text());
    }

    @Test
    public void testParseStartTagWithInvalidParent() {
        parser = new Parser("<body><script>alert('Test');</script></body>", "http://example.com");
        Document doc = parser.parse();

        // Check if the script tag is wrapped in a text node and added to the body
        assertNotNull(doc);
        assertNotNull(doc.body());
        assertEquals("alert('Test');", doc.body().text());
    }
}