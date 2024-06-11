package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlInput = "<root><element attr=\"value\"><subElement attr2=\"value2\"/></element></root>";
        Document xmlDoc = XmlTreeBuilder.parser().parse(xmlInput, null);
        Element root = xmlDoc.getRootElement();
        assertNotNull(root);
        assertTrue(root.tagName().equals("root"));
        Element element = root.element(0);
        assertNotNull(element);
        assertTrue(element.tagName().equals("element"));
        Element subElement = element.element(0);
        assertNotNull(subElement);
        assertTrue(subElement.tagName().equals("subElement"));
        assertTrue(element.attribute("attr").getValue().equals("value"));
        assertTrue(subElement.attribute("attr2").getValue().equals("value2"));
    }

    @Test
    public void testParseWithComments() {
        String xmlInput = "<root><!-- comment1 --><element attr=\"value\"><!-- comment2 --><subElement attr2=\"value2\"/><!-- comment3 --></element></root>";
        Document xmlDoc = XmlTreeBuilder.parser().parse(xmlInput, null);
        Element root = xmlDoc.getRootElement();
        assertNotNull(root);
        assertTrue(root.tagName().equals("root"));
        Element element = root.element(0);
        assertNotNull(element);
        assertTrue(element.tagName().equals("element"));
        Comment comment1 = (Comment) root.child(0);
        assertNotNull(comment1);
        assertTrue(comment1.text().equals(" comment1 "));
        Element subElement = element.element(0);
        assertNotNull(subElement);
        assertTrue(subElement.tagName().equals("subElement"));
        Comment comment2 = (Comment) subElement.child(0);
        assertNotNull(comment2);
        assertTrue(comment2.text().equals(" comment2 "));
        Comment comment3 = (Comment) subElement.child(1);
        assertNotNull(comment3);
        assertTrue(comment3.text().equals(" comment3 "));
    }

    @Test
    public void testParseWithDoctype() {
        String xmlInput = "<!DOCTYPE root [ <!ENTITY entity SYMBOL \"value\"> ]><root><element attr=\"value\"><subElement attr2=\"value2\"/></element></root>";
        Document xmlDoc = XmlTreeBuilder.parser().parse(xmlInput, null);
        DocumentType doctypeNode = xmlDoc.doctype();
        assertNotNull(doctypeNode);
        assertTrue(doctypeNode.name().equals("root"));
        Element root = xmlDoc.getRootElement();
        assertNotNull(root);
        assertTrue(root.tagName().equals("root"));
        Element element = root.element(0);
        assertNotNull(element);
        assertTrue(element.tagName().equals("element"));
        Element subElement = element.element(0);
        assertNotNull(subElement);
        assertTrue(subElement.tagName().equals("subElement"));
        assertNotNull(doctypeNode.entity("entity"));
        assertTrue(doctypeNode.entity("entity").text().equals("value"));
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><element attr=\"value\"><subElement attr2=\"value2\"/></element></root>";
        List<Node> nodes = XmlTreeBuilder.parser().parseFragment(xmlFragment, null, ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();
        Element root = (Element) nodes.get(0);
        assertNotNull(root);
        assertTrue(root.tagName().equals("root"));
        Element element = (Element) nodes.get(1);
        assertNotNull(element);
        assertTrue(element.tagName().equals("element"));
        Element subElement = element.element(0);
        assertNotNull(subElement);
        assertTrue(subElement.tagName().equals("subElement"));
    }

    @Test
    public void testParseInvalidXml() {
        String xmlInput = "<root><element attr=\"value<invalid\">";
        ParseErrorList errors = new ParseErrorList();
        Document xmlDoc = XmlTreeBuilder.parser().parse(xmlInput, null, errors, ParseSettings.preserveCase);
        assertTrue(errors.size() > 0);
        Element root = xmlDoc.getRootElement();
        assertNotNull(root);
        assertTrue(root.tagName().equals("root"));
        Element element = root.element(0);
        assertNotNull(element);
        assertTrue(element.tagName().equals("element"));
        assertTrue(element.children().size() == 0);
    }
}