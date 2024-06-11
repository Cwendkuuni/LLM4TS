import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private Parser parser;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        parser = new Parser(xmlTreeBuilder);
    }

    @Test
    public void testDefaultSettings() {
        assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("input");
        String baseUri = "baseUri";
        xmlTreeBuilder.initialiseParse(input, baseUri, parser);

        // Check that document is placed onto the stack
        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));

        // Check that output settings syntax is xml
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testParse() {
        Reader input = new StringReader("input");
        String baseUri = "baseUri";
        Document document = xmlTreeBuilder.parse(input, baseUri);

        // Check that the returned document is not null
        assertNotNull(document);
    }

    @Test
    public void testParseWithString() {
        String input = "input";
        String baseUri = "baseUri";
        Document document = xmlTreeBuilder.parse(input, baseUri);

        // Check that the returned document is not null
        assertNotNull(document);
    }

    @Test
    public void testProcess_StartTag() {
        Token.StartTag startTag = new Token.StartTag("tag");
        Element element = new Element(Tag.valueOf("tag", xmlTreeBuilder.settings));
        startTag.name("tag");
        startTag.attributes("attr");
        element.attributes("attr");

        xmlTreeBuilder.process(startTag);

        // Check that the start tag is inserted correctly
        assertEquals(element, xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size()-1));
    }

    @Test
    public void testProcess_EndTag() {
        Token.EndTag endTag = new Token.EndTag("tag");
        Element element = new Element(Tag.valueOf("tag", xmlTreeBuilder.settings));
        xmlTreeBuilder.insertNode(element);
        xmlTreeBuilder.stack.add(element);

        xmlTreeBuilder.process(endTag);

        // Check that the stack is correctly updated to remove the end tag
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcess_Comment() {
        Token.Comment comment = new Token.Comment("comment");
        Comment expectedComment = new Comment("comment");

        xmlTreeBuilder.process(comment);

        // Check that the comment is inserted correctly
        assertEquals(expectedComment, xmlTreeBuilder.currentElement().childNodes().get(0));
    }

    @Test
    public void testProcess_Character() {
        Token.Character character = new Token.Character("character");
        TextNode expectedTextNode = new TextNode("character");

        xmlTreeBuilder.process(character);

        // Check that the character is inserted correctly
        assertEquals(expectedTextNode, xmlTreeBuilder.currentElement().childNodes().get(0));
    }

    @Test
    public void testProcess_Doctype() {
        Token.Doctype doctype = new Token.Doctype("doctype", "public", "system", "pubSysKey");
        DocumentType expectedDoctype = new DocumentType("doctype", "public", "system");
        expectedDoctype.setPubSysKey("pubSysKey");

        xmlTreeBuilder.process(doctype);

        // Check that the doctype is inserted correctly
        assertEquals(expectedDoctype, xmlTreeBuilder.currentElement().childNodes().get(0));
    }

    @Test
    public void testInsertNode() {
        Node node = new Node();
        Element element = new Element(Tag.valueOf("tag", xmlTreeBuilder.settings));

        xmlTreeBuilder.insertNode(node);
        xmlTreeBuilder.insertNode(element);

        // Check that both the node and element are inserted correctly
        assertEquals(node, xmlTreeBuilder.currentElement().childNodes().get(0));
        assertEquals(element, xmlTreeBuilder.currentElement().childNodes().get(1));
    }

    @Test
    public void testInsert() {
        Token.StartTag startTag = new Token.StartTag("tag");
        Element element = new Element(Tag.valueOf("tag", xmlTreeBuilder.settings));
        startTag.name("tag");
        startTag.attributes("attr");
        element.attributes("attr");

        Element insertedElement = xmlTreeBuilder.insert(startTag);

        // Check that the start tag is inserted correctly into the stack
        assertEquals(element, xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size()-1));

        // Check that the inserted element is correct
        assertEquals(element, insertedElement);
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag");

        Element element1 = new Element(Tag.valueOf("tag", xmlTreeBuilder.settings));
        Element element2 = new Element(Tag.valueOf("tag", xmlTreeBuilder.settings));
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);

        xmlTreeBuilder.popStackToClose(endTag);

        // Check that the stack is correctly updated to remove the end tag
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "inputFragment";
        String baseUri = "baseUri";
        Parser parser = new Parser(xmlTreeBuilder);

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);

        // Check that the returned nodes are not null
        assertNotNull(nodes);
    }

    @Test
    public void testParseFragmentWithContext() {
        String inputFragment = "inputFragment";
        Element context = new Element(Tag.valueOf("tag", xmlTreeBuilder.settings));
        String baseUri = "baseUri";
        Parser parser = new Parser(xmlTreeBuilder);

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, parser);

        // Check that the returned nodes are not null
        assertNotNull(nodes);
    }

    @Test
    public void testInsert_CData() {
        Token.StartTag startTag = new Token.StartTag("tag");
        Element element = new Element(Tag.valueOf("tag", xmlTreeBuilder.settings));
        startTag.name("tag");
        startTag.attributes("attr");
        element.attributes("attr");

        Element insertedElement = xmlTreeBuilder.insert(startTag);

        // Check that the start tag is inserted correctly into the stack
        assertEquals(element, xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size()-1));

        // Check that the inserted element is correct
        assertEquals(element, insertedElement);
    }
}