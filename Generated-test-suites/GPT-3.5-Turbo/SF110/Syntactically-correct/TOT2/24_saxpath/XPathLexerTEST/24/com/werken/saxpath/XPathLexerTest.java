package com.werken.saxpath;
import static org.junit.Assert.*;
import org.junit.Test;

public class XPathLexerTest {

    @Test
    public void testSetXPath() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("testXPath");
        assertEquals("testXPath", lexer.getXPath());
    }

    @Test
    public void testNextToken() {
        XPathLexer lexer = new XPathLexer("//*");
        Token token = lexer.nextToken();
        assertNotNull(token);
        assertEquals(11, token.getTokenType());
    }

    @Test
    public void testIdentifierOrOperatorName() {
        XPathLexer lexer = new XPathLexer("testIdentifier");
        Token token = lexer.identifierOrOperatorName();
        assertNotNull(token);
        assertEquals(15, token.getTokenType());
    }

    @Test
    public void testOperatorName() {
        XPathLexer lexer = new XPathLexer("operatorName");
        Token token = lexer.operatorName();
        assertNotNull(token);
        assertEquals(27, token.getTokenType());
    }

    @Test
    public void testMod() {
        XPathLexer lexer = new XPathLexer("mod");
        Token token = lexer.mod();
        assertNotNull(token);
        assertEquals(25, token.getTokenType());
    }

    @Test
    public void testDiv() {
        XPathLexer lexer = new XPathLexer("div");
        Token token = lexer.div();
        assertNotNull(token);
        assertEquals(24, token.getTokenType());
    }

    @Test
    public void testAnd() {
        XPathLexer lexer = new XPathLexer("and");
        Token token = lexer.and();
        assertNotNull(token);
        assertEquals(28, token.getTokenType());
    }

    @Test
    public void testOr() {
        XPathLexer lexer = new XPathLexer("or");
        Token token = lexer.or();
        assertNotNull(token);
        assertEquals(29, token.getTokenType());
    }

    @Test
    public void testNumber() {
        XPathLexer lexer = new XPathLexer("123.456");
        Token token = lexer.number();
        assertNotNull(token);
        assertEquals(30, token.getTokenType());
    }

    @Test
    public void testWhitespace() {
        XPathLexer lexer = new XPathLexer("  \n ");
        Token token = lexer.whitespace();
        assertNotNull(token);
        assertEquals(-2, token.getTokenType());
    }
}