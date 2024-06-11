package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        String xml = "<>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml));
        assertNotNull(doc);
        assertTrue(doc.childNodes().isEmpty());
    }

    @Test
    public void testParseRootElement() {
        String xml = "<root></root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml));
        Element root = doc.rootElement();
        assertNotNull(root);
        assertTrue(root.tagName().equals("root"));
        assertTrue(root.childNodes().isEmpty());
    }

    @Test
    public void testParseElementWithTextContent() {
        String xml = "<element>Hello World</element>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml));
        Element element = doc.selectFirst("element");
        assertNotNull(element);
        assertTrue(element.tagName().equals("element"));
        assertTrue(element.text().equals("Hello World"));
    }

    @Test
    public void testParseElementWithAttributes() {
        String xml = "<element attr1=\"value1\" attr2='value2'/>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml));
        Element element = doc.selectFirst("element");
        assertNotNull(element);
        assertTrue(element.tagName().equals("element"));
        assertTrue(element.attr("attr1").equals("value1"));
        assertTrue(element.attr("attr2").equals("value2"));
    }

    @Test
    public void testParseSelfClosingElement() {
        String xml = "<element/>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml));
        Element element = doc.selectFirst("element");
        assertNotNull(element);
        assertTrue(element.tagName().equals("element"));
        assertTrue(element.isSelfClosing());
        assertTrue(element.childNodes().isEmpty());
    }

    @Test
    public void testParseElementWithNestedElements() {
        String xml = "<root><element><subelement/></element></root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml));
        Element root = doc.selectFirst("root");
        Element element = root.child(0);
        Element subelement = element.child(0);
        assertNotNull(root);
        assertTrue(root.tagName().equals("root"));
        assertNotNull(element);
        assertTrue(element.tagName().equals("element"));
        assertNotNull(subelement);
        assertTrue(subelement.tagName().equals("subelement"));
        assertTrue(subelement.isSelfClosing());
        assertTrue(element.childNodes().size() == 1);
        assertTrue(root.childNodes().size() == 1);
    }

    @Test
    public void testParseElementWithTextAndNestedElements() {
        String xml = "<root><element>Hello World<subelement/></element></root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml));
        Element root = doc.selectFirst("root");
        Element element = root.child(0);
        Element subelement = element.child(1);
        assertNotNull(root);
        assertTrue(root.tagName().equals("root"));
        assertNotNull(element);
        assertTrue(element.tagName().equals("element"));
        assertTrue(element.text().equals("Hello World"));
        assertNotNull(subelement);
        assertTrue(subelement.tagName().equals("subelement"));
        assertTrue(subelement.isSelfClosing());
        assertTrue(element.childNodes().size() == 2);
        assertTrue(root.childNodes().size() == 1);
    }

    @Test
    public void testParseComment() {
        String xml = "<!-- This is a comment -->";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml));
        List<Node> commentNodes = doc.childNodes();
        assertTrue(commentNodes.get(0) instanceof Comment);
        Comment comment = (Comment) commentNodes.get(0);
        assertTrue(comment.text().equals("This is a comment"));
    }

    @Test
    public void testParseCDataSection() {
        String xml = "<![CDATA[This is some CData content]]>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml));
        List<Node> cdataNodes = doc.childNodes();
        assertTrue(cdataNodes.get(0) instanceof CDataNode);
        CDataNode cdata = (CDataNode) cdataNodes.get(0);
        assertTrue(cdata.text().equals("This is some CData content"));
    }

    @Test
    public void testParseDoctype() {
        String xml = "<!DOCTYPE root [ <!ATTLIST element att1 CDATA #REQUIRED> ]>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml));
        List<Node> doctypeNodes = doc.childNodes();
        assertTrue(doctypeNodes.get(0) instanceof DocumentType);
        DocumentType doctype = (DocumentType) doctypeNodes.get(0);
        assertTrue(doctype.tagName().equals("root"));
        assertTrue(doctype.entityName().isEmpty());
        assertTrue(doctype.publicId().equals("[ <!ATTLIST element att1 CDATA #REQUIRED> ]"));
        assertTrue(doctype.systemId().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidXmlThrowsException() {
        String xml = "<root><element><subelement><";
        new XmlTreeBuilder().parse(new StringReader(xml)); // should throw an exception
    }
}