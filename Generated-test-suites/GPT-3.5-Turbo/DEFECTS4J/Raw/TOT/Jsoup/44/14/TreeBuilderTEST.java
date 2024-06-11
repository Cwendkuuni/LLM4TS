package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        String input = "<html><body><h1>Test</h1></body></html>";
        String baseUri = "https://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();

        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // dummy implementation for testing purposes
                return true;
            }
        };

        treeBuilder.initialiseParse(input, baseUri, errors);

        Assert.assertNotNull(treeBuilder.doc);
        Assert.assertNotNull(treeBuilder.reader);
        Assert.assertNotNull(treeBuilder.tokeniser);
        Assert.assertNotNull(treeBuilder.stack);
        Assert.assertNotNull(treeBuilder.baseUri);
    }

    @Test
    public void testProcessStartTag() {
        String name = "div";

        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // dummy implementation for testing purposes
                return true;
            }
        };

        boolean result = treeBuilder.processStartTag(name);

        Assert.assertTrue(result);
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        String name = "div";
        Attributes attrs = new Attributes();
        attrs.put("id", "container");

        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // dummy implementation for testing purposes
                return true;
            }
        };

        boolean result = treeBuilder.processStartTag(name, attrs);

        Assert.assertTrue(result);
    }

    @Test
    public void testProcessEndTag() {
        String name = "div";

        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // dummy implementation for testing purposes
                return true;
            }
        };

        boolean result = treeBuilder.processEndTag(name);

        Assert.assertTrue(result);
    }

    @Test
    public void testCurrentElement() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                // dummy implementation for testing purposes
                return true;
            }
        };

        Assert.assertNull(treeBuilder.currentElement());

        treeBuilder.stack.add(new Element("div"));
        treeBuilder.stack.add(new Element("span"));

        Assert.assertNotNull(treeBuilder.currentElement());
        Assert.assertEquals("span", treeBuilder.currentElement().tagName());
    }
}