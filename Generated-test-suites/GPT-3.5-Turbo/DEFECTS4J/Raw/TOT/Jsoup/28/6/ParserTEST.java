import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;

import org.junit.Assert;
import org.junit.Test;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseInput() {
        // Typical use case - valid html input
        String validHtml = "<html><body><h1>Title</h1><p>Content</p></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(validHtml, "https://www.example.com");
        Assert.assertEquals("Title", document.getElementsByTag("h1").text());
        Assert.assertEquals("Content", document.getElementsByTag("p").text());
        
        // Edge case - empty html input
        String emptyHtml = "";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(emptyHtml, "https://www.example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("", document.text());
        
        // Error scenario - invalid html input with incorrect closing tag
        String invalidHtml = "<html><body><h1>Title</h1><p>Content</body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput(invalidHtml, "https://www.example.com");
        List<ParseError> errors = parser.getErrors();
        Assert.assertFalse(errors.isEmpty());
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpected end tag (</p>)", errors.get(0).getMessage());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder initialTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(initialTreeBuilder);

        // Typical use case - set new tree builder
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        Assert.assertEquals(newTreeBuilder, parser.getTreeBuilder());

        // Edge case - set null tree builder
        parser.setTreeBuilder(null);
        Assert.assertNull(parser.getTreeBuilder());
    }

    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        // Typical use case - enable error tracking
        parser.setTrackErrors(10);
        Assert.assertTrue(parser.isTrackErrors());
        Assert.assertEquals(10, parser.getTrackErrors());

        // Edge case - disable error tracking
        parser.setTrackErrors(0);
        Assert.assertFalse(parser.isTrackErrors());
        Assert.assertEquals(0, parser.getTrackErrors());
    }

    @Test
    public void testParse() {
        // Typical use case - valid html input
        String validHtml = "<html><body><h1>Title</h1><p>Content</p></body></html>";
        Document document = Parser.parse(validHtml, "https://www.example.com");
        Assert.assertEquals("Title", document.getElementsByTag("h1").text());
        Assert.assertEquals("Content", document.getElementsByTag("p").text());

        // Edge case - empty html input
        String emptyHtml = "";
        Document document = Parser.parse(emptyHtml, "https://www.example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("", document.text());

        // Error scenario - invalid html input with incorrect closing tag
        String invalidHtml = "<html><body><h1>Title</h1><p>Content</body></html>";
        Document document = Parser.parse(invalidHtml, "https://www.example.com");
        List<ParseError> errors = parser.getErrors();
        Assert.assertFalse(errors.isEmpty());
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpected end tag (</p>)", errors.get(0).getMessage());
    }

    @Test
    public void testParseFragment() {
        // Typical use case - valid fragment html input
        String fragmentHtml = "<h1>Title</h1><p>Content</p>";
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment(fragmentHtml, context, "https://www.example.com");
        Assert.assertEquals(2, nodes.size());
        Assert.assertEquals("h1", nodes.get(0).nodeName());
        Assert.assertEquals("p", nodes.get(1).nodeName());
        Assert.assertEquals("Title", ((Element)nodes.get(0)).text());
        Assert.assertEquals("Content", ((Element)nodes.get(1)).text());

        // Edge case - empty fragment html input
        String emptyFragmentHtml = "";
        Element context = new Element("div");
        List<Node> nodes = Parser.parseFragment(emptyFragmentHtml, context, "https://www.example.com");
        Assert.assertNull(nodes);
    }

    @Test
    public void testParseBodyFragment() {
        // Typical use case - valid html input
        String fragmentHtml = "<h1>Title</h1><p>Content</p>";
        Document document = Parser.parseBodyFragment(fragmentHtml, "https://www.example.com");
        Assert.assertEquals("Title", document.getElementsByTag("h1").text());
        Assert.assertEquals("Content", document.getElementsByTag("p").text());

        // Edge case - empty html input
        String emptyHtml = "";
        Document document = Parser.parseBodyFragment(emptyHtml, "https://www.example.com");
        Assert.assertNotNull(document);
        Assert.assertEquals("", document.text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        // This method is deprecated and redirects to parse method, so we can simply call the parse method
        String bodyHtml = "<html><body><h1>Title</h1><p>Content</p></body></html>";
        Document document = Parser.parse(bodyHtml, "https://www.example.com");
        Assert.assertEquals("Title", document.getElementsByTag("h1").text());
        Assert.assertEquals("Content", document.getElementsByTag("p").text());
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        Assert.assertNotNull(parser);
        Assert.assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        Assert.assertNotNull(parser);
        Assert.assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}