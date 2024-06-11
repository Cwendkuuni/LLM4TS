package com.werken.saxpath;
import org.junit.Test;
import static org.junit.Assert.*;

public class XPathLexerTest {

    @Test
    public void testSetXPath() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("test");
        assertEquals("test", lexer.getXPath());
    }

    @Test
    public void testGetXPath() {
        XPathLexer lexer = new XPathLexer("test");
        assertEquals("test", lexer.getXPath());
    }

    @Test
    public void testNextToken() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.nextToken();
        assertNotNull(token);
        // More tests can be added based on token types
    }

    // Add more test cases for all public methods in the XPathLexer class

}