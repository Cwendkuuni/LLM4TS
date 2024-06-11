package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class TreeBuilderTest {

    private TreeBuilder treeBuilder;

    @Before
    public void setup() {
        treeBuilder = new TestTreeBuilder();
    }

    // Test initialiseParse method
    @Test
    public void testInitialiseParse() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        treeBuilder.initialiseParse(input, baseUri, errors);

        Assert.assertEquals(baseUri, treeBuilder.baseUri);
        Assert.assertEquals(errors, treeBuilder.errors);
        Assert.assertNotNull(treeBuilder.doc);
        Assert.assertNotNull(treeBuilder.reader);
        Assert.assertNotNull(treeBuilder.tokeniser);
        Assert.assertNotNull(treeBuilder.stack);
    }

    // Test parse method
    @Test
    public void testParse() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        Document doc = treeBuilder.parse(input, baseUri, errors);

        Assert.assertNotNull(doc);
        Assert.assertEquals(baseUri, doc.baseUri());
        Assert.assertEquals(input, doc.outerHtml());
    }

    // Test process method
    @Test
    public void testProcess() {
        Token token = new Token.StartTag();
        boolean result = treeBuilder.process(token);

        Assert.assertFalse(result);
    }

    // Test processStartTag method
    @Test
    public void testProcessStartTag() {
        String name = "div";
        boolean result = treeBuilder.processStartTag(name);

        Assert.assertFalse(result);
    }

    // Test processStartTag method with name and attributes
    @Test
    public void testProcessStartTagWithNameAndAttributes() {
        String name = "div";
        Attributes attrs = new Attributes();
        attrs.put("id", "my-div");
        attrs.put("class", "my-class");

        boolean result = treeBuilder.processStartTag(name, attrs);

        Assert.assertFalse(result);
    }

    // Test processEndTag method
    @Test
    public void testProcessEndTag() {
        String name = "div";
        boolean result = treeBuilder.processEndTag(name);

        Assert.assertFalse(result);
    }

    // Test currentElement method
    @Test
    public void testCurrentElement() {
        Element element = treeBuilder.currentElement();

        Assert.assertNull(element);
    }

    // Test runParser method
    @Test
    public void testRunParser() {
        treeBuilder.runParser();
    }

    // Test parse method with W3C DOM
    @Test
    public void testParseWithW3CDOM() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        Document doc = treeBuilder.parse(input, baseUri, errors);
        W3CDom w3CDom = new W3CDom();
        Document w3cDoc = w3CDom.fromJsoup(doc);

        Assert.assertEquals(baseUri, w3cDoc.getDocumentURI());
        Assert.assertNotNull(w3cDoc.getDocumentElement());
    }

    // Test parse method with Jsoup DOM
    @Test
    public void testParseWithJsoupDOM() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        Document doc = treeBuilder.parse(input, baseUri, errors);

        Assert.assertEquals(baseUri, doc.baseUri());
        Assert.assertNotNull(doc.head());
        Assert.assertNotNull(doc.body());
    }

    // Custom TreeBuilder class for testing
    private static class TestTreeBuilder extends TreeBuilder {

        public TestTreeBuilder() {
            super();
        }

        @Override
        protected boolean process(Token token) {
            return false;
        }
    }
}