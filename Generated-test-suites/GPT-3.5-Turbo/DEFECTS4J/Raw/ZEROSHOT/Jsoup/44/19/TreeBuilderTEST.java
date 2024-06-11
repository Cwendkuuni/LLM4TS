package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class TreeBuilderTest {

    private TreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new TreeBuilderImpl();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        treeBuilder.initialiseParse(input, baseUri, errors);

        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertEquals(baseUri, treeBuilder.baseUri);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.errors);
    }

    @Test
    public void testParse() {
        String input = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        Document doc = treeBuilder.parse(input, baseUri, errors);

        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testRunParser() {
        String input = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        treeBuilder.initialiseParse(input, baseUri, errors);
        treeBuilder.runParser();
        Document doc = treeBuilder.doc;

        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testProcessStartTag() {
        String name = "div";
        assertTrue(treeBuilder.processStartTag(name));
    }

    @Test
    public void testProcessStartTagWithAttrs() {
        String name = "a";
        Attributes attrs = new Attributes();
        attrs.put("href", "https://www.example.com");
        assertTrue(treeBuilder.processStartTag(name, attrs));
    }

    @Test
    public void testProcessEndTag() {
        String name = "div";
        assertTrue(treeBuilder.processEndTag(name));
    }

    @Test
    public void testCurrentElement() {
        Element element = new Element("div");
        treeBuilder.stack.add(element);
        assertEquals(element, treeBuilder.currentElement());
    }
}