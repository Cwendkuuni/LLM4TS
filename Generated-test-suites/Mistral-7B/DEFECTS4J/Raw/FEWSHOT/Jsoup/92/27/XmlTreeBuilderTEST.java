package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() throws IOException {
        String xml = "<root><element tag='test' attr='value' /></root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml));
        assertNotNull(doc);
        assertEquals("root", doc.tagName());
        Element root = doc.elementSibling(0);
        assertEquals("element", root.tagName());
        Attribute attr = root.attribute("tag");
        assertEquals("test", attr.getValue());
        assertNull(root.text());
    }

    @Test
    public void testParseWithBaseUri() throws IOException {
        String xml = "<import xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">" +
                "<root xmlns=\"http://test.com/\">" +
                "<element tag='test' attr='value' />" +
                "</root>" +
                "</import>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml), "http://test.com/");
        assertNotNull(doc);
        assertEquals("root", doc.tagName());
        Element root = doc.elementSibling(0);
        assertEquals("element", root.tagName());
        Attribute attr = root.attribute("tag");
        assertEquals("test", attr.getValue());
        assertNull(root.text());
    }

    @Test
    public void testParseFragment() {
        String xml = "<root><element tag='test' attr='value' /></root>";
        List<Node> fragment = new XmlTreeBuilder().parseFragment(xml, null, null, null);
        assertNotNull(fragment);
        assertEquals(1, fragment.size());
        Element root = (Element) fragment.get(0);
        assertEquals("root", root.tagName());
        Element element = root.elementSibling(0);
        assertEquals("element", element.tagName());
        Attribute attr = element.attribute("tag");
        assertEquals("test", attr.getValue());
        assertNull(element.text());
    }

    @Test
    public void testParseFragmentWithContext() {
        String xml = "<root><element tag='test' attr='value' /></root>";
        Element context = new Element("context");
        List<Node> fragment = new XmlTreeBuilder().parseFragment(xml, context, null, null);
        assertNotNull(fragment);
        assertEquals(1, fragment.size());
        Element root = (Element) fragment.get(0);
        assertEquals("root", root.tagName());
        Element element = root.elementSibling(0);
        assertEquals("element", element.tagName());
        Attribute attr = element.attribute("tag");
        assertEquals("test", attr.getValue());
        assertNull(element.text());
        assertEquals(context, root.parent());
    }

    @Test
    public void testDoctypeProcessing() {
        String xml = "<!DOCTYPE root SYSTEM \"file.dtd\">\n<root><element tag='test' /></root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml));
        assertNotNull(doc);
        assertEquals("root", doc.tagName());
        Element element = doc.elementSibling(0);
        assertEquals("element", element.tagName());
        assertNull(element.text());
        DocumentType doctype = doc.doctype();
        assertNotNull(doctype);
        assertEquals("file.dtd", doctype.systemIdentifier());
    }

    @Test
    public void testCommentProcessing() {
        String xml = "<!-- This is a comment -->";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml));
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        Node comment = doc.childNodes().get(0);
        assertTrue(comment instanceof Comment);
        assertEquals("This is a comment", comment.text().toString());
    }

    @Test
    public void testCDataProcessing() {
        String xml = "<![CDATA[This is some CDATA]]>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml));
        assertNotNull(doc);
        assertEquals(1, doc.childNodes().size());
        Node cdata = doc.childNodes().get(0);
        assertTrue(cdata instanceof CDataNode);
        assertEquals("This is some CDATA", cdata.text().toString());
    }

    @Test
    public void testSelfClosingTagProcessing() {
        String xml = "<root><element tag='test' /></root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml));
        assertNotNull(doc);
        assertEquals("root", doc.tagName());
        Element element = doc.elementSibling(0);
        assertEquals("element", element.tagName());
        assertNull(element.text());
        assertTrue(element.hasAttr("tag"));
        assertFalse(element.hasText());
    }

    @Test(expected = RuntimeException.class)
    public void testUnexpectedTokenType() {
        String xml = "<root><element tag='test' /></root><random>";
        new XmlTreeBuilder().parse(new StringReader(xml));
    }
}