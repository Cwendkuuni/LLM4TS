package com.werken.saxpath;

import org.junit.Test;
import static org.junit.Assert.*;

public class XPathLexerTest {

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
    }

    @Test
    public void testSetXPath() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("test");
        assertEquals("test", lexer.getXPath());
    }

    @Test
    public void testIdentifierOrOperatorName() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.identifierOrOperatorName();
        assertNotNull(token);
    }

    @Test
    public void testMod() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.mod();
        assertNull(token);
    }

    @Test
    public void testDiv() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.div();
        assertNull(token);
    }

    @Test
    public void testAnd() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.and();
        assertNull(token);
    }

    @Test
    public void testOr() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.or();
        assertNull(token);
    }

    @Test
    public void testNumber() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.number();
        assertNotNull(token);
    }

    @Test
    public void testWhitespace() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.whitespace();
        assertNotNull(token);
    }

    @Test
    public void testComma() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.comma();
        assertNotNull(token);
    }

    @Test
    public void testEquals() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.equals();
        assertNotNull(token);
    }

    @Test
    public void testMinus() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.minus();
        assertNotNull(token);
    }

    @Test
    public void testPlus() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.plus();
        assertNotNull(token);
    }

    @Test
    public void testDollar() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.dollar();
        assertNotNull(token);
    }

    @Test
    public void testPipe() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.pipe();
        assertNotNull(token);
    }

    @Test
    public void testAt() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.at();
        assertNotNull(token);
    }

    @Test
    public void testColon() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.colon();
        assertNotNull(token);
    }

    @Test
    public void testDoubleColon() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.doubleColon();
        assertNotNull(token);
    }

    @Test
    public void testNot() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.not();
        assertNotNull(token);
    }

    @Test
    public void testNotEquals() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.notEquals();
        assertNotNull(token);
    }

    @Test
    public void testRelationalOperator() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.relationalOperator();
        assertNotNull(token);
    }

    @Test
    public void testStar() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.star();
        assertNotNull(token);
    }

    @Test
    public void testLiteral() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.literal();
        assertNotNull(token);
    }

    @Test
    public void testDots() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.dots();
        assertNotNull(token);
    }

    @Test
    public void testLeftBracket() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.leftBracket();
        assertNotNull(token);
    }

    @Test
    public void testRightBracket() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.rightBracket();
        assertNotNull(token);
    }

    @Test
    public void testLeftParen() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.leftParen();
        assertNotNull(token);
    }

    @Test
    public void testRightParen() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.rightParen();
        assertNotNull(token);
    }

    @Test
    public void testSlashes() {
        XPathLexer lexer = new XPathLexer("test");
        Token token = lexer.slashes();
        assertNotNull(token);
    }

}