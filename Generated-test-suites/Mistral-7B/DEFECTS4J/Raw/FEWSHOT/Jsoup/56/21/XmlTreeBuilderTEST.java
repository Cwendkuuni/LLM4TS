package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testXmlTreeBuilderDefaultSettings() {
        String xmlInput = "<root><element attr1=\"value1\" attr2='value2'><subElement></subElement></element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(xmlInput, "");
        Element rootElement = doc.getRootElement();
        assertNotNull(rootElement);
        assertEquals("root", rootElement.nodeName());
        List<Node> children = rootElement.children();
        assertEquals(1, children.size());
        Element element = (Element) children.get(0);
        assertEquals("element", element.nodeName());
        List<Attribute> attributes = element.attributes();
        assertEquals(2, attributes.size());
        Attribute attr1 = attributes.get(0);
        assertEquals("attr1", attr1.getKey());
        assertEquals("value1", attr1.getValue());
        Attribute attr2 = attributes.get(1);
        assertEquals("attr2", attr2.getKey());
        assertEquals("value2", attr2.getValue());
        Element subElement = (Element) element.children().get(0);
        assertEquals("subElement", subElement.nodeName());
        assertTrue(subElement.children().isEmpty());
    }

    @Test
    public void testXmlTreeBuilderWithComment() {
        String xmlInput = "<root><!-- comment --><element><!-- comment --></element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(xmlInput, "");
        Element rootElement = doc.getRootElement();
        List<Node> children = rootElement.children();
        assertEquals(1, children.size());
        Element element = (Element) children.get(0);
        List<Node> elementChildren = element.children();
        assertEquals(1, elementChildren.size());
        Comment comment = (Comment) elementChildren.get(0);
        assertEquals("comment", comment.data());
        List<Node> rootChildren = doc.children();
        assertEquals(1, rootChildren.size());
        Comment rootComment = (Comment) rootChildren.get(0);
        assertEquals(" comment ", rootComment.data());
    }

    @Test
    public void testXmlTreeBuilderWithDoctype() {
        String xmlInput = "<!DOCTYPE root SYSTEM \"root.dtd\">\n<root></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(xmlInput, "");
        DocumentType doctypeNode = doc.doctype();
        assertEquals("root", doctypeNode.nodeName());
        assertEquals("root.dtd", doctypeNode.systemId());
        List<Node> rootChildren = doc.children();
        assertEquals(1, rootChildren.size());
        Element rootElement = (Element) rootChildren.get(0);
        assertTrue(rootElement.children().isEmpty());
    }

    @Test
    public void testXmlTreeBuilderWithSelfClosingTag() {
        String xmlInput = "<root><element></element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(xmlInput, "");
        Element rootElement = doc.getRootElement();
        List<Node> children = rootElement.children();
        assertEquals(1, children.size());
        Element element = (Element) children.get(0);
        assertEquals("element", element.nodeName());
        List<Attribute> attributes = element.attributes();
        assertTrue(attributes.isEmpty());
        assertTrue(element.children().isEmpty());
    }

    @Test(expected = RuntimeException.class)
    public void testXmlTreeBuilderWithInvalidToken() {
        String xmlInput = "<root><!--comment<element>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(xmlInput, "");
    }

    @Test
    public void testXmlTreeBuilderWithFragment() {
        String xmlFragment = "<element><subElement></subElement></element>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Document doc = new Document();
        doc.setRootElement(nodes.get(0));
        assertEquals(1, doc.childNodes().size());
        Element rootElement = (Element) doc.childNodes().get(0);
        assertEquals("element", rootElement.nodeName());
        List<Node> children = rootElement.children();
        assertEquals(1, children.size());
        Element subElement = (Element) children.get(0);
        assertEquals("subElement", subElement.nodeName());
        assertTrue(subElement.children().isEmpty());
    }

    @Test
    public void testXmlTreeBuilderWithEmptyFragment() {
        String xmlFragment = "";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Document doc = new Document();
        doc.setRootElement(nodes.get(0));
        assertTrue(doc.childNodes().isEmpty());
    }
}