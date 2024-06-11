package org.jsoup.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        String xml = "<>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "baseUri");
        Assertions.assertNotNull(doc);
        Assertions.assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testParseSimpleXml() {
        String xml = "<root><child>Hello World</child></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "baseUri");
        Assertions.assertNotNull(doc);
        Assertions.assertEquals(1, doc.childNodes().size());
        Element root = (Element) doc.child(0);
        Assertions.assertEquals("root", root.tagName());
        Assertions.assertEquals(1, root.childNodes().size());
        Element child = (Element) root.child(0);
        Assertions.assertEquals("child", child.tagName());
        Assertions.assertEquals("Hello World", child.text());
    }

    @Test
    public void testParseSelfClosingTag() {
        String xml = "<img src=\"image.png\" />";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "baseUri");
        Assertions.assertNotNull(doc);
        Assertions.assertEquals(1, doc.childNodes().size());
        Element img = (Element) doc.child(0);
        Assertions.assertEquals("img", img.tagName());
        Assertions.assertFalse(img.hasChildNodes());
    }

    @Test
    public void testParseInvalidXml() {
        String xml = "<root><child><";
        ParseErrorList errors = ParseErrorList.newInstance();
        Document doc = null;
        try {
            doc = XmlTreeBuilder.parse(new StringReader(xml), "baseUri");
        } catch (IllegalArgumentException e) {
            errors.add(e.getCause());
        }
        Assertions.assertNotNull(doc);
        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Error at line 1: Unexpected character '<'. Expected '>'.", errors.get(0).toString());
    }

    @Test
    public void testParseXmlWithComment() {
        String xml = "<!-- This is a comment --><root><child>Hello World</child></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "baseUri");
        Assertions.assertNotNull(doc);
        Assertions.assertEquals(1, doc.childNodes().size());
        Element root = (Element) doc.child(0);
        Assertions.assertEquals("root", root.tagName());
        Assertions.assertEquals(1, root.childNodes().size());
        Element child = (Element) root.child(0);
        Assertions.assertEquals("child", child.tagName());
        Assertions.assertEquals("Hello World", child.text());
        Assertions.assertEquals(1, doc.childNodes().size());
        Node comment = doc.childNodes().get(0);
        Assertions.assertTrue(comment instanceof Comment);
        Assertions.assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXmlWithCDataSection() {
        String xml = "<root><![CDATA[<script type=\"text/javascript\"> var x = 1; </script>]]><child>Hello World</child></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "baseUri");
        Assertions.assertNotNull(doc);
        Assertions.assertEquals(1, doc.childNodes().size());
        Element root = (Element) doc.child(0);
        Assertions.assertEquals("root", root.tagName());
        Assertions.assertEquals(1, root.childNodes().size());
        Element child = (Element) root.child(0);
        Assertions.assertEquals("child", child.tagName());
        Assertions.assertEquals("Hello World", child.text());
        Node cdata = doc.childNodes().get(0);
        Assertions.assertTrue(cdata instanceof CDataNode);
        Assertions.assertEquals("<script type=\"text/javascript\"> var x = 1; </script>", cdata.text());
    }

    @Test
    public void testParseXmlWithDoctype() {
        String xml = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">\n<html><body><h1>Test</h1></body></html>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "baseUri");
        Assertions.assertNotNull(doc);
        Assertions.assertEquals(1, doc.childNodes().size());
        DocumentType doctype = (DocumentType) doc.childNodes().get(0);
        Assertions.assertEquals("html", doctype.nodeName());
        Assertions.assertEquals("about:legacy-compat", doctype.getPublicIdentifier());
        Assertions.assertEquals(null, doctype.getSystemIdentifier());
    }

    @Test
    public void testParseXmlFragment() {
        String xmlFragment = "<root><child>Hello World</child></root>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Document doc = new Document();
        doc.appendChild(nodes.get(0));
        Assertions.assertEquals(1, doc.childNodes().size());
        Element root = (Element) doc.child(0);
        Assertions.assertEquals("root", root.tagName());
        Assertions.assertEquals(1, root.childNodes().size());
        Element child = (Element) root.child(0);
        Assertions.assertEquals("child", child.tagName());
        Assertions.assertEquals("Hello World", child.text());
    }
}