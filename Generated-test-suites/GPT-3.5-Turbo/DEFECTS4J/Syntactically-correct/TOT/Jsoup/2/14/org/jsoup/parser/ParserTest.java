import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser();
    }

    @After
    public void tearDown() {
        parser = null;
    }

    @Test
    public void testParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(input, baseUri);
        
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals("Hello World", document.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseEmptyInput() {
        String input = "";
        String baseUri = "http://example.com";
        Document document = Parser.parse(input, baseUri);
        
        assertNotNull(document);
        assertNull(document.title());
        assertEquals("", document.body().text());
    }

    @Test
    public void testParseBodyFragment() {
        String input = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragment(input, baseUri);
        
        assertNotNull(document);
        assertNull(document.title());
        assertEquals("Hello World", document.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseBodyFragmentEmptyInput() {
        String input = "";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragment(input, baseUri);
        
        assertNotNull(document);
        assertNull(document.title());
        assertEquals("", document.body().text());
    }

    @Test
    public void testParseComment() {
        String input = "<!-- This is a comment -->";
        String baseUri = "http://example.com";
        Document document = Parser.parse(input, baseUri);
        
        assertNotNull(document);
        assertNull(document.title());
        assertEquals("<!-- This is a comment -->", document.body().html());
    }

    @Test
    public void testParseCdata() {
        String input = "<![CDATA[CDATA content]]>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(input, baseUri);
        
        assertNotNull(document);
        assertNull(document.title());
        assertEquals("<![CDATA[CDATA content]]>", document.body().html());
    }

    @Test
    public void testParseXmlDecl() {
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><body>Hello World</body></html>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(input, baseUri);
        
        assertNotNull(document);
        assertNull(document.title());
        assertEquals("Hello World", document.body().text());
    }

    @Test
    public void testParseEndTag() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(input, baseUri);
        
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals("Hello World", document.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseStartTag() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(input, baseUri);
        
        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals("Hello World", document.body().getElementsByTag("h1").text());
    }

}