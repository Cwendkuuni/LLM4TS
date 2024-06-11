package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.util.Collections;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseFragmentEmpty() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parseFragment("", "", new ParseErrorList());
        List<Node> nodes = doc.childNodes();
        assertNull(nodes.get(0));
    }

    @Test
    public void testParseFragmentText() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parseFragment("hello world", "", new ParseErrorList());
        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        TextNode textNode = (TextNode) nodes.get(0);
        assertEquals("hello world", textNode.text());
    }

    @Test
    public void testParseFragmentComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parseFragment("<!-- comment -->", "", new ParseErrorList());
        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        Comment comment = (Comment) nodes.get(0);
        assertEquals("comment", comment.text());
    }

    @Test
    public void testParseFragmentCommentWithData() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parseFragment("<!-- comment data -->", "", new ParseErrorList());
        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        Comment comment = (Comment) nodes.get(0);
        assertEquals("comment data", comment.text());
    }

    @Test
    public void testParseFragmentElementStart() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parseFragment("<root></root>", "", new ParseErrorList());
        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.tagName());
        List<Node> children = root.childNodes();
        assertEquals(0, children.size());
    }

    @Test
    public void testParseFragmentElementStartWithAttributes() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parseFragment("<root attr1='value1' attr2='value2'/>", "", new ParseErrorList());
        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.tagName());
        Attributes attributes = root.attributes();
        assertEquals(2, attributes.size());
        assertEquals("value1", attributes.get("attr1"));
        assertEquals("value2", attributes.get("attr2"));
        List<Node> children = root.childNodes();
        assertEquals(0, children.size());
    }

    @Test
    public void testParseFragmentElementStartWithAttributesAndChildren() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parseFragment("<root attr1='value1'><child>Hello World</child></root>", "", new ParseErrorList());
        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.tagName());
        List<Node> children = root.childNodes();
        assertEquals(1, children.size());
        Element child = (Element) children.get(0);
        assertEquals("child", child.tagName());
        TextNode text = (TextNode) child.childNodes().get(0);
        assertEquals("Hello World", text.text());
    }

    @Test
    public void testParseFragmentElementStartSelfClosing() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parseFragment("<root/>", "", new ParseErrorList());
        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.tagName());
        List<Node> children = root.childNodes();
        assertEquals(0, children.size());
    }

    @Test
    public void testParseFragmentElementEnd() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parseFragment("<root><child></child></root>", "", new ParseErrorList());
        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.tagName());
        List<Node> children = root.childNodes();
        assertEquals(1, children.size());
        Element child = (Element) children.get(0);
        assertEquals("child", child.tagName());
        List<Node> childChildren = child.childNodes();
        assertEquals(0, childChildren.size());
    }

    @Test
    public void testParseFragmentElementEndMissingStart() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parseFragment("</root>", "", new ParseErrorList());
        List<Node> nodes = doc.childNodes();
        assertNull(nodes.get(0));
    }

    @Test
    public void testParseFragmentElementEndMultiple() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parseFragment("<root><child></child></root><foo></foo>", "", new ParseErrorList());
        List<Node> nodes = doc.childNodes();
        assertEquals(2, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.tagName());
        List<Node> children = root.childNodes();
        assertEquals(1, children.size());
        Element child = (Element) children.get(0);
        assertEquals("child", child.tagName());
        List<Node> childChildren = child.childNodes();
        assertEquals(0, childChildren.size());

        Element foo = (Element) nodes.get(1);
        assertEquals("foo", foo.tagName());
        List<Node> fooChildren = foo.childNodes();
        assertEquals(0, fooChildren.size());
    }

    @Test
    public void testParseFragmentDocumentType() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parseFragment("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", "", new ParseErrorList());
        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        DocumentType doctype = (DocumentType) nodes.get(0);
        assertEquals("html", doctype.nodeName());
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", doctype.systemId());
        assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", doctype.publicId());
    }
}