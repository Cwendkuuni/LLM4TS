import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParserTest {
    
    //Method: parse(String html, String baseUri)
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parse(html, baseUri);
        
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("Hello World", body.select("h1").text());
    }

    //Method: parseBodyFragment(String bodyHtml, String baseUri)
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "https://www.example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertNotNull(doc);
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("Hello World", body.select("h1").text());
    }

    //Method: parse()
    @Test
    public void testParse_Comment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "https://www.example.com";
        
        Document doc = new Parser(html, baseUri, false).parse();
        
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertEquals("<!-- This is a comment -->", doc.childNodes().get(0).outerHtml());
    }

    //Method: parse()
    @Test
    public void testParse_Cdata() {
        String html = "<![CDATA[ This is CDATA ]]>";
        String baseUri = "https://www.example.com";
        
        Document doc = new Parser(html, baseUri, false).parse();
        
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertEquals("<![CDATA[ This is CDATA ]]>", doc.childNodes().get(0).outerHtml());
    }

    //Method: parse()
    @Test
    public void testParse_XmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "https://www.example.com";
        
        Document doc = new Parser(html, baseUri, false).parse();
        
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", doc.childNodes().get(0).outerHtml());
    }

    //Method: parse()
    @Test
    public void testParse_EndTag() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        
        Document doc = new Parser(html, baseUri, false).parse();
        
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertEquals("<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>", doc.outerHtml());
    }

    //Method: parse()
    @Test
    public void testParse_StartTag() {
        String html = "<h1>Hello World</h1>";
        String baseUri = "https://www.example.com";
        
        Document doc = new Parser(html, baseUri, false).parse();
        
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertEquals("<h1>Hello World</h1>", doc.childNodes().get(0).outerHtml());
    }

    //Method: parse()
    @Test
    public void testParse_TextNode() {
        String html = "Hello World";
        String baseUri = "https://www.example.com";
        
        Document doc = new Parser(html, baseUri, false).parse();
        
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        assertEquals("Hello World", doc.childNodes().get(0).outerHtml());
    }

    //Method: parse()
    @Test
    public void testParse_BodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "https://www.example.com";
        
        Document doc = new Parser(bodyHtml, baseUri, true).parse();
        
        assertNotNull(doc);
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("<h1>Hello World</h1>", body.html());
    }
}