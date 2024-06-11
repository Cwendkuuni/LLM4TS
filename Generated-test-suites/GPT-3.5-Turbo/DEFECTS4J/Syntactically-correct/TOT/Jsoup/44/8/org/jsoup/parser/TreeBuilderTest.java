package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TreeBuilderTest {

    private TreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
    }

    @After
    public void tearDown() {
        treeBuilder = null;
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><body></body></html>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();

        treeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals(baseUri, treeBuilder.baseUri);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.errors);
    }

    @Test
    public void testParse() {
        String input = "<html><body></body></html>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();

        Document document = treeBuilder.parse(input, baseUri, errors);

        assertNotNull(document);
        assertEquals(input, document.html());
    }

    @Test
    public void testParseWithDefaultErrors() {
        String input = "<html><body></body></html>";
        String baseUri = "https://www.example.com";

        Document document = treeBuilder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals(input, document.html());
    }

    @Test
    public void testRunParser() {
        String input = "<html><body></body></html>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();

        treeBuilder.initialiseParse(input, baseUri, errors);
        treeBuilder.runParser();

        assertNotNull(treeBuilder.doc);
        assertFalse(treeBuilder.stack.isEmpty());
        assertNull(treeBuilder.errors);
    }

    @Test
    public void testProcessStartTag() {
        String tagName = "div";

        boolean result = treeBuilder.processStartTag(tagName);

        assertTrue(result);
        Element currentElement = treeBuilder.currentElement();
        assertEquals(tagName, currentElement.tagName());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        String tagName = "div";
        Attributes attributes = new Attributes();
        attributes.put("id", "myDiv");

        boolean result = treeBuilder.processStartTag(tagName, attributes);

        assertTrue(result);
        Element currentElement = treeBuilder.currentElement();
        assertEquals(tagName, currentElement.tagName());
        assertEquals("myDiv", currentElement.attr("id"));
    }

    @Test
    public void testProcessEndTag() {
        String tagName = "div";

        boolean result = treeBuilder.processEndTag(tagName);

        assertTrue(result);
        assertTrue(treeBuilder.stack.isEmpty());
    }

    @Test
    public void testCurrentElement() {
        Element element1 = new Element("div");
        Element element2 = new Element("span");

        treeBuilder.stack.add(element1);
        treeBuilder.stack.add(element2);

        Element currentElement = treeBuilder.currentElement();

        assertNotNull(currentElement);
        assertEquals(element2, currentElement);
    }
}