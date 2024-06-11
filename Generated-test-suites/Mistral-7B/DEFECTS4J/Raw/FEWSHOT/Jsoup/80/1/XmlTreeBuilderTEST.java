package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(""), "");
        assertEquals(Document.OutputSettings.Syntax.xml, xmlDoc.outputSettings().syntax());
        assertEquals(0, xmlDoc.childNodes().size());
    }

    @Test
    public void testParseXmlWithRootElement() {
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader("<root></root>"), "");
        assertEquals(Document.OutputSettings.Syntax.xml, xmlDoc.outputSettings().syntax());
        Element root = xmlDoc.child(0);
        assertEquals("root", root.nodeName());
        assertEquals(0, root.childNodes().size());
    }

    @Test
    public void testParseXmlWithRootElementAndAttributes() {
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader("<root attr1=\"val1\" attr2=\"val2\"/>"), "");
        Element root = xmlDoc.child(0);
        assertEquals("root", root.nodeName());
        List<Attribute> attrs = root.attributes();
        assertEquals(2, attrs.size());
        assertEquals("attr1", attrs.get(0).getKey());
        assertEquals("val1", attrs.get(0).getValue());
        assertEquals("attr2", attrs.get(1).getKey());
        assertEquals("val2", attrs.get(1).getValue());
    }

    @Test
    public void testParseXmlWithRootElementAndChildren() {
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader("<root><child>Child 1</child><child2><grandChild>Grand Child</grandChild></child2></root>"), "");
        Element root = xmlDoc.child(0);
        assertEquals("root", root.nodeName());
        List<Node> children = root.childNodes();
        assertEquals(2, children.size());

        Element child1 = (Element) children.get(0);
        assertEquals("child", child1.nodeName());
        TextNode child1Text = (TextNode) child1.childNodes().get(0);
        assertEquals("Child 1", child1Text.text());

        Element child2 = (Element) children.get(1);
        assertEquals("child2", child2.nodeName());
        List<Node> child2Children = child2.childNodes();
        assertEquals(1, child2Children.size());

        Element grandChild = (Element) child2Children.get(0);
        assertEquals("grandChild", grandChild.nodeName());
        TextNode grandChildText = (TextNode) grandChild.childNodes().get(0);
        assertEquals("Grand Child", grandChildText.text());
    }

    @Test
    public void testParseXmlWithComment() {
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader("<root><!-- This is a comment --></root>"), "");
        Element root = xmlDoc.child(0);
        List<Node> rootChildren = root.childNodes();
        assertEquals(0, rootChildren.size());
        Node comment = root.previousSibling();
        assertEquals(Node.NodeType.COMMENT, comment.nodeType());
        assertEquals("This is a comment", comment.text().toString());
    }

    @Test
    public void testParseXmlWithCDataSection() {
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader("<root><![CDATA[This is a CData section]]></root>"), "");
        Element root = xmlDoc.child(0);
        List<Node> rootChildren = root.childNodes();
        assertEquals(0, rootChildren.size());
        Node cdata = root.childNodes().get(0);
        assertEquals(Node.NodeType.CDATA, cdata.nodeType());
        assertEquals("This is a CData section", cdata.text().toString());
    }

    @Test
    public void testParseXmlWithDoctype() {
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader("<!DOCTYPE root [<!ATTLIST attr1 CDATA #REQUIRED>]><root></root>"), "");
        DocumentType doctype = (DocumentType) xmlDoc.childNodes().get(0);
        assertEquals("root", doctype.nodeName());
        assertEquals("", doctype.publicId());
        assertEquals("", doctype.systemId());
        assertEquals("root", doctype.getName());
        assertEquals("CDATA", doctype.internalSubset().substring(0, 5));
        assertEquals("[<!ATTLIST attr1 CDATA #REQUIRED>", doctype.externalSubset());
    }

    @Test
    public void testParseXmlWithNestedElements() {
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader("<root><child><grandChild>Grand Child</grandChild></child></root>"), "");
        Element root = xmlDoc.child(0);
        List<Node> children = root.childNodes();
        assertEquals(1, children.size());
        Element child = (Element) children.get(0);
        List<Node> childChildren = child.childNodes();
        assertEquals(1, childChildren.size());
        Element grandChild = (Element) childChildren.get(0);
        assertEquals("grandChild", grandChild.nodeName());
        TextNode grandChildText = (TextNode) grandChild.childNodes().get(0);
        assertEquals("Grand Child", grandChildText.text());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader("<root><img src=\"image.png\" /></root>"), "");
        Element root = xmlDoc.child(0);
        List<Node> children = root.childNodes();
        assertEquals(1, children.size());
        Element img = (Element) children.get(0);
        assertEquals("img", img.nodeName());
        assertSame(Element.Tag.IMG, img.tag());
        assertTrue(img.isSelfClosing());
        TextNode src = (TextNode) img.attributes().get("src");
        assertEquals("image.png", src.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlWithInvalidInput() {
        XmlTreeBuilder.parse(new StringReader("invalid input"), "");
    }
}