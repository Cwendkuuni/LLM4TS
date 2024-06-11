import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader reader = new StringReader("<xml></xml>");
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(reader, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<xml></xml>";
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<xml></xml>");
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        assertNotNull(xmlTreeBuilder.stack);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("http://example.com", xmlTreeBuilder.doc.baseUri());
    }

    @Test
    public void testProcessStartTag() {
        Token token = new Token.StartTag("xml");
        xmlTreeBuilder.process(token);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("xml", xmlTreeBuilder.stack.get(0).nodeName());
    }

    @Test
    public void testProcessEndTag() {
        Token token = new Token.EndTag("xml");
        xmlTreeBuilder.process(token);
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessComment() {
        Token token = new Token.Comment("comment");
        xmlTreeBuilder.process(token);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.get(0) instanceof Comment);
    }

    @Test
    public void testProcessCharacter() {
        Token token = new Token.Character("data");
        xmlTreeBuilder.process(token);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.get(0) instanceof TextNode);
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype token = new Token.Doctype("doctype");
        xmlTreeBuilder.process(token);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.get(0) instanceof DocumentType);
    }

    @Test
    public void testInsertNode() {
        Node node = new Element("element");
        xmlTreeBuilder.insertNode(node);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals("element", xmlTreeBuilder.currentElement().childNodes().get(0).nodeName());
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag");
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals("tag", xmlTreeBuilder.currentElement().childNodes().get(0).nodeName());
        assertFalse(element.isSelfClosing());
    }

    @Test
    public void testInsertStartTagWithUnknownTag() {
        Token.StartTag startTag = new Token.StartTag("unknown");
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals("unknown", xmlTreeBuilder.currentElement().childNodes().get(0).nodeName());
        assertTrue(element.isSelfClosing());
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof Comment);
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("data");
        xmlTreeBuilder.insert(characterToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof TextNode);
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype("doctype");
        xmlTreeBuilder.insert(doctypeToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testPopStackToCloseNotFound() {
        Token.EndTag endTag = new Token.EndTag("unknown");
        xmlTreeBuilder.stack.add(new Element("tag"));
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(1, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<xml></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("xml", nodes.get(0).nodeName());
    }
}