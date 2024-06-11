
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {
    
    private String html;
    private String baseUri;
    
    @Before
    public void setup() {
        html = "<html>\n" +
                "<head>\n" +
                "<title>Test Page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>This is a test page</h1>\n" +
                "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. </p>\n" +
                "</body>\n" +
                "</html>";

        baseUri = "http://www.example.com/";
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse(html, baseUri);

        assertNotNull("Document should not be null", doc);
        assertEquals("Document should have the correct base URI", baseUri, doc.baseUri());
        assertEquals("Document should have the correct title", "Test Page", doc.title());
        assertEquals("Document should have the correct body", "<html>\n" +
                " <head>\n" +
                "  <title>Test Page</title> \n" +
                " </head> \n" +
                " <body> \n" +
                "  <h1>This is a test page</h1> \n" +
                "  <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. </p> \n" +
                " </body> \n" +
                "</html>", doc.body().outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>This is a test page</h1>\n" +
                "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. </p>\n";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull("Document should not be null", doc);
        assertEquals("Document should have the correct base URI", baseUri, doc.baseUri());
        assertEquals("Document should have an empty head", "", doc.head().outerHtml());
        assertEquals("Document should have the correct body", "<h1>This is a test page</h1> \n<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. </p>", doc.body().html());
    }

    @Test
    public void testParseWithInvalidHtml() {
        String invalidHtml = "<h1>This is a test page</h1>\n" +
                "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. ";
        Document doc = Parser.parse(invalidHtml, baseUri);

        assertNull("Document should be null for invalid HTML", doc);
    }
    
    @Test
    public void testParseWithEmptyHtml() {
        String emptyHtml = "";
        Document doc = Parser.parse(emptyHtml, baseUri);

        assertNull("Document should be null for empty HTML", doc);
    }
}

