import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.xml.XmlTreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals(html, doc.html());
        Assert.assertEquals(1, doc.numChildren());

        Element head = doc.head();
        Assert.assertNotNull(head);
        Assert.assertEquals(0, head.numChildren());

        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertEquals(0, body.numChildren());
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();

        Assert.assertEquals(treeBuilder.getClass(), HtmlTreeBuilder.class);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);

        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertEquals(treeBuilder.getClass(), XmlTreeBuilder.class);
    }

    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        boolean isTracking = parser.isTrackErrors();

        Assert.assertFalse(isTracking);
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        boolean isTracking = parser.isTrackErrors();

        Assert.assertTrue(isTracking);
    }

    @Test
    public void testGetErrors() {
        String invalidHtml = "<html><div><script>alert('hello')</script></div></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();

        Assert.assertNotNull(errors);
        Assert.assertEquals(0, errors.size());

        Document doc = parser.parseInput(invalidHtml, null);
        errors = parser.getErrors();

        Assert.assertNotNull(errors);
        Assert.assertEquals(1, errors.size());
        Assert.assertNotNull(errors.get(0).getLine());
        Assert.assertNotNull(errors.get(0).getColumn());
        Assert.assertNotNull(errors.get(0).getMessage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputWithNullHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputWithEmptyHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput("", null);
    }

    @Test
    public void testParseStaticFunctions() {
        Document doc = Parser.parse("<html><head></head><body></body></html>", null);
        Assert.assertNotNull(doc);

        List<Node> nodes = Parser.parseFragment("<script>alert('hello')</script>", null, null);
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());

        Document docBody = Parser.parseBodyFragment("<script>alert('hello')</script>", null);
        Assert.assertNotNull(docBody);
        Assert.assertEquals(1, docBody.numChildren());
        Assert.assertEquals("script", docBody.child(0).tagName());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document doc = Parser.parseBodyFragmentRelaxed("<html><body><script>alert('hello')</script></body></html>", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.numChildren());
        Assert.assertEquals("script", doc.child(0).tagName());
    }

    @Test
    public void testHtmlParser() {
        Document doc = Parser.htmlParser().parseInput("<html><head></head><body></body></html>", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.numChildren());

        Element head = doc.head();
        Assert.assertNotNull(head);
        Assert.assertEquals(0, head.numChildren());

        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertEquals(0, body.numChildren());
    }

    @Test
    public void testXmlParser() {
        Document doc = Parser.xmlParser().parseInput("<root><element></element></root>", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.numChildren());
        Assert.assertEquals("root", doc.tagName());

        Element element = doc.child(0);
        Assert.assertNotNull(element);
        Assert.assertEquals("element", element.tagName());
        Assert.assertEquals(0, element.numChildren());
    }
}