import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    private Parser parser;
    private String html;
    private String baseUri;

    @Before
    public void setup() {
        html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        baseUri = "http://www.example.com";
        parser = new Parser(html, baseUri, false);
    }

    @Test
    public void testParse() {
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello World", doc.body().select("h1").text());
    }

    @Test
    public void testParse_comment() {
        html = "<!-- This is a comment -->";
        parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        assertEquals(1, doc.childNodes().size());
    }

    @Test
    public void testParse_cdata() {
        html = "<![CDATA[This is CDATA]]>";
        parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        assertEquals(1, doc.childNodes().size());
    }

    @Test
    public void testParse_xmlDecl() {
        html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello World", doc.body().select("h1").text());
    }

    @Test
    public void testParse_endTag() {
        html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        doc.body().append("<p>This is a test</p>");
        assertEquals(2, doc.body().children().size());
    }

    @Test
    public void testParse_startTag() {
        html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        doc.body().append("<p>This is a test</p>");
        assertEquals(2, doc.body().children().size());
    }

    @Test
    public void testParse_textNode() {
        html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();
        doc.body().text("This is a test");
        assertEquals("This is a test", doc.body().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Hello World", doc.body().select("h1").text());
    }
}