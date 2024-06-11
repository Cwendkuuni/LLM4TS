package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.nodes.Attributes.fromMap;
import static org.jsoup.nodes.Xml.*;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xml = "<root><element attr1='value1' attr2='value2'><subElement attr3='value3'/></element></root>";
        Document doc = XmlTreeBuilder.parse(xml, "http://test.com");
        Element root = doc.rootElement();
        Element element = root.getChild(0);
        Element subElement = element.getChild(0);

        assertEquals("root", root.tagName());
        assertEquals("element", element.tagName());
        assertEquals("subElement", subElement.tagName());
        assertEquals("http://test.com", root.baseUri());

        Map<String, String> attributes = new HashMap<>();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        assertEquals(attributes, element.attributes());

        assertEquals("value3", subElement.attributes().get("attr3"));
    }

    @Test
    public void testParseSelfClosingTag() {
        String xml = "<root><element xmlns:x='http://test'><x:tag /></element></root>";
        Document doc = XmlTreeBuilder.parse(xml, "http://test.com");
        Element root = doc.rootElement();
        Element element = root.getChild(0);
        Element tag = element.getChild(0);

        assertEquals("root", root.tagName());
        assertEquals("element", element.tagName());
        assertEquals("tag", tag.tagName());
        assertTrue(tag.isSelfClosing());
        assertEquals("http://test", tag.namespaceURI());
    }

    @Test
    public void testParseDoctype() {
        String xml = "<!DOCTYPE root SYSTEM 'http://test.com/doctype.dtd'><root></root>";
        Document doc = XmlTreeBuilder.parse(xml, "http://test.com");
        DocumentType doctype = doc.doctype();

        assertEquals("root", doc.rootElement().tagName());
        assertEquals("root", doctype.getName());
        assertEquals("http://test.com/doctype.dtd", doctype.systemLocation());
    }

    @Test
    public void testParseComment() {
        String xml = "<!-- This is a comment --><root><element /><!-- Another comment --></root>";
        Document doc = XmlTreeBuilder.parse(xml, "http://test.com");
        List<Node> comments = doc.childNodes();

        assertEquals(2, comments.size());
        assertEquals(Comment.class, comments.get(0).getClass());
        assertEquals("This is a comment", comments.get(0).toString());
        assertEquals(Comment.class, comments.get(1).getClass());
        assertEquals("Another comment", comments.get(1).toString());
    }

    @Test
    public void testParseUnknownTag() {
        String xml = "<unknownTag />";
        Document doc = XmlTreeBuilder.parse(xml, "http://test.com");
        Element element = doc.getElementsByTag("unknownTag").get(0);

        assertTrue(element.isUnknown());
        assertTrue(element.isSelfClosing());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><element><subElement><grandChild /></subElement></element></root>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "http://test.com", null, ParseSettings.defaultSettings());

        Node root = nodes.get(0);
        assertNotNull(root);
        assertEquals(Element.class, root.getClass());
        assertEquals("root", root.tagName());

        Node element = nodes.get(1);
        assertNotNull(element);
        assertEquals(Element.class, element.getClass());
        assertEquals("element", element.tagName());

        Node subElement = nodes.get(2);
        assertNotNull(subElement);
        assertEquals(Element.class, subElement.getClass());
        assertEquals("subElement", subElement.tagName());

        Node grandChild = nodes.get(3);
        assertNotNull(grandChild);
        assertEquals(Text.class, grandChild.getClass());
        assertNull(grandChild.attributes());

        assertEquals(4, nodes.size());
    }

    @Test
    public void testParseEmptyDocument() {
        String xml = "<root />";
        Document doc = XmlTreeBuilder.parse(xml, "http://test.com");
        Element root = doc.rootElement();

        assertEquals("root", root.tagName());
        assertEquals(Element.class, root.getClass());
        assertNull(root.childNodes());
    }
}