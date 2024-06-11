package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.parser.XmlTreeBuilder;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xml = "<root><element1 attr1=\"value1\">Content1</element1><element2 attr2=\"value2\"/></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader(xml), "");
        assertEquals("root", doc.tagName());

        Element root = doc.element(0);
        List<Node> children = root.childNodes();
        assertEquals(2, children.size());

        Element element1 = (Element) children.get(0);
        assertEquals("element1", element1.tagName());
        assertEquals("value1", element1.attr("attr1"));
        assertEquals("Content1", element1.text());

        Element element2 = (Element) children.get(1);
        assertEquals("element2", element2.tagName());
        assertNull(element2.attr("attr2"));
    }

    @Test
    public void testProcessTokens() {
        String xml = "<root><element1 attr1=\"value1\">Content1</element1><element2 attr2=\"value2\" /></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        Tokenizer tokenizer = new Tokenizer(new StringReader(xml));
        List<Token> tokens = tokenizer.tokens();

        for (Token token : tokens) {
            xmlTreeBuilder.process(token);
        }

        Document doc = xmlTreeBuilder.doc;
        Element root = doc.element(0);
        List<Node> children = root.childNodes();

        assertEquals(2, children.size());

        Element element1 = (Element) children.get(0);
        assertEquals("element1", element1.tagName());
        assertEquals("value1", element1.attr("attr1"));
        assertEquals("Content1", element1.text());

        Element element2 = (Element) children.get(1);
        assertEquals("element2", element2.tagName());
        assertNull(element2.attr("attr2"));
    }

    @Test
    public void testProcessComment() {
        String xml = "<!-- This is a comment --><root><element attr=\"value\">Content</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        Tokenizer tokenizer = new Tokenizer(new StringReader(xml));
        List<Token> tokens = tokenizer.tokens();

        for (Token token : tokens) {
            xmlTreeBuilder.process(token);
        }

        Document doc = xmlTreeBuilder.doc;
        Element root = doc.element(0);
        List<Node> children = root.childNodes();

        assertEquals(1, children.size());
        Element element = (Element) children.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Content", element.text());

        Node comment = doc.childNodes().get(0);
        assertTrue(comment instanceof Comment);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testProcessDoctype() {
        String xml = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED > ]><root><element attr=\"value\">Content</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        Tokenizer tokenizer = new Tokenizer(new StringReader(xml));
        List<Token> tokens = tokenizer.tokens();

        for (Token token : tokens) {
            xmlTreeBuilder.process(token);
        }

        Document doc = xmlTreeBuilder.doc;
        Element root = doc.element(0);
        List<Node> children = root.childNodes();

        assertEquals(1, children.size());
        Element element = (Element) children.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Content", element.text());

        DocumentType doctype = (DocumentType) doc.childNodes().get(0);
        assertEquals("root", doctype.nodeName());
        assertEquals("root", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        String xml = "<root><element /></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        Tokenizer tokenizer = new Tokenizer(new StringReader(xml));
        List<Token> tokens = tokenizer.tokens();

        for (Token token : tokens) {
            xmlTreeBuilder.process(token);
        }

        assertEquals(1, xmlTreeBuilder.stack.size());
        Element root = (Element) xmlTreeBuilder.stack.get(0);
        assertEquals("root", root.tagName());

        xmlTreeBuilder.process(tokens.get(0)); // EndTag

        assertEquals(0, xmlTreeBuilder.stack.size());
        assertNull(root.parentNode());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr=\"value\">Content</element>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase).get();
        assertEquals(1, nodes.size());
        Element element = (Element) nodes.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Content", element.text());
    }
}