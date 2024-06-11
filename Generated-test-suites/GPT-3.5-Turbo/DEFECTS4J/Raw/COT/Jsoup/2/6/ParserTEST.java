import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>Hello World</h1>\n" +
                " </body>\n" +
                "</html>", doc.outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>Hello World</h1>\n" +
                " </body>\n" +
                "</html>", doc.outerHtml());
    }
}