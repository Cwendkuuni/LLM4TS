import org.jsoup.nodes.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ParserTest {

    // Test cases for parse() method

    @Test
    public void testParse_WhenValidHtml_ShouldReturnValidDocument() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);
        
        assertNotNull(document);
        assertNotNull(document.head());
        assertNotNull(document.title());
        assertNotNull(document.body());
        assertEquals("Test", document.title());
        assertEquals(1, document.select("h1").size());
        assertEquals("Hello World!", document.select("h1").first().text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_WhenNullHtml_ShouldThrowIllegalArgumentException() {
        String html = null;
        String baseUri = "https://www.example.com";
        Parser.parse(html, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_WhenNullBaseUri_ShouldThrowIllegalArgumentException() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = null;
        Parser.parse(html, baseUri);
    }

    // Test cases for parseBodyFragment() method

    @Test
    public void testParseBodyFragment_WhenValidBodyHtml_ShouldReturnValidDocument() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(document);
        assertNull(document.head());
        assertNotNull(document.body());
        assertEquals(1, document.select("h1").size());
        assertEquals("Hello World!", document.select("h1").first().text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragment_WhenNullBodyHtml_ShouldThrowIllegalArgumentException() {
        String bodyHtml = null;
        String baseUri = "https://www.example.com";
        Parser.parseBodyFragment(bodyHtml, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragment_WhenNullBaseUri_ShouldThrowIllegalArgumentException() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = null;
        Parser.parseBodyFragment(bodyHtml, baseUri);
    }

    // Test cases for parse Attribute() method

    @Test
    public void testParseAttribute_WhenValidAttribute_ShouldReturnValidAttribute() {
        Parser parser = new Parser(null, null, false);
        TokenQueue tq = parser.new TokenQueue("id='test'");
        String expectedKey = "id";
        String expectedValue = "test";
        
        Attribute attribute = parser.parseAttribute();
        
        assertNotNull(attribute);
        assertEquals(expectedKey, attribute.getKey());
        assertEquals(expectedValue, attribute.getValue());
    }

    @Test
    public void testParseAttribute_WhenNoAttributeValue_ShouldReturnValidAttributeWithEmptyValue() {
        Parser parser = new Parser(null, null, false);
        TokenQueue tq = parser.new TokenQueue("required");
        String expectedKey = "required";
        String expectedValue = "";
        
        Attribute attribute = parser.parseAttribute();
        
        assertNotNull(attribute);
        assertEquals(expectedKey, attribute.getKey());
        assertEquals(expectedValue, attribute.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseAttribute_WhenNullKey_ShouldThrowIllegalArgumentException() {
        Parser parser = new Parser(null, null, false);
        TokenQueue tq = parser.new TokenQueue("=test");
        parser.parseAttribute();
    }

    @Test
    public void testParseAttribute_WhenUnknownChar_ShouldReturnNullAttribute() {
        Parser parser = new Parser(null, null, false);
        TokenQueue tq = parser.new TokenQueue("test");
        
        Attribute attribute = parser.parseAttribute();
        
        assertNull(attribute);
    }

    // Test cases for parseTextNode() method

    @Test
    public void testParseTextNode_WhenValidText_ShouldReturnValidTextNode() {
        Parser parser = new Parser(null, null, false);
        TokenQueue tq = parser.new TokenQueue("Hello World!");
        String expectedText = "Hello World!";
        
        parser.parseTextNode();
        
        assertEquals(expectedText, parser.last().text());
    }

    // Test cases for parseCdata() method

    @Test
    public void testParseCdata_WhenValidCdata_ShouldReturnValidTextNode() {
        Parser parser = new Parser(null, null, false);
        TokenQueue tq = parser.new TokenQueue("<![CDATA[Hello World!]]>");
        String expectedText = "Hello World!";
        
        parser.parseCdata();
        
        assertEquals(expectedText, parser.last().text());
    }

    // Additional test cases

    @Test(timeout = 100)
    public void testLoopPerformance() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        for (int i = 0; i < 100000; i++) {
            Document document = Parser.parse(html, baseUri);
        }
    }

}