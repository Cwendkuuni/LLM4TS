package com.werken.saxpath;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XPathLexerTest {
    
    private XPathLexer lexer;
    
    @Before
    public void setUp() {
        lexer = new XPathLexer();
    }
    
    @Test
    public void testSetXPath() {
        String xpath = "/bookstore/book[1]/title";
        lexer.setXPath(xpath);
        assertEquals(xpath, lexer.getXPath());
    }
    
    @Test
    public void testGetXPath() {
        String xpath = "/bookstore/book[1]/title";
        lexer.setXPath(xpath);
        assertEquals(xpath, lexer.getXPath());
    }
    
    @Test
    public void testNextToken() {
        String xpath = "/bookstore/book[1]/title";
        lexer.setXPath(xpath);
        
        Token token = lexer.nextToken();
        assertNotNull(token);
        assertEquals(11, token.getTokenType());
        assertEquals("/", token.getValue());
        
        token = lexer.nextToken();
        assertNotNull(token);
        assertEquals(4, token.getTokenType());
        assertEquals("bookstore", token.getValue());
        
        // continue to test all token types
        
        // ...
    }
    
    @Test
    public void testIdentifierOrOperatorName() {
        String xpath = "/bookstore/book[1]/title";
        lexer.setXPath(xpath);
        
        lexer.nextToken(); // move to the "/": token type 11
        lexer.nextToken(); // move to "bookstore": token type 4
        Token token = lexer.identifierOrOperatorName();
        assertNotNull(token);
        assertEquals(15, token.getTokenType());
        assertEquals("bookstore", token.getValue());
    }
    
    // ...
    
}