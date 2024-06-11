package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

public class TreeBuilderTest {
    private TreeBuilderTestImpl treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new TreeBuilderTestImpl();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";

        treeBuilder.initialiseParse(input, baseUri, null);

        assertEquals(baseUri, treeBuilder.baseUri);
        assertEquals(0, treeBuilder.stack.size());
        assertNull(treeBuilder.errors);
    }

    @Test
    public void testParseWithoutErrors() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";

        Document document = treeBuilder.parse(input, baseUri);

        assertEquals(baseUri, document.baseUri());
        assertEquals("html", document.tagName());
        assertEquals("head", document.head().tagName());
        assertEquals("body", document.body().tagName());
    }

    @Test
    public void testParseWithErrors() {
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        Document document = treeBuilder.parse(input, baseUri, errors);

        assertEquals(baseUri, document.baseUri());
        assertEquals("html", document.tagName());
        assertEquals("head", document.head().tagName());
        assertEquals("body", document.body().tagName());
        assertEquals(0, errors.size());
    }

    @Test
    public void testProcessStartTag() {
        String tagName = "div";

        boolean result = treeBuilder.processStartTag(tagName);

        assertTrue(result);
        assertSame(Element.class, treeBuilder.currentElement().getClass());
        assertEquals(tagName, treeBuilder.currentElement().tagName());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        String tagName = "div";
        Attributes attrs = new Attributes();

        boolean result = treeBuilder.processStartTag(tagName, attrs);

        assertTrue(result);
        assertSame(Element.class, treeBuilder.currentElement().getClass());
        assertEquals(tagName, treeBuilder.currentElement().tagName());
        assertSame(attrs, treeBuilder.currentElement().attributes());
    }

    @Test
    public void testProcessEndTag() {
        String tagName = "div";

        boolean result = treeBuilder.processEndTag(tagName);

        assertTrue(result);
        assertNull(treeBuilder.currentElement());
    }

    private static class TreeBuilderTestImpl extends TreeBuilder {
        @Override
        protected boolean process(Token token) {
            return true;
        }
    }
}