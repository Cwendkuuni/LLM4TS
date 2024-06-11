package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {
    @Test
    public void testParse() {
        String xmlString = "<root><element attr='value'>Content</element></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com/");
        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        Element element = root.child(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
        assertEquals("Content", element.child(0).text());
    }

    @Test
    public void testParseWithEmptyXml() {
        Document doc = XmlTreeBuilder.parse(new StringReader(""), "http://example.com/");
        assertNotNull(doc);
        assertTrue(doc.childNodeSize() == 0);
    }

    @Test
    public void testParseWithInvalidXml() {
        String invalidXmlString = "<root><element<attr='value'>Content</element>";
        ParseErrorList errors = new ParseErrorList();
        Document doc = XmlTreeBuilder.parse(new StringReader(invalidXmlString), "http://example.com/", ParseSettings.preserveCase, errors);
        assertEquals(1, errors.size());
        assertEquals("Invalid XML: Element type " + "element" + " must be fully-qualiified", errors.get(0).getMessage());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr='value'>Content</element>";
        List<Node> fragments = XmlTreeBuilder.parseFragment(xmlFragment, "http://example.com/", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertEquals(1, fragments.size());
        Element element = (Element) fragments.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
        assertEquals("Content", element.child(0).text());
    }

    @Test
    public void testProcessComments() {
        String xmlStringWithComment = "<root><!-- This is a comment --><element attr='value'>Content</element></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xmlStringWithComment), "http://example.com/");
        assertEquals(1, doc.childNodes().size());
        Element root = doc.rootElement();
        Node comment = root.child(0);
        assertTrue(comment instanceof Comment);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testProcessSelfClosingTags() {
        String xmlString = "<root><img src='image.jpg'/><element attr='value'>Content</element></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com/");
        Element root = doc.rootElement();
        Element img = root.child(0);
        assertEquals("img", img.tagName());
        assertTrue(img.isSelfClosing());
        assertTrue(img.tagName().isSelfClosing());
        Element element = root.child(1);
        assertEquals("element", element.tagName());
        assertFalse(element.isSelfClosing());
    }
}