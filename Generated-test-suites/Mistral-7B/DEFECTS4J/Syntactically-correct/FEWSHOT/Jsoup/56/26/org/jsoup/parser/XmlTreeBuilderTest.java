package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.nodes.Xml.Attribute;
import static org.jsoup.nodes.Xml.Tag;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("<>", "baseUri");
        assertNotNull(doc);
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testParseRootElement() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("<root/>", "baseUri");
        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        assertEquals(0, root.childNodes().size());
    }

    @Test
    public void testParseRootElementWithContent() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("<root><content/></root>", "baseUri");
        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        assertEquals(1, root.childNodes().size());
        Element content = root.child(0);
        assertEquals(0, content.childNodes().size());
    }

    @Test
    public void testParseElementWithAttributes() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("<element attr1='value1' attr2='value2'/>", "baseUri");
        Element element = doc.selectSingleTag("element");
        assertEquals("element", element.tagName());
        assertEquals(2, element.attributeCount());
        List<Attribute> attributes = element.attributes();
        assertEquals("value1", attributes.get(0).getValue());
        assertEquals("attr1", attributes.get(0).getKey());
        assertEquals("value2", attributes.get(1).getValue());
        assertEquals("attr2", attributes.get(1).getKey());
    }

    @Test
    public void testParseNestedElements() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("<root><element><subElement/></element></root>", "baseUri");
        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        assertEquals(1, root.childNodes().size());
        Element element = root.child(0);
        assertEquals("element", element.tagName());
        assertEquals(1, element.childNodes().size());
        Element subElement = element.child(0);
        assertEquals(0, subElement.childNodes().size());
    }

    @Test
    public void testParseDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]>", "baseUri");
        DocumentType doctype = doc.doctype();
        assertEquals("root", doctype.name());
        assertEquals("root", doc.rootElement().tagName());
        assertEquals(1, doc.childNodes().size());
        assertEquals(1, doctype.attributes().size());
        Attribute declaration = doctype.attributes().get(0);
        assertEquals("ATTLIST", declaration.getValue());
        assertEquals("element", declaration.getKey());
    }

    @Test
    public void testParseComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("<!-- This is a comment -->", "baseUri");
        Comment comment = doc.body();
        assertEquals("This is a comment", comment.text());
        assertEquals(0, comment.childNodes().size());
    }

    @Test
    public void testParseTextNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("<root><text>Hello World</text></root>", "baseUri");
        Text text = doc.selectSingleText();
        assertEquals("Hello World", text.text());
        assertEquals(0, text.childNodes().size());
    }

    @Test
    public void testParseProcessSelfClosingTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("<selfClosing/>", "baseUri");
        Element selfClosing = doc.selectSingleTag("selfClosing");
        assertEquals("selfClosing", selfClosing.tagName());
        assertEquals(0, selfClosing.childNodes().size());
        assertTrue(selfClosing.hasAttr("xml:isSelfClosing"));
        assertEquals("yes", selfClosing.attr("xml:isSelfClosing"));
    }

    @Test
    public void testParseProcessUnknownTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("<unknown/>", "baseUri");
        Element unknown = doc.selectSingleTag("unknown");
        assertEquals("unknown", unknown.tagName());
        assertEquals(0, unknown.childNodes().size());
        assertFalse(unknown.hasAttr("xml:isKnownTag"));
        assertNull(unknown.attr("xml:isKnownTag"));
        assertTrue(unknown.hasAttr("xml:isSelfClosing"));
        assertEquals("yes", unknown.attr("xml:isSelfClosing"));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<root><element><subElement/></element></root>", "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();
        assertEquals(3, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.tagName());
        assertEquals(1, root.childNodes().size());
        Element element = (Element) root.child(0);
        assertEquals("element", element.tagName());
        assertEquals(1, element.childNodes().size());
        Element subElement = (Element) element.child(0);
        assertEquals("subElement", subElement.tagName());
        assertEquals(0, subElement.childNodes().size());
    }
}