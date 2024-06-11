package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.safety.Cleaner;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        
        assertTrue(defaultSettings.preserveCase());
        assertFalse(defaultSettings.normalizeTagNames());
        assertFalse(defaultSettings.htmlMode());
        assertFalse(defaultSettings.inBody());
        assertFalse(defaultSettings.inTable());
        assertFalse(defaultSettings.insertSpaceBetweenInlineTags());
    }

    @Test
    public void testParse() throws IOException {
        String input = "<root><a>Test</a><b>Test2</b></root>";
        Document document = xmlTreeBuilder.parse(input, "http://example.com");

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());

        Element rootElement = document.getElementsByTag("root").first();
        assertNotNull(rootElement);

        Element aElement = rootElement.getElementsByTag("a").first();
        assertNotNull(aElement);
        assertEquals("Test", aElement.text());

        Element bElement = rootElement.getElementsByTag("b").first();
        assertNotNull(bElement);
        assertEquals("Test2", bElement.text());
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<root><a>Test</a></root>");
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        Document doc = xmlTreeBuilder.doc;
        assertNotNull(doc);

        Element rootElement = doc.getElementsByTag("root").first();
        assertNotNull(rootElement);
        assertEquals("root", rootElement.nodeName());

        Element aElement = rootElement.getElementsByTag("a").first();
        assertNotNull(aElement);
        assertEquals("a", aElement.nodeName());
    }

    @Test
    public void testProcess_StartTag() {
        Token.StartTag startTag = new Token.StartTag("a", "key", "value");
        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals("a", element.nodeName());
        assertEquals("key", element.attr("key"));
        assertEquals("value", element.attr("value"));
    }

    @Test
    public void testProcess_EndTag() {
        Token.EndTag endTag = new Token.EndTag("a");
        xmlTreeBuilder.stack.add(new Element("a"));

        xmlTreeBuilder.process(endTag);

        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcess_Comment() {
        Token.Comment comment = new Token.Comment("This is a comment");
        Comment commentNode = xmlTreeBuilder.insert(comment);

        assertNotNull(commentNode);
        assertEquals("This is a comment", commentNode.getData());
    }

    @Test
    public void testProcess_Character() {
        Token.Character character = new Token.Character("This is a character");
        TextNode textNode = xmlTreeBuilder.insert(character);

        assertNotNull(textNode);
        assertEquals("This is a character", textNode.text());
    }

    @Test
    public void testProcess_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        xmlTreeBuilder.insert(doctype);

        Node documentTypeNode = xmlTreeBuilder.doc.childNode(0);

        assertNotNull(documentTypeNode);
        assertEquals(Node.NodeType.DocumentType, documentTypeNode.nodeType());
        assertEquals("html", documentTypeNode.nodeName());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("a");
        Element elementA = new Element("a");
        Element elementB = new Element("b");

        xmlTreeBuilder.stack.add(elementA);
        xmlTreeBuilder.stack.add(elementB);
        xmlTreeBuilder.stack.add(elementA);

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(elementB, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<a>Link</a>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());

        Node node = nodes.get(0);
        assertNotNull(node);
        assertTrue(node instanceof Element);

        Element element = (Element) node;
        assertEquals("a", element.nodeName());
        assertEquals("Link", element.text());
    }
}