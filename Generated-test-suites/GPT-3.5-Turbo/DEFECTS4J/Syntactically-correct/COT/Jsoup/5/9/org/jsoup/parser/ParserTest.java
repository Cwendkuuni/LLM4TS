
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "https://example.com";

        Document parsedDoc = Parser.parse(html, baseUri);

        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>Hello, World!</h1>\n" +
                " </body>\n" +
                "</html>", parsedDoc.html());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "https://example.com";

        Document parsedDoc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>Hello, World!</h1>\n" +
                " </body>\n" +
                "</html>", parsedDoc.html());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "https://example.com";

        Document parsedDoc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <h1>Hello, World!</h1>\n" +
                " </body>\n" +
                "</html>", parsedDoc.html());
    }
}
