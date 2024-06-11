
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("<html>\n <head> \n  <title> Test </title> \n </head> \n <body> \n  <h1> Hello World </h1> \n </body>\n</html>", doc.toString());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("", doc.title());
        assertEquals("<h1> Hello World </h1>", doc.body().html());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertNotNull(doc);
        assertEquals("", doc.title());
        assertEquals("<h1> Hello World </h1>", doc.body().html());
    }
    
    @Test
    public void testParseComment() {
        String html = "<!--This is a comment-->";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("", doc.title());
        assertEquals("<!-- This is a comment -->", doc.toString());
    }
    
    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version='1.0' encoding='UTF-8'?>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("", doc.title());
        assertEquals("<<? xml version='1.0' encoding='UTF-8' ?>", doc.toString());
    }
    
    @Test
    public void testParseEndTag() {
        String html = "</title>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("", doc.title());
        assertEquals("</title>", doc.toString());
    }
    
    @Test
    public void testParseStartTag() {
        String html = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("", doc.title());
        assertEquals("<h1> Hello World </h1>", doc.body().html());
    }
    
    @Test
    public void testParseTextNode() {
        String html = "Hello World";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("", doc.title());
        assertEquals("Hello World", doc.body().text());
    }
    
    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is CDATA]]>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("", doc.title());
        assertEquals("<![CDATA[This is CDATA]]>", doc.toString());
    }
    
    @Test
    public void testParseAttribute() {
        String html = "<a href='http://example.com'>Link</a>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("", doc.title());
        assertEquals("<a href=\"http://example.com\"> Link </a>", doc.toString());
    }
}

