package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TreeBuilderTest {
    private TreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = mock(TreeBuilder.class);
    }

    @Test
    public void testInitialiseParse() {
        // Test with valid input and base URI
        treeBuilder.baseUri = "http://www.example.com";
        
        treeBuilder.initialiseParse("<html></html>", treeBuilder.baseUri, null);
        assertEquals("http://www.example.com", treeBuilder.baseUri);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNull(treeBuilder.errors);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        
        // Test with null input
        try {
            treeBuilder.initialiseParse(null, treeBuilder.baseUri, null);
        } catch (IllegalArgumentException e) {
            assertEquals("String input must not be null", e.getMessage());
        }
        
        // Test with null base URI
        try {
            treeBuilder.initialiseParse("<html></html>", null, null);
        } catch (IllegalArgumentException e) {
            assertEquals("BaseURI must not be null", e.getMessage());
        }
    }

    @Test
    public void testParse() {
        // Test with valid input and base URI
        String input = "<html></html>";
        String baseUri = "http://www.example.com";
        
        Document doc = treeBuilder.parse(input, baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());

        // Test with valid input, base URI, and errors
        ParseErrorList errors = mock(ParseErrorList.class);
        
        doc = treeBuilder.parse(input, baseUri, errors);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals(errors, treeBuilder.errors);

        // Verify that runParser() is called
        verify(treeBuilder, times(2)).runParser();
    }

    @Test
    public void testRunParser() {
        // Test with EOF token
        Token eofToken = new Token(Token.TokenType.EOF);
        when(treeBuilder.tokeniser.read()).thenReturn(eofToken);

        treeBuilder.runParser();

        // Verify that process() is called with the EOF token
        verify(treeBuilder).process(eofToken);
        verify(eofToken).reset();

        // Test with non-EOF token
        Token startTagToken = new Token(Token.TokenType.StartTag);
        Token eofToken = new Token(Token.TokenType.EOF);
        when(treeBuilder.tokeniser.read()).thenReturn(startTagToken, eofToken);

        treeBuilder.runParser();

        // Verify that process() is called with the non-EOF token
        verify(treeBuilder).process(startTagToken);
        verify(startTagToken).reset();
        verify(eofToken).reset();
    }

    @Test
    public void testProcessStartTag() {
        // Test with valid name
        String name = "div";
        Token startTagToken = new Token(Token.TokenType.StartTag);
        when(treeBuilder.start.reset()).thenReturn(startTagToken);

        boolean result = treeBuilder.processStartTag(name);

        assertTrue(result);
        verify(treeBuilder.start).reset();
        verify(startTagToken).name(name);
        verify(treeBuilder).process(startTagToken);

        // Test with null name
        String nullName = null;

        boolean result = treeBuilder.processStartTag(nullName);

        assertFalse(result);
        verify(treeBuilder, never()).process(any(Token.class));
    }

    @Test
    public void testProcessStartTagWithAttrs() {
        // Test with valid name and attributes
        String name = "div";
        Attributes attrs = mock(Attributes.class);
        Token startTagToken = new Token(Token.TokenType.StartTag);
        when(treeBuilder.start.reset()).thenReturn(startTagToken);

        boolean result = treeBuilder.processStartTag(name, attrs);

        assertTrue(result);
        verify(treeBuilder.start).reset();
        verify(startTagToken).nameAttr(name, attrs);
        verify(treeBuilder).process(startTagToken);

        // Test with null name and attributes
        String nullName = null;
        Attributes nullAttrs = null;

        boolean result = treeBuilder.processStartTag(nullName, nullAttrs);

        assertFalse(result);
        verify(treeBuilder, never()).process(any(Token.class));
    }

    @Test
    public void testProcessEndTag() {
        // Test with valid name
        String name = "div";
        Token endTagToken = new Token(Token.TokenType.EndTag);
        when(treeBuilder.end.reset()).thenReturn(endTagToken);

        boolean result = treeBuilder.processEndTag(name);

        assertTrue(result);
        verify(treeBuilder.end).reset();
        verify(endTagToken).name(name);
        verify(treeBuilder).process(endTagToken);

        // Test with null name
        String nullName = null;

        boolean result = treeBuilder.processEndTag(nullName);

        assertFalse(result);
        verify(treeBuilder, never()).process(any(Token.class));
    }

    @Test
    public void testCurrentElement() {
        // Test with non-empty stack
        Element element = mock(Element.class);
        treeBuilder.stack.add(element);

        Element result = treeBuilder.currentElement();

        assertEquals(element, result);

        // Test with empty stack
        treeBuilder.stack.clear();

        Element result = treeBuilder.currentElement();

        assertNull(result);
    }
}