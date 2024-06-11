
import org.jsoup.nodes.Node;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        Assertions.assertEquals(ParseSettings.preserveCase, parseSettings);
    }

    @Test
    public void testInitialiseParse() {
        Reader reader = new StringReader("test");
        String baseUri = "https://example.com";
        Parser parser = new Parser();
        
        xmlTreeBuilder.initialiseParse(reader, baseUri, parser);

        // Assert the stack contains the document
        Assertions.assertEquals(1, xmlTreeBuilder.stack.size());
        Assertions.assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));

        // Assert the document output settings
        Assertions.assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader reader = new StringReader("test");
        String baseUri = "https://example.com";

        Document document = xmlTreeBuilder.parse(reader, baseUri);

        // Assert the parsed document
        Assertions.assertNotNull(document);
        // Add assertions for expected content of the document
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<xml>test</xml>";
        String baseUri = "https://example.com";

        Document document = xmlTreeBuilder.parse(input, baseUri);

        // Assert the parsed document
        Assertions.assertNotNull(document);
        // Add assertions for expected content of the document
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        Element element = xmlTreeBuilder.insert(startTag);

        // Assert the inserted element
        Assertions.assertNotNull(element);
        Assertions.assertEquals("div", element.tagName());
        // Add assertions for expected attributes of the element
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.stack.add(xmlTreeBuilder.doc.createElement("div")); // add a div element to the stack

        xmlTreeBuilder.popStackToClose(endTag);

        // Assert the stack is empty after removing the div element
        Assertions.assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment("test comment");
        xmlTreeBuilder.insert(comment);

        // Assert the inserted comment
        List<Node> childNodes = xmlTreeBuilder.doc.childNodes();
        Assertions.assertEquals(1, childNodes.size());
        Assertions.assertEquals("<!--test comment-->", childNodes.get(0).toString());
    }

    @Test
    public void testProcessCharacterData() {
        Token.Character characterData = new Token.Character("test data");
        xmlTreeBuilder.insert(characterData);

        // Assert the inserted character data
        List<Node> childNodes = xmlTreeBuilder.doc.childNodes();
        Assertions.assertEquals(1, childNodes.size());
        Assertions.assertEquals("test data", childNodes.get(0).toString());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        xmlTreeBuilder.insert(doctype);

        // Assert the inserted doctype
        List<Node> childNodes = xmlTreeBuilder.doc.childNodes();
        Assertions.assertEquals(1, childNodes.size());
        // Add assertions for expected properties of the inserted doctype
    }

    @Test
    public void testInsertNode() {
        Node node = new Node();
        xmlTreeBuilder.insertNode(node);

        // Assert the inserted node
        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        Assertions.assertEquals(1, childNodes.size());
        Assertions.assertEquals(node, childNodes.get(0));
    }

    @Test
    public void testParseFragmentWithInputFragmentAndBaseUri() {
        String inputFragment = "<p>test</p>";
        String baseUri = "https://example.com";

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, new Parser());

        // Assert the parsed nodes
        Assertions.assertNotNull(nodes);
        // Add assertions for expected content of the nodes
    }

    @Test
    public void testParseFragmentWithInputFragmentContextAndBaseUri() {
        String inputFragment = "<p>test</p>";
        Element context = xmlTreeBuilder.doc.createElement("div");
        String baseUri = "https://example.com";

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, new Parser());

        // Assert the parsed nodes
        Assertions.assertNotNull(nodes);
        // Add assertions for expected content of the nodes
    }

    // Add additional test cases for all the other public methods in the XmlTreeBuilder class

}
