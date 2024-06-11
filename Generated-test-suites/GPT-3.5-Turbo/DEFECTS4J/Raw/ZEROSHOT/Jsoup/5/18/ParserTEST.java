import org.junit.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello World", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<p>Hello World</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(html, baseUri);

        Assert.assertEquals("body", doc.tagName());
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("Hello World", doc.body().select("p").text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String html = "<p>Hello World</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(html, baseUri);

        Assert.assertEquals("body", doc.tagName());
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("Hello World", doc.body().select("p").text());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("<!-- This is a comment -->", doc.childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version='1.0' encoding='UTF-8'?>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", doc.childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseEndTag() {
        String html = "<p>Hello World</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("<p>Hello World</p>", doc.childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseStartTag() {
        String html = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("<h1>Hello World</h1>", doc.childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseTextNode() {
        String html = "Hello World";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("Hello World", doc.childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is CDATA]]>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("<![CDATA[This is CDATA]]>", doc.childNodes().get(0).outerHtml());
    }

    @Test
    public void testParseAttribute() {
        String html = "<p class='intro'>Hello World</p>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertEquals("html", doc.tagName());
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("<p class=\"intro\">Hello World</p>", doc.childNodes().get(0).outerHtml());
    }

    @Test
    public void testAddChildToParent() {
        Parser parser = new Parser("", "", true);
        Element parent = parser.addChildToParent(new Element(Tag.valueOf("p"), ""), false);
        Element child = parent.children().get(0);

        Assert.assertEquals("p", child.tagName());
        Assert.assertEquals("", child.baseUri());
        Assert.assertEquals(0, child.childNodes().size());
    }

    @Test
    public void testStackHasValidParent() {
        Parser parser = new Parser("", "", true);
        Tag childTag = Tag.valueOf("p");

        boolean result = parser.stackHasValidParent(childTag);

        Assert.assertTrue(result);
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Parser parser = new Parser("", "", true);
        Tag tag = Tag.valueOf("p");
        Element element = new Element(tag, "");

        Element result = parser.popStackToSuitableContainer(tag);

        Assert.assertEquals(element, result);
    }

    @Test
    public void testPopStackToClose() {
        Parser parser = new Parser("", "", true);
        Tag tag = Tag.valueOf("p");
        Element element = new Element(tag, "");

        Element result = parser.popStackToClose(tag);

        Assert.assertEquals(element, result);
    }

    @Test
    public void testLast() {
        Parser parser = new Parser("", "", true);
        Tag tag = Tag.valueOf("p");
        Element element = new Element(tag, "");

        parser.stack.add(element);

        Element result = parser.last();

        Assert.assertEquals(element, result);
    }
}