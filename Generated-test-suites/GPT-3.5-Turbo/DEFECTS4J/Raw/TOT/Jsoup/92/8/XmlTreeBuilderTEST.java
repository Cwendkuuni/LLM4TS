import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

import java.io.IOException;
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
    }

    @Test
    public void testInitialiseParse() {
        String input = "<tag></tag>";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(xmlTreeBuilder);

        try {
            xmlTreeBuilder.initialiseParse(new StringReader(input), baseUri, parser);
            assertEquals(baseUri, xmlTreeBuilder.doc.baseUri());
        } catch (IOException e) {
            fail("IOException was thrown");
        }
    }

    @Test
    public void testParseWithReader() {
        String input = "<tag></tag>";
        String baseUri = "http://www.example.com";

        try {
            Document document = xmlTreeBuilder.parse(new StringReader(input), baseUri);
            assertNotNull(document);
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail("IOException was thrown");
        }
    }

    @Test
    public void testParseWithString() {
        String input = "<tag></tag>";
        String baseUri = "http://www.example.com";

        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testProcessStartTag() {
        Token token = new Token.StartTag();
        ((Token.StartTag) token).name("tag");

        assertTrue(xmlTreeBuilder.process(token));
        Element element = xmlTreeBuilder.currentElement();
        assertNotNull(element);
        assertEquals("tag", element.tagName());
    }

    @Test
    public void testProcessEndTag() {
        Token token = new Token.EndTag();
        ((Token.EndTag) token).tagName("tag");
        xmlTreeBuilder.stack.add(xmlTreeBuilder.doc.createElement("tag"));

        assertTrue(xmlTreeBuilder.process(token));
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcessComment() {
        String commentData = "This is a comment";
        Token token = new Token.Comment();
        ((Token.Comment) token).data(commentData);

        assertTrue(xmlTreeBuilder.process(token));
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertEquals(Node.COMMENT_NODE, node.getNodeType());
        assertEquals(commentData, ((Comment) node).getData());
    }

    @Test
    public void testProcessCharacter() {
        String characterData = "This is some character data";
        Token token = new Token.Character();
        ((Token.Character) token).data(characterData);

        assertTrue(xmlTreeBuilder.process(token));
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertEquals(Node.TEXT_NODE, node.getNodeType());
        assertEquals(characterData, node.outerHtml());
    }

    @Test
    public void testProcessDoctype() {
        String doctypeName = "html";
        String publicIdentifier = "-//W3C//DTD HTML 4.01//EN";
        String systemIdentifier = "http://www.w3.org/TR/html4/strict.dtd";
        Token token = new Token.Doctype();
        ((Token.Doctype) token).name(doctypeName);
        ((Token.Doctype) token).pubSysKey(publicIdentifier);
        ((Token.Doctype) token).pubSysKey(systemIdentifier);

        assertTrue(xmlTreeBuilder.process(token));
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertEquals(Node.DOCUMENT_TYPE_NODE, node.getNodeType());
        DocumentType doctypeNode = (DocumentType) node;
        assertEquals(doctypeName, doctypeNode.nodeName());
        assertEquals(publicIdentifier, doctypeNode.getPublicIdentifier());
        assertEquals(systemIdentifier, doctypeNode.getSystemIdentifier());
    }

    @Test
    public void testInsertNode() {
        Node node = xmlTreeBuilder.doc.createElement("element");
        xmlTreeBuilder.insertNode(node);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertEquals(node, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag");
        startTag.attributes().put("attr", "value");

        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("tag", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertFalse(element.isSelfClosing());
        assertEquals(1, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testInsertStartTagSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag");
        startTag.attributes().put("attr", "value");
        startTag.selfClosing();

        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("tag", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertTrue(element.isSelfClosing());
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testInsertComment() {
        String commentData = "This is a comment";
        Token.Comment commentToken = new Token.Comment();
        commentToken.data(commentData);

        xmlTreeBuilder.insert(commentToken);
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertEquals(Node.COMMENT_NODE, node.getNodeType());
        assertEquals(commentData, ((Comment) node).getData());
    }

    @Test
    public void testInsertCharacterNotCData() {
        String characterData = "This is some character data";
        Token.Character characterToken = new Token.Character();
        characterToken.data(characterData);

        xmlTreeBuilder.insert(characterToken);
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertEquals(Node.TEXT_NODE, node.getNodeType());
        assertEquals(characterData, node.outerHtml());
    }

    @Test
    public void testInsertCharacterCData() {
        String characterData = "This is some character data";
        Token.Character characterToken = new Token.Character();
        characterToken.data(characterData);
        characterToken.setCData();

        xmlTreeBuilder.insert(characterToken);
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertEquals(Node.CDATA_SECTION_NODE, node.getNodeType());
        assertEquals(characterData, node.outerHtml());
    }

    @Test
    public void testInsertDoctype() {
        String doctypeName = "html";
        String publicIdentifier = "-//W3C//DTD HTML 4.01//EN";
        String systemIdentifier = "http://www.w3.org/TR/html4/strict.dtd";
        Token.Doctype doctypeToken = new Token.Doctype();
        doctypeToken.name(doctypeName);
        doctypeToken.pubSysKey(publicIdentifier);
        doctypeToken.pubSysKey(systemIdentifier);

        xmlTreeBuilder.insert(doctypeToken);
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        assertNotNull(node);
        assertEquals(Node.DOCUMENT_TYPE_NODE, node.getNodeType());
        DocumentType doctypeNode = (DocumentType) node;
        assertEquals(doctypeName, doctypeNode.nodeName());
        assertEquals(publicIdentifier, doctypeNode.getPublicIdentifier());
        assertEquals(systemIdentifier, doctypeNode.getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        Element element1 = xmlTreeBuilder.doc.createElement("tag");
        Element element2 = xmlTreeBuilder.doc.createElement("tag");
        Element element3 = xmlTreeBuilder.doc.createElement("tag");
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element3);

        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName("tag");

        xmlTreeBuilder.popStackToClose(endTag);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag></tag>";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(xmlTreeBuilder);

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals(Node.ELEMENT_NODE, nodes.get(0).getNodeType());
        Element element = (Element) nodes.get(0);
        assertEquals("tag", element.tagName());
    }

    @Test
    public void testParseFragmentWithContext() {
        String inputFragment = "<tag></tag>";
        Element context = xmlTreeBuilder.doc.createElement("context");
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(xmlTreeBuilder);

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, parser);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals(Node.ELEMENT_NODE, nodes.get(0).getNodeType());
        Element element = (Element) nodes.get(0);
        assertEquals("tag", element.tagName());
        assertEquals("context", element.parent().tagName());
    }

    @Test
    public void testParseFragmentWithContextNull() {
        String inputFragment = "<tag></tag>";
        String baseUri = "http://www.example.com";
        Parser parser = new Parser(xmlTreeBuilder);

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, null, baseUri, parser);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals(Node.ELEMENT_NODE, nodes.get(0).getNodeType());
        Element element = (Element) nodes.get(0);
        assertEquals("tag", element.tagName());
        assertTrue(element.parent() instanceof Document);
    }

}