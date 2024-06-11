package com.werken.saxpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XPathLexerTest {
    @Test
    public void testSetXPath() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("//test");

        assertEquals("//test", lexer.getXPath());
    }

    @Test
    public void testNextToken() {
        XPathLexer lexer = new XPathLexer("//test[value=5]");
        Token token = lexer.nextToken();

        assertEquals("//", token.getTokenType());

        token = lexer.nextToken();
        assertEquals("test", token.getTokenType());

        token = lexer.nextToken();
        assertEquals("[", token.getTokenType());

        token = lexer.nextToken();
        assertEquals("value", token.getTokenType());

        token = lexer.nextToken();
        assertEquals("=", token.getTokenType());

        token = lexer.nextToken();
        assertEquals("5", token.getTokenType());

        token = lexer.nextToken();
        assertEquals("]", token.getTokenType());
    }

    @Test
    public void testWhitespace() {
        XPathLexer lexer = new XPathLexer("   ");
        Token token = lexer.nextToken();

        assertEquals(-2, token.getTokenType());
    }
}