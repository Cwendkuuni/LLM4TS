package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        Document doc = new XmlTreeBuilder().parse("", "").parse();
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testParseSimpleXml() {
        String xml = "<root><child>hello</child></root>";
        Document doc = new XmlTreeBuilder().parse(xml, "").parse();
        Element root = doc.select("root").first();
        Element child = root.child(0);
        assertEquals("child", child.tagName());
        assertEquals("hello", child.text());
        assertEquals(2, doc.childNodes().size());
    }

    @Test
    public void testParseXmlWithAttributes() {
        String xml = "<root attr1=\"value1\" attr2='value2'><child attr3=\"value3\"/></root>";
        Document doc = new XmlTreeBuilder().parse(xml, "").parse();
        Element root = doc.select("root").first();
        Map<String, String> attrs = root.attributes();
        assertEquals("value1", attrs.get("attr1"));
        assertEquals("value2", attrs.get("attr2"));
        assertEquals(1, root.childNodes().size());
        Element child = root.child(0);
        assertEquals("child", child.tagName());
        attrs = child.attributes();
        assertEquals("value3", attrs.get("attr3"));
        assertEquals(2, doc.childNodes().size());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        String xml = "<root><selfClosing tag=\"hello\"/></root>";
        Document doc = new XmlTreeBuilder().parse(xml, "").parse();
        Element root = doc.select("root").first();
        assertEquals(1, root.childNodes().size());
        Element selfClosingTag = root.child(0);
        assertFalse(selfClosingTag.hasText());
        assertTrue(selfClosingTag.tagName().endsWith(":"));
    }

    @Test
    public void testParseXmlWithCDataSection() {
        String xml = "<root><![CDATA[this is a cdata section]]></root>";
        Document doc = new XmlTreeBuilder().parse(xml, "").parse();
        Element root = doc.select("root").first();
        assertEquals(1, root.childNodes().size());
        Node cdataNode = root.child(0);
        assertTrue(cdataNode instanceof CDataNode);
        assertEquals("this is a cdata section", cdataNode.text());
    }

    @Test
    public void testParseXmlWithComments() {
        String xml = "<root><!-- this is a comment --><child>hello</child></root>";
        Document doc = new XmlTreeBuilder().parse(xml, "").parse();
        Element root = doc.select("root").first();
        assertEquals(1, root.childNodes().size());
        Element child = root.child(0);
        assertEquals(2, root.childNodes().size());
        Node commentNode = root.child(0);
        assertTrue(commentNode instanceof Comment);
        assertEquals(" this is a comment ", commentNode.text());
    }

    @Test
    public void testParseXmlWithDocTypeDeclaration() {
        String xml = "<!DOCTYPE root [ <!ATTLIST root attr CDATA #REQUIRED > ]><root><child>hello</child></root>";
        Document doc = new XmlTreeBuilder().parse(xml, "").parse();
        Element root = doc.select("root").first();
        assertEquals(1, root.childNodes().size());
        Element child = root.child(0);
        DocumentType docType = doc.doctype();
        assertEquals("root", docType.name());
        assertEquals("root", root.nodeName());
    }

    @Test
    public void testParseXmlWithXmlDeclaration() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><child>hello</child></root>";
        Document doc = new XmlTreeBuilder().parse(xml, "").parse();
        XmlDeclaration xmlDeclaration = doc.xmlDeclaration();
        assertEquals("UTF-8", xmlDeclaration.encoding());
        assertEquals("1.0", xmlDeclaration.version());
    }

    @Test
    public void testParseXmlWithEmptyElement() {
        String xml = "<root><element/></root>";
        Document doc = new XmlTreeBuilder().parse(xml, "").parse();
        Element root = doc.select("root").first();
        assertEquals(1, root.childNodes().size());
        Element element = root.child(0);
        assertEquals(0, element.childNodes().size());
        assertEquals("element", element.tagName());
    }

    @Test
    public void testParseXmlWithEmptyElementWithText() {
        String xml = "<root><element></element></root>";
        Document doc = new XmlTreeBuilder().parse(xml, "").parse();
        Element root = doc.select("root").first();
        assertEquals(1, root.childNodes().size());
        Element element = root.child(0);
        assertEquals(1, element.childNodes().size());
        TextNode textNode = (TextNode) element.child(0);
        assertEquals("", textNode.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlWithInvalidInput() {
        new XmlTreeBuilder().parse("", "");
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><child>hello</child></root>";
        List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlFragment, "").childNodes();
        assertEquals(2, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.tagName());
        assertEquals(1, root.childNodes().size());
        Element child = (Element) root.child(0);
        assertEquals("child", child.tagName());
        assertEquals("hello", child.text());
    }

    @Test
    public void testParseFragmentWithContext() {
        String xmlFragment = "<root><child>hello</child></root>";
        Document contextDocument = Jsoup.parse("<html><body></body></html>");
        List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlFragment, contextDocument.select("body").first(), "").childNodes();
        assertEquals(2, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.tagName());
        assertEquals(1, root.childNodes().size());
        Element child = (Element) root.child(0);
        assertEquals("child", child.tagName());
        assertEquals("hello", child.text());
        assertEquals(nodes, contextDocument.select("body").first().child(0).childNodes());
    }
}