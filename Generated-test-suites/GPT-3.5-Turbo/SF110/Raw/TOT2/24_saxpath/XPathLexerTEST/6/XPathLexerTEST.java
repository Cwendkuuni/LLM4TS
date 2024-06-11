package com.werken.saxpath;

import org.junit.Test;
import static org.junit.Assert.*;

public class XPathLexerTest {

    @Test
    public void testGetXPath() {
        XPathLexer lexer = new XPathLexer("testXPath");
        assertEquals("testXPath", lexer.getXPath());
    }

    @Test
    public void testNextToken() {
        XPathLexer lexer = new XPathLexer("testXPath");
        Token token = lexer.nextToken();
        assertNotNull(token);
    }

    // Add more test cases for other methods here

}