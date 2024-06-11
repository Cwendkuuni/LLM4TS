import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser("<html><body><p>This is a test</p></body></html>", "https://www.example.com", false);
    }

    @Test
    public void testParse() {
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("<html><head></head><body><p>This is a test</p></body></html>", doc.toString());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<p>This is a test</p>", "https://www.example.com");
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("<html><head></head><body><p>This is a test</p></body></html>", doc.toString());
    }
}