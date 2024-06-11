package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

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

    @Test
    public void testInitialiseParse() {
        String input = "<html><body><div></div></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();

        treeBuilder.initialiseParse(input, baseUri, errors);

        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.baseUri);
        assertNotNull(treeBuilder.errors);
    }

    @Test
    public void testParse() {
        String input = "<html><body><div></div></body></html>";
        String baseUri = "http://example.com";

        Document result = treeBuilder.parse(input, baseUri);

        assertNotNull(result);
        assertEquals("http://example.com", result.baseUri());
        assertNotNull(result.body());
        assertNotNull(result.head());
        assertEquals(1, result.body().childNodeSize());
        assertEquals("div", result.body().child(0).nodeName());
    }

    @Test
    public void testProcessStartTag() {
        String name = "div";

        boolean result = treeBuilder.processStartTag(name);

        assertFalse(result);
    }

    @Test
    public void testProcessStartTagWithAttrs() {
        String name = "div";
        Attributes attrs = new Attributes();

        boolean result = treeBuilder.processStartTag(name, attrs);

        assertFalse(result);
    }

    @Test
    public void testProcessEndTag() {
        String name = "div";

        boolean result = treeBuilder.processEndTag(name);

        assertFalse(result);
    }

    @Test
    public void testCurrentElement() {
        assertNull(treeBuilder.currentElement());
    }
}