
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.select.Elements;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private Reader reader;
    private String baseUri;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        reader = new StringReader("<doc><element1>Test</element1><element2>123</element2></doc>");
        baseUri = "";
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        Document doc = xmlTreeBuilder.parse(reader, baseUri);
        assertNotNull(doc);
        assertEquals("xml", doc.outputSettings().syntax().name());
        Elements elements = doc.select("element1");
        assertEquals(1, elements.size());
        assertEquals("Test", elements.get(0).text());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        Document doc = xmlTreeBuilder.parse("<doc><element1>Test</element1><element2>123</element2></doc>", baseUri);
        assertNotNull(doc);
        assertEquals("xml", doc.outputSettings().syntax().name());
        Elements elements = doc.select("element2");
        assertEquals(1, elements.size());
        assertEquals("123", elements.get(0).text());
    }

    @Test
    public void testInitialiseParse() {
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();

        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);

        assertNotNull(xmlTreeBuilder.stack);
        assertEquals(1, xmlTreeBuilder.stack.size());
        Node doc = xmlTreeBuilder.stack.get(0);
        assertTrue(doc instanceof Document);
        assertEquals("xml", ((Document) doc).outputSettings().syntax().name());
    }

    @Test
    public void testProcessStartTagToken() {
        Token token = new Token.StartTag("element");
        xmlTreeBuilder.stack.add(new Element("parent", baseUri));
        xmlTreeBuilder.process(token);

        assertEquals(2, xmlTreeBuilder.stack.size());
        Element element = xmlTreeBuilder.stack.get(1);
        assertNotNull(element);
        assertEquals("element", element.tagName());
        assertEquals(baseUri, element.baseUri());
    }

    @Test
    public void testProcessEndTagToken() {
        Element element = new Element("element", baseUri);
        xmlTreeBuilder.stack.add(new Element("parent", baseUri));
        xmlTreeBuilder.stack.add(element);
        xmlTreeBuilder.process(new Token.EndTag("element"));

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("parent", xmlTreeBuilder.stack.get(0).tagName());
    }

    @Test
    public void testProcessCommentToken() {
        String commentData = "Test Comment";
        Comment comment = new Comment(commentData);
        xmlTreeBuilder.process(new Token.Comment(commentData));

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(comment, xmlTreeBuilder.stack.get(0).childNode(0));
    }

    @Test
    public void testProcessCharacterToken() {
        Token.Character token = new Token.Character("Test Data");
        xmlTreeBuilder.process(token);

        assertEquals(1, xmlTreeBuilder.stack.size());
        Node node = xmlTreeBuilder.stack.get(0).childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof TextNode);
        assertEquals("Test Data", ((TextNode) node).text());
    }

    @Test
    public void testProcessDoctypeToken() {
        String tagName = "doctype";
        String publicIdentifier = "-//TEST//DTD Test//EN";
        String systemIdentifier = "http://www.example.com/test.dtd";
        Token.Doctype doctypeToken = new Token.Doctype(tagName, publicIdentifier, systemIdentifier);
        xmlTreeBuilder.process(doctypeToken);

        assertEquals(1, xmlTreeBuilder.stack.size());
        Node node = xmlTreeBuilder.stack.get(0).childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof DocumentType);
        assertEquals(tagName, ((DocumentType) node).tagName());
        assertEquals(publicIdentifier, ((DocumentType) node).publicIdentifier());
        assertEquals(systemIdentifier, ((DocumentType) node).systemIdentifier());
        assertNull(((DocumentType) node).pubSysKey());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element("element", baseUri);
        xmlTreeBuilder.stack.add(new Element("parent", baseUri));
        xmlTreeBuilder.insertNode(element);

        assertEquals(1, xmlTreeBuilder.stack.get(0).childNodeSize());
    }

    @Test
    public void testInsertStartTag() {
        String tagName = "element";
        xmlTreeBuilder.baseUri = baseUri;
        xmlTreeBuilder.settings = ParseSettings.htmlDefault;

        Token.StartTag startTag = new Token.StartTag(tagName);
        xmlTreeBuilder.insert(startTag);

        assertEquals(2, xmlTreeBuilder.stack.size());
        Element element = xmlTreeBuilder.stack.get(1);
        assertNotNull(element);
        assertEquals(tagName, element.tagName());
        assertEquals(baseUri, element.baseUri());
    }

    @Test
    public void testInsertComment() {
        String commentData = "Test Comment";
        Token.Comment commentToken = new Token.Comment(commentData);
        xmlTreeBuilder.insert(commentToken);

        assertEquals(1, xmlTreeBuilder.stack.size());
        Element parent = xmlTreeBuilder.stack.get(0);
        assertNotNull(parent);
        Node childNode = parent.childNode(0);
        assertNotNull(childNode);
        assertTrue(childNode instanceof Comment);
        assertEquals(commentData, ((Comment) childNode).getData());
    }

    @Test
    public void testInsertCharacter() {
        String characterData = "Test Data";
        Token.Character characterToken = new Token.Character(characterData);
        xmlTreeBuilder.insert(characterToken);

        assertEquals(1, xmlTreeBuilder.stack.size());
        Element parent = xmlTreeBuilder.stack.get(0);
        assertNotNull(parent);
        Node childNode = parent.childNode(0);
        assertNotNull(childNode);
        assertTrue(childNode instanceof TextNode);
        assertEquals(characterData, ((TextNode) childNode).text());
    }

    @Test
    public void testInsertDoctype() {
        String tagName = "doctype";
        String publicIdentifier = "-//TEST//DTD Test//EN";
        String systemIdentifier = "http://www.example.com/test.dtd";
        String pubSysKey = "Test";
        Token.Doctype doctypeToken = new Token.Doctype(tagName, publicIdentifier, systemIdentifier);
        doctypeToken.setPubSysKey(pubSysKey);
        xmlTreeBuilder.insert(doctypeToken);

        assertEquals(1, xmlTreeBuilder.stack.size());
        Element parent = xmlTreeBuilder.stack.get(0);
        assertNotNull(parent);
        Node childNode = parent.childNode(0);
        assertNotNull(childNode);
        assertTrue(childNode instanceof DocumentType);
        assertEquals(tagName, ((DocumentType) childNode).tagName());
        assertEquals(publicIdentifier, ((DocumentType) childNode).publicIdentifier());
        assertEquals(systemIdentifier, ((DocumentType) childNode).systemIdentifier());
        assertEquals(pubSysKey, ((DocumentType) childNode).pubSysKey());
    }

    @Test
    public void testPopStackToClose() {
        String tagName = "element";
        xmlTreeBuilder.stack.add(new Element(tagName, baseUri));
        xmlTreeBuilder.stack.add(new Element("parent", baseUri));
        xmlTreeBuilder.stack.add(new Element(tagName, baseUri));

        Token.EndTag endTag = new Token.EndTag(tagName);
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(2, xmlTreeBuilder.stack.size());
        assertEquals("parent", xmlTreeBuilder.stack.get(0).tagName());
        assertEquals(tagName, xmlTreeBuilder.stack.get(1).tagName());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<element>Test</element>";
        String expectedTagName = "element";
        xmlTreeBuilder.baseUri = baseUri;
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.htmlDefault;

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertNotNull(node);
        assertTrue(node instanceof Element);
        assertEquals(expectedTagName, ((Element) node).tagName());
    }
}

