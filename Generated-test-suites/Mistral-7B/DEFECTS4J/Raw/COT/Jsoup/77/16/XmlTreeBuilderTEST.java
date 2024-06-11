package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = XmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
    }

    @Test
    public void testParseWithReader() {
        String xml = "<root><element>Hello World</element></root>";
        Reader input = new StringReader(xml);
        String baseUri = "http://example.com";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(input, baseUri);
        assertNotNull(doc);
        Element root = doc.getRootElement();
        Element element = root.getChild(0);
        assertNotNull(element);
        assertNotNull(element.text());
        assertNotNull(element.tagName());
        assertNull(element.baseUri());
    }

    @Test
    public void testParseWithString() {
        String xml = "<root><element>Hello World</element></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(xml, "http://example.com");
        assertNotNull(doc);
        Element root = doc.getRootElement();
        Element element = root.getChild(0);
        assertNotNull(element);
        assertNotNull(element.text());
        assertNotNull(element.tagName());
        assertNotNull(element.baseUri());
        assertEquals("http://example.com", element.baseUri().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        String xml = "<root><element>Hello World</element></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse(null, "http://example.com");
    }

    @Test
    public void testInitialiseParse() {
        String xml = "<root><element>Hello World</element></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader(xml);
        builder.initialiseParse(input, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertNotNull(builder.doc);
    }

    @Test
    public void testProcessStartTag() {
        String xml = "<root><element><subElement/></element></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader(xml);
        builder.initialiseParse(input, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        builder.runParser();
        Element root = builder.doc.getRootElement();
        Element element = root.getChild(0);
        Element subElement = element.getChild(0);
        assertNotNull(root);
        assertNotNull(element);
        assertNotNull(subElement);
    }

    @Test
    public void testProcessEndTag() {
        String xml = "<root><element><subElement/></element></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader(xml);
        builder.initialiseParse(input, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        builder.runParser();
        assertNotNull(builder.stack);
        Element root = builder.doc.getRootElement();
        Element element = root.getChild(0);
        assertEquals(1, builder.stack.size());
        builder.popStackToClose(element.tagName());
        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testProcessComment() {
        String xml = "<!-- This is a comment -->";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader(xml);
        builder.initialiseParse(input, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        builder.runParser();
        assertEquals(1, builder.doc.childNodes().size());
        Comment comment = (Comment) builder.doc.childNodes().get(0);
        assertNotNull(comment);
        assertNotNull(comment.getData());
    }

    @Test
    public void testProcessCharacter() {
        String xml = "<root>Hello World</root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader(xml);
        builder.initialiseParse(input, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        builder.runParser();
        assertEquals(1, builder.doc.childNodes().size());
        TextNode textNode = (TextNode) builder.doc.childNodes().get(0);
        assertNotNull(textNode);
        assertNotNull(textNode.getData());
    }

    @Test
    public void testProcessDoctype() {
        String xml = "<!DOCTYPE root SYSTEM \"http://example.com/root.dtd\">";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader(xml);
        builder.initialiseParse(input, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        builder.runParser();
        DocumentType doctypeNode = (DocumentType) builder.doc.childNodes().get(0);
        assertNotNull(doctypeNode);
        assertNotNull(doctypeNode.nodeName());
        assertNotNull(doctypeNode.getPublicIdentifier());
        assertNotNull(doctypeNode.getPublicSysKey());
        assertNotNull(doctypeNode.getSystemIdentifier());
    }

    @Test
    public void testParseFragment() {
        String xml = "<root><element><subElement/></element></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment(xml, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertEquals(3, nodes.size());
        Document doc = nodes.get(0).getParent();
        assertNotNull(doc);
        Element root = (Element) doc.getRootElement();
        Element element = (Element) root.getChild(0);
        Element subElement = (Element) element.getChild(0);
        assertNotNull(root);
        assertNotNull(element);
        assertNotNull(subElement);
    }

    @Test
    public void testPopStackToCloseNullEndTag() {
        String xml = "<root><element></root>";
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader(xml);
        builder.initialiseParse(input, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        builder.runParser();
        assertNull(builder.stack);
    }
}