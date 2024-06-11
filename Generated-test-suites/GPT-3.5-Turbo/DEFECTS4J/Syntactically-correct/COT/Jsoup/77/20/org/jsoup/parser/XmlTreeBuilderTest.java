
import org.jsoup.nodes.Node;
import org.jsoup.parser.Token;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private Document doc;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
        doc = new Document("");
    }

    @Test
    public void testDefaultSettings() {
        // Test case for defaultSettings() method
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, parseSettings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        // Test case for parse(Reader input, String baseUri) method
        Reader input = new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root></root>");
        Document result = xmlTreeBuilder.parse(input, "http://example.com");
        assertTrue(result instanceof Document);
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        // Test case for parse(String input, String baseUri) method
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root></root>";
        Document result = xmlTreeBuilder.parse(input, "http://example.com");
        assertTrue(result instanceof Document);
    }

    @Test
    public void testInitialiseParse() {
        // Test case for initialiseParse(Reader input, String baseUri, ParseErrorList errors, ParseSettings settings) method
        Reader input = new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root></root>");
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        Element currentElement = xmlTreeBuilder.currentElement();
        assertEquals("root", currentElement.nodeName());

        Document.OutputSettings.Syntax syntax = xmlTreeBuilder.doc.outputSettings().syntax();
        assertEquals(Document.OutputSettings.Syntax.xml, syntax);
    }

    @Test
    public void testProcessStartTag() {
        // Test case for insert(Token.StartTag startTag) method
        Token.StartTag startTag = new Token.StartTag("tag", null);
        Element result = xmlTreeBuilder.insert(startTag);
        assertTrue(result instanceof Element);
    }

    @Test
    public void testProcessEndTag() {
        // Test case for popStackToClose(Token.EndTag endTag) method
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.stack.add(new Element("tag", ""));
        xmlTreeBuilder.stack.add(new Element("div", ""));
        xmlTreeBuilder.stack.add(new Element("body", ""));

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(2, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessComment() {
        // Test case for insert(Token.Comment commentToken) method
        Token.Comment comment = new Token.Comment("test comment");
        xmlTreeBuilder.insert(comment);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());

        Node node = childNodes.get(0);
        assertTrue(node instanceof Comment);
        assertEquals("test comment", ((Comment) node).getData());
    }

    @Test
    public void testProcessCharacter() {
        // Test case for insert(Token.Character token) method
        Token.Character character = new Token.Character("test character");
        xmlTreeBuilder.insert(character);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());

        Node node = childNodes.get(0);
        assertTrue(node instanceof TextNode);
        assertEquals("test character", ((TextNode) node).getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        // Test case for insert(Token.Doctype d) method
        Token.Doctype doctype = new Token.Doctype("test", "", "");
        xmlTreeBuilder.insert(doctype);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());

        Node node = childNodes.get(0);
        assertTrue(node instanceof DocumentType);
        assertEquals("test", ((DocumentType) node).nodeName());
    }

    @Test
    public void testInsertNode() {
        // Test case for insertNode(Node node) method
        Node node = new Element("test", "");
        xmlTreeBuilder.insertNode(node);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(node, childNodes.get(0));
    }

    @Test
    public void testParseFragment() {
        // Test case for parseFragment(String inputFragment, String baseUri, ParseErrorList errors, ParseSettings settings) method
        List<Node> childNodes = xmlTreeBuilder.parseFragment("<root></root>", "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertEquals(1, childNodes.size());
    }
}