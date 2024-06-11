import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = parser.parseInput(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("Hello, World!", doc.body().select("p").text());
    }
    
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
    
    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTreeBuilder(new XmlTreeBuilder());
        
        assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
    
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        assertFalse(parser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        
        assertTrue(parser.isTrackErrors());
    }
    
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(3);
        parser.parseInput("invalid html", "http://example.com");
        
        assertEquals(3, parser.getErrors().size());
    }
    
    @Test
    public void testParse() {
        String html = "<html><body><p>Hello, World!</p></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("Hello, World!", doc.body().select("p").text());
    }
    
    // Additional test cases for other methods can be added here
    
}