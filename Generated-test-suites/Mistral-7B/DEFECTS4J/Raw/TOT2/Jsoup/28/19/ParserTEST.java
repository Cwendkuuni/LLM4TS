import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);

        Assert.assertEquals("Hello World", doc.head().title().text());
    }

    @Test
    public void testParseInputWithTrackingErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><script>alert('Hello')</script></body></html>";
        String baseUri = "http://example.com";

        List<ParseError> errors = parser.parseInput(html, baseUri).getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Malformed HTML: Unexpected tag Name <script>", errors.get(0).getMessage());
    }

    @Test
    public void testStaticParse() {
        Document doc = Parser.parse("<html><body><h1>Hello World</h1></body></html>", "http://example.com");
        Assert.assertEquals("Hello World", doc.head().title().text());
    }

    @Test
    public void testStaticParseFragment() {
        List<Node> nodes = Parser.parseFragment("<div id=\"content\"><p>Hello World</p></div>", null, "http://example.com");
        Node node = nodes.get(0);
        Assert.assertEquals("div", node.tagName());
        Assert.assertEquals("content", node.id());
        Assert.assertEquals("Hello World", ((Element) node.child(0)).text());
    }

    @Test
    public void testStaticParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<body><h1>Hello World</h1></body>", "http://example.com");
        Assert.assertEquals("Hello World", doc.head().title().text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputNullHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String baseUri = "http://example.com";
        parser.parseInput(null, baseUri);
    }

    @Test
    public void testParseInputEmptyHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "";
        String baseUri = "http://example.com";
        Document doc = parser.parseInput(html, baseUri);
        Assert.assertEquals(0, doc.body().childNodeSize());
    }
}