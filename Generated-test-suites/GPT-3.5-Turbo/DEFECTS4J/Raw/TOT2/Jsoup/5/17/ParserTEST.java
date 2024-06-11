import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, World!</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Missing closing tag";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testParseComment() {
        // Edge case: Comment with special characters
        Parser parser = new Parser("<!--Special characters & symbols-->", "http://example.com", false);
        parser.parseComment();
        // Exception handling: Check if Comment node is added to the Document
        assertNotNull(parser.last().childNode(0));
    }

    @Test
    public void testParseXmlDecl() {
        // Exception handling: Check for invalid XML declaration
        Parser parser = new Parser("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "http://example.com", false);
        parser.parseXmlDecl();
        // Edge case: Check if XmlDeclaration node is added to the Document
        assertNotNull(parser.last().childNode(0));
    }

    @Test
    public void testParseEndTag() {
        // Edge case: Empty end tag
        Parser parser = new Parser("</>", "http://example.com", false);
        parser.parseEndTag();
        // Exception handling: Check if stack is empty after parsing empty end tag
        assertEquals(0, parser.stack.size());
    }

    @Test
    public void testParseStartTag() {
        // Edge case: Self-closed element
        Parser parser = new Parser("<img src=\"image.jpg\"/>", "http://example.com", false);
        parser.parseStartTag();
        // Check if Image element is added to the Document
        assertEquals("img", parser.last().tagName());
    }

    @Test
    public void testParseAttribute() {
        // Edge case: No value for attribute
        Parser parser = new Parser("<div id>", "http://example.com", false);
        assertNotNull(parser.parseAttribute());
    }

    @Test
    public void testParseTextNode() {
        // Edge case: Text containing special characters
        Parser parser = new Parser("Hello, < World!", "http://example.com", false);
        parser.parseTextNode();
        // Check if Text node is added to the Document
        assertEquals("Hello, <", parser.last().childNode(0).outerHtml());
    }

    @Test
    public void testParseCdata() {
        // Edge case: CDATA section
        Parser parser = new Parser("<![CDATA[<html>CDATA content</html>]]>", "http://example.com", false);
        parser.parseCdata();
        // Check if CDATA section is added to the Document
        assertNotNull(parser.last().childNode(0));
    }

}