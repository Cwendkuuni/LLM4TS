import org.jsoup.parser.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

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
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
    }

    @Test
    public void testInitialiseParse() {
        // Setup
        String input = "<xml></xml>";
        String baseUri = "https://www.example.com";
        Parser parser = new Parser(xmlTreeBuilder);

        // Execution
        xmlTreeBuilder.initialiseParse(new StringReader(input), baseUri, parser);

        // Verification
        Document doc = xmlTreeBuilder.getDocument();
        assertNotNull(doc);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        // Setup
        String input = "<xml></xml>";
        String baseUri = "https://www.example.com";

        // Execution
        Document doc = xmlTreeBuilder.parse(new StringReader(input), baseUri);

        // Verification
        assertNotNull(doc);
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        // Setup
        String input = "<xml></xml>";
        String baseUri = "https://www.example.com";

        // Execution
        Document doc = xmlTreeBuilder.parse(input, baseUri);

        // Verification
        assertNotNull(doc);
    }

    @Test
    public void testProcess() {
        // Setup
        Token token = new Token.StartTag("TestTag");

        // Execution
        boolean result = xmlTreeBuilder.process(token);

        // Verification
        assertTrue(result);
    }

    @Test
    public void testInsertNode() {
        // Setup
        Element element = new Element("TestElement");

        // Execution
        xmlTreeBuilder.insertNode(element);

        // Verification
        Element currentElement = xmlTreeBuilder.currentElement();
        assertEquals(currentElement, element);
    }

    @Test
    public void testInsertStartTag() {
        // Setup
        Token.StartTag startTag = new Token.StartTag("TestTag");

        // Execution
        Element element = xmlTreeBuilder.insert(startTag);

        // Verification
        assertNotNull(element);
    }

    @Test
    public void testInsertComment() {
        // Setup
        Token.Comment commentToken = new Token.Comment("TestComment");

        // Execution
        xmlTreeBuilder.insert(commentToken);

        // Verification
        Node insert = xmlTreeBuilder.getCurrentElement().childNodes().get(0);
        assertTrue(insert instanceof Comment);
    }

    @Test
    public void testInsertCharacter() {
        // Setup
        Token.Character characterToken = new Token.Character("TestData");

        // Execution
        xmlTreeBuilder.insert(characterToken);

        // Verification
        Node insert = xmlTreeBuilder.getCurrentElement().childNodes().get(0);
        assertTrue(insert instanceof TextNode);
    }

    @Test
    public void testInsertDoctype() {
        // Setup
        Token.Doctype doctypeToken = new Token.Doctype("html");

        // Execution
        xmlTreeBuilder.insert(doctypeToken);

        // Verification
        Node insert = xmlTreeBuilder.getCurrentElement().childNodes().get(0);
        assertTrue(insert instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        // Setup
        Token.EndTag endTag = new Token.EndTag("TestTag");
        Element element = new Element("TestTag");
        xmlTreeBuilder.pushStack(element);

        // Execution
        xmlTreeBuilder.popStackToClose(endTag);

        // Verification
        Element topElement = xmlTreeBuilder.getCurrentElement();
        assertNull(topElement);
    }

    @Test
    public void testParseFragmentWithInputFragmentAndBaseUri() {
        // Setup
        String inputFragment = "<div></div>";
        String baseUri = "https://www.example.com";
        Parser parser = new Parser(xmlTreeBuilder);

        // Execution
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);

        // Verification
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
    }

    @Test
    public void testParseFragmentWithInputFragmentContextAndBaseUri() {
        // Setup
        Element context = new Element("context");
        String inputFragment = "<div></div>";
        String baseUri = "https://www.example.com";
        Parser parser = new Parser(xmlTreeBuilder);

        // Execution
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, parser);

        // Verification
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
    }

}