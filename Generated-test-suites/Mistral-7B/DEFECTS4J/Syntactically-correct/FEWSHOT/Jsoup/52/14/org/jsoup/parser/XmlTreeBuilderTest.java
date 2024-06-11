package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.helper.Validate.*;

import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParseFragment() {
        String xmlString = "<root><element attr1=\"value1\" attr2='value2'><subElement/></element></root>";
        String baseUri = "http://test.com";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlString, baseUri, errors);

        Document doc = (Document) parsedNodes.get(0);
        Element root = (Element) doc.child(0);
        Element element = (Element) root.child(0);

        assertEquals("root", root.nodeName());
        assertEquals("element", element.nodeName());
        assertEquals("value1", element.attr("attr1"));
        assertEquals("value2", element.attr("attr2"));

        Element subElement = (Element) element.child(0);
        assertNull(subElement.attr("attr1"));
        assertNull(subElement.attr("attr2"));

        assertEquals(4, parsedNodes.size());
    }

    @Test
    public void testProcessUnknownTags() {
        String xmlString = "<unknownTag/><root><element attr1=\"value1\" attr2='value2'><subElement/></element></root>";
        String baseUri = "http://test.com";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlString, baseUri, errors);

        Document doc = (Document) parsedNodes.get(0);
        Element root = (Element) doc.child(0);
        Element element = (Element) root.child(1);

        assertEquals("element", element.nodeName());
        assertFalse(element.isSelfClosing());
        assertEquals(2, stack.size());

        Element unknownTag = (Element) root.child(0);
        assertTrue(unknownTag.isSelfClosing());
        assertEquals("unknownTag", unknownTag.nodeName());

        assertEquals(5, parsedNodes.size());
    }

    @Test
    public void testProcessSelfClosingUnknownTags() {
        String xmlString = "<root><unknownTag/><element attr1=\"value1\" attr2='value2'><subElement/></element></root>";
        String baseUri = "http://test.com";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlString, baseUri, errors);

        Document doc = (Document) parsedNodes.get(0);
        Element root = (Element) doc.child(0);
        Element element = (Element) root.child(1);

        assertEquals("element", element.nodeName());
        assertFalse(element.isSelfClosing());
        assertEquals(2, stack.size());

        Element unknownTag = (Element) root.child(0);
        assertTrue(unknownTag.isSelfClosing());
        assertEquals("unknownTag", unknownTag.nodeName());

        assertEquals(4, parsedNodes.size());
    }

    @Test
    public void testProcessComments() {
        String xmlString = "<root><!-- This is a comment --><element attr1=\"value1\" attr2='value2'><subElement/></element></root>";
        String baseUri = "http://test.com";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlString, baseUri, errors);

        Document doc = (Document) parsedNodes.get(0);
        Element root = (Element) doc.child(0);
        Comment comment = (Comment) root.child(0);

        assertEquals("This is a comment", comment.text());
        assertEquals("--", comment.getData());

        assertEquals(2, parsedNodes.size());
    }

    @Test
    public void testProcessDocType() {
        String xmlString = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED> ]><root><element attr=\"value\"/></root>";
        String baseUri = "http://test.com";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlString, baseUri, errors);

        Document doc = (Document) parsedNodes.get(0);
        Element root = (Element) doc.child(0);
        DocumentType documentType = (DocumentType) doc.child(0);

        assertEquals("root", documentType.getName());
        assertEquals("[ <!ATTLIST element attr CDATA #REQUIRED> ],", documentType.getData());

        assertEquals(2, parsedNodes.size());
    }

    @Test(expected = AssertionError.class)
    public void testProcessUnexpectedTokenType() {
        String xmlString = "<root><!-- This is a comment --><!><!-- This is another comment --></root>";
        String baseUri = "http://test.com";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlString, baseUri, errors);

        fail("ProcessUnexpectedTokenType should have been thrown.");
    }
}