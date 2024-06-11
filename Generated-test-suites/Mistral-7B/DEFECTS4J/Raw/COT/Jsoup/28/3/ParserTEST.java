import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    @Test
    public void testParserConstructor() {
        Parser parser = new Parser(new TreeBuilder());
        Assert.assertNotNull(parser);
    }

    @Test
    public void testParseInput() {
        String html = "<html><body></body></html>";
        Parser parser = new Parser(new TreeBuilder());
        Document doc = parser.parseInput(html, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(doc.tagName(), "html");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputNullHtml() {
        Parser parser = new Parser(new TreeBuilder());
        parser.parseInput(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputNullBaseUri() {
        String html = "<html><body></body></html>";
        Parser parser = new Parser(new TreeBuilder());
        parser.parseInput(html, null);
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertEquals(parser.getTreeBuilder(), treeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new TreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeTreeBuilder);
        Assert.assertEquals(parser.getTreeBuilder(), newTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new TreeBuilder());
        Assert.assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(1);
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsZero() {
        Parser parser = new Parser(new TreeBuilder());
        parser.setTrackErrors(0);
        Assert.assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testGetErrorsEmpty() {
        Parser parser = new Parser(new TreeBuilder());
        List<ParseError> errors = parser.getErrors();
        Assert.assertTrue(errors.isEmpty());
    }

    @Test
    public void testParseInputWithErrors() {
        String html = "<html><body><script>"; // intentionally malformed HTML
        Parser parser = new Parser(new TreeBuilder());
        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertNotNull(errors.get(0).toString());
        Assert.assertNull(doc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputWithNegativeMaxErrors() {
        Parser parser = new Parser(new TreeBuilder());
        parser.setTrackErrors(-1);
        parser.parseInput("<html></html>", null);
    }

    @Test
    public void testStaticParseFunction() {
        String html = "<html><body></body></html>";
        Document doc = Parser.parse(html, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(doc.tagName(), "html");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStaticParseFunctionNullHtml() {
        Parser.parse(null, null);
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<p>Hello, World!</p>";
        Element context = Jsoup.parse("<html><body></body></html>").body();
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, null);
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Element);
        Assert.assertEquals("p", nodes.get(0).tagName());
        Assert.assertEquals("Hello, World!", nodes.get(0).text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFragmentNullFragmentHtml() {
        Element context = Jsoup.parse("<html><body></body></html>").body();
        Parser.parseFragment(null, context, null);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, World!</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, null);
        Element body = doc.body();
        Assert.assertEquals(1, body.childNodes().size());
        Assert.assertEquals("p", body.childNodes().get(0).tagName());
        Assert.assertEquals("Hello, World!", body.childNodes().get(0).text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentNullBodyHtml() {
        Parser.parseBodyFragment(null, null);
    }

    @Test
    public void testUnescapeHtmlEntities() {
        String input = "&lt;p&gt;Hello, &amp;World!&lt;/p&gt;";
        String output = Parser.unescapeHtmlEntities(input);
        Assert.assertEquals("<p>Hello, World!</p>", output);
    }

    @Test
    public void testParseBodyFragmentRelaxedDeprecated() {
        String bodyHtml = "<p>Hello, World!</p>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, null);
        Element body = doc.body();
        Assert.assertEquals(1, body.childNodes().size());
        Assert.assertEquals("p", body.childNodes().get(0).tagName());
        Assert.assertEquals("Hello, World!", body.childNodes().get(0).text());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput("<html><body></body></html>", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(doc.tagName(), "html");
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        String xml = "<root><child></child></root>";
        Document doc = parser.parseInput(xml, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(doc.tagName(), "root");
    }
}