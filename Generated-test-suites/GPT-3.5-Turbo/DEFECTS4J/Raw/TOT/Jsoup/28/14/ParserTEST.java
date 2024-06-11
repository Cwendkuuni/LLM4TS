
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser(new HtmlTreeBuilder());
    }

    @Test
    public void testParseInput_ValidHTML_ReturnsDocument() {
        String html = "<html><head></head><body></body></html>";
        String baseUri = "http://www.example.com";
        Document doc = parser.parseInput(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals(html, doc.html());
        Assert.assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseInput_InvalidHTML_ReturnsDocumentWithErrors() {
        String html = "<html><body><div></html>";
        String baseUri = "http://www.example.com";
        Document doc = parser.parseInput(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertNotNull(parser.getErrors());
        Assert.assertTrue(parser.getErrors().size() > 0);
    }

    @Test
    public void testGetTreeBuilder_ReturnsTreeBuilder() {
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertNotNull(treeBuilder);
        Assert.assertEquals(HtmlTreeBuilder.class, treeBuilder.getClass());
    }

    @Test
    public void testSetTreeBuilder_ChangesTreeBuilder() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(treeBuilder);
        Assert.assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors_ReturnsCorrectState() {
        parser.setTrackErrors(5);
        Assert.assertTrue(parser.isTrackErrors());

        parser.setTrackErrors(0);
        Assert.assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors_ChangesTrackErrorsState() {
        parser.setTrackErrors(0);
        Assert.assertFalse(parser.isTrackErrors());

        parser.setTrackErrors(10);
        Assert.assertTrue(parser.isTrackErrors());
        Assert.assertEquals(10, parser.getTrackErrors());
    }

    @Test
    public void testParse_ValidHTML_ReturnsDocument() {
        String html = "<html><head></head><body></body></html>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals(html, doc.html());
        Assert.assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseFragment_ValidHTML_ReturnsListOfNodes() {
        String fragmentHtml = "<div></div>";
        Element context = new Element("div");
        String baseUri = "http://www.example.com";
        List<Node> nodeList = Parser.parseFragment(fragmentHtml, context, baseUri);
        Assert.assertNotNull(nodeList);
        Assert.assertEquals(1, nodeList.size());
        Assert.assertEquals(fragmentHtml, nodeList.get(0).outerHtml());
    }

    @Test
    public void testParseBodyFragment_ValidHTML_ReturnsDocumentWithBody() {
        String bodyHtml = "<div></div>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("<body><div></div></body>", doc.body().outerHtml());
        Assert.assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseBodyFragmentRelaxed_ValidHTML_ReturnsDocument() {
        String bodyHtml = "<html><head></head><body></body></html>";
        String baseUri = "http://www.example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals(bodyHtml, doc.html());
        Assert.assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testHtmlParser_ReturnsHTMLParser() {
        Parser htmlParser = Parser.htmlParser();
        Assert.assertNotNull(htmlParser);
        Assert.assertEquals(HtmlTreeBuilder.class, htmlParser.getTreeBuilder().getClass());
    }

    @Test
    public void testXmlParser_ReturnsXMLParser() {
        Parser xmlParser = Parser.xmlParser();
        Assert.assertNotNull(xmlParser);
        Assert.assertEquals(XmlTreeBuilder.class, xmlParser.getTreeBuilder().getClass());
    }
}

