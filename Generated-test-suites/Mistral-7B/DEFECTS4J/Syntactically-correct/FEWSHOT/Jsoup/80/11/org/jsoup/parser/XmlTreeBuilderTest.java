package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.helper.Validate;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<xml><root><child>Hello</child></root></xml>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "");

        Element root = xmlDoc.getRootElement();
        assertTrue(root.tagName().equals("root"));
        List<Node> children = root.children();
        assertEquals(1, children.size());
        Element child = (Element) children.get(0);
        assertTrue(child.tagName().equals("child"));
        assertEquals("Hello", child.text());
    }

    @Test
    public void testParseWithComments() {
        String xmlString = "<xml><!-- comment --><root><child>Hello</child></root></xml>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "");

        List<Node> commentNodes = xmlDoc.body().childNodes();
        assertEquals(1, commentNodes.size());
        Comment comment = (Comment) commentNodes.get(0);
        assertEquals(" comment ", comment.text());

        Element root = xmlDoc.getRootElement();
        List<Node> children = root.children();
        assertEquals(1, children.size());
        Element child = (Element) children.get(0);
        assertTrue(child.tagName().equals("child"));
        assertEquals("Hello", child.text());
    }

    @Test
    public void testParseWithDoctype() {
        String xmlString = "<!DOCTYPE xml [<!ATTLIST xml root CDATA #REQUIRED>]<xml><root><child>Hello</child></root></xml>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "");

        DocumentType docType = (DocumentType) xmlDoc.childNodes().get(0);
        assertTrue(docType.nodeName().equals("#doctype"));
        assertEquals("xml", docType.nodeName());
        assertEquals("root", docType.publicId());
        assertEquals("", docType.systemId());

        Element root = xmlDoc.getRootElement();
        List<Node> children = root.children();
        assertEquals(1, children.size());
        Element child = (Element) children.get(0);
        assertTrue(child.tagName().equals("child"));
        assertEquals("Hello", child.text());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><child>Hello</child></root>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase).get(0).childNodes();

        Element root = (Element) nodes.get(0);
        List<Node> children = root.children();
        assertEquals(1, children.size());
        Element child = (Element) children.get(0);
        assertTrue(child.tagName().equals("child"));
        assertEquals("Hello", child.text());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        builder.stack.add(root);

        Element child1 = new Element("child1");
        Element child2 = new Element("child2");
        root.appendChild(child1);
        root.appendChild(child2);

        builder.popStackToClose(child1.tagName());

        List<Node> stack = builder.stack;
        assertEquals(1, stack.size());
        assertEquals(child2, stack.get(0));
    }

    @Test
    public void testUnexpectedTokenType() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token(1, 1, 1, "foo", -1, -1, "expected");

        assertFalse(builder.process(token));
    }
}