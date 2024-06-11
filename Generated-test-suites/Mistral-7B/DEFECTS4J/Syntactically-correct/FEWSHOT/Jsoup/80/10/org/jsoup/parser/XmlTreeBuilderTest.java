package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.List;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.helper.Validate;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParseXml() {
        String xmlString = "<root><elem attr1=\"value1\">Content with &amp; special chars</elem><elem2 attr2=\"value2\"/></root>";
        XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlBuilder.parse(new StringReader(xmlString), "base-uri");

        Element root = xmlDoc.select("root").first();
        assertNotNull(root);

        List<Element> elements = new ArrayList<>();
        elements.addAll(root.children());

        assertEquals(2, elements.size());
        Element elem1 = elements.get(0);
        assertEquals("elem", elem1.tagName());
        assertTrue(elem1.attributes().size() > 0);
        assertEquals("attr1", elem1.attributes().get("attr1").getKey());
        assertEquals("value1", elem1.attributes().get("attr1").getValue());
        assertEquals("Content with &amp; special chars", elem1.text());

        Element elem2 = elements.get(1);
        assertEquals("elem2", elem2.tagName());
        assertTrue(elem2.attributes().isEmpty());
    }

    @Test
    public void testParseXmlWithComments() {
        String xmlString = "<root><!-- comment1 --><elem attr1=\"value1\">Content with &amp; special chars</elem><!-- comment2 --></root>";
        XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlBuilder.parse(new StringReader(xmlString), "base-uri");

        Element root = xmlDoc.select("root").first();
        assertNotNull(root);

        List<Node> comments = new ArrayList<>();
        comments.addAll(root.children());
        comments.removeAll(root.children(Element.class));

        assertEquals(2, comments.size());
        Comment comment1 = (Comment) comments.get(0);
        assertEquals("comment1", comment1.text());

        Comment comment2 = (Comment) comments.get(1);
        assertEquals("comment2", comment2.text());
    }

    @Test
    public void testParseXmlWithCDataSection() {
        String xmlString = "<root><![CDATA[Content with &amp; special chars]]><elem attr1=\"value1\">Content with <tag> special chars</elem></root>";
        XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlBuilder.parse(new StringReader(xmlString), "base-uri");

        Element root = xmlDoc.select("root").first();
        assertNotNull(root);

        List<Node> cdataSections = new ArrayList<>();
        cdataSections.addAll(root.children());
        cdataSections.removeAll(root.children(Element.class, Comment.class));

        assertEquals(1, cdataSections.size());
        CDataNode cdataNode = (CDataNode) cdataSections.get(0);
        assertEquals("Content with &amp; special chars", cdataNode.getData());
    }

    @Test
    public void testParseXmlWithSelfClosingTags() {
        String xmlString = "<root><elem attr1=\"value1\"/><elem2 attr2=\"value2\"/></root>";
        XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlBuilder.parse(new StringReader(xmlString), "base-uri");

        Element root = xmlDoc.select("root").first();
        assertNotNull(root);

        List<Element> elements = new ArrayList<>();
        elements.addAll(root.children());

        assertEquals(2, elements.size());
        Element elem1 = elements.get(0);
        assertEquals("elem", elem1.tagName());
        assertTrue(elem1.attributes().size() > 0);
        assertEquals("attr1", elem1.attributes().get("attr1").getKey());
        assertEquals("value1", elem1.attributes().get("attr1").getValue());
        assertTrue(elem1.hasAttr("xmlns"));
        assertTrue(elem1.isClosed());

        Element elem2 = elements.get(1);
        assertEquals("elem2", elem2.tagName());
        assertTrue(elem2.attributes().isEmpty());
        assertTrue(elem2.isClosed());
    }

    @Test
    public void testParseXmlWithEmptyElement() {
        String xmlString = "<root><elem attr1=\"value1\"/></root>";
        XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlBuilder.parse(new StringReader(xmlString), "base-uri");

        Element root = xmlDoc.select("root").first();
        assertNotNull(root);

        List<Element> elements = new ArrayList<>();
        elements.addAll(root.children());

        assertEquals(1, elements.size());
        Element elem = elements.get(0);
        assertEquals("elem", elem.tagName());
        assertTrue(elem.attributes().size() > 0);
        assertEquals("attr1", elem.attributes().get("attr1").getKey());
        assertEquals("value1", elem.attributes().get("attr1").getValue());
        assertTrue(elem.isClosed());
        assertTrue(elem.childNodes().isEmpty());
    }

    @Test
    public void testParseXmlWithInvalidInput() {
        XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
        try {
            xmlBuilder.parse(new StringReader(""), "base-uri");
            Validate.fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testParseXmlFragment() {
        String xmlFragment = "<elem attr1=\"value1\">Content with &amp; special chars</elem>";
        XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlBuilder.parseFragment(xmlFragment, "base-uri", ParseErrorList.noTracking(), ParseSettings.preserveCase).get();

        Node node = nodes.get(0);
        assertEquals(Element.class, node.getClass());
        Element elem = (Element) node;
        assertEquals("elem", elem.tagName());
        assertTrue(elem.attributes().size() > 0);
        assertEquals("attr1", elem.attributes().get("attr1").getKey());
        assertEquals("value1", elem.attributes().get("attr1").getValue());
        assertEquals("Content with &amp; special chars", elem.text());
    }
}