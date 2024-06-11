import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.ParseError;

public class ParserTest {

    @Test
    public void testParseInput() {
        // Test with valid HTML input and baseUri
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(html, "http://www.example.com");
        Assert.assertEquals("Hello World", document.getElementsByTag("h1").text());

        // Test with empty input and baseUri
        html = "";
        document = parser.parseInput(html, "http://www.example.com");
        Assert.assertNull(document);

        // Test with invalid HTML input and baseUri
        html = "<html><body><h1><script>alert('XSS')</script></h1></body></html>";
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Error parsing start tag : script", errors.get(0).message());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertSame(new HtmlTreeBuilder(), treeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Assert.assertFalse(parser.isTrackErrors());

        parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);
        Assert.assertFalse(parser.isTrackErrors());

        parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        parser.parseInput("<html><body><script>alert('XSS')</script></body></html>", "http://www.example.com");
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Error parsing start tag : script", errors.get(0).message());
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = parser.settings();
        Assert.assertNotNull(settings);
    }

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document document = Parser.parse(html, "http://www.example.com");
        Assert.assertEquals("Hello World", document.getElementsByTag("h1").text());
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Hello World</h1>";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://www.example.com");
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Element);
        Assert.assertEquals("Hello World", nodes.get(0).text());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<xml><name>John</name></xml>";
        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, "http://www.example.com");
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Element);
        Assert.assertEquals("John", nodes.get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<html><body><h1>Hello World</h1></body></html>";
        Document document = Parser.parseBodyFragment(bodyHtml, "http://www.example.com");
        Assert.assertEquals("Hello World", document.getElementsByTag("h1").text());
    }

    @Test
    public void testUnescapeEntities() {
        String string = "&lt;hi&gt; &amp; &gt; &apos; &quot;";
        String unescaped = Parser.unescapeEntities(string, false);
        Assert.assertEquals("<hi> & & > ' \"", unescaped);
    }
}