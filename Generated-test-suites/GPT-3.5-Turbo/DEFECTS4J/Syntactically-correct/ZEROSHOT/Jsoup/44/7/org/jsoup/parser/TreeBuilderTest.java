package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

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
    public void initialiseParse_validInputAndBaseUri_successfullyInitialises() {
        String input = "test input";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        treeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals(input, treeBuilder.reader.getInput());
        assertEquals(baseUri, treeBuilder.baseUri);
        assertEquals(errors, treeBuilder.errors);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void initialiseParse_nullInput_throwsIllegalArgumentException() {
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        treeBuilder.initialiseParse(null, baseUri, errors);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void initialiseParse_nullBaseUri_throwsIllegalArgumentException() {
        String input = "test input";
        ParseErrorList errors = new ParseErrorList();
        
        treeBuilder.initialiseParse(input, null, errors);
    }
    
    @Test
    public void parse_validInputAndBaseUri_returnsDocument() {
        String input = "test input";
        String baseUri = "http://example.com";
        
        Document document = treeBuilder.parse(input, baseUri);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void parse_validInputBaseUriAndErrors_returnsDocumentWithErrors() {
        String input = "test input";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        Document document = treeBuilder.parse(input, baseUri, errors);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals(errors, treeBuilder.errors);
    }
    
    @Test
    public void runParser_eofToken_breaksLoop() {
        Token eofToken = new Token(Token.TokenType.EOF);
        treeBuilder.tokeniser = new MockTokeniser(eofToken);
        
        treeBuilder.runParser();
        
        assertTrue(treeBuilder.tokeniser.isReadCalled());
    }
    
    @Test
    public void processStartTag_withName_callsProcess() {
        String tagName = "div";
        Token.StartTag startTag = new Token.StartTag().name(tagName);
        
        boolean result = treeBuilder.processStartTag(tagName);
        
        assertTrue(result);
        assertTrue(treeBuilder.start.wasResetCalled());
    }
    
    @Test
    public void processStartTag_withNameAndAttrs_callsProcess() {
        String tagName = "div";
        Attributes attrs = new Attributes();
        Token.StartTag startTag = new Token.StartTag().nameAttr(tagName, attrs);
        
        boolean result = treeBuilder.processStartTag(tagName, attrs);
        
        assertTrue(result);
        assertTrue(treeBuilder.start.wasResetCalled());
    }
    
    @Test
    public void processEndTag_withName_callsProcess() {
        String tagName = "div";
        Token.EndTag endTag = new Token.EndTag().name(tagName);
        
        boolean result = treeBuilder.processEndTag(tagName);
        
        assertTrue(result);
        assertTrue(treeBuilder.end.wasResetCalled());
    }
    
    @Test
    public void currentElement_stackNotEmpty_returnsCurrentElement() {
        Element element = new Element("div");
        treeBuilder.stack = new ArrayList<>();
        treeBuilder.stack.add(element);
        
        Element currentElement = treeBuilder.currentElement();
        
        assertSame(element, currentElement);
    }
    
    @Test
    public void currentElement_stackEmpty_returnsNull() {
        treeBuilder.stack = new ArrayList<>();
        
        Element currentElement = treeBuilder.currentElement();
        
        assertNull(currentElement);
    }
    
    private class MockTokeniser extends Tokeniser {
    
        private Token token;
        private boolean isReadCalled;
        
        public MockTokeniser(Token token) {
            super(null, null);
            this.token = token;
        }
        
        @Override
        public Token read() {
            isReadCalled = true;
            return token;
        }
        
        public boolean isReadCalled() {
            return isReadCalled;
        }
    }
}