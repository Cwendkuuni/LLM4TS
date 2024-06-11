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
    public void testParseInput() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, "http://example.com");
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title().text());
        Assert.assertEquals("Hello World", doc.body().text());

        // Test with exception tracking
        parser.setTrackErrors(5);
        String invalidHtml = "<html><body></body></html>"; // missing title tag
        doc = parser.parseInput(invalidHtml, "http://example.com");
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(5, errors.size());
        Assert.assertEquals("Error at line 1 col 1: Unbalanced begin tag html start tag", errors.get(0).getMessage());
    }

    @Test
    public void testStaticParseFunctions() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        Assert.assertNotNull(doc);
        Element title = doc.title();
        Assert.assertEquals("Test", title.text());

        Element context = Jsoup.parse("<div id='container'></div>").body();
        String fragmentHtml = "<p>Fragment</p>";
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "http://example.com");
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        Assert.assertNotNull(node);
        Assert.assertEquals(Element.Tag.PARAGRAPH, node.tagName());

        Document docBody = Parser.parseBodyFragment(fragmentHtml, "http://example.com");
        Assert.assertNotNull(docBody);
        Assert.assertEquals(1, docBody.numberOfElements());
        Assert.assertEquals(Element.Tag.PARAGRAPH, docBody.body().tagName());
        Assert.assertEquals("Fragment", docBody.body().text());
    }

    @Test
    public void testStaticMethods_parseBodyFragmentRelaxed() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.numberOfElements());
        Assert.assertEquals(Element.Tag.PARAGRAPH, doc.body().tagName());
        Assert.assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testNewHtmlParser() {
        Parser parser = Parser.htmlParser();
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document doc = parser.parseInput(html, "http://example.com");
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title().text());
        Assert.assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testNewXmlParser() {
        Parser parser = Parser.xmlParser();
        String xml = "<root><element>Hello World</element></root>";
        Document doc = parser.parseInput(xml, "http://example.com");
        Assert.assertNotNull(doc);
        Assert.assertEquals(1, doc.numberOfElements());
        Assert.assertEquals(Element.Tag.ELEMENT, doc.rootElement().tagName());
        Element element = doc.rootElement().child(0);
        Assert.assertEquals("Hello World", element.text());
    }
}