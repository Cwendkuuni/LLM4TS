import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser();
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<h1>Hello, World!</h1>";
        String baseUri = "http://example.com";
        Document doc = parser.parseBodyFragment(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.select("h1").text());
    }
}