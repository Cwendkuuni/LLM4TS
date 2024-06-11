import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;

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
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseReaderString() {
        String input = "<root><child></child></root>";
        String baseUri = "http://example.com";
        Reader reader = new StringReader(input);
        Document document = xmlTreeBuilder.parse(reader, baseUri);

        assertNotNull(document);
        assertEquals(input, document.html());
    }

    @Test
    public void testParseStringString() {
        String input = "<root><child></child></root>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals(input, document.html());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root><child></child></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(new StringReader(input), baseUri, errors, settings);

        assertNotNull(xmlTreeBuilder.getStack());
        assertNotNull(xmlTreeBuilder.getDoc());
        assertNotNull(xmlTreeBuilder.getDoc().outputSettings());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.getDoc().outputSettings().syntax());
    }

    @Test
    public void testProcessStartTagToken() {
        Token.StartTag startTag = new Token.StartTag("child");
        startTag.attributes().put("attribute", "value");

        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals("child", element.tagName());
        assertEquals("value", element.attr("attribute"));
    }

    @Test
    public void testProcessEndTagToken() {
        Element element1 = new Element(Tag.valueOf("root"), "http://example.com");
        Element element2 = new Element(Tag.valueOf("child"), "http://example.com");
        xmlTreeBuilder.getStack().add(element1);
        xmlTreeBuilder.getStack().add(element2);

        Token.EndTag endTag = new Token.EndTag("child");
        xmlTreeBuilder.popStackToClose(endTag);

        assertFalse(xmlTreeBuilder.getStack().contains(element2));
    }

    @Test
    public void testProcessCommentToken() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);

        List<Node> childNodes = xmlTreeBuilder.getDoc().childNodes();
        assertFalse(childNodes.isEmpty());
        assertEquals(1, childNodes.size());

        assertEquals(NodeType.COMMENT_NODE, childNodes.get(0).nodeType());
        assertEquals("comment", ((Comment) childNodes.get(0)).getData());
    }

    @Test
    public void testProcessCharacterToken() {
        Token.Character characterToken = new Token.Character("data");
        xmlTreeBuilder.insert(characterToken);

        List<Node> childNodes = xmlTreeBuilder.getDoc().childNodes();
        assertFalse(childNodes.isEmpty());
        assertEquals(1, childNodes.size());

        assertEquals(NodeType.TEXT_NODE, childNodes.get(0).nodeType());
        assertEquals("data", ((TextNode) childNodes.get(0)).getWholeText());
    }

    @Test
    public void testProcessDoctypeToken() {
        Token.Doctype doctype = new Token.Doctype("html", "", "");
        xmlTreeBuilder.insert(doctype);

        List<Node> childNodes = xmlTreeBuilder.getDoc().childNodes();
        assertFalse(childNodes.isEmpty());
        assertEquals(1, childNodes.size());

        assertEquals(NodeType.DOCUMENT_TYPE_NODE, childNodes.get(0).nodeType());
        assertEquals("html", ((DocumentType) childNodes.get(0)).getName());
    }

    @Test
    public void testPopStackToClose() {
        Element element1 = new Element(Tag.valueOf("root"), "http://example.com");
        Element element2 = new Element(Tag.valueOf("child"), "http://example.com");
        xmlTreeBuilder.getStack().add(element1);
        xmlTreeBuilder.getStack().add(element2);

        Token.EndTag endTag = new Token.EndTag("child");
        xmlTreeBuilder.popStackToClose(endTag);

        assertFalse(xmlTreeBuilder.getStack().contains(element2));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<child></child>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        assertFalse(nodes.isEmpty());
        assertEquals(1, nodes.size());
        assertEquals(NodeType.ELEMENT_NODE, nodes.get(0).nodeType());
        assertEquals("child", ((Element) nodes.get(0)).tagName());
    }
}