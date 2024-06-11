import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private Document doc;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        doc = xmlTreeBuilder.parse("<root><child1>text1</child1><child2>text2</child2></root>", "");
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();

        assertNotNull(defaultSettings);
        // Add assertions for the properties of defaultSettings
    }

    @Test
    public void testParse() {
        Document parsedDoc = xmlTreeBuilder.parse("<root><child>text</child></root>", "");

        assertNotNull(parsedDoc);
        // Add assertions for the parsedDoc
    }

    @Test
    public void testInitialiseParse() throws NoSuchFieldException, IllegalAccessException {
        // Set up the required parameters for the test
        String input = "<root><child>text</child></root>";
        String baseUri = "";
        ParseErrorList errors = null;
        ParseSettings settings = null;

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        // Access the private fields of XmlTreeBuilder to assert their values
        // For example:
        // Field stackField = XmlTreeBuilder.class.getDeclaredField("stack");
        // stackField.setAccessible(true);
        // List<Node> stack = (List<Node>) stackField.get(xmlTreeBuilder);
        // assertNotNull(stack);
        // assertEquals(1, stack.size());

        // Add assertions for other private fields of XmlTreeBuilder that are modified by initialiseParse method
    }

    @Test
    public void testProcess() {
        Token startTagToken = new Token.StartTag("child", null);
        boolean result = xmlTreeBuilder.process(startTagToken);

        assertTrue(result);
        // Add assertions to verify the modification made by the process method
    }

    @Test
    public void testInsertNode() {
        // Set up the required parameters for the test
        Node node = new TextNode("text", "");

        xmlTreeBuilder.insertNode(node);

        // Verify the inserted node is added to the current element
        assertEquals(node, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        // Set up the required parameters for the test
        Token.StartTag startTag = new Token.StartTag("child", null);

        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        // Add assertions to verify the created element and its insertion
    }

    @Test
    public void testInsertComment() {
        // Set up the required parameters for the test
        Token.Comment commentToken = new Token.Comment("Comment");

        xmlTreeBuilder.insert(commentToken);

        // Verify the inserted comment is added to the current element
        assertEquals(commentToken.getData(), xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testInsertCharacter() {
        // Set up the required parameters for the test
        Token.Character characterToken = new Token.Character("text");

        xmlTreeBuilder.insert(characterToken);

        // Verify the inserted character is added to the current element
        assertEquals(characterToken.getData(), xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testInsertDoctype() {
        // Set up the required parameters for the test
        Token.Doctype doctypeToken = new Token.Doctype("HTML");

        xmlTreeBuilder.insert(doctypeToken);

        // Verify the inserted doctype is added to the current element
        assertEquals(doctypeToken.getName(), xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testPopStackToClose() throws NoSuchFieldException, IllegalAccessException {
        // Add elements to the stack to test popping them
        Element childElement = new Element("child");
        Element rootElement = new Element("root");
        xmlTreeBuilder.stack.add(childElement);
        xmlTreeBuilder.stack.add(rootElement);

        Token.EndTag endTag = new Token.EndTag("child");
        xmlTreeBuilder.popStackToClose(endTag);

        // Verify that the child element is removed from the stack
        assertFalse(xmlTreeBuilder.stack.contains(childElement));
        assertTrue(xmlTreeBuilder.stack.contains(rootElement));
    }

    @Test
    public void testParseFragment() {
        ParseErrorList errors = null;
        ParseSettings settings = null;

        List<Node> nodes = xmlTreeBuilder.parseFragment("<child>text</child>", "", errors, settings);

        assertNotNull(nodes);
        // Add assertions for the parsed fragment nodes
    }

    // Add additional test methods for other public methods in the XmlTreeBuilder class

    // 

    // Merge all the test methods into a single JUnit test file

    // 
}