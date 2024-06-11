package com.werken.saxpath;
import static org.junit.Assert.*;
import org.junit.Test;

public class XPathLexerTest {
    
    @Test
    public void testSetXPath() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("test");
        assertEquals("test", lexer.getXPath());
    }
    
    @Test
    public void testNextToken() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("1 + 2");
        Token token = lexer.nextToken();
        assertEquals(30, token.getTokenType());
        
        lexer.setXPath("10 + 20");
        token = lexer.nextToken();
        assertEquals(31, token.getTokenType());
    }
    
    @Test
    public void testIdentifierOrOperatorName() {
        XPathLexer lexer = new XPathLexer();
        Token token = lexer.identifierOrOperatorName();
        assertNull(token);
        
        lexer.setPreviousToken(new Token(1, "", 0, 0));
        token = lexer.identifierOrOperatorName();
        assertEquals(15, token.getTokenType());
    }
    
    // Additional test cases for other methods can be added here
    
}