package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXML() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        Document doc = new XmlTreeBuilder().parse(xml, "baseUri");
        assertNotNull(doc);
        assertNull(doc.body());
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testParseSimpleXML() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><element attr=\"value\">Content</element></root>";
        Document doc = new XmlTreeBuilder().parse(xml, "baseUri");
        Element root = doc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.nodeName());
        Element element = root.child(0);
        assertNotNull(element);
        assertEquals("element", element.nodeName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Content", element.text());
    }

    @Test
    public void testParseXMLWithProcessingInstruction() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<?myPI system=\"mySystem.xml\"?>\n<root><element attr=\"value\">Content</element></root>";
        Document doc = new XmlTreeBuilder().parse(xml, "baseUri");
        Element root = doc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.nodeName());
        Element pi = doc.head().child(0);
        assertNotNull(pi);
        assertEquals("myPI", pi.nodeName());
        assertEquals("mySystem.xml", pi.attr("system"));
        Element element = root.child(0);
        assertNotNull(element);
        assertEquals("element", element.nodeName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Content", element.text());
    }

    @Test
    public void testParseXMLWithDoctype() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE root [ <!ATTLIST element attr CDATA #IMPLIED> ]>\n<root>\n<element attr=\"value\">Content</element>\n</root>";
        Document doc = new XmlTreeBuilder().parse(xml, "baseUri");
        Element root = doc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.nodeName());
        DocumentType doctype = doc.doctype();
        assertNotNull(doctype);
        assertEquals("root", doctype.nodeName());
        assertEquals("[ <!ATTLIST element attr CDATA #IMPLIED> ]", doctype.getInternalSubset());
        Element element = root.child(0);
        assertNotNull(element);
        assertEquals("element", element.nodeName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Content", element.text());
    }

    @Test
    public void testParseXMLWithComment() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!-- This is a comment -->\n<root>\n<element attr=\"value\">Content</element>\n</root>";
        Document doc = new XmlTreeBuilder().parse(xml, "baseUri");
        Element root = doc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.nodeName());
        Comment comment = root.child(0);
        assertNull(comment);
        Element element = root.child(1);
        assertNotNull(element);
        assertEquals("element", element.nodeName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Content", element.text());
    }

    @Test
    public void testParseXMLWithSelfClosingTag() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><selfClosingTag attr=\"value\"/></root>";
        Document doc = new XmlTreeBuilder().parse(xml, "baseUri");
        Element root = doc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.nodeName());
        Element selfClosingTag = root.child(0);
        assertNotNull(selfClosingTag);
        assertEquals("selfClosingTag", selfClosingTag.nodeName());
        assertEquals("value", selfClosingTag.attr("attr"));
        assertEquals(0, selfClosingTag.childNodes().size());
    }

    @Test
    public void testParseXMLWithNestedTags() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><element attr=\"value1\">\n<nestedElement attr=\"value2\">Nested content</nestedElement>\n</element>\n</root>";
        Document doc = new XmlTreeBuilder().parse(xml, "baseUri");
        Element root = doc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.nodeName());
        Element element = root.child(0);
        assertNotNull(element);
        assertEquals("element", element.nodeName());
        assertEquals("value1", element.attr("attr"));
        assertEquals(1, element.childNodes().size());
        Element nestedElement = element.child(0);
        assertNotNull(nestedElement);
        assertEquals("nestedElement", nestedElement.nodeName());
        assertEquals("value2", nestedElement.attr("attr"));
        assertEquals("Nested content", nestedElement.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXMLWithInvalidTokenType() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><!--Invalid-->";
        new XmlTreeBuilder().parse(xml, "baseUri"); // This test will throw an IllegalArgumentException
    }

    @Test
    public void testParseXMLWithFragment() {
        String xmlFragment = "<root><element attr=\"value\">Content</element></root>";
        List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlFragment, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase).stream().filter(node -> node != null).collect(Collectors.toList());
        Document doc = new Document();
        doc.setDefaultMetaCharset("UTF-8");
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        for (Node node : nodes) {
            doc.appendChild(node);
        }
        Element root = doc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.nodeName());
        Element element = root.child(0);
        assertNotNull(element);
        assertEquals("element", element.nodeName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Content", element.text());
    }
}