package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<root><element>Hello World</element></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xmlString), "baseUrl");
        assertNotNull(doc);
        assertNotNull(doc.childNodes());
        assertNotNull(doc.childNodes().get(0));
        assertNotNull(((Element) doc.childNodes().get(0)).childNodes());
        assertNotNull(((Element) doc.childNodes().get(0)).childNodes().get(0));
        assertEquals("Hello World", ((TextNode) ((Element) doc.childNodes().get(0)).childNodes().get(0)).getText());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element>Hello World</element>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "baseUrl", ParseErrorList.noTracking(), ParseSettings.preserveCase).get();
        assertNotNull(nodes);
        assertNotNull(nodes.get(0));
        assertNotNull(((Element) nodes.get(0)).childNodes());
        assertNotNull(((Element) nodes.get(0)).childNodes().get(0));
        assertEquals("Hello World", ((TextNode) ((Element) nodes.get(0)).childNodes().get(0)).getText());
    }

    @Test
    public void testProcessStartTag() {
        String xmlString = "<root><element/></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "baseUrl", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        Element root = (Element) xmlTreeBuilder.doc.childNodes().get(0);
        Element element = (Element) root.childNodes().get(0);
        assertTrue(element.isSelfClosing());
    }

    @Test
    public void testProcessEndTag() {
        String xmlString = "<root><element><subElement/></element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "baseUrl", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        Element root = (Element) xmlTreeBuilder.doc.childNodes().get(0);
        Element element = (Element) root.childNodes().get(0);
        assertEquals(1, stackSize(element));
        Stack<Element> stack = xmlTreeBuilder.stack;
        Element subElement = (Element) stack.get(stack.size() - 1);
        stack.remove(stack.size() - 1);
        assertNull(subElement);
    }

    @Test
    public void testProcessComment() {
        String xmlString = "<root><!-- Hello World --></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "baseUrl", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        List<Node> commentNodes = xmlTreeBuilder.doc.childNodes();
        Node comment = commentNodes.get(1);
        assertTrue(comment instanceof Comment);
        assertEquals("Hello World", comment.toString());
    }

    @Test
    public void testProcessCharacter() {
        String xmlString = "<root>&#x27;Hello World&#x27;</root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "baseUrl", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        List<Node> textNodes = xmlTreeBuilder.doc.childNodes();
        Node textNode = textNodes.get(1);
        assertTrue(textNode instanceof TextNode);
        assertEquals("Hello World", textNode.toString());
    }

    @Test
    public void testProcessDoctype() {
        String xmlString = "<!DOCTYPE root SYSTEM \"document.dtd\" []>\r\n<root></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "baseUrl", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
        assertEquals("root", doctypeNode.nodeName());
        assertEquals("document.dtd", doctypeNode.getSystemIdentifier());
    }

    private int stackSize(Element element) {
        int size = 0;
        for (int i = 0; i < stack.size(); i++) {
            if (stack.get(i).equals(element)) {
                size = i + 1;
                break;
            }
        }
        return size;
    }

    @Test(expected = AssertionError.class)
    public void testUnexpectedTokenType() {
        String xmlString = "<root><!-- Hello World -->< unexpectedTag/></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "baseUrl", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
    }

    @Test
    public void testParseWithInvalidXml() {
        String xmlString = "<root><!-- Hello World --> <element>"; // invalid xml with an unclosed tag
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "baseUrl", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        try {
            xmlTreeBuilder.runParser();
            fail("Expected parse error.");
        } catch (AssertionError e) {
            // expected
        }
        assertNull(xmlTreeBuilder.doc);
    }
}