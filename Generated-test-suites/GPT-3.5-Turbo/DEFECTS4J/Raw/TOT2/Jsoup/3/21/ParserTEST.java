import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;

public class ParserTest {
    
    private Document doc;
    
    @Before
    public void setUp() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        doc = Parser.parse(html, baseUri);
    }
    
    @Test
    public void testParse() {
        assertNotNull(doc);
        assertEquals("Test", doc.title());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h2>Fragment</h2><p>Body</p>";
        Document fragment = Parser.parseBodyFragment(bodyHtml, "");
        assertNotNull(fragment);
        assertNotNull(fragment.body());
        assertTrue(fragment.body().children().size() == 2);
    }
    
    @Test
    public void testParseRelaxed() {
        String relaxedHtml = "<html><div><span>Relaxed</span></div></html>";
        Document relaxedDoc = Parser.parseBodyFragment(relaxedHtml, "");
        assertNotNull(relaxedDoc);
        assertNotNull(relaxedDoc.body());
        assertTrue(relaxedDoc.body().children().size() == 1);
    }
    
    @Test
    public void testParseComment() {
        String commentHtml = "<!-- This is a comment -->";
        Document commentDoc = Parser.parseBodyFragment(commentHtml, "");
        assertNotNull(commentDoc);
        assertEquals(1, commentDoc.childNodes().size());
    }
    
    // Additional test cases for other methods would be added here
    
}