import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html>\n" +
                "<head>\n" +
                "<title>Test</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World!</h1>\n" +
                "<p>This is a test.</p>\n" +
                "</body>\n" +
                "</html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);

        // Check if the document is not null
        assertNotNull(doc);
        // Check if the document has the correct base URI
        assertEquals(baseUri, doc.baseUri());
        // Check if the document has the correct HTML
        String expectedHtml = "<html>\n" +
                " <head>\n" +
                "  <title>Test</title>\n" +
                " </head>\n" +
                " <body>\n" +
                "  <h1>Hello World!</h1>\n" +
                "  <p>This is a test.</p>\n" +
                " </body>\n" +
                "</html>";
        assertEquals(expectedHtml, doc.html());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>\n" +
                "<p>This is a test.</p>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        // Check if the document is not null
        assertNotNull(doc);
        // Check if the document has the correct base URI
        assertEquals(baseUri, doc.baseUri());
        // Check if the document has the correct HTML
        String expectedHtml = "<body>\n" +
                " <h1>Hello World!</h1>\n" +
                " <p>This is a test.</p>\n" +
                "</body>";
        assertEquals(expectedHtml, doc.html());
    }

    @Test
    public void testParseTextNode() {
        String text = "This is a text node";
        String baseUri = "https://www.example.com";
        Parser parser = new Parser("", baseUri, false);
        Element parent = new Element(org.jsoup.parser.Tag.valueOf("div"), baseUri);
        parser.parseTextNode(text, parent);

        // Check if the parent element has the correct text node
        assertEquals(text, parent.text());
    }

    @Test
    public void testParseCdata() {
        String cdata = "This is a CDATA section";
        String baseUri = "https://www.example.com";
        Parser parser = new Parser("", baseUri, false);
        Element parent = new Element(org.jsoup.parser.Tag.valueOf("div"), baseUri);
        parser.parseCdata(cdata, parent);

        // Check if the parent element has the correct CDATA section
        assertEquals(cdata, parent.ownText());
    }

    // Add more test methods for testing other methods in the Parser class
}