import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "");
        
        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals("head", doc.head().tagName());
        Assert.assertEquals("title", doc.title().tagName());
        Assert.assertEquals("Test", doc.title().text());
        Assert.assertEquals("body", doc.body().tagName());
        Assert.assertEquals("Hello World", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1><p>This is a test.</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "");
        
        Assert.assertEquals("body", doc.tagName());
        Assert.assertEquals("Hello World", doc.select("h1").text());
        Assert.assertEquals("This is a test.", doc.select("p").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Hello World</h1><p>This is a test.</p>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "");
        
        Assert.assertEquals("body", doc.tagName());
        Assert.assertEquals("Hello World", doc.select("h1").text());
        Assert.assertEquals("This is a test.", doc.select("p").text());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        Document doc = Parser.parse(html, "");
        
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("<!-- This is a comment -->", doc.childNode(0).outerHtml());
        Assert.assertTrue(doc.childNode(0).toString().startsWith("<!--"));
        Assert.assertTrue(doc.childNode(0).toString().endsWith("-->"));
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[<h1>Hello World</h1>]]>";
        Document doc = Parser.parse(html, "");
        
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("<h1>Hello World</h1>", doc.childNode(0).outerHtml());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Document doc = Parser.parse(html, "");
        
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", doc.childNode(0).outerHtml());
    }

    @Test
    public void testParseStartTag() {
        String html = "<h1 class=\"header\">Hello World</h1>";
        Document doc = Parser.parse(html, "");
        
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("<h1 class=\"header\">Hello World</h1>", doc.childNode(0).outerHtml());
    }

    @Test
    public void testParseEndTag() {
        String html = "</h1>";
        Document doc = Parser.parse(html, "");
        
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("</h1>", doc.childNode(0).outerHtml());
    }

    @Test
    public void testParseTextNode() {
        String html = "This is a text node.";
        Document doc = Parser.parse(html, "");
        
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("This is a text node.", doc.childNode(0).outerHtml());
    }

    @Test
    public void testParseAttribute() {
        String html = "<div id=\"container\" class=\"wrapper\">";
        Document doc = Parser.parse(html, "");
        
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("<div id=\"container\" class=\"wrapper\"></div>", doc.childNode(0).outerHtml());
    }
}