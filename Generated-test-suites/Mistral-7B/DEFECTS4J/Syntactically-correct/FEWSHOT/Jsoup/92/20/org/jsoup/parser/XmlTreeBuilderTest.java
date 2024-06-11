package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseXmlDocument() {
        String xmlString = "<root><child><grandchild>Hello World</grandchild></child></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString));
        assertEquals("root", xmlDoc.tagName());

        Element root = xmlDoc.select("root").first();
        Element child = root.select("child").first();
        Element grandchild = child.select("grandchild").first();
        assertEquals("child", child.tagName());
        assertEquals("grandchild", grandchild.tagName());
        assertEquals("Hello World", grandchild.text());
    }

    @Test
    public void testParseXmlDocumentWithComments() {
        String xmlString = "<root><!-- comment1 --><child><!-- comment2 --><grandchild>Hello World</grandchild></child></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString));

        Element root = xmlDoc.select("root").first();
        assertEquals(2, root.select("comment").size());
        Comment comment1 = root.select("comment:eq(0)").first();
        Comment comment2 = root.select("comment:eq(1)").first();
        assertEquals("comment1", comment1.text());
        assertEquals("comment2", comment2.text());
    }

    @Test
    public void testParseXmlDocumentWithCDataSection() {
        String xmlString = "<root><child><![CDATA[Hello World]]></child></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString));

        Element root = xmlDoc.select("root").first();
        Element child = root.select("child").first();
        TextNode cdataNode = (TextNode) child.childNodes().get(0);
        assertEquals("Hello World", cdataNode.text());
    }

    @Test
    public void testParseXmlDocumentWithSelfClosingTag() {
        String xmlString = "<root><child><img src=\"image.jpg\" /></child></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString));

        Element root = xmlDoc.select("root").first();
        Element child = root.select("child").first();
        Element img = child.select("img").first();
        assertFalse(img.hasChildren());
        assertEquals("img", img.tagName());
    }

    @Test
    public void testParseXmlDocumentWithNoSelfClosingTag() {
        String xmlString = "<root><child><img src=\"image.jpg\"></img></child></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString));

        Element root = xmlDoc.select("root").first();
        Element child = root.select("child").first();
        Element img = child.select("img").first();
        assertFalse(img.hasChildren());
        assertEquals("img", img.tagName());
    }

    @Test
    public void testParseXmlDocumentWithDoctype() {
        String xmlString = "<!DOCTYPE root SYSTEM \"root.dtd\">\n<root></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString));

        DocumentType doctype = xmlDoc.doctype();
        assertEquals("root", doctype.name());
        assertEquals("root.dtd", doctype.systemIdentifier());
    }

    @Test
    public void testParseXmlFragment() {
        String xmlFragment = "<child><grandchild>Hello World</grandchild></child>";
        List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlFragment, null, null, new Parser(new XmlTreeBuilder()));

        Element child = (Element) nodes.get(0);
        Element grandchild = (Element) child.childNodes().get(0);
        assertEquals("child", child.tagName());
        assertEquals("grandchild", grandchild.tagName());
        assertEquals("Hello World", grandchild.text());
    }

    @Test
    public void testParseXmlFragmentWithComment() {
        String xmlFragment = "<!-- comment --><child></child>";
        List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlFragment, null, null, new Parser(new XmlTreeBuilder()));

        Comment comment = (Comment) nodes.get(0);
        Element child = (Element) nodes.get(1);
        assertEquals("comment", comment.text());
        assertFalse(child.hasChildren());
        assertEquals("child", child.tagName());
    }

    @Test
    public void testParseXmlFragmentWithDoctype() {
        String xmlFragment = "<!DOCTYPE child [<!ATTLIST child attr CDATA #REQUIRED>]><child></child>";
        List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlFragment, null, null, new Parser(new XmlTreeBuilder()));

        DocumentType doctype = (DocumentType) nodes.get(0);
        Element child = (Element) nodes.get(1);
        assertEquals("child", doctype.name());
        assertEquals("child", child.tagName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullInput() {
        new XmlTreeBuilder().parse(null);
    }
}