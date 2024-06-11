import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseError;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {
    // Test constructor
    @Test
    public void testParserConstructor() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Assert.assertNotNull(parser);
    }

    // Test parseInput method with valid HTML and baseUri
    @Test
    public void testParseInputValidHtml() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title().text());
        Assert.assertEquals("Hello World", doc.body().text());
    }

    // Test parseInput method with null html
    @Test(expected = IllegalArgumentException.class)
    public void testParseInputNullHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(null, "http://example.com");
    }

    // Test parseInput method with empty html
    @Test
    public void testParseInputEmptyHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("", "http://example.com");
        Assert.assertNull(doc);
    }

    // Test parseInput method with invalid html
    @Test(expected = org.jsoup.parser.Parser.MalformedHtmlException.class)
    public void testParseInputInvalidHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput("<html<title>Test</title>", "http://example.com");
    }

    // Test parseInput method with ParseError tracking
    @Test
    public void testParseInputTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.parseInput("<html<title>Test</title>", "http://example.com").getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Error parsing start tag : html", errors.get(0).toString());
    }

    // Test parseInput method with maxErrors set to 1
    @Test
    public void testParseInputMaxErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder()).setTrackErrors(1);
        List<ParseError> errors = parser.parseInput("<html<title>Test</title><p>Hello World</p>", "http://example.com").getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Error parsing start tag : html", errors.get(0).toString());
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        errors = parser.parseInput(html, "http://example.com").getErrors();
        Assert.assertEquals(0, errors.size());
    }

    // Test getTreeBuilder method
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertEquals(HtmlTreeBuilder.class, treeBuilder.getClass());
    }

    // Test parseInput method with static parse function
    @Test
    public void testParseStaticFunction() {
        Document doc = Parser.parse("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>", "http://example.com");
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title().text());
        Assert.assertEquals("Hello World", doc.body().text());
    }

    // Test parseFragment method
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello World</p>";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Element);
        Assert.assertEquals("Hello World", nodes.get(0).text());
    }

    // Test parseFragment method with null fragmentHtml
    @Test(expected = IllegalArgumentException.class)
    public void testParseFragmentNullFragmentHtml() {
        Element context = Jsoup.parse("<html><body></body></html>").body();
        String baseUri = "http://example.com";
        Parser.parseFragment(null, context, baseUri);
    }

    // Test parseFragment method with empty fragmentHtml
    @Test
    public void testParseFragmentEmptyFragmentHtml() {
        Element context = Jsoup.parse("<html><body></body></html>").body();
        String baseUri = "http://example.com";
        List<Node> nodes = Parser.parseFragment("", context, baseUri);
        Assert.assertEmpty(nodes);
    }

    // Test parseFragment method with ParseError tracking
    @Test
    public void testParseFragmentTrackErrors() {
        Element context = Jsoup.parse("<html><body></body></html>").body();
        String baseUri = "http://example.com";
        List<ParseError> errors = Parser.parseFragment("<p>Hello World</p>", context, baseUri).getErrors();
        Assert.assertEquals(0, errors.size());
        errors = Parser.parseFragment("<p>Hello<p>World</p>", context, baseUri).getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Error parsing end tag : p", errors.get(0).toString());
    }
}