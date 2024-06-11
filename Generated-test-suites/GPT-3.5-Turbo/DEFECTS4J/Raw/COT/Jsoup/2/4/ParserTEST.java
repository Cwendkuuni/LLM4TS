import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {
    
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("Hello World!", body.text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertNotNull(doc);
        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("Hello World!", body.text());
    }
    
    @Test
    public void testParseWithComment() {
        String html = "<!--This is a comment--><p>Hello World!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("Hello World!", body.text());
    }
    
    @Test
    public void testParseWithCDATA() {
        String html = "<![CDATA[<p>Hello World!</p>]]>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("<p>Hello World!</p>", body.text());
    }
    
    @Test
    public void testParseWithXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><p>Hello World!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("Hello World!", body.text());
    }
    
    @Test
    public void testParseEndTag() {
        String html = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("Hello World!", body.text());
    }
    
    @Test
    public void testParseStartTag() {
        String html = "<p>Hello World!</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("Hello World!", body.text());
    }
    
    @Test
    public void testParseTextNode() {
        String html = "Hello World!";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("Hello World!", body.text());
    }
    
    @Test
    public void testParseCdata() {
        String html = "<![CDATA[Hello World!]]>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("Hello World!", body.text());
    }
    
    @Test
    public void testParseAttribute() {
        String html = "<div class=\"test\"></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Element div = doc.selectFirst("div");
        Assert.assertNotNull(div);
        Assert.assertEquals("test", div.className());
    }
    
    @Test
    public void testParseAttributeWithWhitespace() {
        String html = "<div class=    \"test\"></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Element div = doc.selectFirst("div");
        Assert.assertNotNull(div);
        Assert.assertEquals("test", div.className());
    }
    
    @Test
    public void testParseAttributeWithoutValue() {
        String html = "<div class></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Element div = doc.selectFirst("div");
        Assert.assertNotNull(div);
        Assert.assertTrue(div.attributes().hasKey("class"));
    }
    
    @Test
    public void testParseAttributeWithInvalidChar() {
        String html = "<div class=\"%\"></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Element div = doc.selectFirst("div");
        Assert.assertNotNull(div);
        Assert.assertEquals("%", div.attr("class"));
    }
    
    @Test
    public void testParseNestedTags() {
        String html = "<div><p>Hello World!</p></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Element div = doc.selectFirst("div");
        Assert.assertNotNull(div);
        Element p = div.selectFirst("p");
        Assert.assertNotNull(p);
        Assert.assertEquals("Hello World!", p.text());
    }
    
    @Test
    public void testParseWithImplicitParent() {
        String html = "<li>Item 1</li><li>Item 2</li>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Element ul = doc.selectFirst("ul");
        Assert.assertNotNull(ul);
        Assert.assertEquals(2, ul.children().size());
    }
    
    @Test
    public void testParseWithBaseHref() {
        String html = "<base href=\"http://example.com/\"><a href=\"page.html\">Link</a>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Element base = doc.selectFirst("base");
        Assert.assertNotNull(base);
        Assert.assertEquals("http://example.com/", base.attr("href"));
        Element a = doc.selectFirst("a");
        Assert.assertNotNull(a);
        Assert.assertEquals("http://example.com/page.html", a.absUrl("href"));
    }
}