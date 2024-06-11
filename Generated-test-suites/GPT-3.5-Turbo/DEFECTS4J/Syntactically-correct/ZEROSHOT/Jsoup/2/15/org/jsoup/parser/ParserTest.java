import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
  
    private String html;
    private String baseUri;
    private boolean isBodyFragment;
    private Parser parser;
  
    @BeforeEach
    void setUp() {
        html = "<html><head><title>Test</title></head><body><div id='content'><h1>Hello World</h1></div></body></html>";
        baseUri = "http://www.example.com";
        isBodyFragment = false;
        parser = new Parser(html, baseUri, isBodyFragment);
    }

    @Test
    void testParse() {
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello World", doc.selectFirst("h1").text());
    }

    @Test
    void testParseInvalidHtml() {
        String invalidHtml = "<html><head><titlTest</title></head><body><div id='content'><h1>Hello World</h1></div></body></html>";
        parser = new Parser(invalidHtml, baseUri, isBodyFragment);
        Document doc = parser.parse();
        assertNull(doc);
    }

    @Test
    void testParseBodyFragment() {
        isBodyFragment = true;
        parser = new Parser(html, baseUri, isBodyFragment);
        Document doc = parser.parse();
        assertNotNull(doc);
        assertNull(doc.title());
        assertEquals("Hello World", doc.selectFirst("h1").text());
    }

    @Test
    void testParseComment() {
        String commentHtml = "<!-- This is a comment -->";
        parser = new Parser(commentHtml, baseUri, isBodyFragment);
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals(commentHtml, doc.html().trim());
    }

    @Test
    void testParseCdata() {
        String cdataHtml = "<![CDATA[This is CDATA]]>";
        parser = new Parser(cdataHtml, baseUri, isBodyFragment);
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals(cdataHtml, doc.html().trim());
    }

    @Test
    void testParseXmlDecl() {
        String xmlDeclHtml = "<?xml version='1.0' encoding='UTF-8'?>";
        parser = new Parser(xmlDeclHtml, baseUri, isBodyFragment);
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals(xmlDeclHtml, doc.html().trim());
    }

    @Test
    void testParseEndTag() {
        String endTagHtml = "<html><head><title>Test</title></head><body></body></html>";
        parser = new Parser(endTagHtml, baseUri, isBodyFragment);
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertNull(doc.selectFirst("body"));
    }

    @Test
    void testParseStartTag() {
        String startTagHtml = "<html><head><title>Test</title></head><body><div id='content'></div></body></html>";
        parser = new Parser(startTagHtml, baseUri, isBodyFragment);
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertNotNull(doc.selectFirst("div#content"));
    }

    @Test
    void testParseStartTagWithoutTagName() {
        String startTagHtml = "<html><head><title>Test</title></head><body><>Invalid Tag</div></body></html>";
        parser = new Parser(startTagHtml, baseUri, isBodyFragment);
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertNull(doc.selectFirst("div#content"));
        assertEquals("&lt;Invalid Tag", doc.body().text());
    }

    @Test
    void testParseAttribute() {
        String attributeHtml = "<div id='content' class='container'></div>";
        parser = new Parser(attributeHtml, baseUri, isBodyFragment);
        Document doc = parser.parse();
        assertNotNull(doc);
        assertNotNull(doc.selectFirst("div#content"));
        assertEquals("container", doc.selectFirst("div#content").className());
    }

    @Test
    void testParseText() {
        String textHtml = "<div id='content'>Hello World</div>";
        parser = new Parser(textHtml, baseUri, isBodyFragment);
        Document doc = parser.parse();
        assertNotNull(doc);
        assertNotNull(doc.selectFirst("div#content"));
        assertEquals("Hello World", doc.selectFirst("div#content").text());
    }

    @Test
    void testParseTextWithCDATA() {
        String textHtml = "<div id='content'><![CDATA[Hello World]]></div>";
        parser = new Parser(textHtml, baseUri, isBodyFragment);
        Document doc = parser.parse();
        assertNotNull(doc);
        assertNotNull(doc.selectFirst("div#content"));
        assertEquals("Hello World", doc.selectFirst("div#content").text());
    }

    @Test
    void testParseTextWithDataTag() {
        String textHtml = "<title>This is a <span>title</span></title>";
        parser = new Parser(textHtml, baseUri, isBodyFragment);
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("This is a title", doc.title());
    }

    @Test
    void testParseBaseTag() {
        String baseTagHtml = "<base href='http://example.com'>";
        parser = new Parser(baseTagHtml, baseUri, isBodyFragment);
        Document doc = parser.parse();
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }
}