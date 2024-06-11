import org.jsoup.nodes.Document;
import org.jsoup.parser.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    private Parser parser;
    
    @Test
    public void testParseInput() {
        parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><p>Hello, World!</p></body></html>";
        String baseUri = "https://www.example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("Hello, World!", doc.body().text());
    }
    
    @Test
    public void testSetTreeBuilder() {
        parser = new Parser(new XmlTreeBuilder());
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        
        parser.setTreeBuilder(newTreeBuilder);
        
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testSetTrackErrors() {
        parser = new Parser(new HtmlTreeBuilder());
        int maxErrors = 5;
        
        parser.setTrackErrors(maxErrors);
        
        assertTrue(parser.isTrackErrors());
        assertEquals(maxErrors, parser.getErrors().size());
    }
    
    // Additional tests for edge cases and exceptions can be added here
}