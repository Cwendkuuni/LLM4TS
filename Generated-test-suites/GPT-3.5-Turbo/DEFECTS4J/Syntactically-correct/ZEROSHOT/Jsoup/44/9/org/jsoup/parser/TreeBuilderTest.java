package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TreeBuilderTest {

    private TreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new TreeBuilderImplementation();
    }

    @Test
    public void parse_shouldReturnDocument() {
        String input = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = treeBuilder.parse(input, baseUri);

        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void processStartTag_shouldReturnTrue() {
        String tagName = "div";
        assertTrue(treeBuilder.processStartTag(tagName));
    }

    @Test
    public void processStartTag_withAttributes_shouldReturnTrue() {
        String tagName = "div";
        Attributes attrs = new Attributes();
        attrs.put("id", "myDiv");
        attrs.put("class", "myClass");

        assertTrue(treeBuilder.processStartTag(tagName, attrs));
    }

    @Test
    public void processEndTag_shouldReturnTrue() {
        String tagName = "div";
        assertTrue(treeBuilder.processEndTag(tagName));
    }

    @Test
    public void currentElement_withStackEmpty_shouldReturnNull() {
        assertNull(treeBuilder.currentElement());
    }

    @Test
    public void currentElement_withStackNotEmpty_shouldReturnElement() {
        Element element = new Element("div");

        treeBuilder.stack.add(element);

        assertEquals(element, treeBuilder.currentElement());
    }
}

class TreeBuilderImplementation extends TreeBuilder {

    @Override
    protected boolean process(Token token) {
        return false;
    }
}