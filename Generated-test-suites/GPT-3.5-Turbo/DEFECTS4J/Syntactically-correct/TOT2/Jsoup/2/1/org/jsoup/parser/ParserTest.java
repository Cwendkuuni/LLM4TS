import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Parser Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("Parser Test", doc.title());
        assertEquals("Hello World", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Fragment Test</p></div>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("Fragment Test", doc.body().select("p").text());
    }

    // Additional Edge Cases and Exception Handling Tests

    @Test
    public void testEmptyHtmlForParse() {
        String html = "";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertTrue(doc.body().children().isEmpty());
    }

    @Test
    public void testInvalidHtmlForParse() {
        String html = "<html><body><div></div>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals(0, doc.body().children().size());
    }

    @Test
    public void testMissingEndTagForParse() {
        String html = "<html><head><title>Missing End Tag</head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("Missing End Tag", doc.title());
    }

    @Test
    public void testInvalidBodyFragmentTagsForParseBodyFragment() {
        String bodyHtml = "<div><p>Fragment Test</div>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals(0, doc.body().children().size());
    }

    // Add more edge cases and exception handling tests as needed

}