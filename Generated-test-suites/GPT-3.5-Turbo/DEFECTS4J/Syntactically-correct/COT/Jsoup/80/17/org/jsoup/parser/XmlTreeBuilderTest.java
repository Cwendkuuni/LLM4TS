
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
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
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
    }

    @Test
    public void testParseReaderString() {
        String input = "<xml><tag>content</tag></xml>";
        Reader reader = new StringReader(input);
        Document doc = xmlTreeBuilder.parse(reader, "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseStringString() {
        String input = "<xml><tag>content</tag></xml>";
        Document doc = xmlTreeBuilder.parse(input, "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<xml><tag>content</tag></xml>");
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        assertNotNull(xmlTreeBuilder.getDoc());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.getDoc().outputSettings().syntax());
    }

    @Test
    public void testProcess() {
        Token token = new Token.StartTag("tag");
        assertTrue(xmlTreeBuilder.process(token));
    }

    @Test
    public void testInsertNode() {
        Node node = new Element(null);
        xmlTreeBuilder.insertNode(node);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertEquals(node, childNodes.get(0));
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag");
        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals("tag", element.nodeName());
        assertEquals(xmlTreeBuilder.getBaseUri(), element.baseUri());
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Comment);
        assertEquals("comment", ((Comment) childNodes.get(0)).getData());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("character");
        xmlTreeBuilder.insert(characterToken);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof TextNode);
        assertEquals("character", ((TextNode) childNodes.get(0)).getWholeText());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("doctype", "", "");
        xmlTreeBuilder.insert(doctype);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof DocumentType);
        assertEquals("doctype", ((DocumentType) childNodes.get(0)).getName());
    }

    @Test
    public void testPopStackToClose() {
        Element parent1 = new Element("parent");
        Element parent2 = new Element("parent");
        Element child = new Element("child");

        xmlTreeBuilder.getStack().add(parent1);
        xmlTreeBuilder.getStack().add(parent2);
        xmlTreeBuilder.getStack().add(child);

        Token.EndTag endTag = new Token.EndTag("parent");
        xmlTreeBuilder.popStackToClose(endTag);

        assertFalse(xmlTreeBuilder.getStack().contains(parent2));
        assertFalse(xmlTreeBuilder.getStack().contains(child));
    }

    @Test
    public void testParseFragment() {
        String input = "<tag>content</tag>";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> nodes = xmlTreeBuilder.parseFragment(input, "http://example.com", errors, settings);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals("tag", nodes.get(0).nodeName());
        assertEquals("content", nodes.get(0).ownText());
    }
}
