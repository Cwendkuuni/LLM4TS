import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ParserTest {

    @Mock
    private TreeBuilder treeBuilder;
    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser(treeBuilder);
    }

    @Test
    public void testParseInput() {
        String html = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        
        ParseErrorList errors = new ParseErrorList();
        Mockito.when(treeBuilder.parse(html, baseUri, errors)).thenReturn(new Document(html));
        
        parser.setTrackErrors(5);
        Document doc = parser.parseInput(html, baseUri);
        
        assertEquals(html, doc.toString());
        assertEquals(errors, parser.getErrors());
        assertTrue(parser.isTrackErrors());
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testParse() {
        String html = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Document doc = treeBuilder.parse(html, baseUri, ParseErrorList.noTracking());

        assertEquals(doc, Parser.parse(html, baseUri));
    }

    @Test
    public void testParseFragment() {
        String fragmentHtml = "<div></div>";
        Element context = new Element("body");
        String baseUri = "http://example.com";
        
        HtmlTreeBuilder treeBuilder = new HtmlTreeBuilder();
        List<Node> nodes = treeBuilder.parseFragment(fragmentHtml, context, baseUri, ParseErrorList.noTracking());

        assertEquals(nodes, Parser.parseFragment(fragmentHtml, context, baseUri));
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div></div>";
        String baseUri = "http://example.com";
        
        Document doc = Document.createShell(baseUri);
        Element body = doc.body();
        List<Node> nodeList = Parser.parseFragment(bodyHtml, body, baseUri);
        Node[] nodes = nodeList.toArray(new Node[nodeList.size()]);
        for (Node node : nodes) {
            body.appendChild(node);
        }

        assertEquals(doc, Parser.parseBodyFragment(bodyHtml, baseUri));
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div></div>";
        String baseUri = "http://example.com";
        
        assertEquals(Parser.parse(bodyHtml, baseUri), Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri));
    }

    @Test
    public void testGetTreeBuilder() {
        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new TreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testIsTrackErrors() {
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        int maxErrors = 5;
        parser.setTrackErrors(maxErrors);
        assertEquals(maxErrors, parser.getTrackErrors());
    }

    @Test
    public void testGetErrors() {
        ParseErrorList errors = new ParseErrorList();
        assertEquals(errors, parser.getErrors());
    }

    @Test
    public void testHtmlParser() {
        TreeBuilder treeBuilder = new HtmlTreeBuilder();
        Parser htmlParser = Parser.htmlParser();
        assertEquals(treeBuilder, htmlParser.getTreeBuilder());
    }

    @Test
    public void testXmlParser() {
        TreeBuilder treeBuilder = new XmlTreeBuilder();
        Parser xmlParser = Parser.xmlParser();
        assertEquals(treeBuilder, xmlParser.getTreeBuilder());
    }
}