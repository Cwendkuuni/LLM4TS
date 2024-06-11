package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.StringReader;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<root><element attr1=\"value1\" attr2=\"value2\"><subElement/></element></root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "http://test.com");
        assertNotNull(xmlDoc);
        Element root = xmlDoc.getRootElement();
        Element element = root.element(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());
        List<Attribute> attributes = element.attributes();
        assertEquals(2, attributes.size());
        for (Attribute attribute : attributes) {
            assertEquals("attr1", attribute.getKey());
            assertEquals("value1", attribute.getValue());
        }
        Element subElement = element.element(0);
        assertNull(subElement);
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<subElement/>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase).stream().collect(Collectors.toList());
        Element subElement = (Element) nodes.get(0);
        assertNotNull(subElement);
        assertEquals("subElement", subElement.tagName());
    }

    @Test
    public void testProcessStartTag() {
        String xmlString = "<root><element attr1=\"value1\" attr2=\"value2\"><subElement/></element></root>";
        TreeBuilder tb = new XmlTreeBuilder();
        StringReader reader = new StringReader(xmlString);
        tb.initialiseParse(reader, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Node node;
        while ((node = tb.processNextToken()) != null) {
            tb.process(node);
        }

        Element root = (Element) tb.doc.child(0);
        Element element = (Element) root.child(0);
        assertEquals("element", element.tagName());
        List<Attribute> attributes = element.attributes();
        assertEquals(2, attributes.size());
        for (Attribute attribute : attributes) {
            assertEquals("attr1", attribute.getKey());
            assertEquals("value1", attribute.getValue());
        }
        Element subElement = (Element) element.child(0);
        assertNull(subElement);
    }

    @Test
    public void testProcessEndTag() {
        String xmlString = "<root><element><subElement/></element></root>";
        TreeBuilder tb = new XmlTreeBuilder();
        StringReader reader = new StringReader(xmlString);
        tb.initialiseParse(reader, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Node node;
        while ((node = tb.processNextToken()) != null) {
            tb.process(node);
        }

        List<Node> stack = tb.stack;
        Element root = (Element) stack.get(stack.size() - 1);
        Element element = (Element) stack.get(stack.size() - 2);
        Element subElement = (Element) stack.get(stack.size() - 3);
        assertNotNull(root);
        assertNotNull(element);
        assertNotNull(subElement);
        assertEquals("root", root.tagName());
        assertEquals("element", element.tagName());
        assertEquals("subElement", subElement.tagName());

        tb.popStackToClose((Token.EndTag) stack.remove(stack.size() - 1));
        assertEquals(stack.size(), 2);
    }

    @Test
    public void testProcessComment() {
        String xmlString = "<!-- This is a comment -->";
        TreeBuilder tb = new XmlTreeBuilder();
        StringReader reader = new StringReader(xmlString);
        tb.initialiseParse(reader, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Node node;
        while ((node = tb.processNextToken()) != null) {
            tb.process(node);
        }

        List<Node> stack = tb.stack;
        Comment comment = (Comment) stack.get(stack.size() - 1);
        assertNotNull(comment);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testProcessCharacter() {
        String xmlString = "<root>&amp;<foo/></root>";
        TreeBuilder tb = new XmlTreeBuilder();
        StringReader reader = new StringReader(xmlString);
        tb.initialiseParse(reader, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Node node;
        while ((node = tb.processNextToken()) != null) {
            tb.process(node);
        }

        List<Node> stack = tb.stack;
        Element root = (Element) stack.get(stack.size() - 1);
        TextNode character = (TextNode) stack.get(stack.size() - 2);
        assertNotNull(root);
        assertNotNull(character);
        assertEquals("&amp;", character.text());
    }

    @Test
    public void testProcessDoctype() {
        String xmlString = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]>";
        TreeBuilder tb = new XmlTreeBuilder();
        StringReader reader = new StringReader(xmlString);
        tb.initialiseParse(reader, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Node node;
        while ((node = tb.processNextToken()) != null) {
            tb.process(node);
        }

        List<Node> stack = tb.stack;
        DocumentType doctype = (DocumentType) stack.get(stack.size() - 1);
        assertNotNull(doctype);
        assertEquals("root", doctype.nodeName());
        assertEquals("[ <!ATTLIST element attr CDATA #REQUIRED > ]", doctype.systemIdentifier());
        List<AttributeSpec> attributeSpecs = doctype.attributeSpecs();
        assertEquals(1, attributeSpecs.size());
        AttributeSpec attributeSpec = attributeSpecs.get(0);
        assertEquals("element", attributeSpec.getName());
        assertEquals("attr", attributeSpec.getName().toLowerCase());
        assertEquals("CDATA", attributeSpec.getType());
        assertEquals("#REQUIRED", attributeSpec.getDefaultValue());
    }

    @Test
    public void testProcessEOF() {
        String xmlString = "<root><element><subElement/></element></root>";
        TreeBuilder tb = new XmlTreeBuilder();
        StringReader reader = new StringReader(xmlString);
        tb.initialiseParse(reader, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Node node;
        while ((node = tb.processNextToken()) != null) {
            tb.process(node);
        }
        assertTrue(tb.doc.childNodes().isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void testProcessUnexpectedTokenType() {
        String xmlString = "<root><element><subElement/></element></root><notALeghitTag/>";
        TreeBuilder tb = new XmlTreeBuilder();
        StringReader reader = new StringReader(xmlString);
        tb.initialiseParse(reader, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Node node;
        while ((node = tb.processNextToken()) != null) {
            tb.process(node);
        }
    }
}