import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("Test Page", document.title());
        Assert.assertEquals("Hello World", document.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragmentWithRelaxedParsing() {
        String bodyHtml = "<div><p>Paragraph 1</div></p>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("Paragraph 1", document.body().text());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals(1, document.childNodes().size());
        Assert.assertEquals("<!-- This is a comment -->", document.childNode(0).outerHtml());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version='1.0' encoding='UTF-8'?>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertTrue(document.childNodes().isEmpty());
    }

    @Test
    public void testParseEndTag() {
        String html = "<p>This is a paragraph</p>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("This is a paragraph", document.select("p").text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<div class='container'><p>This is a paragraph</p></div>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("container", document.select("div").attr("class"));
        Assert.assertEquals("This is a paragraph", document.select("p").text());
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>This is a <span>text</span> node</p>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("This is a text node", document.select("p").text());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[<h1>Hello World</h1>]]>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("<h1>Hello World</h1>", document.body().html());
    }

    @Test
    public void testParseAttribute() {
        String html = "<div id='container' class='main'></div>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("container", document.select("div").attr("id"));
        Assert.assertEquals("main", document.select("div").attr("class"));
    }

    @Test
    public void testParserWithNullHtml() {
        String baseUri = "http://example.com";
        
        try {
            Parser.parse(null, baseUri);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParserWithNullBaseUri() {
        String html = "<p>This is a paragraph</p>";
        
        try {
            Parser.parse(html, null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseBodyFragmentWithNullHtml() {
        String baseUri = "http://example.com";
        
        try {
            Parser.parseBodyFragment(null, baseUri);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseBodyFragmentWithNullBaseUri() {
        String bodyHtml = "<h1>Hello World</h1>";
        
        try {
            Parser.parseBodyFragment(bodyHtml, null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

}