import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();

        assertNotNull(parseSettings);
        // Test that preserveCase is set as default
        assertTrue(parseSettings.preserveCase());
    }

    @Test
    public void testParseWithoutErrors() {
        String input = "<html><head><title>Test</title></head><body><p>Test</p></body></html>";
        String baseUri = "";

        Document document = xmlTreeBuilder.parse(input, baseUri);

        assertNotNull(document);
        // Test that the parsed document contains the expected elements
        Elements titleElement = document.select("title");
        assertEquals(1, titleElement.size());
        assertEquals("Test", titleElement.text());

        Elements paragraphElement = document.select("p");
        assertEquals(1, paragraphElement.size());
        assertEquals("Test", paragraphElement.text());
    }

    @Test
    public void testParseWithErrors() {
        String input = "<html><head><title>Test<title></head><body><p>Test</p></body></html>";
        String baseUri = "";

        Document document = xmlTreeBuilder.parse(input, baseUri);

        assertNotNull(document);
        // Test that the parsed document contains the expected elements, despite the errors
        Elements titleElement = document.select("title");
        assertEquals(1, titleElement.size());
        assertEquals("Test", titleElement.text());

        Elements paragraphElement = document.select("p");
        assertEquals(1, paragraphElement.size());
        assertEquals("Test", paragraphElement.text());
    }

    @Test
    public void testInitialiseParse() {
        String inputFragment = "<p>Test</p>";
        String baseUri = "";

        // Initialize parse with errors
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings(true);
        xmlTreeBuilder.initialiseParse(inputFragment, baseUri, errors, settings);

        // Test that the parse is properly initialized
        assertEquals(inputFragment, xmlTreeBuilder.currentTextNode().toString());
        assertEquals(baseUri, xmlTreeBuilder.baseUri());
        assertEquals(errors, xmlTreeBuilder.errors());
        assertEquals(settings, xmlTreeBuilder.settings());
        assertEquals(0, xmlTreeBuilder.stack().size());
    }

    @Test
    public void testProcess_StartTagToken() {
        Token.StartTag startTag = new Token.StartTag("p", "p", new Attributes());
        xmlTreeBuilder.process(startTag);

        // Test that the start tag is properly inserted into the stack
        assertEquals("p", xmlTreeBuilder.stack().get(0).nodeName());
    }

    @Test
    public void testProcess_EndTagToken() {
        // Insert a start tag into the stack
        Token.StartTag startTag = new Token.StartTag("p", "p", new Attributes());
        xmlTreeBuilder.process(startTag);
        // Insert an end tag for the start tag
        Token.EndTag endTag = new Token.EndTag("p");
        xmlTreeBuilder.process(endTag);

        // Test that the stack is properly popped
        assertEquals(0, xmlTreeBuilder.stack().size());
    }

    @Test
    public void testProcess_CommentToken() {
        Token.Comment commentToken = new Token.Comment("Test");
        xmlTreeBuilder.process(commentToken);

        // Test that the comment is properly inserted into the stack
        assertEquals("Test", xmlTreeBuilder.stack().get(0).nodeName());
    }

    @Test
    public void testProcess_CharacterToken() {
        Token.Character characterToken = new Token.Character("Test");
        xmlTreeBuilder.process(characterToken);

        // Test that the character is properly inserted into the stack
        assertEquals("Test", xmlTreeBuilder.stack().get(0).nodeName());
    }

    @Test
    public void testProcess_DoctypeToken() {
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "", "");
        xmlTreeBuilder.process(doctypeToken);

        // Test that the doctype is properly inserted into the stack
        assertEquals("html", xmlTreeBuilder.stack().get(0).nodeName());
    }

    @Test
    public void testInsertNode() {
        Token.StartTag startTag = new Token.StartTag("p", "p", new Attributes());
        xmlTreeBuilder.process(startTag);

        // Insert a node into the stack
        Node node = new Element(Tag.valueOf("span"), xmlTreeBuilder.baseUri());
        xmlTreeBuilder.insertNode(node);

        // Test that the node is properly inserted into the current element
        Elements elements = xmlTreeBuilder.currentElement().select("span");
        assertEquals(1, elements.size());
    }

    @Test
    public void testInsert_StartTagToken() {
        Token.StartTag startTag = new Token.StartTag("p", "p", new Attributes());
        Element element = xmlTreeBuilder.insert(startTag);

        // Test that the start tag is properly inserted as an element
        assertEquals("p", element.nodeName());
        assertEquals(xmlTreeBuilder.baseUri(), element.baseUri());
    }

    @Test
    public void testInsert_CommentToken() {
        Token.Comment commentToken = new Token.Comment("Test");
        xmlTreeBuilder.insert(commentToken);

        // Test that the comment is properly inserted into the current element
        Elements elements = xmlTreeBuilder.currentElement().select("Test");
        assertEquals(1, elements.size());
    }

    @Test
    public void testInsert_CharacterToken() {
        Token.Character characterToken = new Token.Character("Test");
        xmlTreeBuilder.insert(characterToken);

        // Test that the character is properly inserted into the current element
        Elements elements = xmlTreeBuilder.currentElement().select("Test");
        assertEquals(1, elements.size());
    }

    @Test
    public void testInsert_DoctypeToken() {
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "", "");
        xmlTreeBuilder.insert(doctypeToken);

        // Test that the doctype is properly inserted into the current element
        Elements elements = xmlTreeBuilder.currentElement().select("html");
        assertEquals(1, elements.size());
    }

    @Test
    public void testPopStackToClose() {
        // Insert two elements into the stack
        Element element1 = new Element(Tag.valueOf("p"), xmlTreeBuilder.baseUri());
        Element element2 = new Element(Tag.valueOf("div"), xmlTreeBuilder.baseUri());
        xmlTreeBuilder.stack().add(element1);
        xmlTreeBuilder.stack().add(element2);

        // Insert third element into the stack, which is the same as the first element
        Element element3 = new Element(Tag.valueOf("p"), xmlTreeBuilder.baseUri());
        xmlTreeBuilder.stack().add(element3);

        // Pop the stack to remove the first occurrence of the element
        Token.EndTag endTag = new Token.EndTag("p");
        xmlTreeBuilder.popStackToClose(endTag);

        // Test that the stack is properly popped
        assertEquals(1, xmlTreeBuilder.stack().size());
        assertEquals("div", xmlTreeBuilder.stack().get(0).nodeName());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<p>Test</p>";
        String baseUri = "";

        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings(true);
        xmlTreeBuilder.initialiseParse(inputFragment, baseUri, errors, settings);
        xmlTreeBuilder.runParser();

        // Test that the fragment is properly parsed and contains the expected elements
        assertNotNull(xmlTreeBuilder.doc());
        assertEquals(1, xmlTreeBuilder.doc().childNodes().size());

        Elements elements = xmlTreeBuilder.doc().select("p");
        assertEquals(1, elements.size());
        assertEquals("Test", elements.text());
    }
}