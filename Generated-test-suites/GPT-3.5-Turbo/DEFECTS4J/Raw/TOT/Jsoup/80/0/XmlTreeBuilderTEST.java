import org.junit.*;
import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseReaderAndBaseUri() {
        String xml = "<root><element1>text1</element1><element2>text2</element2></root>";
        Reader reader = new StringReader(xml);
        String baseUri = "http://example.com";

        Document document = xmlTreeBuilder.parse(reader, baseUri);

        assertNotNull(document);
        assertTrue(document instanceof Document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(xml, document.toString());
    }

    @Test
    public void testParseStringAndBaseUri() {
        String xml = "<root><element1>text1</element1><element2>text2</element2></root>";
        String baseUri = "http://example.com";

        Document document = xmlTreeBuilder.parse(xml, baseUri);

        assertNotNull(document);
        assertTrue(document instanceof Document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(xml, document.toString());
    }

    @Test
    public void testInitialiseParse() {
        StringReader reader = new StringReader("<root><element1>text1</element1></root>");
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);

        assertEquals(baseUri, xmlTreeBuilder.baseUri);
        assertSame(errors, xmlTreeBuilder.errors);
        assertSame(settings, xmlTreeBuilder.settings);
    }

    @Test
    public void testProcessWithStartTagToken() {
        Token.StartTag startTag = new Token.StartTag("element", null);
        Element element = new Element(Tag.valueOf("element"), "http://example.com");
        xmlTreeBuilder.stack.push(element);

        xmlTreeBuilder.process(startTag);

        assertEquals(1, element.childNodes().size());
        assertTrue(element.childNodes().get(0) instanceof Element);
        assertEquals("element", ((Element) element.childNodes().get(0)).tagName());
    }

    @Test
    public void testProcessWithEndTagToken() {
        Token.EndTag endTag = new Token.EndTag("element");
        Element element = new Element(Tag.valueOf("element"), "http://example.com");
        xmlTreeBuilder.stack.push(element);

        xmlTreeBuilder.process(endTag);

        assertEquals(0, element.childNodes().size());
    }

    @Test
    public void testProcessWithCommentToken() {
        Token.Comment comment = new Token.Comment("Test Comment");
        Element element = new Element(Tag.valueOf("element"), "http://example.com");
        xmlTreeBuilder.stack.push(element);

        xmlTreeBuilder.process(comment);

        assertEquals(1, element.childNodes().size());
        assertTrue(element.childNodes().get(0) instanceof Comment);
        assertEquals("Test Comment", ((Comment) element.childNodes().get(0)).getData());
    }

    @Test
    public void testProcessWithCharacterToken() {
        Token.Character character = new Token.Character("Text Character");
        Element element = new Element(Tag.valueOf("element"), "http://example.com");
        xmlTreeBuilder.stack.push(element);

        xmlTreeBuilder.process(character);

        assertEquals(1, element.childNodes().size());
        assertTrue(element.childNodes().get(0) instanceof TextNode);
        assertEquals("Text Character", ((TextNode) element.childNodes().get(0)).getWholeText());
    }

    @Test
    public void testProcessWithDoctypeToken() {
        Token.Doctype doctype = new Token.Doctype("doctype");
        Element element = new Element(Tag.valueOf("element"), "http://example.com");
        xmlTreeBuilder.stack.push(element);

        xmlTreeBuilder.process(doctype);

        assertEquals(1, element.childNodes().size());
        assertTrue(element.childNodes().get(0) instanceof DocumentType);
        assertEquals("doctype", ((DocumentType) element.childNodes().get(0)).getName());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("element"), "http://example.com");
        xmlTreeBuilder.stack.push(element);
        Node node = new Element(Tag.valueOf("node"), "http://example.com");

        xmlTreeBuilder.insertNode(node);

        assertEquals(1, element.childNodes().size());
        assertSame(node, element.childNodes().get(0));
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("element", null);
        Tag tag = Tag.valueOf("element");
        xmlTreeBuilder.settings = ParseSettings.preserveCase;

        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals(tag, element.tag());
        assertEquals(xmlTreeBuilder.baseUri, element.baseUri());
        assertEquals(xmlTreeBuilder.settings.normalizeAttributes(startTag.attributes), element.attributes());
        assertEquals(1, element.siblingIndex());
    }

    @Test
    public void testInsertSelfClosingStartTag() {
        Token.StartTag selfClosingStartTag = new Token.StartTag("element", null);
        Tag tag = Tag.valueOf("element");

        Element element = xmlTreeBuilder.insert(selfClosingStartTag);

        assertNotNull(element);
        assertEquals(tag, element.tag());
        assertEquals(xmlTreeBuilder.baseUri, element.baseUri());
        assertTrue(element.tag().isKnownTag());
        assertTrue(element.tag().isSelfClosing());
        assertTrue(element.siblingIndex() > 0);
    }

    @Test
    public void testInsertComment() {
        Token.Comment comment = new Token.Comment("Test Comment");

        xmlTreeBuilder.insert(comment);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof Comment);
        assertEquals("Test Comment", ((Comment) xmlTreeBuilder.currentElement().childNodes().get(0)).getData());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character character = new Token.Character("Text Character");

        xmlTreeBuilder.insert(character);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof TextNode);
        assertEquals("Text Character", ((TextNode) xmlTreeBuilder.currentElement().childNodes().get(0)).getWholeText());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("doctype");

        xmlTreeBuilder.insert(doctype);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof DocumentType);
        assertEquals("doctype", ((DocumentType) xmlTreeBuilder.currentElement().childNodes().get(0)).getName());
    }

    @Test
    public void testPopStackToCloseWithExistingElement() {
        Token.EndTag endTag = new Token.EndTag("element");
        Element element = new Element(Tag.valueOf("element"), "http://example.com");
        xmlTreeBuilder.stack.push(element);

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testPopStackToCloseWithoutExistingElement() {
        Token.EndTag endTag = new Token.EndTag("element");
        Element element = new Element(Tag.valueOf("otherElement"), "http://example.com");
        xmlTreeBuilder.stack.push(element);

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertSame(element, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<root><element1>text1</element1><element2>text2</element2></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        assertNotNull(nodes);
        assertFalse(nodes.isEmpty());
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals(baseUri, ((Element) nodes.get(0)).baseUri());
        assertEquals(inputFragment, nodes.get(0).toString());
    }

}