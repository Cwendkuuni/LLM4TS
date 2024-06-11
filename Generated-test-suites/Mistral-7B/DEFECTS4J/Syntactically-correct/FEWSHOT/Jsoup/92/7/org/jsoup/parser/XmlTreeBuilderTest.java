package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmpty() {
        Document doc = new XmlTreeBuilder().parse(new StringReader(""), "baseUri");
        assertTrue(doc.body().childNodeSize() == 0);
    }

    @Test
    public void testParseWithTextNode() {
        Document doc = new XmlTreeBuilder().parse(new StringReader("<root><text>Hello World</text></root>"), "baseUri");
        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        TextNode textNode = (TextNode) root.childNodes().get(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseWithCommentNode() {
        Document doc = new XmlTreeBuilder().parse(new StringReader("<root><!-- This is a comment --></root>"), "baseUri");
        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        Comment comment = (Comment) root.childNodes().get(0);
        assertEquals("This is a comment", comment.data());
    }

    @Test
    public void testParseWithDoctypeNode() {
        Document doc = new XmlTreeBuilder().parse(new StringReader("<!DOCTYPE root SYSTEM \"root.dtd\">"), "baseUri");
        DocumentType doctypeNode = doc.doctype();
        assertEquals("root", doctypeNode.name());
        assertEquals("root.dtd", doctypeNode.systemIdentifier());
        assertNull(doctypeNode.publicIdentifier());
    }

    @Test
    public void testParseWithCDataNode() {
        Document doc = new XmlTreeBuilder().parse(new StringReader("<root><![CDATA[Hello World]]]>"), "baseUri");
        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        CDataNode cdataNode = (CDataNode) root.childNodes().get(0);
        assertEquals("Hello World", cdataNode.text().toString());
    }

    @Test
    public void testParseWithElementNode() {
        Document doc = new XmlTreeBuilder().parse(new StringReader("<root><child element=\"value\"/></root>"), "baseUri");
        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        Element child = (Element) root.childNodes().get(0);
        assertEquals("child", child.tagName());
        assertEquals("value", child.attr("element"));
    }

    @Test
    public void testParseWithSelfClosingElement() {
        Document doc = new XmlTreeBuilder().parse(new StringReader("<root><child/></root>"), "baseUri");
        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        Element child = (Element) root.childNodes().get(0);
        assertEquals("child", child.tagName());
        assertTrue(child.isSelfClosing());
    }

    @Test
    public void testParseWithNestedElements() {
        Document doc = new XmlTreeBuilder().parse(new StringReader("<root><child><grandchild/></child></root>"), "baseUri");
        Element root = doc.rootElement();
        Element child = (Element) root.childNodes().get(0);
        Element grandchild = (Element) child.childNodes().get(0);
        assertEquals("root", root.tagName());
        assertEquals("child", child.tagName());
        assertEquals("grandchild", grandchild.tagName());
    }

    @Test
    public void testParseWithAttributes() {
        Document doc = new XmlTreeBuilder().parse(new StringReader("<root attrib1=\"value1\" attrib2='value2'/>"), "baseUri");
        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        assertEquals("value1", root.attr("attrib1"));
        assertEquals("value2", root.attr("attrib2"));
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        Element child = new Element("child");
        Element grandchild = new Element("grandchild");
        builder.stack.push(root);
        builder.stack.push(child);
        builder.stack.push(grandchild);
        builder.popStackToClose(child.asEndTag());
        List<Element> expectedStack = new ArrayList<>();
        expectedStack.add(root);
        assertEquals(expectedStack, builder.stack);
    }

    @Test(expected = AssertionError.class)
    public void testPopStackToCloseUnknownTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        Element child = new Element("child");
        Element grandchild = new Element("grandchild");
        builder.stack.push(root);
        builder.stack.push(child);
        builder.stack.push(grandchild);
        builder.popStackToClose(new Element("unknown").asEndTag());
    }

    @Test
    public void testParseFragment() {
        List<Node> nodes = new XmlTreeBuilder().parseFragment("<root><child><grandchild><text>Hello World</text></grandchild></child></root>", "baseUri", new Parser(new XmlTreeBuilder()));
        assertEquals(4, nodes.size());
        assertSame(nodes.get(0), nodes.get(3).childNodes().get(0));
        TextNode textNode = (TextNode) nodes.get(3);
        assertEquals("Hello World", textNode.text());
    }
}