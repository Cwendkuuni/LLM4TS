package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Doctype;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;

/**
 * Test class for XmlTreeBuilder
 */
public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
        assertTrue(settings.isPreserveCase());
    }

    @Test
    public void testParseWithBaseUri() throws IOException {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><child attr='value'/></root>";
        Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
        assertNotNull(doc);
        Element root = doc.element("root");
        Element child = root.element("child");
        assertNotNull(child);
        Attribute attr = child.attribute("attr");
        assertNotNull(attr);
        assertEquals("value", attr.value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithNullBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><child attr='value'/></root>";
        xmlTreeBuilder.parse(new StringReader(xmlString), null);
    }

    @Test
    public void testProcessStartTag() throws IOException {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><child attr='value'/></root>";
        Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
        Element root = doc.element("root");
        Element child = root.element("child");
        Node node = xmlTreeBuilder.insert(Token.startTag("child", null, null, null, null, null, null));
        assertEquals(Element.class, node.getClass());
        assertEquals("child", node.nodeName());
        assertTrue(child.equals(node));
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root><child/></root>";
        Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
        Node nodeToPop = xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
        xmlTreeBuilder.popStackToClose(Token.endTag("child", null, null, null, null, null, null));
        assertNull(xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));
        assertEquals(Element.class, nodeToPop.getClass());
        assertEquals("child", nodeToPop.nodeName());
    }

    @Test
    public void testProcessComment() throws IOException {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<!-- This is a comment -->";
        Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
        Comment comment = (Comment) doc.childNodes().get(0);
        String commentData = comment.data();
        assertEquals(" This is a comment ", commentData);
    }

    @Test
    public void testProcessCharacter() throws IOException {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root>Text &amp; <>&> </root>";
        Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
        TextNode textNode1 = (TextNode) doc.childNodes().get(0);
        assertEquals("Text &amp; <>&>", textNode1.text());
        TextNode textNode2 = (TextNode) doc.childNodes().get(1);
        assertEquals("", textNode2.text());
    }

    @Test
    public void testProcessDoctype() throws IOException {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<!DOCTYPE root [ <!ENTITY abc SYMBOL 'xyz'> ]>";
        Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
        DocumentType doctypeNode = (DocumentType) doc.childNodes().get(0);
        assertEquals("root", doctypeNode.name());
        assertEquals("[", doctypeNode.internalSubset().data());
        assertEquals("xyz", doctypeNode.entity("abc").nodeName());
        assertEquals("xyz", doctypeNode.entity("abc").text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessUnexpectedTokenType() throws IOException {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<root>";
        Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
        Token token = doc.tokens().get(0);
        xmlTreeBuilder.process(token);
        Validate.fail("Unexpected test passing");
    }

    @Test
    public void testParseFragment() throws IOException {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlFragment = "<root><child attr='value'/></root>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Node root = nodes.get(0);
        Node child = root.childNodes().get(0);
        assertEquals(Element.class, root.getClass());
        assertEquals("root", root.nodeName());
        assertEquals(Element.class, child.getClass());
        assertEquals("child", child.nodeName());
        assertEquals("value", child.attribute("attr").value());
    }
}