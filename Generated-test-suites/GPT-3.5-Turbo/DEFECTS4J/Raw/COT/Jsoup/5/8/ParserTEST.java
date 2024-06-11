import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser();
    }

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello world!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = parser.parse(html, baseUri);
        assertEquals("Test Page", document.title());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>This is a paragraph</p></div>";
        String baseUri = "http://example.com";
        Document document = parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals("<div>\n <p>This is a paragraph</p>\n</div>", document.body().outerHtml());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>This is a paragraph</p></div>";
        String baseUri = "http://example.com";
        Document document = parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        assertEquals("<div>\n <p>This is a paragraph</p>\n</div>", document.body().outerHtml());
    }

    @Test
    public void testParseComment() {
        String html = "<html><!-- This is a comment --></html>";
        String baseUri = "http://example.com";
        Document document = parser.parse(html, baseUri);
        assertEquals(1, document.select("comment").size());
    }

    @Test
    public void testParseEndTag() {
        String html = "<html><body><div>This is a div</div></body></html>";
        String baseUri = "http://example.com";
        Document document = parser.parse(html, baseUri);
        assertEquals("<div>This is a div</div>", document.select("body").html());
    }

    @Test
    public void testParseStartTag() {
        String html = "<html><body><h1>This is a heading</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = parser.parse(html, baseUri);
        assertEquals("<h1>This is a heading</h1>", document.select("body").html());
    }

    @Test
    public void testParseTextNode() {
        String html = "<html><body>Hello world!</body></html>";
        String baseUri = "http://example.com";
        Document document = parser.parse(html, baseUri);
        assertEquals("Hello world!", document.select("body").text());
    }
}