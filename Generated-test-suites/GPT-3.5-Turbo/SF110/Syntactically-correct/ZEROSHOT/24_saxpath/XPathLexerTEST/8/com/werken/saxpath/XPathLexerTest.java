package com.werken.saxpath;

import org.junit.Test;
import static org.junit.Assert.*;

public class XPathLexerTest {
    
    @Test
    public void testSetXPath() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("");
        assertEquals("", lexer.getXPath());
        
        lexer.setXPath("test");
        assertEquals("test", lexer.getXPath());
    }
    
    @Test
    public void testNextToken() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("1 + 2");
        Token token = lexer.nextToken();
        assertEquals(new Token(30, "1 + 2", 0, 1), token);
        
        token = lexer.nextToken();
        assertEquals(new Token(5, "1 + 2", 2, 3), token);
        
        token = lexer.nextToken();
        assertEquals(new Token(30, "1 + 2", 4, 5), token);
        
        token = lexer.nextToken();
        assertEquals(new Token(-1, "1 + 2", 6, 6), token);
    }
    
    @Test
    public void testIdentifierOrOperatorName() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("test");
        Token token = lexer.identifierOrOperatorName();
        assertEquals(new Token(15, "test", 0, 3), token);
        
        lexer.setXPath("and");
        token = lexer.identifierOrOperatorName();
        assertEquals(new Token(28, "and", 0, 2), token);
        
        lexer.setXPath("div");
        token = lexer.identifierOrOperatorName();
        assertEquals(new Token(24, "div", 0, 2), token);
    }
    
    // Add more test methods for other methods in XPathLexer class
    
}