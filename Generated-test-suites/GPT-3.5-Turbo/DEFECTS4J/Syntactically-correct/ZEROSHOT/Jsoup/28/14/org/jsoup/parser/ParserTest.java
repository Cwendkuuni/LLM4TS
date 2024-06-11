import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class ParserTest {
    private Parser parser;
    private TreeBuilder treeBuilder;
    private ParseErrorList parseErrorList;
    private Document document;
    private Element element;
    private String html;
    private String baseUri;
    private List<Node> nodeList;
    private Node[] nodes;

    @Before
    public void setUp() {
        treeBuilder = mock(TreeBuilder.class);
        parseErrorList = mock(ParseErrorList.class);
        document = mock(Document.class);
        element = mock(Element.class);
        html = "<html></html>";
        baseUri = "http://example.com";
        nodeList = new ArrayList<>();
        nodes = new Node[]{element};

        parser = new Parser(treeBuilder);
        parser.setTrackErrors(1);
        when(treeBuilder.parse(html, baseUri, parseErrorList)).thenReturn(document);
        when(treeBuilder.parseFragment(html, element, baseUri, parseErrorList)).thenReturn(nodeList);
        when(element.appendChild(element)).thenReturn(null);
    }

    @Test
    public void testParseInput() {
        Document result = parser.parseInput(html, baseUri);

        assertEquals(document, result);
        verify(treeBuilder).parse(html, baseUri, parseErrorList);
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder result = parser.getTreeBuilder();

        assertEquals(treeBuilder, result);
    }

    @Test
    public void testSetTreeBuilder() {
        Parser result = parser.setTreeBuilder(treeBuilder);

        assertEquals(treeBuilder, parser.getTreeBuilder());
        assertEquals(parser, result);
    }

    @Test
    public void testIsTrackErrors() {
        boolean result = parser.isTrackErrors();

        assertEquals(true, result);
    }

    @Test
    public void testSetTrackErrors() {
        Parser result = parser.setTrackErrors(0);

        assertEquals(0, parser.getTrackErrors());
        assertEquals(parser, result);
    }

    @Test
    public void testGetErrors() {
        List<ParseError> result = parser.getErrors();

        assertEquals(parseErrorList, result);
    }

    @Test
    public void testParse() {
        Document result = Parser.parse(html, baseUri);

        assertEquals(document, result);
        verify(treeBuilder).parse(html, baseUri, parseErrorList);
    }

    @Test
    public void testParseFragment() {
        List<Node> result = Parser.parseFragment(html, element, baseUri);

        assertEquals(nodeList, result);
        verify(treeBuilder).parseFragment(html, element, baseUri, parseErrorList);
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = mock(Document.class);
        Element body = mock(Element.class);
        List<Node> nodeList = new ArrayList<>(Arrays.asList(nodes));

        when(Document.createShell(baseUri)).thenReturn(doc);
        when(doc.body()).thenReturn(body);
        when(treeBuilder.parseFragment(html, body, baseUri, parseErrorList)).thenReturn(nodeList);

        Document result = Parser.parseBodyFragment(html, baseUri);

        assertEquals(doc, result);
        verify(treeBuilder).parseFragment(html, body, baseUri, parseErrorList);
        verify(body).appendChild(element);
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document result = Parser.parseBodyFragmentRelaxed(html, baseUri);

        assertEquals(document, result);
        verify(treeBuilder).parse(html, baseUri, parseErrorList);
    }

    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();

        assertEquals(treeBuilder, parser.getTreeBuilder());
    }

    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();

        assertEquals(treeBuilder, parser.getTreeBuilder());
    }
}