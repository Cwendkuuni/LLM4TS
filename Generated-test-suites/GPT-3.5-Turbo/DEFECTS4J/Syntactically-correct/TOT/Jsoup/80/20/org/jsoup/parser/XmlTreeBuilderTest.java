import org.jsoup.nodes.*;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        assertNotNull(defaultSettings);
        assertEquals(ParseSettings.preserveCase, defaultSettings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader input = new StringReader("<tag>content</tag>");
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        Element rootElement = document.child(0);
        assertNotNull(rootElement);
        assertEquals("tag", rootElement.nodeName());
        assertEquals("content", rootElement.text());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<tag>content</tag>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        Element rootElement = document.child(0);
        assertNotNull(rootElement);
        assertEquals("tag", rootElement.nodeName());
        assertEquals("content", rootElement.text());
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<tag>content</tag>");
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        assertEquals(input, xmlTreeBuilder.input);
        assertEquals(baseUri, xmlTreeBuilder.baseUri);
        assertEquals(errors, xmlTreeBuilder.errors);
        assertEquals(settings, xmlTreeBuilder.settings);
        assertNotNull(xmlTreeBuilder.doc);
        assertNotNull(xmlTreeBuilder.stack);
    }

    @Test
    public void testProcessWithStartTagToken() {
        Token token = new Token.StartTag("tag", null);
        xmlTreeBuilder.process(token);
        Element rootElement = xmlTreeBuilder.doc.child(0);
        assertNotNull(rootElement);
        assertEquals("tag", rootElement.nodeName());
    }

    @Test
    public void testProcessWithEndTagToken() {
        Element element = new Element("tag");
        xmlTreeBuilder.stack.add(element);
        Token token = new Token.EndTag("tag");
        xmlTreeBuilder.process(token);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcessWithCommentToken() {
        Token token = new Token.Comment("comment");
        xmlTreeBuilder.process(token);
        Comment commentNode = (Comment) xmlTreeBuilder.doc.childNode(0);
        assertNotNull(commentNode);
        assertEquals("comment", commentNode.getData());
    }

    @Test
    public void testProcessWithCharacterToken() {
        Token token = new Token.Character("data", false);
        xmlTreeBuilder.process(token);
        TextNode textNode = (TextNode) xmlTreeBuilder.doc.childNode(0);
        assertNotNull(textNode);
        assertEquals("data", textNode.getWholeText());
    }

    @Test
    public void testProcessWithDoctypeToken() {
        Token.Doctype token = new Token.Doctype("doctype", "pubId", "sysId");
        xmlTreeBuilder.process(token);
        DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.doc.childNode(0);
        assertNotNull(doctypeNode);
        assertEquals("doctype", doctypeNode.getName());
        assertEquals("pubId", doctypeNode.getPublicId());
        assertEquals("sysId", doctypeNode.getSystemId());
    }

    @Test
    public void testInsertNode() {
        Element parentElement = new Element("parent");
        Element childElement = new Element("child");
        xmlTreeBuilder.stack.add(parentElement);
        xmlTreeBuilder.insertNode(childElement);
        assertEquals(childElement, parentElement.child(0));
    }

    @Test
    public void testInsertWithStartTagToken() {
        Token.StartTag token = new Token.StartTag("tag", null);
        Element element = xmlTreeBuilder.insert(token);
        assertNotNull(element);
        assertNotNull(xmlTreeBuilder.stack);
        assertTrue(xmlTreeBuilder.stack.contains(element));
        assertEquals("tag", element.nodeName());
    }

    @Test
    public void testInsertWithCommentToken() {
        Token.Comment token = new Token.Comment("comment");
        xmlTreeBuilder.insert(token);
        Comment commentNode = (Comment) xmlTreeBuilder.doc.childNode(0);
        assertNotNull(commentNode);
        assertEquals("comment", commentNode.getData());
    }

    @Test
    public void testInsertWithCharacterToken() {
        Token.Character token = new Token.Character("data", false);
        xmlTreeBuilder.insert(token);
        TextNode textNode = (TextNode) xmlTreeBuilder.doc.childNode(0);
        assertNotNull(textNode);
        assertEquals("data", textNode.getWholeText());
    }

    @Test
    public void testInsertWithDoctypeToken() {
        Token.Doctype token = new Token.Doctype("doctype", "pubId", "sysId");
        xmlTreeBuilder.insert(token);
        DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.doc.childNode(0);
        assertNotNull(doctypeNode);
        assertEquals("doctype", doctypeNode.getName());
        assertEquals("pubId", doctypeNode.getPublicId());
        assertEquals("sysId", doctypeNode.getSystemId());
    }

    @Test
    public void testPopStackToClose() {
        Element element1 = new Element("tag");
        Element element2 = new Element("tag");
        Element element3 = new Element("tag");
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element3);
        Token.EndTag token = new Token.EndTag("tag");
        xmlTreeBuilder.popStackToClose(token);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testPopStackToCloseWithNoMatchingElement() {
        Element element1 = new Element("tag1");
        Element element2 = new Element("tag2");
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        Token.EndTag token = new Token.EndTag("tag3");
        xmlTreeBuilder.popStackToClose(token);
        assertFalse(xmlTreeBuilder.stack.isEmpty());
        assertTrue(xmlTreeBuilder.stack.contains(element1));
        assertTrue(xmlTreeBuilder.stack.contains(element2));
    }

    @Test
    public void testParseFragment() {
        String input = "<tag>content</tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, baseUri, errors, settings);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Element rootElement = (Element) nodes.get(0);
        assertEquals("tag", rootElement.nodeName());
        assertEquals("content", rootElement.text());
    }
}