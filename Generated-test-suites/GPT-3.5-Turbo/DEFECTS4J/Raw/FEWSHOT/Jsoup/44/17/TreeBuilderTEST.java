package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

public class TreeBuilderTest {
    private TreeBuilder treeBuilder;

    @Before
    public void setup() {
        treeBuilder = new TreeBuilderMock();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><head><title>Test</title></head><body><p>Paragraph</p></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();

        treeBuilder.initialiseParse(input, baseUri, errors);

        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.errors);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertEquals(baseUri, treeBuilder.baseUri);
    }

    @Test
    public void testParse() {
        String input = "<html><head><title>Test</title></head><body><p>Paragraph</p></body></html>";
        String baseUri = "http://example.com";

        Document doc = treeBuilder.parse(input, baseUri);

        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.title());
        assertNotNull(doc.body());
        assertEquals(1, doc.getElementsByTag("p").size());
    }

    @Test
    public void testProcessStartTag() {
        String name = "div";

        boolean result = treeBuilder.processStartTag(name);

        assertEquals(true, result);
    }

    @Test
    public void testProcessStartTagWithAttrs() {
        String name = "div";
        Attributes attrs = new Attributes();
        attrs.put("class", "container");

        boolean result = treeBuilder.processStartTag(name, attrs);

        assertEquals(true, result);
    }

    @Test
    public void testProcessEndTag() {
        String name = "div";

        boolean result = treeBuilder.processEndTag(name);

        assertEquals(true, result);
    }

    @Test
    public void testCurrentElement() {
        Element element = treeBuilder.currentElement();

        assertNull(element);
    }

    // Mock class to test abstract TreeBuilder
    private class TreeBuilderMock extends TreeBuilder {
        @Override
        protected boolean process(Token token) {
            return true;
        }
    }
}