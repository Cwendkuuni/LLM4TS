package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.initialiseParse("input", "baseUri", null);

        assertEquals("input", treeBuilder.reader.input);
        assertEquals("baseUri", treeBuilder.baseUri);
        assertNull(treeBuilder.errors);
        assertEquals("baseUri", treeBuilder.doc.baseUri());
        assertEquals(32, treeBuilder.stack.size());
        assertNull(treeBuilder.tokeniser.tokeniserOutput);
    }

    @Test
    public void testParse() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Document document = treeBuilder.parse("input", "baseUri");
        assertNull(treeBuilder.errors);
        assertEquals("baseUri", document.baseUri());
    }

    @Test
    public void testProcess() {
        TreeBuilderImpl treeBuilder = new TreeBuilderImpl();
        Token token = new Token(Token.TokenType.StartTag);
        boolean result = treeBuilder.process(token);
        assertTrue(result);
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilderImpl treeBuilder = new TreeBuilderImpl();
        boolean result = treeBuilder.processStartTag("name");
        assertTrue(result);
    }

    @Test
    public void testProcessStartTagWithAttrs() {
        TreeBuilderImpl treeBuilder = new TreeBuilderImpl();
        Attributes attrs = new Attributes();
        boolean result = treeBuilder.processStartTag("name", attrs);
        assertTrue(result);
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilderImpl treeBuilder = new TreeBuilderImpl();
        boolean result = treeBuilder.processEndTag("name");
        assertTrue(result);
    }

    @Test
    public void testCurrentElement() {
        TreeBuilderImpl treeBuilder = new TreeBuilderImpl();
        Element element = treeBuilder.currentElement();
        assertNull(element);
    }

    private class TreeBuilderImpl extends TreeBuilder {

        @Override
        protected boolean process(Token token) {
            return true;
        }
    }
}