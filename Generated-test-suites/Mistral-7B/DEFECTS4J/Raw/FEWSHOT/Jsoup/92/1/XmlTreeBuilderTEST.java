package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParseXmlWithNoContent() {
        Document xmlDoc = new XmlTreeBuilder().parse("", "");
        assertEquals(XmlDeclaration.class, xmlDoc.childNodes().get(0).getClass());
        assertTrue(xmlDoc.childNodes().get(0).nodeName().equals("xml"));
        assertTrue(xmlDoc.childNodes().get(0).attributes().size() == 3);
        assertEquals("1.0", xmlDoc.childNodes().get(0).attributes().get("version"));
        assertEquals("UTF-8", xmlDoc.childNodes().get(0).attributes().get("encoding"));
        assertEquals("yes", xmlDoc.childNodes().get(0).attributes().get("standalone"));
        assertTrue(xmlDoc.childNodes().size() == 1);
    }

    @Test
    public void testParseXmlWithEmptyElement() {
        Document xmlDoc = new XmlTreeBuilder().parse("<root/>", "");
        Element root = xmlDoc.select("root").first();
        assertEquals(Element.class, root.getClass());
        assertTrue(root.nodeName().equals("root"));
        assertTrue(root.childNodes().size() == 0);
    }

    @Test
    public void testParseXmlWithSingleElement() {
        Document xmlDoc = new XmlTreeBuilder().parse("<root><child/></root>", "");
        Element root = xmlDoc.select("root").first();
        Element child = root.select("child").first();
        assertEquals(Element.class, root.getClass());
        assertEquals(Element.class, child.getClass());
        assertTrue(root.nodeName().equals("root"));
        assertTrue(child.nodeName().equals("child"));
        assertTrue(root.childNodes().size() == 1);
        assertTrue(child.childNodes().size() == 0);
    }

    @Test
    public void testParseXmlWithSingleElementAndTextContent() {
        Document xmlDoc = new XmlTreeBuilder().parse("<root><child>Hello World</child></root>", "");
        Element root = xmlDoc.select("root").first();
        Element child = root.select("child").first();
        TextNode textNode = (TextNode) child.childNodes().get(0);
        assertEquals(TextNode.class, textNode.getClass());
        assertEquals("Hello World", textNode.text());
        assertTrue(root.childNodes().size() == 1);
        assertTrue(child.childNodes().size() == 1);
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        Document xmlDoc = new XmlTreeBuilder().parse("<root><img src=\"image.jpg\" /></root>", "");
        Element root = xmlDoc.select("root").first();
        Element img = root.select("img").first();
        assertTrue(img.nodeName().equals("img"));
        assertTrue(img.isTagClosed());
        assertTrue(root.childNodes().size() == 1);
        assertTrue(img.childNodes().size() == 0);
    }

    @Test
    public void testParseXmlWithComment() {
        Document xmlDoc = new XmlTreeBuilder().parse("<root><!-- comment --></root>", "");
        Comment comment = (Comment) xmlDoc.childNodes().get(1);
        assertEquals(Comment.class, comment.getClass());
        assertEquals(" comment ", comment.getData());
        assertTrue(xmlDoc.childNodes().size() == 2);
    }

    @Test
    public void testParseXmlWithDoctype() {
        Document xmlDoc = new XmlTreeBuilder().parse("<!DOCTYPE root [<!ATTLIST root attr CDATA #REQUIRED>]<root><child/></root>", "");
        DocumentType doctype = (DocumentType) xmlDoc.childNodes().get(0);
        assertEquals("root", doctype.nodeName());
        assertEquals("<!ATTLIST root attr CDATA #REQUIRED>", doctype.getData());
        Element root = xmlDoc.select("root").first();
        Element child = root.select("child").first();
        assertTrue(xmlDoc.childNodes().size() == 2);
        assertTrue(root.childNodes().size() == 1);
        assertTrue(child.childNodes().size() == 0);
    }

    @Test
    public void testParseXmlWithNestedElements() {
        Document xmlDoc = new XmlTreeBuilder().parse("<root><child><grandChild/></child></root>", "");
        Element root = xmlDoc.select("root").first();
        Element child = root.select("child").first();
        Element grandChild = child.select("grandChild").first();
        assertEquals(Element.class, root.getClass());
        assertEquals(Element.class, child.getClass());
        assertEquals(Element.class, grandChild.getClass());
        assertTrue(root.nodeName().equals("root"));
        assertTrue(child.nodeName().equals("child"));
        assertTrue(grandChild.nodeName().equals("grandChild"));
        assertTrue(root.childNodes().size() == 1);
        assertTrue(child.childNodes().size() == 1);
        assertTrue(grandChild.childNodes().size() == 0);
    }

    @Test
    public void testParseXmlWithNestedElementsAndTextContent() {
        Document xmlDoc = new XmlTreeBuilder().parse("<root><child><grandChild>Hello World</grandChild></child></root>", "");
        Element root = xmlDoc.select("root").first();
        Element child = root.select("child").first();
        Element grandChild = child.select("grandChild").first();
        TextNode textNode = (TextNode) grandChild.childNodes().get(0);
        assertEquals(TextNode.class, textNode.getClass());
        assertEquals("Hello World", textNode.text());
        assertTrue(root.childNodes().size() == 1);
        assertTrue(child.childNodes().size() == 1);
        assertTrue(grandChild.childNodes().size() == 1);
    }

    @Test
    public void testParseXmlWithEmptyComment() {
        Document xmlDoc = new XmlTreeBuilder().parse("<root><!-- --></root>", "");
        Comment comment = (Comment) xmlDoc.childNodes().get(1);
        assertEquals(Comment.class, comment.getClass());
        assertNull(comment.getData());
        assertTrue(xmlDoc.childNodes().size() == 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlWithInvalidXml() {
        new XmlTreeBuilder().parse("<html><head><title>Test</title></head><body><root/></body>", "");
    }
}