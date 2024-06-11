package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Test class for TreeBuilder
 */
public class TreeBuilderTest {

    @Test
    public void testParse() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        String input = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = "http://test.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        Document doc = treeBuilder.parse(input, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testParseWithErrors() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        String input = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = "http://test.com";
        ParseErrorList errors = new ParseErrorList();
        Document doc = treeBuilder.parse(input, baseUri, errors);
        assertNotNull(doc);
        // Add assertions for errors here
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullInput() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        String input = null;
        String baseUri = "http://test.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        treeBuilder.parse(input, baseUri);
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullBaseUri() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        String input = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = null;
        ParseErrorList errors = ParseErrorList.noTracking();
        treeBuilder.parse(input, baseUri);
    }

    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processStartTag(Token token) {
                return false;
            }
        };
        String input = "<html>";
        String baseUri = "http://test.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        treeBuilder.initialiseParse(input, baseUri, errors);
        StartTag startTag = mock(StartTag.class);
        when(startTag.name()).thenReturn("html");
        treeBuilder.process(startTag);
        assertNull(treeBuilder.currentElement());
    }

    @Test
    public void testProcessEndTag() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processEndTag(Token token) {
                return false;
            }
        };
        String input = "<html><body></body></html>";
        String baseUri = "http://test.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        treeBuilder.initialiseParse(input, baseUri, errors);
        EndTag endTag = mock(EndTag.class);
        when(endTag.name()).thenReturn("html");
        treeBuilder.process(endTag);
        assertNull(treeBuilder.currentElement());
    }

    @Test
    public void testProcessStartTagWithAttributes() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processStartTag(Token token) {
                return false;
            }

            @Override
            protected boolean processStartTag(String name, Attributes attrs) {
                return false;
            }
        };
        String input = "<html lang='en'><head><title>Test</title></head><body></body></html>";
        String baseUri = "http://test.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        treeBuilder.initialiseParse(input, baseUri, errors);
        StartTag startTag = mock(StartTag.class);
        when(startTag.name()).thenReturn("html");
        Attributes attrs = mock(Attributes.class);
        when(attrs.get("lang")).thenReturn("en");
        treeBuilder.process(startTag);
        Element currentElement = treeBuilder.currentElement();
        assertNotNull(currentElement);
        //Add assertions for attributes here
    }

    @Test
    public void testRunParser() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        String input = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = "http://test.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        treeBuilder.initialiseParse(input, baseUri, errors);
        treeBuilder.runParser();
        Element rootElement = treeBuilder.currentElement();
        assertNotNull(rootElement);
        assertNotNull(rootElement.tagName());
    }

    @Test
    public void testInitialiseParseWithNullInput() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        String input = null;
        String baseUri = "http://test.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        treeBuilder.initialiseParse(input, baseUri, errors);
        // Add assertions for errors here
    }

    @Test
    public void testInitialiseParseWithNullBaseUri() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        String input = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = null;
        ParseErrorList errors = ParseErrorList.noTracking();
        treeBuilder.initialiseParse(input, baseUri, errors);
        // Add assertions for errors here
    }

    @Test
    public void testInitialiseParseWithErrors() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }
        };
        String input = "<html><head><title>Test</title></head><body></body></html>";
        String baseUri = "http://test.com";
        ParseErrorList errors = new ParseErrorList();
        treeBuilder.initialiseParse(input, baseUri, errors);
        // Add assertions for errors here
    }

    @Test
    public void testProcessStartTagWithEmptyName() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return true;
            }

            @Override
            protected boolean processStartTag(Token token) {
                return false;
            }

            @Override
            protected boolean processStartTag(String name, Attributes attrs) {
                return false;
            }
        };
        String input = "<html>";
        String baseUri = "http://test.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        treeBuilder.initialiseParse(input, baseUri, errors);
        StartTag startTag = mock(StartTag.class);
        when(startTag.name()).thenReturn("");
        treeBuilder.process(startTag);
        assertNull(treeBuilder.currentElement());
        // Add assertions for errors here
    }
}