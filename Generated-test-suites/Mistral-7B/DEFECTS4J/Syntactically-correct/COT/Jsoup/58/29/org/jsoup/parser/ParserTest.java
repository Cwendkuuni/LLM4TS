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

    private Parser parser;

    @Test
    public void testParserConstructor() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
        Assert.assertNotNull(parser);
    }

    @Test
    public void testParseInputNullHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.parseInput(null, "baseUri");
        Assert.assertNotNull(errors);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("HTML document must not be null", errors.get(0).getMessage());
    }

    @Test
    public void testParseInputEmptyHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.parseInput("", "baseUri");
        Assert.assertNotNull(errors);
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testParseInputValidHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput("<html><body><h1>Hello World</h1></body></html>", "baseUri");
        Assert.assertNotNull(document);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputNegativeMaxErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(-1);
        List<ParseError> errors = parser.parseInput("<html><body></body></html>", "baseUri");
        Assert.assertEquals(0, errors.size()); // this assertion will not be reached as an exception is thrown
    }

    @Test
    public void testParseInputMaxErrorsReached() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        List<ParseError> errors = parser.parseInput("<html><body><invalid tag></body></html>", "baseUri");
        Assert.assertNotNull(errors);
        Assert.assertEquals(1, errors.size());
        Assert.assertNotNull(errors.get(0).getLine());
        Assert.assertNotNull(errors.get(0).getColumn());
        Assert.assertNotNull(errors.get(0).getLineNumber());
        Assert.assertNotNull(errors.get(0).getRowNumber());
        Assert.assertNotNull(errors.get(0).getSource());
        Assert.assertNotNull(errors.get(0).getMessage());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertSame(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        Assert.assertSame(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Assert.assertFalse(parser.isTrackErrors());
        parser.setTrackErrors(1);
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsZero() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(0);
        Assert.assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsPositive() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Assert.assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrorsNull() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();
        Assert.assertNull(errors);
    }

    @Test
    public void testGetErrorsWithErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        List<ParseError> errors = parser.parseInput("<html><body><invalid tag></body></html>", "baseUri").getErrors();
        Assert.assertNotNull(errors);
        Assert.assertEquals(1, errors.size());
        // assertions on errors similar to testParseInputMaxErrorsReached()
    }

    @Test
    public void testSettings() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings settings = parser.settings();
        Assert.assertNotNull(settings);
    }

    @Test
    public void testSettingsSet() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        ParseSettings newSettings = new ParseSettings();
        parser.settings(newSettings);
        Assert.assertSame(newSettings, parser.settings());
    }

    @Test
    public void testParseStaticParse() {
        List<Node> nodes = Parser.parseFragment("<div><p>Hello World</p></div>", null, "baseUri");
        Element div = (Element) nodes.get(0);
        Element p = (Element) nodes.get(1);
        Assert.assertEquals("div", div.tagName());
        Assert.assertEquals("p", p.tagName());
        Assert.assertEquals("Hello World", p.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStaticParseNullHtml() {
        List<Node> nodes = Parser.parseFragment(null, null, "baseUri");
        // this assertion will not be reached as an exception is thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStaticParseNullContext() {
        List<Node> nodes = Parser.parseFragment("<div></div>", null, "baseUri", null);
        // this assertion will not be reached as an exception is thrown
    }

    @Test(expected = NullPointerException.class)
    public void testParseStaticParseNullBaseUri() {
        List<Node> nodes = Parser.parseFragment("<div></div>", null, null);
        // this assertion will not be reached as a NullPointerException is thrown
    }

    @Test
    public void testParseStaticParseWithErrors() {
        List<ParseError> errors = Parser.parseFragment("<html><body><invalid tag></body></html>", null, "baseUri", ParseErrorList.tracking(1));
        Assert.assertNotNull(errors);
        Assert.assertEquals(1, errors.size());
        // assertions on errors similar to testParseInputMaxErrorsReached()
    }

    @Test
    public void testParseXmlFragment() {
        List<Node> nodes = Parser.parseXmlFragment("<xml><tag>Hello World</tag></xml>", "baseUri");
        Element tag = (Element) nodes.get(0);
        Assert.assertEquals("tag", tag.tagName());
        Assert.assertEquals("Hello World", tag.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlFragmentNullHtml() {
        List<Node> nodes = Parser.parseXmlFragment(null, "baseUri");
        // this assertion will not be reached as an exception is thrown
    }

    @Test(expected = NullPointerException.class)
    public void testParseXmlFragmentNullBaseUri() {
        List<Node> nodes = Parser.parseXmlFragment("<xml></xml>", null);
        // this assertion will not be reached as a NullPointerException is thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlFragmentInvalidXml() {
        List<Node> nodes = Parser.parseXmlFragment("<html><tag></html>", "baseUri");
        // this assertion will not be reached as an exception is thrown
    }

    @Test
    public void testParseBodyFragment() {
        Document document = Parser.parseBodyFragment("<body><p>Hello World</p></body>", "baseUri");
        Element p = (Element) document.body().child(0);
        Assert.assertEquals("p", p.tagName());
        Assert.assertEquals("Hello World", p.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentNullHtml() {
        Document document = Parser.parseBodyFragment(null, "baseUri");
        // this assertion will not be reached as an exception is thrown
    }

    @Test(expected = NullPointerException.class)
    public void testParseBodyFragmentNullBaseUri() {
        Document document = Parser.parseBodyFragment("<body></body>", null);
        // this assertion will not be reached as a NullPointerException is thrown
    }

    @Test
    public void testParseBodyFragmentWithErrors() {
        List<ParseError> errors = Parser.parseBodyFragment("<html><body><invalid tag></body></html>", "baseUri", ParseErrorList.tracking(1));
        Assert.assertNotNull(errors);
        Assert.assertEquals(1, errors.size());
        // assertions on errors similar to testParseInputMaxErrorsReached()
    }
}