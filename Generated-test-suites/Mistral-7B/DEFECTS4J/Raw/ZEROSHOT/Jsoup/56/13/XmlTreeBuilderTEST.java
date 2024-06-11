package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for XmlTreeBuilder.
 */
public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.PreserveCase, settings);
    }

    @Test
    public void testParse() {
        String xmlString = "<root><element attr=\"value\"/></root>";
        Document xmlDoc = xmlTreeBuilder.parse(xmlString, "");
        assertNotNull(xmlDoc);
        Element rootElement = xmlDoc.rootElement();
        assertEquals("root", rootElement.tagName());
        List<Node> children = rootElement.children();
        assertEquals(1, children.size());
        Element element = (Element) children.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
    }

    @Test(expected = IllegalStateException.class)
    public void testProcessTokenUnexpectedType() {
        String input = "<html>"; // This input should not be parsed by XmlTreeBuilder
        xmlTreeBuilder.initialiseParse(input, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        tokeniser = new Tokenizer(); // Assuming tokeniser is a private member variable
        Token token = tokeniser.next(); // Assuming next method returns a token
        token.type = 99; // Setting an unexpected token type
        xmlTreeBuilder.process(token);
    }

    @Test
    public void testInsertNode() {
        String xmlString = "<root><element><subElement/></element></root>";
        Document xmlDoc = xmlTreeBuilder.parse(xmlString, "");
        Element rootElement = xmlDoc.rootElement();
        Element element = rootElement.element(0);
        Element subElement = element.element(0);
        xmlTreeBuilder.insertNode(subElement);
        Element insertedSubElement = rootElement.element(0).element(0);
        assertEquals(subElement, insertedSubElement);
    }

    @Test
    public void testInsertComment() {
        String xmlString = "<!-- This is a comment -->";
        Document xmlDoc = xmlTreeBuilder.parse(xmlString, "");
        Comment comment = xmlDoc.body().child(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testInsertCharacter() {
        String xmlString = "<root><element>&amp;</element></root>";
        Document xmlDoc = xmlTreeBuilder.parse(xmlString, "");
        TextNode characterNode = (TextNode) xmlDoc.rootElement().element(0).child(0);
        assertEquals("&amp;", characterNode.text());
    }

    @Test
    public void testInsertDoctype() {
        String xmlString = "<!DOCTYPE html [ <!ATTLIST html foo CDATA #REQUIRED > ]>";
        Document xmlDoc = xmlTreeBuilder.parse(xmlString, "");
        DocumentType doctypeNode = (DocumentType) xmlDoc.childNodes().get(0);
        assertEquals("html", doctypeNode.tagName());
        assertEquals("CDATA", doctypeNode.internalSubset().type());
        assertEquals("foo", doctypeNode.internalSubset().entityName());
        assertEquals("REQUIRED", doctypeNode.internalSubset().required());
    }

    @Test
    public void testPopStackToClose() {
        String xmlString = "<root><element><subElement/></element></root>";
        Document xmlDoc = xmlTreeBuilder.parse(xmlString, "");
        xmlTreeBuilder.initialiseParse(xmlString, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        xmlTreeBuilder.runParser();
        Element rootElement = xmlDoc.rootElement();
        Element element = rootElement.element(0);
        xmlTreeBuilder.popStackToClose(element.element(0).tag());
        assertEquals(0, rootElement.children().size());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><element><subElement/></element></root>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Element rootElement = (Element) nodes.get(0);
        assertEquals("root", rootElement.tagName());
        List<Node> children = rootElement.children();
        assertEquals(1, children.size());
        Element element = (Element) children.get(0);
        assertEquals("element", element.tagName());
        List<Node> subElements = element.children();
        assertEquals(1, subElements.size());
        Element subElement = (Element) subElements.get(0);
        assertEquals("subElement", subElement.tagName());
    }
}