import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.xml.XmlTreeBuilder;
import org.junit.Test;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("<html><body></body></html>", "baseUri");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseInputWithErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document doc = parser.parseInput("<html><body><script>alert('XSS')</script></body></html>", "baseUri");
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpected '<', expecting '<!DOCTYPE' or '<html'", errors.get(0).getMessage());
    }

    @Test
    public void testParseInputWithNoErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);
        Document doc = parser.parseInput("<html><body></body></html>", "baseUri");
        List<ParseError> errors = parser.getErrors();
        Assert.assertNull(errors);
    }

    @Test
    public void testParseInputWithEmptyString() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("", "baseUri");
        Assert.assertNotNull(doc);
        Assert.assertEquals(0, doc.numChildren());
    }

    @Test
    public void testParseInputWithNullBaseUri() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        try {
            parser.parseInput("<html></html>", null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputWithInvalidBaseUri() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        try {
            parser.parseInput("<html><base href='javascript:void(0)'><a href='/'></a></html>", "javascript:void(0)");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertSame(new HtmlTreeBuilder(), treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertSame(newTreeBuilder, treeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Assert.assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(1);
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsWithZero() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);
        Assert.assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsWithNegativeNumber() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        try {
            parser.setTrackErrors(-1);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        Assert.assertNull(errors);
    }

    @Test
    public void testParse() {
        Document doc = Parser.parse("<html><body></body></html>", "baseUri");
        Assert.assertNotNull(doc);
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullBaseUri() {
        Parser.parse("<html></html>", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidBaseUri() {
        Parser.parse("<html><base href='javascript:void(0)'><a href='/'></a></html>", "javascript:void(0)");
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Test</p>";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "baseUri");
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        Assert.assertTrue(node instanceof Element);
        Element p = (Element) node;
        Assert.assertEquals("Test", p.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "baseUri");
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.numChildren());
        Element p = (Element) doc.body().child(0);
        Assert.assertEquals("Test", p.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Test</p>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "baseUri");
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.numChildren());
        Element p = (Element) doc.body().child(0);
        Assert.assertEquals("Test", p.text());
    }

    @Test(expected = NullPointerException.class)
    public void testParseBodyFragmentWithNullBaseUri() {
        String bodyHtml = "<p>Test</p>";
        Parser.parseBodyFragment(bodyHtml, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentWithInvalidBaseUri() {
        String bodyHtml = "<p>Test</p>";
        Parser.parseBodyFragment(bodyHtml, "javascript:void(0)");
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertSame(new HtmlTreeBuilder(), treeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertSame(new XmlTreeBuilder(), treeBuilder);
    }
}