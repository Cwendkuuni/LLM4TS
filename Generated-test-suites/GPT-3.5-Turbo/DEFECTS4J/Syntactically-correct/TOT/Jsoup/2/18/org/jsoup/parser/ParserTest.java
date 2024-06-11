package org.jsoup.parser;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    // Test case for parse() method
    @Test
    public void testParse() {
        String html = "<html>" +
                        "<head>" +
                            "<title>Test Title</title>" +
                        "</head>" +
                        "<body>" +
                            "<h1>Test Heading</h1>" +
                            "<p>Test Paragraph</p>" +
                        "</body>" +
                     "</html>";

        String baseUri = "http://www.example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test Title", doc.title());
        assertEquals("Test Heading", doc.body().getElementsByTag("h1").text());
        assertEquals("Test Paragraph", doc.body().getElementsByTag("p").text());
    }

    // Test case for parseBodyFragment() method
    @Test
    public void testParseBodyFragment() {
        String html = "<div id=\"testDiv\">" +
                        "<p>Test Paragraph</p>" +
                     "</div>";

        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragment(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test Paragraph", doc.getElementById("testDiv").getElementsByTag("p").text());
    }

    // Test case for parseComment() method
    @Test
    public void testParseComment() {
        String html = "<div><!-- Test Comment --></div>";

        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragment(html, baseUri);

        assertNotNull(doc);
        assertEquals("<!-- Test Comment -->", doc.getElementById("testDiv").html());
    }

    // Test case for parseXmlDecl() method
    @Test
    public void testParseXmlDecl() {
        String html = "<div><?xml version=\"1.0\" encoding=\"UTF-8\"?><p>Test Paragraph</p></div>";

        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragment(html, baseUri);

        assertNotNull(doc);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", doc.getElementById("testDiv").html());
    }

    // Test case for parseEndTag() method
    @Test
    public void testParseEndTag() {
        String html = "<div><p>Test Paragraph</p></div>";

        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragment(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test Paragraph", doc.getElementById("testDiv").getElementsByTag("p").text());
    }

    // Test case for parseStartTag() method
    @Test
    public void testParseStartTag() {
        String html = "<div><p>Test Paragraph</p></div>";

        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragment(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test Paragraph", doc.getElementById("testDiv").getElementsByTag("p").text());
    }

    // Test case for parseTextNode() method
    @Test
    public void testParseTextNode() {
        String html = "<div>Test Text Node</div>";

        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragment(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test Text Node", doc.getElementById("testDiv").text());
    }

    // Test case for parseCdata() method
    @Test
    public void testParseCdata() {
        String html = "<div><![CDATA[Test CDATA]]></div>";

        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragment(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test CDATA", doc.getElementById("testDiv").text());
    }

    // Test case for addChildToParent() method
    @Test
    public void testAddChildToParent() {
        String html = "<div><p>Test Child</p></div>";

        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragment(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test Child", doc.getElementById("testDiv").getElementsByTag("p").text());
    }

    // Test case for stackHasValidParent() method
    @Test
    public void testStackHasValidParent() {
        String html = "<div><span>Test Parent</span><p>Test Child</p></div>";

        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragment(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test Parent", doc.getElementById("testDiv").getElementsByTag("span").text());
        assertEquals("Test Child", doc.getElementById("testDiv").getElementsByTag("p").text());
    }

    // Test case for popStackToSuitableContainer() method
    @Test
    public void testPopStackToSuitableContainer() {
        String html = "<div><p><span>Test Parent</span></p><p>Test Child</p></div>";

        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragment(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test Parent", doc.getElementById("testDiv").getElementsByTag("span").text());
        assertEquals("Test Child", doc.getElementById("testDiv").getElementsByTag("p").get(1).text());
    }

    // Test case for popStackToClose() method
    @Test
    public void testPopStackToClose() {
        String html = "<div><p><span>Test Paragraph</span></p></div>";

        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragment(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test Paragraph", doc.getElementById("testDiv").getElementsByTag("span").text());
    }

    // Test case for last() method
    @Test
    public void testLast() {
        String html = "<div><p><span>Test Paragraph</span></p></div>";

        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragment(html, baseUri);

        assertNotNull(doc);
        assertEquals("Test Paragraph", doc.getElementById("testDiv").getElementsByTag("span").text());
        assertEquals("Test Paragraph", ((Element)doc.last()).getElementsByTag("span").text());
    }
}