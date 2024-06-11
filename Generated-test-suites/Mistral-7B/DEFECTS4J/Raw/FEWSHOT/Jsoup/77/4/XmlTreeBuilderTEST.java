package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseXmlDocument() {
        String xmlString = "<root><element1 attr1=\"value1\">Content1</element1><element2 attr2=\"value2\"/></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://test.com");

        Element root = xmlDoc.rootElement();
        List<Node> nodes = root.childNodes();

        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                if (i == 0) {
                    assertTrue(element.tagName().equals("root"));
                    List<Attribute> attributes = element.attributes();
                    assertTrue(attributes.size() == 0);
                } else if (i == 1) {
                    assertTrue(element.tagName().equals("element1"));
                    List<Attribute> attributes = element.attributes();
                    assertTrue(attributes.size() == 1);
                    Attribute attr1 = attributes.get(0);
                    assertTrue(attr1.getKey().equals("attr1"));
                    assertTrue(attr1.getValue().equals("value1"));
                } else {
                    assertTrue(element.tagName().equals("element2"));
                    List<Attribute> attributes = element.attributes();
                    assertTrue(attributes.size() == 0);
                }
            }
        }
    }

    @Test
    public void testParseEmptyXmlDocument() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(""), "http://test.com");
        assertTrue(xmlDoc.childNodes().size() == 0);
    }

    @Test
    public void testParseXmlDocumentWithComment() {
        String xmlString = "<root><!-- This is a comment --><element attr=\"value\">Content</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://test.com");

        Element root = xmlDoc.rootElement();
        List<Node> nodes = root.childNodes();

        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                if (i == 0) {
                    assertTrue(element.tagName().equals("root"));
                    List<Attribute> attributes = element.attributes();
                    assertTrue(attributes.size() == 0);
                } else {
                    assertTrue(element.tagName().equals("element"));
                    List<Attribute> attributes = element.attributes();
                    assertTrue(attributes.size() == 1);
                    Attribute attr = attributes.get(0);
                    assertTrue(attr.getKey().equals("attr"));
                    assertTrue(attr.getValue().equals("value"));
                }
            } else if (node instanceof Comment) {
                Comment comment = (Comment) node;
                assertTrue(comment.getData().equals(" This is a comment "));
            }
        }
    }

    @Test
    public void testParseXmlDocumentWithCDataSection() {
        String xmlString = "<root><![CDATA[This is a CData section]]><element attr=\"value\">Content</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://test.com");

        Element root = xmlDoc.rootElement();
        List<Node> nodes = root.childNodes();

        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                if (i == 0) {
                    assertTrue(element.tagName().equals("root"));
                    List<Attribute> attributes = element.attributes();
                    assertTrue(attributes.size() == 0);
                } else {
                    assertTrue(element.tagName().equals("element"));
                    List<Attribute> attributes = element.attributes();
                    assertTrue(attributes.size() == 1);
                    Attribute attr = attributes.get(0);
                    assertTrue(attr.getKey().equals("attr"));
                    assertTrue(attr.getValue().equals("value"));
                }
            } else if (node instanceof TextNode) {
                TextNode textNode = (TextNode) node;
                assertTrue(textNode.text().equals("This is a CData section"));
            }
        }
    }

    @Test
    public void testParseXmlDocumentWithDoctype() {
        String xmlString = "<!DOCTYPE root SYSTEM \"http://test.com/dtd.xml\">" +
                "<root><element attr=\"value\">Content</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://test.com");

        DocumentType doctype = xmlDoc.doctype();
        assertTrue(doctype != null);
        assertTrue(doctype.getName().equals("root"));
        assertTrue(doctype.getPublicId().equals("http://test.com/dtd.xml"));
        assertTrue(doctype.getSystemId().equals("http://test.com/dtd.xml"));

        Element root = xmlDoc.rootElement();
        List<Node> nodes = root.childNodes();

        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                if (i == 0) {
                    assertTrue(element.tagName().equals("root"));
                    List<Attribute> attributes = element.attributes();
                    assertTrue(attributes.size() == 0);
                } else {
                    assertTrue(element.tagName().equals("element"));
                    List<Attribute> attributes = element.attributes();
                    assertTrue(attributes.size() == 1);
                    Attribute attr = attributes.get(0);
                    assertTrue(attr.getKey().equals("attr"));
                    assertTrue(attr.getValue().equals("value"));
                }
            }
        }
    }

    @Test
    public void testParseXmlFragment() {
        String xmlFragment = "<element1 attr1=\"value1\"><subElement attr2=\"value2\"/></element1>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase).get();

        Element element1 = (Element) nodes.get(0);
        List<Attribute> attributes1 = element1.attributes();
        assertTrue(attributes1.size() == 1);
        Attribute attr1 = attributes1.get(0);
        assertTrue(attr1.getKey().equals("attr1"));
        assertTrue(attr1.getValue().equals("value1"));

        Element subElement = (Element) nodes.get(1);
        List<Attribute> attributes2 = subElement.attributes();
        assertTrue(attributes2.size() == 1);
        Attribute attr2 = attributes2.get(0);
        assertTrue(attr2.getKey().equals("attr2"));
        assertTrue(attr2.getValue().equals("value2"));
    }

    @Test
    public void testParseXmlFragmentWithCDataSection() {
        String xmlFragment = "<![CDATA[This is a CData section]]><element attr=\"value\">Content</element>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase).get();

        TextNode cdataNode = (TextNode) nodes.get(0);
        assertTrue(cdataNode.text().equals("This is a CData section"));

        Element element = (Element) nodes.get(1);
        List<Attribute> attributes = element.attributes();
        assertTrue(attributes.size() == 1);
        Attribute attr = attributes.get(0);
        assertTrue(attr.getKey().equals("attr"));
        assertTrue(attr.getValue().equals("value"));
    }

    @Test
    public void testParseXmlFragmentWithComment() {
        String xmlFragment = "<!-- This is a comment --><element attr=\"value\">Content</element>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase).get();

        Comment comment = (Comment) nodes.get(0);
        assertTrue(comment.getData().equals(" This is a comment "));

        Element element = (Element) nodes.get(1);
        List<Attribute> attributes = element.attributes();
        assertTrue(attributes.size() == 1);
        Attribute attr = attributes.get(0);
        assertTrue(attr.getKey().equals("attr"));
        assertTrue(attr.getValue().equals("value"));
    }

    @Test
    public void testParseXmlFragmentWithDoctype() {
        String xmlFragment = "<!DOCTYPE root [<!ATTLIST root attr CDATA #REQUIRED>]><root><element attr=\"value\">Content</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase).get();

        DocumentType doctype = (DocumentType) nodes.get(0);
        assertTrue(doctype.getName().equals("root"));
        assertTrue(doctype.getPublicId() == null);
        assertTrue(doctype.getSystemId() == null);
        List<Attribute> attributes = doctype.attributes();
        Attribute attr = attributes.get(0);
        assertTrue(attr.getKey().equals("ATTLIST"));
        List<Attribute> attributeList = attr.getValue().asAttributes();
        Attribute attr1 = attributeList.get(0);
        assertTrue(attr1.getKey().equals("root"));
        assertTrue(attr1.getValue().getValue().equals("attr CDATA #REQUIRED"));

        Element root = (Element) nodes.get(1);
        List<Attribute> attributesRoot = root.attributes();
        assertTrue(attributesRoot.size() == 0);

        Element element = (Element) nodes.get(2);
        List<Attribute> attributesElement = element.attributes();
        assertTrue(attributesElement.size() == 1);
        Attribute attrElement = attributesElement.get(0);
        assertTrue(attrElement.getKey().equals("attr"));
        assertTrue(attrElement.getValue().equals("value"));
    }
}