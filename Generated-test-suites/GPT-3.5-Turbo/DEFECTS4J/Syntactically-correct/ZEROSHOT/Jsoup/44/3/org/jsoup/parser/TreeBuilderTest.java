package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TreeBuilderTest {
  
    private TreeBuilder treeBuilder;
    private CharacterReader reader;
    private Tokeniser tokeniser;
    private Document doc;
    private ArrayList<Element> stack;
    private String baseUri;
    private Token currentToken;
    private ParseErrorList errors;
  
    @Before
    public void setUp() {
        treeBuilder = new TreeBuilder() {
            @Override
            protected boolean process(Token token) {
                return false;
            }
        };
        reader = mock(CharacterReader.class);
        tokeniser = mock(Tokeniser.class);
        doc = mock(Document.class);
        stack = mock(ArrayList.class);
        baseUri = "http://example.com";
        currentToken = mock(Token.class);
        errors = mock(ParseErrorList.class);
    }
    
    @Test
    public void testInitialiseParse() {
        treeBuilder.initialiseParse("input", baseUri, errors);
        
        assertNotNull(treeBuilder.doc);
        assertNotNull(treeBuilder.reader);
        assertNotNull(treeBuilder.tokeniser);
        assertNotNull(treeBuilder.stack);
        assertNotNull(treeBuilder.baseUri);
        assertEquals(treeBuilder.errors, errors);
    }
    
    @Test
    public void testParseWithErrors() {
        Document result = treeBuilder.parse("input", baseUri, errors);
        
        assertNotNull(result);
        verify(errors, times(1)).noTracking();
        verify(treeBuilder, times(1)).initialiseParse("input", baseUri, errors);
        verify(treeBuilder, times(1)).runParser();
    }
    
    @Test
    public void testParseWithoutErrors() {
        Document result = treeBuilder.parse("input", baseUri);
        
        assertNotNull(result);
        verify(errors, never()).noTracking();
        verify(treeBuilder, times(1)).initialiseParse("input", baseUri, ParseErrorList.noTracking());
        verify(treeBuilder, times(1)).runParser();
    }
    
    @Test
    public void testRunParser() {
        when(tokeniser.read()).thenReturn(new Token());
        when(tokeniser.read()).thenReturn(new Token(Token.TokenType.EOF));
        
        treeBuilder.runParser();
        
        verify(tokeniser, times(2)).read();
        verify(treeBuilder, times(2)).process(any(Token.class));
        verify(currentToken, times(1)).reset();
    }
    
    @Test
    public void testProcessStartTag() {
        boolean result = treeBuilder.processStartTag("name");
        
        assertFalse(result);
        verify(treeBuilder, times(1)).process(any(Token.class));
        verify(treeBuilder.start, times(1)).reset();
        verify(treeBuilder.start, times(1)).name("name");
    }
    
    @Test
    public void testProcessStartTagWithAttrs() {
        String name = "name";
        Attributes attrs = mock(Attributes.class);
        
        boolean result = treeBuilder.processStartTag(name, attrs);
        
        assertFalse(result);
        verify(treeBuilder, times(1)).process(any(Token.class));
        verify(treeBuilder.start, times(1)).reset();
        verify(treeBuilder.start, times(1)).nameAttr(name, attrs);
    }
    
    @Test
    public void testProcessEndTag() {
        boolean result = treeBuilder.processEndTag("name");
        
        assertFalse(result);
        verify(treeBuilder, times(1)).process(any(Token.class));
        verify(treeBuilder.end, times(1)).reset();
        verify(treeBuilder.end, times(1)).name("name");
    }
    
    @Test
    public void testCurrentElementNotEmptyStack() {
        when(stack.size()).thenReturn(1);
        when(stack.get(0)).thenReturn(mock(Element.class));
        
        Element result = treeBuilder.currentElement();
        
        assertNotNull(result);
        verify(stack, times(1)).size();
        verify(stack, times(1)).get(0);
    }
    
    @Test
    public void testCurrentElementEmptyStack() {
        when(stack.size()).thenReturn(0);
        
        Element result = treeBuilder.currentElement();
        
        assertNull(result);
        verify(stack, times(1)).size();
        verify(stack, never()).get(anyInt());
    }
}