package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        String input = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        TreeBuilder treeBuilder = new TreeBuilderStub(input, baseUri, errors);

        assertEquals("http://www.example.com", treeBuilder.baseUri);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.currentToken);
        assertNotNull(treeBuilder.errors);
    }

    @Test
    public void testParse() {
        String input = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        TreeBuilder treeBuilder = new TreeBuilderStub(input, baseUri, errors);

        Document parsedDoc = treeBuilder.parse(input, baseUri);

        assertNotNull(parsedDoc);
        assertEquals("html", parsedDoc.nodeName());
        assertNotNull(parsedDoc.body());
        assertEquals(1, parsedDoc.body().children().size());
        assertEquals("Hello World", parsedDoc.body().getElementsByTag("h1").text());
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilderStub("", "", null);

        boolean result = treeBuilder.processStartTag("div");

        assertTrue(result);
        assertEquals("div", treeBuilder.currentToken.name());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder treeBuilder = new TreeBuilderStub("", "", null);
        Attributes attrs = new Attributes();
        attrs.put("class", "container");

        boolean result = treeBuilder.processStartTag("div", attrs);

        assertTrue(result);
        assertEquals("div", treeBuilder.currentToken.name());
        assertEquals("class", treeBuilder.currentToken.attrs().get(0).getKey());
        assertEquals("container", treeBuilder.currentToken.attrs().get(0).getValue());
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilderStub("", "", null);

        boolean result = treeBuilder.processEndTag("div");

        assertTrue(result);
        assertEquals("div", treeBuilder.currentToken.name());
    }

    @Test
    public void testCurrentElement() {
        TreeBuilder treeBuilder = new TreeBuilderStub("", "", null);

        Element element = treeBuilder.currentElement();

        assertNull(element);
    }


    private class TreeBuilderStub extends TreeBuilder {
        private String input;
        private String baseUri;
        private ParseErrorList errors;

        public TreeBuilderStub(String input, String baseUri, ParseErrorList errors) {
            this.input = input;
            this.baseUri = baseUri;
            this.errors = errors;
        }

        @Override
        protected void initialiseParse(String input, String baseUri, ParseErrorList errors) {
            assertEquals(this.input, input);
            assertEquals(this.baseUri, baseUri);
            assertEquals(this.errors, errors);
        }

        @Override
        protected void runParser() {
            // Stub implementation
        }

        @Override
        protected boolean process(Token token) {
            // Stub implementation
            return false;
        }
    }
}