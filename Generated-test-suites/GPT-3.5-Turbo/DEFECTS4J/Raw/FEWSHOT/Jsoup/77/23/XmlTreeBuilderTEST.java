
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Before;
import org.junit.Test;

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
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseReaderString() {
        Reader input = new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root></root>");
        Document document = xmlTreeBuilder.parse(input, "http://example.com");
        assertNotNull(document);
        assertEquals("root", document.child(0).nodeName());
    }

    @Test
    public void testParseStringString() {
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root></root>";
        Document document = xmlTreeBuilder.parse(input, "http://example.com");
        assertNotNull(document);
        assertEquals("root", document.child(0).nodeName());
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<root></root>");
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        assertNotNull(xmlTreeBuilder.stack);
        assertNotNull(xmlTreeBuilder.doc);
        assertNotNull(xmlTreeBuilder.doc.outputSettings());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
        assertEquals(errors, xmlTreeBuilder.errors);
        assertEquals(settings, xmlTreeBuilder.settings);
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testProcessStartTag() {
        StartTag startTag = new StartTag("div", null, false);
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertNull(element.baseUri());
        assertNotNull(element.attributes());
        assertEquals(0, element.siblingIndex());
        assertEquals(1, element.childNodes().size());
        assertEquals(element, xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size()-1));
    }

    @Test
    public void testProcessEndTag() {
        StartTag startTag = new StartTag("div", null, false);
        xmlTreeBuilder.insert(startTag);
        EndTag endTag = new EndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessCommentToken() {
        Comment commentToken = new Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        Node node = xmlTreeBuilder.doc.childNodes().get(0);
        assertNotNull(node);
        assertEquals(Node.COMMENT_NODE, node.nodeType());
        Comment comment = (Comment) node;
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testProcessCharacterToken() {
        Character characterToken = new Character("Hello, World!");
        xmlTreeBuilder.insert(characterToken);
        Node node = xmlTreeBuilder.doc.childNodes().get(0);
        assertNotNull(node);
        assertEquals(Node.TEXT_NODE, node.nodeType());
        TextNode textNode = (TextNode) node;
        assertEquals("Hello, World!", textNode.getWholeText());
    }

    @Test
    public void testProcessDoctypeToken() {
        Doctype doctypeToken = new Doctype("html", null, null);
        xmlTreeBuilder.insert(doctypeToken);
        Node node = xmlTreeBuilder.doc.childNodes().get(0);
        assertNotNull(node);
        assertEquals(Node.DOCUMENT_TYPE_NODE, node.nodeType());
        DocumentType documentType = (DocumentType) node;
        assertEquals("html", documentType.nodeName());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("div"), "http://example.com", new Attributes());
        xmlTreeBuilder.insertNode(element);
        Node node = xmlTreeBuilder.currentElement().childNodes().get(0);
        assertNotNull(node);
        assertEquals(Node.ELEMENT_NODE, node.nodeType());
        assertEquals(element, node);
    }

    @Test
    public void testInsertCDATA() {
        Character characterToken = new Character("Hello, World!");
        characterToken.setCData(true);
        xmlTreeBuilder.insert(characterToken);
        Node node = xmlTreeBuilder.doc.childNodes().get(0);
        assertNotNull(node);
        assertEquals(Node.CDATA_SECTION_NODE, node.nodeType());
        CDataNode cdataNode = (CDataNode) node;
        assertEquals("Hello, World!", cdataNode.getWholeText());
    }

    @Test
    public void testParseFragment() {
        String input = "<div>Hello, World!</div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> nodes = xmlTreeBuilder.parseFragment(input, baseUri, errors, settings);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Node node = nodes.get(0);
        assertNotNull(node);
        assertEquals(Node.ELEMENT_NODE, node.nodeType());
        Element element = (Element) node;
        assertEquals("div", element.nodeName());
    }
}
