package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.helper.Validate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xml = "<root><element attr=\"value\">Content</element></root>";
        Document doc = new XmlTreeBuilder().parse(xml, "http://test.com");
        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        Element element = root.child(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
        assertEquals("Content", element.text());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr=\"value\">Content</element>";
        List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlFragment, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase).stream().collect(Collectors.toList());
        Node node = nodes.get(0);
        assertEquals(Element.class, node.getClass());
        Element element = (Element) node;
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
        assertEquals("Content", element.text());
    }

    @Test
    public void testSelfClosingTag() {
        String xml = "<root><img src=\"image.jpg\" /> </root>";
        Document doc = new XmlTreeBuilder().parse(xml, "http://test.com");
        Element root = doc.rootElement();
        Element img = root.child(0);
        assertEquals("img", img.tagName());
        assertEquals("image.jpg", img.attribute("src"));
        assertNull(img.child(0));
    }

    @Test
    public void testComment() {
        String xml = "<!-- This is a comment --><root><element attr=\"value\">Content</element></root>";
        Document doc = new XmlTreeBuilder().parse(xml, "http://test.com");
        Element root = doc.rootElement();
        Element element = root.child(0);
        List<Node> comments = doc.childNodes().stream()
                .filter(node -> node instanceof Comment)
                .collect(Collectors.toList());
        assertEquals(1, comments.size());
        Comment comment = (Comment) comments.get(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testCharacter() {
        String xml = "<root>&#123;Content</root>";
        Document doc = new XmlTreeBuilder().parse(xml, "http://test.com");
        Element root = doc.rootElement();
        List<Node> textNodes = doc.childNodes().stream()
                .filter(node -> node instanceof TextNode)
                .collect(Collectors.toList());
        assertEquals(1, textNodes.size());
        TextNode textNode = (TextNode) textNodes.get(0);
        assertEquals("Content", textNode.text());
    }

    @Test
    public void testDoctype() {
        String xml = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]><root><element attr=\"value\">Content</element></root>";
        Document doc = new XmlTreeBuilder().parse(xml, "http://test.com");
        DocumentType doctypeNode = doc.doctype();
        assertEquals("root", doctypeNode.name());
        assertEquals("[ <!ATTLIST element attr CDATA #REQUIRED > ]", doctypeNode.systemIdentifier());
        List<Attribute> attributes = doctypeNode.attributes();
        assertEquals(1, attributes.size());
        Attribute attribute = attributes.get(0);
        assertEquals("element", attribute.name());
        assertEquals("attr", attribute.attributeName());
        assertEquals("CDATA", attribute.attributeType());
        assertEquals("REQUIRED", attribute.required());
    }

    @Test
    public void testUnknownTag() {
        String xml = "<root><my:tag attr=\"value\">Content</my:tag></root>";
        Document doc = new XmlTreeBuilder().parse(xml, "http://test.com");
        Element root = doc.rootElement();
        Element myTag = root.child(0);
        assertNotNull(myTag);
        assertEquals("my:tag", myTag.tagName());
        assertEquals("value", myTag.attribute("attr"));
        assertEquals("Content", myTag.text());
    }

    @Test
    public void testPopStackToClose() {
        String xml = "<root><element><element2></element2></element></root>";
        Document doc = new XmlTreeBuilder().parse(xml, "http://test.com");
        Element root = doc.rootElement();
        Element element = root.child(0);
        Element element2 = element.child(0);
        assertEquals("element", element.tagName());
        assertEquals("element2", element2.tagName());
        new XmlTreeBuilder().popStackToClose(element.child(1).asEndTag());
        assertEquals(0, stack.size());
    }
}