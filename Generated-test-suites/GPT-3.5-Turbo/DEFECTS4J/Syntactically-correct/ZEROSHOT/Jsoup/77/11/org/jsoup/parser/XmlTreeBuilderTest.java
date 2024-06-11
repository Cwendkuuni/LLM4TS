import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.nodes.*;

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
        assertNotNull(parseSettings);
        assertEquals(ParseSettings.preserveCase, parseSettings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader reader = new StringReader("<xml><tag></tag></xml>");
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(reader, baseUri);
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<xml><tag></tag></xml>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<xml><tag></tag></xml>");
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        // Check if document is added to stack
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));

        // Check if output settings syntax is set to xml
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTagToken() {
        Token.StartTag startTag = new Token.StartTag(Tag.valueOf("tag"), "");
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("tag", element.tagName());
    }

    @Test
    public void testProcessEndTagToken() {
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("tag"), ""));
        xmlTreeBuilder.process(endTag);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcessCommentToken() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof Comment);
        assertEquals("comment", ((Comment) node).getData());
    }

    @Test
    public void testProcessCharacterToken() {
        Token.Character characterToken = new Token.Character("text");
        xmlTreeBuilder.insert(characterToken);
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof TextNode);
        assertEquals("text", ((TextNode) node).getWholeText());
    }

    @Test
    public void testProcessDoctypeToken() {
        Token.Doctype doctypeToken = new Token.Doctype("doctype");
        xmlTreeBuilder.insert(doctypeToken);
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof DocumentType);
        DocumentType doctype = (DocumentType) node;
        assertEquals("doctype", doctype.name());
    }

    @Test
    public void testInsertNode() {
        Node node = new Element(Tag.valueOf("tag"), "");
        xmlTreeBuilder.insertNode(node);
        Node insertedNode = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(insertedNode);
        assertEquals(node, insertedNode);
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag(Tag.valueOf("tag"), "", new Attributes());
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("tag", element.tagName());
        assertTrue(xmlTreeBuilder.stack.contains(element));
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof Comment);
        assertEquals("comment", ((Comment) node).getData());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("text");
        xmlTreeBuilder.insert(characterToken);
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof TextNode);
        assertEquals("text", ((TextNode) node).getWholeText());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype("doctype");
        xmlTreeBuilder.insert(doctypeToken);
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertTrue(node instanceof DocumentType);
        DocumentType doctype = (DocumentType) node;
        assertEquals("doctype", doctype.name());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag");
        Element element = new Element(Tag.valueOf("tag"), "");
        xmlTreeBuilder.stack.add(element);
        xmlTreeBuilder.popStackToClose(endTag);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag></tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("tag", ((Element) nodes.get(0)).tagName());
    }
}