import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Title</title></head><body><p>Hello, World!</p></body></html>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("Title", document.title());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, World!</p>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals(1, document.body().children().size());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals(1, document.childNodes().size());
    }

    // More test cases for other methods can be added here

}