package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseFragment() {
        String xmlString = "<root><element attr1=\"value1\">content</element><anotherElement/></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlString, "", new ParseErrorList());

        assertEquals(2, parsedNodes.size());

        Element rootElement = (Element) parsedNodes.get(0);
        assertEquals("root", rootElement.nodeName());

        List<Node> children = rootElement.childNodes();
        assertEquals(2, children.size());

        Element element1 = (Element) children.get(0);
        assertEquals("element", element1.nodeName());
        assertEquals("value1", element1.attribute("attr1"));
        assertEquals("content", element1.childNodes().get(0).nodeValue());

        Element anotherElement = (Element) children.get(1);
        assertEquals("anotherElement", anotherElement.nodeName());
        assertTrue(anotherElement.childNodes().isEmpty());
    }

    @Test
    public void testParseEmptyXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> parsedNodes = xmlTreeBuilder.parseFragment("", "", new ParseErrorList());
        assertTrue(parsedNodes.isEmpty());
    }

    @Test
    public void testParseInvalidXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String invalidXml = "<root<element>";
        ParseErrorList errors = new ParseErrorList();
        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(invalidXml, "", errors);
        assertNotNull(errors.size());
        assertTrue(parsedNodes.isEmpty());
    }

    @Test
    public void testParseXmlWithComments() {
        String xmlString = "<root><!--comment1-->\n<element attr1=\"value1\">content</element>\n<!--comment2-->\n</root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlString, "", new ParseErrorList());

        assertEquals(3, parsedNodes.size());

        Element rootElement = (Element) parsedNodes.get(0);
        List<Node> children = rootElement.childNodes();

        Node comment1 = children.get(0);
        assertEquals(Node.COMMENT_NODE, comment1.nodeType());

        Element element1 = (Element) children.get(1);
        assertEquals("element", element1.nodeName());
        // ... test other node properties as done in previous test

        Node comment2 = children.get(2);
        assertEquals(Node.COMMENT_NODE, comment2.nodeType());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        String xmlString = "<root><element attr1=\"value1\" />content</root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlString, "", new ParseErrorList());

        assertEquals(2, parsedNodes.size());

        Element rootElement = (Element) parsedNodes.get(0);
        List<Node> children = rootElement.childNodes();

        Element element1 = (Element) children.get(0);
        assertEquals("element", element1.nodeName());
        assertEquals("value1", element1.attribute("attr1"));
        assertTrue(element1.isSelfClosing());
        assertTrue(element1.childNodes().isEmpty());

        Text content = (Text) children.get(1);
        assertEquals("content", content.nodeValue());
    }

    @Test
    public void testParseXmlWithDoctype() {
        String xmlString = "<!DOCTYPE root [ <!-- Declaration --> ]><root></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlString, "", new ParseErrorList());

        assertEquals(2, parsedNodes.size());

        DocumentType doctype = (DocumentType) parsedNodes.get(0);
        assertEquals("root", doctype.nodeName());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());

        Element rootElement = (Element) parsedNodes.get(1);
        assertTrue(rootElement.childNodes().isEmpty());
    }
}