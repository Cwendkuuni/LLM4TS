import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ParserTest {
    private Parser parser;
    private TreeBuilder treeBuilder;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        treeBuilder = new TreeBuilder();
        parser = new Parser(treeBuilder);
        errors = new ParseErrorList();
    }

    @Test
    public void testParseInput_withValidHtml_returnsDocument() {
        String inputHtml = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "http://example.com";

        Document document = parser.parseInput(inputHtml, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("Title", document.select("h1").text());
    }

    @Test
    public void testParseInput_withEmptyHtml_returnsEmptyDocument() {
        String inputHtml = "";
        String baseUri = "http://example.com";

        Document document = parser.parseInput(inputHtml, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("", document.html());
    }

    @Test
    public void testGetTreeBuilder_returnsTreeBuilder() {
        TreeBuilder actualTreeBuilder = parser.getTreeBuilder();

        Assert.assertNotNull(actualTreeBuilder);
        Assert.assertEquals(treeBuilder, actualTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder_returnsParserWithUpdatedTreeBuilder() {
        TreeBuilder newTreeBuilder = new TreeBuilder();

        Parser actualParser = parser.setTreeBuilder(newTreeBuilder);

        Assert.assertNotNull(actualParser);
        Assert.assertEquals(newTreeBuilder, actualParser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors_withNoErrorsSet_returnsFalse() {
        boolean trackErrors = parser.isTrackErrors();

        Assert.assertFalse(trackErrors);
    }

    @Test
    public void testSetTrackErrors_withValidMaxErrors_returnsParserWithUpdatedMaxErrors() {
        int maxErrors = 10;

        Parser actualParser = parser.setTrackErrors(maxErrors);

        Assert.assertNotNull(actualParser);
        Assert.assertTrue(actualParser.isTrackErrors());
        Assert.assertEquals(maxErrors, actualParser.getErrors().size());
    }

    @Test
    public void testGetErrors_withNoErrors_returnsEmptyList() {
        List<ParseError> errorList = parser.getErrors();

        Assert.assertNotNull(errorList);
        Assert.assertTrue(errorList.isEmpty());
    }

    @Test
    public void testParse_withValidHtmlAndBaseUri_returnsDocument() {
        String html = "<html><body><h1>Title</h1></body></html>";
        String baseUri = "http://example.com";

        Document document = Parser.parse(html, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("Title", document.select("h1").text());
    }

    @Test
    public void testParseFragment_withValidHtmlAndContext_returnsListOfNodes() {
        String htmlFragment = "<div><p>Paragraph 1</p><p>Paragraph 2</p></div>";
        Element context = new Element("div");
        String baseUri = "http://example.com";

        List<Node> nodeList = Parser.parseFragment(htmlFragment, context, baseUri);

        Assert.assertNotNull(nodeList);
        Assert.assertEquals(2, nodeList.size());
        Assert.assertEquals("p", nodeList.get(0).nodeName());
        Assert.assertEquals("Paragraph 1", nodeList.get(0).text());
        Assert.assertEquals("p", nodeList.get(1).nodeName());
        Assert.assertEquals("Paragraph 2", nodeList.get(1).text());
    }

    @Test
    public void testParseBodyFragment_withValidHtml_returnsDocumentWithParsedBody() {
        String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
        String baseUri = "http://example.com";

        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals(2, document.getElementsByTag("p").size());
    }

    @Test
    public void testParseBodyFragmentRelaxed_withValidHtml_returnsDocument() {
        String bodyHtml = "<p>Paragraph 1</p><p>Paragraph 2</p>";
        String baseUri = "http://example.com";

        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals(2, document.getElementsByTag("p").size());
    }

    @Test
    public void testHtmlParser_returnsParserWithHtmlTreeBuilder() {
        Parser htmlParser = Parser.htmlParser();

        Assert.assertNotNull(htmlParser);
        Assert.assertTrue(htmlParser.getTreeBuilder() instanceof HtmlTreeBuilder);
    }

    @Test
    public void testXmlParser_returnsParserWithXmlTreeBuilder() {
        Parser xmlParser = Parser.xmlParser();

        Assert.assertNotNull(xmlParser);
        Assert.assertTrue(xmlParser.getTreeBuilder() instanceof XmlTreeBuilder);
    }
}