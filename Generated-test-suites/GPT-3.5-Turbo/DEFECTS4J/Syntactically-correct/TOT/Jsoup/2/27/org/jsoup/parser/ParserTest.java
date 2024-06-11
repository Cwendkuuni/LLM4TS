import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1><p>Test Paragraph</p></body></html>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertEquals("Test Title", document.title());
        Assert.assertEquals("Test Heading", document.body().select("h1").text());
        Assert.assertEquals("Test Paragraph", document.body().select("p").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h2>Test Heading</h2><p>Test Paragraph</p>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertEquals("Test Heading", document.body().select("h2").text());
        Assert.assertEquals("Test Paragraph", document.body().select("p").text());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- Test Comment -->";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertEquals("<!-- Test Comment -->", document.body().html());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[Test CDATA]]>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertEquals("Test CDATA", document.body().html());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", document.body().html());
    }

    @Test
    public void testParseEndTag() {
        String html = "<p>Test Text</p>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertEquals("Test Text", document.body().select("p").text());
    }

    @Test
    public void testParseStartTag() {
        String html = "<div>Test Div</div>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertEquals("Test Div", document.body().select("div").text());
    }

    @Test
    public void testParseTextNode() {
        String html = "Test Text";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertEquals("Test Text", document.body().text());
    }

    @Test
    public void testParseAttribute() {
        String html = "<p class=\"test\" id=\"1\">Test Text</p>";
        String baseUri = "http://example.com";
        
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertEquals("test", document.body().select("p").attr("class"));
        Assert.assertEquals("1", document.body().select("p").attr("id"));
    }

    @Test
    public void testStackHasValidParent() {
        Parser parser = new Parser("", "http://example.com", false);
        
        Assert.assertFalse(parser.stackHasValidParent(Tag.valueOf("h1")));
        Assert.assertTrue(parser.stackHasValidParent(Tag.valueOf("body")));
        Assert.assertTrue(parser.stackHasValidParent(Tag.valueOf("h2")));
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Parser parser = new Parser("", "http://example.com", false);
        parser.stack.add(new Element(Tag.valueOf("html"), "http://example.com"));
        parser.stack.add(new Element(Tag.valueOf("body"), "http://example.com"));
        parser.stack.add(new Element(Tag.valueOf("div"), "http://example.com"));
        
        Element result = parser.popStackToSuitableContainer(Tag.valueOf("h2"));
        
        Assert.assertEquals(Tag.valueOf("body"), result.tag());
    }

    @Test
    public void testPopStackToClose() {
        Parser parser = new Parser("", "http://example.com", false);
        parser.stack.add(new Element(Tag.valueOf("html"), "http://example.com"));
        parser.stack.add(new Element(Tag.valueOf("body"), "http://example.com"));
        parser.stack.add(new Element(Tag.valueOf("div"), "http://example.com"));
        
        Element result = parser.popStackToClose(Tag.valueOf("body"));
        
        Assert.assertEquals(Tag.valueOf("div"), result.tag());
    }

    @Test
    public void testLast() {
        Parser parser = new Parser("", "http://example.com", false);
        parser.stack.add(new Element(Tag.valueOf("html"), "http://example.com"));
        parser.stack.add(new Element(Tag.valueOf("body"), "http://example.com"));
        parser.stack.add(new Element(Tag.valueOf("div"), "http://example.com"));
        
        Element result = parser.last();
        
        Assert.assertEquals(Tag.valueOf("div"), result.tag());
    }
}