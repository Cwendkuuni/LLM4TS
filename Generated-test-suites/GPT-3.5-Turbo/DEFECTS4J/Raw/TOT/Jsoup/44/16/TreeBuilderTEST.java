package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import java.util.ArrayList;

public class TreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        String input = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        CharacterReader reader = new CharacterReader(input);
        ParseErrorList errors = new ParseErrorList();
        
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        treeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals(reader, treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.stack);
        assertEquals(baseUri, treeBuilder.baseUri);
        assertNotNull(treeBuilder.currentToken);
        assertEquals(errors, treeBuilder.errors);
    }

    @Test
    public void testParse() {
        String input = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        Document doc = treeBuilder.parse(input, baseUri);
        
        assertEquals("html", doc.tagName());
        
        Element headElement = doc.head();
        assertNotNull(headElement);
        assertEquals("head", headElement.tagName());
        
        Element titleElement = headElement.getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals("title", titleElement.tagName());
        assertEquals("Test Page", titleElement.text());
        
        Element bodyElement = doc.body();
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.tagName());
        
        Element h1Element = bodyElement.getElementsByTag("h1").first();
        assertNotNull(h1Element);
        assertEquals("h1", h1Element.tagName());
        assertEquals("Hello World!", h1Element.text());
    }

    @Test
    public void testRunParser_singleToken() {
        String input = "<html>";
        
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        treeBuilder.initialiseParse(input, "https://www.example.com", null);
        
        treeBuilder.runParser();
        
        assertEquals(1, treeBuilder.stack.size());
        assertEquals("html", treeBuilder.currentElement().tagName());
    }

    @Test
    public void testRunParser_multipleTokens() {
        String input = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>";
        
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        treeBuilder.initialiseParse(input, "https://www.example.com", null);
        
        treeBuilder.runParser();
        
        assertEquals(0, treeBuilder.stack.size());
        assertEquals(null, treeBuilder.currentElement());
    }

    @Test
    public void testProcessStartTag() {
        String input = "<html>";
        
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        treeBuilder.initialiseParse(input, "https://www.example.com", null);
        
        assertTrue(treeBuilder.processStartTag("html"));
        
        assertEquals(1, treeBuilder.stack.size());
        assertEquals("html", treeBuilder.currentElement().tagName());
    }

    @Test
    public void testProcessStartTag_withAttributes() {
        String input = "<html lang=\"en\">";
        
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        treeBuilder.initialiseParse(input, "https://www.example.com", null);
        Attributes attributes = new Attributes();
        attributes.put("lang", "en");
        
        assertTrue(treeBuilder.processStartTag("html", attributes));
        
        assertEquals(1, treeBuilder.stack.size());
        assertEquals("html", treeBuilder.currentElement().tagName());
        assertEquals("en", treeBuilder.currentElement().attr("lang"));
    }

    @Test
    public void testProcessEndTag() {
        String input = "<html></html>";
        
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        treeBuilder.initialiseParse(input, "https://www.example.com", null);
        
        assertTrue(treeBuilder.processStartTag("html"));
        assertTrue(treeBuilder.processEndTag("html"));
        
        assertEquals(0, treeBuilder.stack.size());
        assertEquals(null, treeBuilder.currentElement());
    }

    @Test
    public void testCurrentElement_stackNotEmpty() {
        String input = "<html><head><title>Test Page</title></head></html>";
        
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        treeBuilder.initialiseParse(input, "https://www.example.com", null);
        treeBuilder.processStartTag("html");
        treeBuilder.processStartTag("head");
        
        Element currentElement = treeBuilder.currentElement();
        
        assertEquals("head", currentElement.tagName());
    }

    @Test
    public void testCurrentElement_stackEmpty() {
        String input = "<html></html>";
        
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        
        treeBuilder.initialiseParse(input, "https://www.example.com", null);
        
        Element currentElement = treeBuilder.currentElement();
        
        assertNull(currentElement);
    }
}