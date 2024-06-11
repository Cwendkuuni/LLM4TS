
package org.jsoup.parser;

import org.jsoup.nodes.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class TreeBuilderTest {
    private static final String BASE_URI = "https://www.example.com/";

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
    public void testInitialiseParseNotNullInput() {
        try {
            treeBuilder.initialiseParse(null, BASE_URI, null);
        } catch (Exception e) {
            Assert.fail("Should not throw an exception for null input");
        }
    }

    @Test
    public void testInitialiseParseNotNullBaseUri() {
        try {
            treeBuilder.initialiseParse("", null, null);
        } catch (Exception e) {
            Assert.fail("Should not throw an exception for null baseUri");
        }
    }

    @Test
    public void testParse() {
        Document document = treeBuilder.parse("", BASE_URI);
        Assert.assertNotNull("Document should not be null", document);
    }

    @Test
    public void testParseWithErrors() {
        ParseErrorList errorList = new ParseErrorList();
        Document document = treeBuilder.parse("", BASE_URI, errorList);
        Assert.assertNotNull("Document should not be null", document);
        Assert.assertNotNull("ParseErrorList should not be null", errorList);
    }

    @Test
    public void testRunParser() {
        treeBuilder.initialiseParse("", BASE_URI, null);

        try {
            treeBuilder.runParser();
        } catch (Exception e) {
            Assert.fail("Should not throw an exception");
        }
    }

    @Test
    public void testProcessStartTag() {
        treeBuilder.initialiseParse("", BASE_URI, null);

        boolean result = treeBuilder.processStartTag("div");
        Assert.assertFalse("Should return false if process() returns false", result);
    }

    @Test
    public void testProcessStartTagWithAttrs() {
        treeBuilder.initialiseParse("", BASE_URI, null);

        boolean result = treeBuilder.processStartTag("a", new Attributes());
        Assert.assertFalse("Should return false if process() returns false", result);
    }

    @Test
    public void testProcessEndTag() {
        treeBuilder.initialiseParse("", BASE_URI, null);

        boolean result = treeBuilder.processEndTag("div");
        Assert.assertFalse("Should return false if process() returns false", result);
    }

    @Test
    public void testCurrentElementNonEmptyStack() {
        Element element = new Element(Tag.valueOf("div"), BASE_URI);
        treeBuilder.stack = new ArrayList<>();
        treeBuilder.stack.add(element);

        Element currentElement = treeBuilder.currentElement();
        Assert.assertEquals("Should return the top element in the stack", element, currentElement);
    }

    @Test
    public void testCurrentElementEmptyStack() {
        treeBuilder.stack = new ArrayList<>();

        Element currentElement = treeBuilder.currentElement();
        Assert.assertNull("Should return null if stack is empty", currentElement);
    }
}
