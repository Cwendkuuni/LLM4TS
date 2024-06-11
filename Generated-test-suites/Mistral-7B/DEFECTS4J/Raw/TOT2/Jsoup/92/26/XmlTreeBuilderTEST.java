package org.junit.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {

    @Test
    public void testParseXmlDocument() {
        String xmlString = "<root><element attr='value'></element></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString));
        assertNotNull(xmlDoc);
        assertNotNull(xmlDoc.rootElement());
        assertNotNull(xmlDoc.rootElement().child(0));
        assertNotNull(xmlDoc.rootElement().child(0).attr("attr"));
        assertNotNull(xmlDoc.rootElement().child(0).text());
    }

    @Test
    public void testParseEmptyXmlDocument() {
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(""));
        assertNotNull(xmlDoc);
        assertNotNull(xmlDoc.rootElement());
        assertNull(xmlDoc.rootElement().child(0));
    }

    @Test
    public void testParseXmlWithInvalidXml() {
        String invalidXmlString = "<root><element><";
        try {
            Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(invalidXmlString));
            fail("It should not be able to parse invalid XML");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testStartTag() {
        String xmlString = "<root><element attr='value' /><element2 attr='value2'></element2></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString));
        Element root = xmlDoc.rootElement();
        Element element = root.child(0);
        Element element2 = root.child(1);

        assertEquals("element", element.tagName());
        assertEquals("element2", element2.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("value2", element2.attr("attr"));
        assertNull(element.text());
        assertNull(element2.text());
    }

    @Test
    public void testSelfClosingStartTag() {
        String xmlString = "<root><element attr='value' /> <element2 attr='value2'></element2></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString));
        Element root = xmlDoc.rootElement();
        Element element = root.child(0);
        Element element2 = root.child(1);

        assertEquals("element", element.tagName());
        assertEquals("element2", element2.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("value2", element2.attr("attr"));
        assertNull(element.text());
        assertNull(element2.text());
    }

    @Test
    public void testEndTag() {
        String xmlString = "<root><element attr='value'><element2 attr='value2'></element2></element></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString));
        Element root = xmlDoc.rootElement();
        Element element = root.child(0);
        Element element2 = element.child(0);

        assertEquals(2, root.childCount());
        assertEquals(1, element.childCount());

        assertEquals("element", element.tagName());
        assertEquals("element2", element2.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("value2", element2.attr("attr"));
    }

    @Test
    public void testParseXmlWithEmptyElements() {
        String xmlString = "<root><element attr='value'></element><element2 attr='value2'></element2></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString));
        Element root = xmlDoc.rootElement();
        Element element = root.child(0);
        Element element2 = root.child(1);

        assertEquals("root", root.tagName());
        assertEquals("element", element.tagName());
        assertEquals("element2", element2.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("value2", element2.attr("attr"));
        assertNull(element.text());
        assertNull(element2.text());
    }

    @Test
    public void testParseXmlWithComments() {
        String xmlString = "<root><!-- comment1 --><element attr='value'><!-- comment2 --></element><!-- comment3 --></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString));
        Element root = xmlDoc.rootElement();
        Element element = root.child(0);

        assertEquals(1, root.childCount());
        assertEquals(1, element.childCount());

        assertEquals("root", root.tagName());
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));

        List<Node> comments = element.childTraversingElements();
        assertEquals(2, comments.size());

        TextNode comment1 = (TextNode) comments.get(0);
        TextNode comment2 = (TextNode) comments.get(1);

        assertEquals("comment1", comment1.data());
        assertEquals("comment2", comment2.data());
    }

    @Test
    public void testParseXmlWithCDataSections() {
        String xmlString = "<root><![CDATA[<script>alert(\"Hello World\");</script>]]><element attr='value'></element></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString));
        Element root = xmlDoc.rootElement();
        Element element = root.child(0);

        assertEquals(1, root.childCount());
        assertEquals(1, element.childCount());

        assertEquals("root", root.tagName());
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));

        List<Node> cdataSections = element.childTraversingElements();
        assertEquals(1, cdataSections.size());

        CDataNode cdataSection = (CDataNode) cdataSections.get(0);

        assertTrue(cdataSection.data().startsWith("<script>"));
        assertTrue(cdataSection.data().endsWith("</script>"));
    }

    @Test
    public void testParseXmlWithDoctypeDeclaration() {
        String xmlString = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED> ]><root><element attr='value'></element></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString));
        Element root = xmlDoc.rootElement();
        Element element = root.child(0);

        assertEquals("root", root.tagName());
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));

        DocumentType docType = root.document().doctype();
        assertEquals("root", docType.name());
        assertEquals("CDATA", docType.inputSpec());
        assertNotNull(docType.entity("element"));
    }
}