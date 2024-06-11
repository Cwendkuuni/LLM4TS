package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Test cases for the TreeBuilder class
 */
public class TreeBuilderTest {

    private TreeBuilder treeBuilder;
    private Document doc;
    private Tokeniser tokeniser;
    private ArrayList<Element> stack;
    private ParseErrorList errors;

    @Before
    public void setup() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        doc = null;
        tokeniser = null;
        stack = new ArrayList<>();
        errors = new ParseErrorList();
    }

    // Test method: initialiseParse
    @Test
    public void testInitialiseParse() {
        treeBuilder.initialiseParse("input", "baseUri", errors);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.baseUri);
        assertNull(treeBuilder.currentToken);
        assertNull(treeBuilder.errors);
    }

    // Test method: parse
    @Test
    public void testParse() {
        treeBuilder.parse("input", "baseUri");
        assertNotNull(treeBuilder.doc);
    }

    // Test method: parse with errors tracking
    @Test
    public void testParseWithErrors() {
        treeBuilder.parse("input", "baseUri", errors);
        assertEquals(0, errors.size());
    }

    // Test method: processStartTag
    @Test
    public void testProcessStartTag() {
        treeBuilder.processStartTag("name");
        assertNotNull(treeBuilder.currentElement());
    }

    // Test method: processStartTag with attributes
    @Test
    public void testProcessStartTagWithAttributes() {
        Attributes attrs = new Attributes();
        attrs.put("key", "value");
        treeBuilder.processStartTag("name", attrs);
        Element element = treeBuilder.currentElement();
        assertNotNull(element.attributes());
        assertEquals("value", element.attr("key"));
    }

    // Test method: processEndTag
    @Test
    public void testProcessEndTag() {
        treeBuilder.processEndTag("name");
        assertEquals(stack.size() - 1, treeBuilder.stack.lastIndexOf(treeBuilder.currentElement()).intValue());
    }

    // Test method: processStartTag and processEndTag with same name
    @Test(expected = IllegalStateException.class)
    public void testProcessStartTagAndEndTagWithSameName() {
        treeBuilder.processStartTag("name");
        treeBuilder.processEndTag("name");
    }

    // Test method: processEndTag with null name
    @Test(expected = NullPointerException.class)
    public void testProcessEndTagWithNullName() {
        treeBuilder.processEndTag(null);
    }

    // Test method: processEndTag with empty stack
    @Test
    public void testProcessEndTagWithEmptyStack() {
        treeBuilder.processEndTag("name");
        assertNull(treeBuilder.currentElement());
    }
}