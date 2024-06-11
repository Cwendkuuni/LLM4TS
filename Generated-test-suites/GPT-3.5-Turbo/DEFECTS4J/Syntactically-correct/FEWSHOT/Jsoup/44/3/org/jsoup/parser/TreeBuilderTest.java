package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class TreeBuilderTest {
    
    private TreeBuilder treeBuilder;
    
    @Before
    public void setUp() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false; // Just a mock implementation for testing purposes
            }
        };
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = mock(ParseErrorList.class);
        
        treeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals(baseUri, treeBuilder.baseUri);
        assertEquals(input, treeBuilder.reader.getInput());
        assertEquals(errors, treeBuilder.errors);
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.stack);
    }
    
    @Test
    public void testParseWithNoErrors() {
        String input = "";
        String baseUri = "http://www.example.com";
        
        Document doc = treeBuilder.parse(input, baseUri);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testParseWithErrors() {
        String input = "";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = mock(ParseErrorList.class);
        
        Document doc = treeBuilder.parse(input, baseUri, errors);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        assertEquals(errors, treeBuilder.errors);
    }
    
    @Test
    public void testRunParserWithEOFToken() {
        Tokeniser tokeniser = mock(Tokeniser.class);
        when(tokeniser.read()).thenReturn(new Token(Token.TokenType.EOF));
        treeBuilder.tokeniser = tokeniser;
        
        treeBuilder.runParser();
        
        verify(tokeniser, times(1)).read();
    }
    
    @Test
    public void testProcessStartTag() {
        Token startTagToken = new Token.StartTag().name("div");
        treeBuilder.process(startTagToken);
        
        assertEquals(startTagToken, treeBuilder.start);
    }
    
    @Test
    public void testProcessStartTagWithAttributes() {
        Token.StartTag startTagToken = new Token.StartTag().name("div");
        Attributes attrs = new Attributes();
        startTagToken.attrs(attrs);
        treeBuilder.process(startTagToken);
        
        assertEquals(startTagToken, treeBuilder.start);
        assertEquals(attrs, treeBuilder.start.getAttrs());
    }
    
    @Test
    public void testProcessEndTag() {
        Token endTagToken = new Token.EndTag().name("div");
        treeBuilder.process(endTagToken);
        
        assertEquals(endTagToken, treeBuilder.end);
    }
    
    @Test
    public void testCurrentElementWithEmptyStack() {
        assertNull(treeBuilder.currentElement());
    }
    
    @Test
    public void testCurrentElementWithNonEmptyStack() {
        Element element = mock(Element.class);
        treeBuilder.stack.add(element);
        
        assertEquals(element, treeBuilder.currentElement());
    }
}