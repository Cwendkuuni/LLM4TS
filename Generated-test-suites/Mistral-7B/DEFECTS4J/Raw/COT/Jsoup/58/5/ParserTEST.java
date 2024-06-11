import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());

        Element title = doc.title();
        Assert.assertNull(title);

        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testParseInputWithErrors() {
        String html = "<html><head><title><script>alert('XSS')</script></title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("<html><head><title><script>alert('XSS')</script></title></head><body><h1>Hello World</h1></body></html>", doc.html());

        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        Assert.assertEquals("Error parsing start tag : script\n" +
                "Position: 18:1 in: title", error.getMessage());
    }

    @Test
    public void testParseInputWithMaxErrorsReached() {
        String html = "<html><head><title><script>alert('XSS')</script></title></head>" +
                "<body><h1>Hello World</h1><script>alert('XSS')</script></body></html>";
        String baseUri = "http://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);

        Document doc1 = parser.parseInput(html, baseUri);
        List<ParseError> errors1 = parser.getErrors();
        Assert.assertEquals(1, errors1.size());

        parser.setTreeBuilder(new HtmlTreeBuilder()); // create a new tree builder to test with

        Document doc2 = parser.parseInput(html, baseUri);
        List<ParseError> errors2 = parser.getErrors();
        Assert.assertEquals(2, errors2.size());
        Assert.assertEquals(errors1.get(0), errors2.get(0));
    }

    @Test
    public void testStaticParse() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";

        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputNullHtml() {
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(null, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputNullBaseUri() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(html, null);
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello World</p>";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        String baseUri = "http://example.com";

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Element);
        Assert.assertEquals("Hello World", nodes.get(0).text());
    }

    @Test
    public void testParseFragmentWithErrors() {
        String fragmentHtml = "<p><script>alert('XSS')</script></p>";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        String baseUri = "http://example.com";

        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);

        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Error parsing start tag : script\n" +
                "Position: 3:1 in: p", errors.get(0).getMessage());

        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Element);
        Assert.assertFalse(nodes.get(0).text().isEmpty());
    }

    @Test
    public void testParseXmlFragment() {
        String fragmentXml = "<xml><item><name>Apple</name></item></xml>";
        String baseUri = "http://example.com";

        List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Element);
        Assert.assertEquals("Apple", nodes.get(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        String baseUri = "http://example.com";

        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("<html><head></head><body><p>Hello World</p></body></html>", doc.html());
    }

    @Test
    public void testUnescapeEntities() {
        String string = "&lt;p&gt;Hello World&amp; &nbsp; &lt;/p&gt;";
        String inAttribute = "true";

        String unescapedString = Parser.unescapeEntities(string, inAttribute);
        Assert.assertEquals("<p>Hello World &amp; &nbsp;</p>", unescapedString);

        string = "&lt;p&gt;Hello World&amp; &nbsp; &lt;/p&gt;";
        inAttribute = "false";

        unescapedString = Parser.unescapeEntities(string, inAttribute);
        Assert.assertEquals("<p>Hello World&amp; &nbsp;</p>", unescapedString);
    }
}