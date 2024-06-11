import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>";
        String baseUri = "https://www.example.com";

        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals(1, doc.body().children().size());
        assertEquals("p", doc.body().children().get(0).tagName());
        assertEquals("Hello, World!", doc.body().text());
    }

    // Other test cases for parseBodyFragment() and parseBodyFragmentRelaxed() methods should be added according to the requirements.

}