package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xml = "<root><element attr='value'/><anotherElement/></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "");
        assertEquals("root", doc.tagName());
        Element root = doc.rootElement();
        assertEquals("element", root.child(0).tagName());
        assertEquals("value", root.child(0).attribute("attr"));
        assertEquals("anotherElement", root.child(1).tagName());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr='value'/>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase).childNodes();
        assertEquals(1, nodes.size());
        Element element = (Element) nodes.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder builder = new XmlTreeBuilder();
        String xml = "<root><element attr='value'/><anotherElement/></root>";
        Reader input = new StringReader(xml);
        ParseSettings settings = builder.defaultSettings();
        ParseErrorList errors = ParseErrorList.noTracking();
        builder.initialiseParse(input, "", errors, settings);

        Token token;
        while ((token = builder.nextToken()) != null) {
            builder.process(token);
        }

        assertEquals("root", builder.doc.tagName());
        Element root = builder.doc.rootElement();
        assertEquals("element", root.child(0).tagName());
        assertEquals("value", root.child(0).attribute("attr"));
        assertEquals("anotherElement", root.child(1).tagName());
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder builder = new XmlTreeBuilder();
        String xml = "<root><element><anotherElement/></element></root>";
        Reader input = new StringReader(xml);
        ParseSettings settings = builder.defaultSettings();
        ParseErrorList errors = ParseErrorList.noTracking();
        builder.initialiseParse(input, "", errors, settings);

        Token token;
        while ((token = builder.nextToken()) != null) {
            builder.process(token);
        }

        assertEquals("root", builder.doc.tagName());
        Element root = builder.doc.rootElement();
        Element element = root.child(0);
        assertEquals("anotherElement", element.child(0).tagName());
    }

    @Test
    public void testProcessComment() {
        TreeBuilder builder = new XmlTreeBuilder();
        String xml = "<!-- This is a comment -->";
        Reader input = new StringReader(xml);
        ParseSettings settings = builder.defaultSettings();
        ParseErrorList errors = ParseErrorList.noTracking();
        builder.initialiseParse(input, "", errors, settings);

        Token token;
        while ((token = builder.nextToken()) != null) {
            builder.process(token);
        }

        assertEquals(1, builder.doc.childNodes().size());
        Comment comment = (Comment) builder.doc.childNodes().get(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testProcessCharacter() {
        TreeBuilder builder = new XmlTreeBuilder();
        String xml = "<root>&#x00A9;</root>";
        Reader input = new StringReader(xml);
        ParseSettings settings = builder.defaultSettings();
        ParseErrorList errors = ParseErrorList.noTracking();
        builder.initialiseParse(input, "", errors, settings);

        Token token;
        while ((token = builder.nextToken()) != null) {
            builder.process(token);
        }

        assertEquals("root", builder.doc.tagName());
        Element root = builder.doc.rootElement();
        TextNode textNode = (TextNode) root.child(0);
        assertEquals("©", textNode.text());
    }

    @Test
    public void testProcessDoctype() {
        TreeBuilder builder = new XmlTreeBuilder();
        String xml = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
        Reader input = new StringReader(xml);
        ParseSettings settings = builder.defaultSettings();
        ParseErrorList errors = ParseErrorList.noTracking();
        builder.initialiseParse(input, "", errors, settings);

        Token token;
        while ((token = builder.nextToken()) != null) {
            builder.process(token);
        }

        Document doc = builder.doc;
        DocumentType doctype = (DocumentType) doc.childNodes().get(0);
        assertEquals("html", doctype.tagName());
        assertEquals("about:legacy-compat", doctype.systemId());
    }

    @Test(expected = AssertionError.class)
    public void testProcessUnexpectedTokenType() {
        TreeBuilder builder = new XmlTreeBuilder();
        String xml = "<root>";
        Reader input = new StringReader(xml);
        ParseSettings settings = builder.defaultSettings();
        ParseErrorList errors = ParseErrorList.noTracking();
        builder.initialiseParse(input, "", errors, settings);

        Token token = builder.nextToken();
        builder.process(token);

        // This should never be reached as the test case should have thrown an AssertionError when processing the unexpected token type.
        fail("Test case did not fail as expected.");
    }

    @Test
    public void testInsertNode() {
        TreeBuilder builder = new XmlTreeBuilder();
        String xml = "<root><element/></root>";
        Reader input = new StringReader(xml);
        ParseSettings settings = builder.defaultSettings();
        ParseErrorList errors = ParseErrorList.noTracking();
        builder.initialiseParse(input, "", errors, settings);

        Token token;
        while ((token = builder.nextToken()) != null) {
            builder.process(token);
        }

        Element root = builder.doc.rootElement();
        Element element = new Element("element");
        builder.insertNode(element);
        root.appendChild(element);
        assertEquals(2, root.childNodes().size());
    }
}