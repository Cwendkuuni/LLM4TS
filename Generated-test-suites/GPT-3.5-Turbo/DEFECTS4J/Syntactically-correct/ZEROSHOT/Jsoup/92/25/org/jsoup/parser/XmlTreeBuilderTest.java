package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private Document doc;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        doc = new Document("");
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("");
        String baseUri = "https://example.com";
        Parser parser = new Parser();
        xmlTreeBuilder.initialiseParse(input, baseUri, parser);

        assertNotNull(xmlTreeBuilder.getStack());
        assertEquals(doc, xmlTreeBuilder.getStack().get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }

    @Test
    public void testParseWithReader() {
        Reader input = new StringReader("");
        String baseUri = "https://example.com";
        Document parsedDoc = xmlTreeBuilder.parse(input, baseUri);

        assertNotNull(parsedDoc);
        assertEquals(baseUri, parsedDoc.baseUri());
    }

    @Test
    public void testParseWithString() {
        String input = "";
        String baseUri = "https://example.com";
        Document parsedDoc = xmlTreeBuilder.parse(input, baseUri);

        assertNotNull(parsedDoc);
        assertEquals(baseUri, parsedDoc.baseUri());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals("div", element.tagName());
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.getStack().add(new Element("div"));

        xmlTreeBuilder.process(endTag);

        assertTrue(xmlTreeBuilder.getStack().isEmpty());
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("Test comment");
        xmlTreeBuilder.process(commentToken);

        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNode(0) instanceof Comment);
        assertEquals("Test comment", ((Comment) doc.childNode(0)).getData());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("Test character");
        xmlTreeBuilder.process(characterToken);

        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNode(0) instanceof TextNode);
        assertEquals("Test character", ((TextNode) doc.childNode(0)).getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "");
        xmlTreeBuilder.process(doctype);

        assertEquals(1, doc.childNodes().size());
        assertTrue(doc.childNode(0) instanceof DocumentType);
        assertEquals("html", ((DocumentType) doc.childNode(0)).getName());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div>Hello World</div>";
        String baseUri = "https://example.com";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, new Parser());

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("div", element.tagName());
    }
}