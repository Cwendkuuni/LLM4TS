package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<root><element attr='value'></element></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString), "");
        assertNotNull(xmlDoc);
        assertEquals("root", xmlDoc.tagName());
        Element root = xmlDoc.rootElement();
        assertEquals("element", root.child(0).tagName());
        assertEquals("value", root.child(0).attr("attr"));
    }

    @Test
    public void testParseWithComments() {
        String xmlString = "<root><!-- comment --><element attr='value'></element></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString), "");
        Element root = xmlDoc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.tagName());
        Element element = root.child(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        Comment comment = (Comment) root.child(0).sibling();
        assertNotNull(comment);
        assertEquals("comment", comment.text());
    }

    @Test
    public void testParseWithSelfClosingTag() {
        String xmlString = "<root><img src='image.jpg' /> <element attr='value'></element></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString), "");
        Element root = xmlDoc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.tagName());
        Element img = root.child(0);
        assertEquals("img", img.tagName());
        assertEquals("image.jpg", img.attr("src"));
        Element element = root.child(1);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
    }

    @Test
    public void testParseWithDoctype() {
        String xmlString = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED> ]><root><element attr='value'></element></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString), "");
        assertNotNull(xmlDoc);
        DocumentType doctype = xmlDoc.docType();
        assertNotNull(doctype);
        assertEquals("root", doctype.name());
        assertNotNull(doctype.internalSubset());
        Element root = xmlDoc.rootElement();
        Element element = root.child(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr='value'></element>";
        List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Element element = (Element) nodes.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidInput() {
        new XmlTreeBuilder().parse(new StringReader("invalid XML"), "");
    }

    @Test
    public void testParseWithNullInput() {
        try {
            new XmlTreeBuilder().parse(null, "");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseWithNullBaseUri() {
        try {
            new XmlTreeBuilder().parse(new StringReader("<root><element attr='value'></element></root>"), null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseWithEmptyInput() {
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(""), "");
        assertNotNull(xmlDoc);
        assertEquals("", xmlDoc.html());
    }

    @Test
    public void testParseWithCommentAndSelfClosingTag() {
        String xmlString = "<root><!-- comment --><img src='image.jpg' /> <element attr='value'></element></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString), "");
        Element root = xmlDoc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.tagName());
        Comment comment = (Comment) root.child(0).sibling();
        assertNotNull(comment);
        assertEquals("comment", comment.text());
        Element img = root.child(1);
        assertEquals("img", img.tagName());
        assertEquals("image.jpg", img.attr("src"));
        Element element = root.child(2);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
    }

    @Test
    public void testParseWithDoctypeAndSelfClosingTag() {
        String xmlString = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED> ]><root><!-- comment --><img src='image.jpg' /> <element attr='value'></element></root>";
        Document xmlDoc = new XmlTreeBuilder().parse(new StringReader(xmlString), "");
        DocumentType doctype = xmlDoc.docType();
        assertNotNull(doctype);
        assertEquals("root", doctype.name());
        assertNotNull(doctype.internalSubset());
        Element root = xmlDoc.rootElement();
        Comment comment = (Comment) root.child(0).sibling();
        assertNotNull(comment);
        assertEquals("comment", comment.text());
        Element img = root.child(1);
        assertEquals("img", img.tagName());
        assertEquals("image.jpg", img.attr("src"));
        Element element = root.child(2);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
    }
}