import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParserTest {
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://www.example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        assertNotNull(document);
        assertEquals("Hello, World!", document.select("h1").first().text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>This is a body fragment</p></div>";
        String baseUri = "http://www.example.com";
        
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(document);
        assertEquals("This is a body fragment", document.select("p").first().text());
    }
    
    @Test
    public void testParseCDATA() {
        String cdata = "<![CDATA[This is CDATA section]]>";
        
        // Create a Parser instance and test the private method parseCdata()
    }
    
    // Add more test cases for other public methods and edge cases
    
}