package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

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

    @Test
    public void testInitialiseParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        treeBuilder.initialiseParse(input, baseUri, errors);

        Assert.assertEquals(baseUri, treeBuilder.baseUri);
        Assert.assertNotNull(treeBuilder.doc);
        Assert.assertNotNull(treeBuilder.reader);
        Assert.assertNotNull(treeBuilder.tokeniser);
        Assert.assertNotNull(treeBuilder.stack);
        Assert.assertEquals(errors, treeBuilder.errors);
    }

    @Test
    public void testParse() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";

        Document document = treeBuilder.parse(input, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseWithErrors() {
        String input = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        Document document = treeBuilder.parse(input, baseUri, errors);
        Assert.assertNotNull(document);
    }

    @Test
    public void testRunParser() {
        treeBuilder.runParser();
    }

    @Test
    public void testProcessStartTag() {
        String tagName = "div";

        boolean result = treeBuilder.processStartTag(tagName);
        Assert.assertFalse(result);
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        String tagName = "div";
        Attributes attrs = new Attributes();

        boolean result = treeBuilder.processStartTag(tagName, attrs);
        Assert.assertFalse(result);
    }

    @Test
    public void testProcessEndTag() {
        String tagName = "div";

        boolean result = treeBuilder.processEndTag(tagName);
        Assert.assertFalse(result);
    }

    @Test
    public void testCurrentElement() {
        Element element = treeBuilder.currentElement();
        Assert.assertNull(element);
    }
}