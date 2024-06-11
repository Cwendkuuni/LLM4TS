import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.parser.Token;
import org.junit.Before;
import org.junit.Test;

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
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
    }

    @Test
    public void testParseWithReader() {
        Reader input = new StringReader("<xml><tag></tag></xml>");
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(doc);
        assertEquals("xml", doc.nodeName());
    }

    @Test
    public void testParseWithString() {
        String input = "<xml><tag></tag></xml>";
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(doc);
        assertEquals("xml", doc.nodeName());
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<xml><tag></tag></xml>");
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        // Verify that the document is placed on the stack
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("xml", xmlTreeBuilder.stack.get(0).nodeName());
        // Verify that the document output settings are set to XML syntax
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcessTokenStartTag() {
        Token.StartTag startTag = new Token.StartTag(Tag.valueOf("tag"), "");
        Element el = xmlTreeBuilder.insert(startTag);
        assertNotNull(el);
        assertEquals("tag", el.nodeName());
    }

    @Test
    public void testProcessTokenEndTag() {
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("tag"), "", ParseSettings.preserveCase.normalizeAttributes(startTag.attributes)));
        xmlTreeBuilder.popStackToClose(endTag);
        // Verify that the stack is empty
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessTokenComment() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
        List<Node> children = xmlTreeBuilder.doc.childNodes();
        assertNotNull(children);
        assertEquals(1, children.size());
        assertTrue(children.get(0) instanceof Comment);
        assertEquals("comment", ((Comment) children.get(0)).getData());
    }

    @Test
    public void testProcessTokenCharacter() {
        Token.Character characterToken = new Token.Character("text");
        xmlTreeBuilder.insert(characterToken);
        List<Node> children = xmlTreeBuilder.doc.childNodes();
        assertNotNull(children);
        assertEquals(1, children.size());
        assertTrue(children.get(0) instanceof TextNode);
        assertEquals("text", ((TextNode) children.get(0)).getWholeText());
    }

    @Test
    public void testProcessTokenDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "");
        xmlTreeBuilder.insert(doctype);
        List<Node> children = xmlTreeBuilder.doc.childNodes();
        assertNotNull(children);
        assertEquals(1, children.size());
        assertTrue(children.get(0) instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) children.get(0);
        assertEquals("html", doctypeNode.nodeName());
        assertEquals("", doctypeNode.getPublicIdentifier());
        assertEquals("", doctypeNode.getSystemIdentifier());
    }

    @Test
    public void testPopStackToCloseNotFound() {
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("otherTag"), "", ParseSettings.preserveCase.normalizeAttributes(startTag.attributes)));
        xmlTreeBuilder.popStackToClose(endTag);
        // Verify that the stack remains unchanged
        assertEquals(1, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag");
        Element el1 = new Element(Tag.valueOf("tag"), "", ParseSettings.preserveCase.normalizeAttributes(startTag.attributes));
        Element el2 = new Element(Tag.valueOf("tag"), "", ParseSettings.preserveCase.normalizeAttributes(startTag.attributes));
        xmlTreeBuilder.stack.add(el1);
        xmlTreeBuilder.stack.add(el2);
        xmlTreeBuilder.popStackToClose(endTag);
        // Verify that the first occurrence of the element is removed from the stack
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(xmlTreeBuilder.stack.get(0) == el2);
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
        assertEquals("tag", ((Element) nodes.get(0)).nodeName());
    }
}