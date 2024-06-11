package org.jsoup.parser;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

/**
 * Test class for the XmlTreeBuilder class.
 */
public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        String xml = "<>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "");
        assertTrue(doc.outputSettings().syntax() == Document.OutputSettings.Syntax.xml);
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testParseSimpleXml() {
        String xml = "<root><element>Hello World</element></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "");
        Element root = (Element) doc.child(0);
        Element element = (Element) root.child(0);
        assertEquals("root", root.nodeName());
        assertEquals("element", element.nodeName());
        assertEquals("Hello World", element.text().toString());
    }

    @Test
    public void testParseXmlWithComment() {
        String xml = "<root><!-- This is a comment --><element>Hello World</element></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "");
        Element root = (Element) doc.child(0);
        Comment comment = (Comment) root.child(0);
        Element element = (Element) root.child(1);
        assertEquals("root", root.nodeName());
        assertEquals("!-- This is a comment -->", comment.getData());
        assertEquals("element", element.nodeName());
        assertEquals("Hello World", element.text().toString());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        String xml = "<root><img src='image.jpg' /> <element>Hello World</element></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "");
        Element root = (Element) doc.child(0);
        Element img = (Element) root.child(0);
        Element element = (Element) root.child(1);
        assertEquals("root", root.nodeName());
        assertEquals("img", img.nodeName());
        assertTrue(img.tagName().isSelfClosing());
        assertEquals("element", element.nodeName());
        assertEquals("Hello World", element.text().toString());
    }

    @Test
    public void testParseXmlWithDoctype() {
        String xml = "<!DOCTYPE root [ <!ATTLIST element att CDATA #REQUIRED > ]><root><element att='value'>Hello World</element></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "");
        DocumentType doctype = (DocumentType) doc.child(0);
        Element root = (Element) doc.child(1);
        Element element = (Element) root.child(0);
        assertEquals("root", root.nodeName());
        assertEquals("root", doctype.getName());
        assertEquals("root", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertEquals("element", element.nodeName());
        assertEquals("value", element.getAttribute("att"));
        assertEquals("Hello World", element.text().toString());
    }

    @Test
    public void testParseXmlWithAttributes() {
        String xml = "<root attr1='value1' attr2='value2'><element attr1='value1' attr2='value2'></element></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "");
        Element root = (Element) doc.child(0);
        Element element = (Element) root.child(0);
        assertEquals("root", root.nodeName());
        assertEquals("attr1", root.attribute("attr1"));
        assertEquals("value1", root.attribute("attr1"));
        assertEquals("attr2", root.attribute("attr2"));
        assertEquals("value2", root.attribute("attr2"));
        assertEquals("element", element.nodeName());
        assertEquals("attr1", element.attribute("attr1"));
        assertEquals("value1", element.attribute("attr1"));
        assertEquals("attr2", element.attribute("attr2"));
        assertEquals("value2", element.attribute("attr2"));
        assertEquals("", element.attribute("nonexistent"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlWithInvalidTag() {
        String xml = "<root><invalid></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "");
        Validate.fail("Expected IllegalArgumentException, but was not thrown.");
    }

    @Test
    public void testParseXmlFragment() {
        String xml = "<root><element>Hello World</element></root>";
        List<Node> fragments = XmlTreeBuilder.parseFragment(xml, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Element root = (Element) fragments.get(0);
        Element element = (Element) root.child(0);
        assertEquals("root", root.nodeName());
        assertEquals("element", element.nodeName());
        assertEquals("Hello World", element.text().toString());
    }

    @Test
    public void testParseXmlFragmentWithComment() {
        String xml = "<root><!-- This is a comment --><element>Hello World</element></root>";
        List<Node> fragments = XmlTreeBuilder.parseFragment(xml, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Element root = (Element) fragments.get(0);
        Comment comment = (Comment) root.child(0);
        Element element = (Element) root.child(1);
        assertEquals("root", root.nodeName());
        assertEquals("!-- This is a comment -->", comment.getData());
        assertEquals("element", element.nodeName());
        assertEquals("Hello World", element.text().toString());
    }

    @Test
    public void testParseXmlFragmentWithDoctype() {
        String xml = "<!DOCTYPE root [ <!ATTLIST element att CDATA #REQUIRED > ]><root><element att='value'>Hello World</element></root>";
        List<Node> fragments = XmlTreeBuilder.parseFragment(xml, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        DocumentType doctype = (DocumentType) fragments.get(0);
        Element root = (Element) fragments.get(1);
        Element element = (Element) root.child(0);
        assertEquals("root", root.nodeName());
        assertEquals("root", doctype.getName());
        assertEquals("root", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertEquals("element", element.nodeName());
        assertEquals("value", element.getAttribute("att"));
        assertEquals("Hello World", element.text().toString());
    }
}