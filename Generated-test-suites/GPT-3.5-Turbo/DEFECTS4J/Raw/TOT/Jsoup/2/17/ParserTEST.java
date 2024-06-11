import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Welcome!</h1><p>This is a test page.</p></body></html>";
        String baseUri = "https://www.example.com/";
        
        Document doc = Parser.parse(html, baseUri);

        // Test if the parsed document is not null
        assertNotNull(doc);

        // Test the parsed document structure
        assertEquals("<html> <head> <title> Test Page </title> </head> <body> <h1> Welcome! </h1> <p> This is a test page. </p> </body> </html>", doc.html());
        assertEquals("Test Page", doc.title());
        assertEquals("Welcome!", doc.select("h1").text());
        assertEquals("This is a test page.", doc.select("p").text());
        assertEquals("https://www.example.com/", doc.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Welcome!</h1><p>This is a test page.</p>";
        String baseUri = "https://www.example.com/";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        // Test if the parsed document is not null
        assertNotNull(doc);

        // Test the parsed document structure
        assertEquals("<body> <h1> Welcome! </h1> <p> This is a test page. </p> </body>", doc.html());
        assertEquals("Welcome!", doc.select("h1").text());
        assertEquals("This is a test page.", doc.select("p").text());
        assertEquals("https://www.example.com/", doc.baseUri());
    }

    @Test
    public void testParse_EmptyHTML() {
        String html = "";
        String baseUri = "https://www.example.com/";

        Document doc = Parser.parse(html, baseUri);

        // Test if the parsed document is not null
        assertNotNull(doc);

        // Test the parsed document structure
        assertEquals("<html> <head></head> <body></body> </html>", doc.html());
        assertEquals("", doc.title());
        assertEquals("", doc.select("h1").text());
        assertEquals("", doc.select("p").text());
        assertEquals("https://www.example.com/", doc.baseUri());
    }

    @Test
    public void testParseBodyFragment_EmptyHTML() {
        String bodyHtml = "";
        String baseUri = "https://www.example.com/";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        // Test if the parsed document is not null
        assertNotNull(doc);

        // Test the parsed document structure
        assertEquals("<body></body>", doc.html());
        assertEquals("", doc.select("h1").text());
        assertEquals("", doc.select("p").text());
        assertEquals("https://www.example.com/", doc.baseUri());
    }

    @Test
    public void testParse_NullHTML() {
        String baseUri = "https://www.example.com/";

        Document doc = Parser.parse(null, baseUri);

        // Test if the parsed document is null
        assertNull(doc);
    }

    @Test
    public void testParseBodyFragment_NullHTML() {
        String baseUri = "https://www.example.com/";

        Document doc = Parser.parseBodyFragment(null, baseUri);

        // Test if the parsed document is null
        assertNull(doc);
    }

    @Test
    public void testParse_InvalidHTML() {
        String html = "<html><head><title>Test Page<title></head><body><h1>Welcome!</h1><p>This is a test page.</p></body></html>";
        String baseUri = "https://www.example.com/";

        Document doc = Parser.parse(html, baseUri);

        // Test if the parsed document is null
        assertNull(doc);
    }

    @Test
    public void testParseBodyFragment_InvalidHTML() {
        String bodyHtml = "<h1>Welcome!<p>This is a test page.</p>";
        String baseUri = "https://www.example.com/";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        // Test if the parsed document is null
        assertNull(doc);
    }

}