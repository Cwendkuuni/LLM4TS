package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String input = "<html></html>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();
        treeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals("https://example.com", treeBuilder.baseUri);
        assertEquals("<html></html>", treeBuilder.reader.getInput());
        assertEquals(errors, treeBuilder.errors);
        assertNull(treeBuilder.doc);
        assertEquals(0, treeBuilder.stack.size());
        assertEquals(null, treeBuilder.currentToken);
        assertEquals(0, errors.size());
    }

    @Test
    public void testParse() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String input = "<html></html>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();
        Document document = treeBuilder.parse(input, baseUri, errors);

        assertNotNull(document);
        assertEquals("https://example.com", treeBuilder.baseUri);
        assertEquals("<html></html>", treeBuilder.reader.getInput());
        assertEquals(errors, treeBuilder.errors);
        assertEquals(document, treeBuilder.doc);
    }

    @Test
    public void testRunParser() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String input = "<html></html>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();
        treeBuilder.initialiseParse(input, baseUri, errors);
        treeBuilder.runParser();

        assertEquals("https://example.com", treeBuilder.baseUri);
        assertEquals("<html></html>", treeBuilder.reader.getInput());
        assertEquals(errors, treeBuilder.errors);
        assertNotNull(treeBuilder.doc);
        assertEquals(0, treeBuilder.stack.size());
        assertNull(treeBuilder.currentToken);
        assertEquals(0, errors.size());
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String name = "html";
        boolean result = treeBuilder.processStartTag(name);

        assertTrue(result);
        assertEquals(name, treeBuilder.start.name);
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String name = "div";
        Attributes attrs = new Attributes();
        attrs.put("class", "container");

        boolean result = treeBuilder.processStartTag(name, attrs);

        assertTrue(result);
        assertEquals(name, treeBuilder.start.name);
        assertEquals(attrs, treeBuilder.start.getAttributes());
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder();
        String name = "html";
        boolean result = treeBuilder.processEndTag(name);

        assertTrue(result);
        assertEquals(name, treeBuilder.end.name);
    }

    @Test
    public void testCurrentElement() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Element element1 = new Element("div");
        Element element2 = new Element("span");
        treeBuilder.stack.add(element1);
        treeBuilder.stack.add(element2);

        Element currentElement = treeBuilder.currentElement();

        assertEquals(element2, currentElement);
    }
}