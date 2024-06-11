

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);
        
        assertEquals("Test", document.title());
        assertEquals("Hello World!", document.body().select("h1").text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Hello World!</p></div>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertEquals("Hello World!", document.body().select("p").text());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>Hello World!</p></div>";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertEquals("Hello World!", document.body().select("p").text());
    }

    // Additional test cases for potential edge cases and exception handling scenarios

    @Test
    public void testParse_emptyHtml_returnsEmptyDocument() {
        String html = "";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("", document.title());
        assertEquals("", document.text());
    }

    @Test
    public void testParse_emptyBaseUri_returnsDocumentWithEmptyBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "";
        Document document = Parser.parse(html, baseUri);

        assertEquals("", document.baseUri());
    }

    @Test
    public void testParse_invalidHtml_returnsPartialDocument() {
        String html = "<html><head><title>Test<title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Parser.parse(html, baseUri);

        assertNotNull(document);
        assertEquals("Test", document.title());
        assertEquals("Hello World!", document.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment_invalidBodyHtml_returnsPartialDocument() {
        String bodyHtml = "<div><p>Hello World!";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("Hello World!", document.body().text());
    }

    @Test
    public void testParseBodyFragmentRelaxed_invalidBodyHtml_returnsPartialDocument() {
        String bodyHtml = "<div><p>Hello World!";
        String baseUri = "http://example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(document);
        assertEquals("Hello World!", document.body().text());
    }
}

