package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import org.jsoup.Jsoup;
import static org.jsoup.nodes.Attributes.fromMap;
import static org.jsoup.nodes.Attributes.fromMapEmpty;
import static org.jsoup.nodes.Attributes.fromMapWithNull;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xml = "<root><element attr=\"value\">content</element></root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), "");
        assertEquals("<root><element attr=\"value\">content</element></root>", doc.html());
        assertNotNull(doc.rootElement());
        Element root = doc.rootElement();
        List<Element> elements = root.children();
        assertEquals(1, elements.size());
        Element element = elements.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("content", element.text());
    }

    @Test
    public void testParseWithEmptyAttributes() {
        String xml = "<root/>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), "");
        assertEquals("<root/>", doc.html());
        assertNotNull(doc.rootElement());
        Element root = doc.rootElement();
        assertNotNull(root.attributes());
        assertEquals(0, root.attributes().size());
    }

    @Test
    public void testParseWithNullAttributes() {
        String xml = "<root attr=null/>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), "");
        assertEquals("<root attr=null/>", doc.html());
        assertNotNull(doc.rootElement());
        Element root = doc.rootElement();
        Map<String, Object> attributes = root.attributes();
        assertNotNull(attributes);
        assertEquals(1, attributes.size());
        assertEquals(null, attributes.get("attr"));
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr=\"value\">content</element>";
        List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();
        assertEquals(1, nodes.size());
        Element element = (Element) nodes.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("content", element.text());
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidTokenType() {
        new XmlTreeBuilder().parse(new StringReader("invalid"), "").runParser();
    }

    @Test
    public void testParseUnknownTag() {
        String xml = "<unknown/>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), "");
        assertEquals("<unknown/>", doc.html());
        assertNotNull(doc.rootElement());
        Element root = doc.rootElement();
        assertFalse(root.tag().isKnownTag());
    }

    @Test
    public void testParseSelfClosingTag() {
        String xml = "<test xmlns:x=\"http://example.com\" xmlns:my=\"http://example.com/my\">" +
                "<selfClosing tag=\"value\" x:attr1=\"attr1Value\" my:attr2=\"attr2Value\"/></test>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), "");
        assertEquals("<test xmlns:x=\"http://example.com\" xmlns:my=\"http://example.com/my\">" +
                "<selfClosing tag=\"value\" x:attr1=\"attr1Value\" my:attr2=\"attr2Value\"/></test>", doc.html());
        assertNotNull(doc.rootElement());
        Element root = doc.rootElement();
        List<Element> elements = root.children();
        assertEquals(1, elements.size());
        Element selfClosingElement = elements.get(0);
        assertEquals("selfClosing", selfClosingElement.tagName());
        assertEquals("value", selfClosingElement.attr("tag"));
        assertNotNull(selfClosingElement.attributes());
        assertEquals(2, selfClosingElement.attributes().size());
        assertEquals("attr1Value", selfClosingElement.attr("x:attr1"));
        assertEquals("attr2Value", selfClosingElement.attr("my:attr2"));
    }

    @Test
    public void testParseXmlDeclaration() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), "");
        assertNull(doc.rootElement());
        DocumentType doctype = doc.documentType();
        assertEquals("xml", doctype.tagName());
        assertEquals("1.0", doctype.version());
        assertEquals("UTF-8", doctype.encoding());
        assertEquals("yes", doctype.getPublicId());
        assertEquals("", doctype.getSystemId());
    }

    @Test
    public void testParseComment() {
        String xml = "<!-- This is a comment -->";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), "");
        assertEquals("<!-- This is a comment -->", doc.html());
        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        Comment comment = (Comment) nodes.get(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseEmptyDocument() {
        String xml = "<>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), "");
        assertEquals("<>", doc.html());
        assertNotNull(doc.rootElement());
        Element root = doc.rootElement();
        assertNull(root.childNode(0));
    }
}