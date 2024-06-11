package com.werken.saxpath;
import static org.junit.Assert.*;
import org.junit.Test;

public class XPathLexerTest {
    
    // Test for constructor with parameter
    @Test
    public void testConstructorWithParameter() {
        XPathLexer lexer = new XPathLexer("SomeXPathExpression");
        assertEquals("SomeXPathExpression", lexer.getXPath());
    }
    
    // Test for setXPath method
    @Test
    public void testSetXPath() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("NewXPathExpression");
        assertEquals("NewXPathExpression", lexer.getXPath());
    }
    
    // Test for nextToken method
    @Test
    public void testNextToken() {
        XPathLexer lexer = new XPathLexer("1 + 2");
        Token token = lexer.nextToken();
        assertEquals(30, token.getTokenType());
    }
    
    // Test for identifierOrOperatorName method
    @Test
    public void testIdentifierOrOperatorName() {
        XPathLexer lexer = new XPathLexer();
        Token token = lexer.identifierOrOperatorName();
        assertEquals(15, token.getTokenType());
    }
    
    // Test for edge case of operatorName method
    @Test
    public void testOperatorName() {
        XPathLexer lexer = new XPathLexer();
        Token token = lexer.operatorName();
        assertNull(token);
    }
    
    // Test for mod method
    @Test
    public void testMod() {
        XPathLexer lexer = new XPathLexer("mod");
        Token token = lexer.mod();
        assertEquals(25, token.getTokenType());
    }
    
    // Test for div method
    @Test
    public void testDiv() {
        XPathLexer lexer = new XPathLexer("div");
        Token token = lexer.div();
        assertEquals(24, token.getTokenType());
    }
    
    // Test for and method
    @Test
    public void testAnd() {
        XPathLexer lexer = new XPathLexer("and");
        Token token = lexer.and();
        assertEquals(28, token.getTokenType());
    }
    
    // Test for or method
    @Test
    public void testOr() {
        XPathLexer lexer = new XPathLexer("or");
        Token token = lexer.or();
        assertEquals(29, token.getTokenType());
    }
    
    // Test for number method
    @Test
    public void testNumber() {
        XPathLexer lexer = new XPathLexer("123");
        Token token = lexer.number();
        assertEquals(30, token.getTokenType());
    }
    
    // Test for whitespace method
    @Test
    public void testWhitespace() {
        XPathLexer lexer = new XPathLexer("   ");
        Token token = lexer.whitespace();
        assertEquals(-2, token.getTokenType());
    }
    
    // Test for comma method
    @Test
    public void testComma() {
        XPathLexer lexer = new XPathLexer(",");
        Token token = lexer.comma();
        assertEquals(32, token.getTokenType());
    }
    
    // Test for equals method
    @Test
    public void testEquals() {
        XPathLexer lexer = new XPathLexer("=");
        Token token = lexer.equals();
        assertEquals(21, token.getTokenType());
    }
    
    // Test for minus method
    @Test
    public void testMinus() {
        XPathLexer lexer = new XPathLexer("-");
        Token token = lexer.minus();
        assertEquals(6, token.getTokenType());
    }
    
    // Test for plus method
    @Test
    public void testPlus() {
        XPathLexer lexer = new XPathLexer("+");
        Token token = lexer.plus();
        assertEquals(5, token.getTokenType());
    }
    
    // Test for dollar method
    @Test
    public void testDollar() {
        XPathLexer lexer = new XPathLexer("$");
        Token token = lexer.dollar();
        assertEquals(26, token.getTokenType());
    }
    
    // Test for pipe method
    @Test
    public void testPipe() {
        XPathLexer lexer = new XPathLexer("|");
        Token token = lexer.pipe();
        assertEquals(17, token.getTokenType());
    }
    
    // Test for at method
    @Test
    public void testAt() {
        XPathLexer lexer = new XPathLexer("@");
        Token token = lexer.at();
        assertEquals(16, token.getTokenType());
    }
    
    // Test for colon method
    @Test
    public void testColon() {
        XPathLexer lexer = new XPathLexer(":");
        Token token = lexer.colon();
        assertEquals(18, token.getTokenType());
    }
    
    // Test for doubleColon method
    @Test
    public void testDoubleColon() {
        XPathLexer lexer = new XPathLexer("::");
        Token token = lexer.doubleColon();
        assertEquals(19, token.getTokenType());
    }
    
    // Test for not method
    @Test
    public void testNot() {
        XPathLexer lexer = new XPathLexer("!");
        Token token = lexer.not();
        assertEquals(23, token.getTokenType());
    }
    
    // Test for notEquals method
    @Test
    public void testNotEquals() {
        XPathLexer lexer = new XPathLexer("!=");
        Token token = lexer.notEquals();
        assertEquals(22, token.getTokenType());
    }
    
    // Test for relationalOperator method
    @Test
    public void testRelationalOperator() {
        XPathLexer lexer = new XPathLexer("<=");
        Token token = lexer.relationalOperator();
        assertEquals(7, token.getTokenType());
    }
    
    // Test for star method
    @Test
    public void testStar() {
        XPathLexer lexer = new XPathLexer("*");
        Token token = lexer.star();
        assertEquals(20, token.getTokenType());
    }
    
    // Test for literal method
    @Test
    public void testLiteral() {
        XPathLexer lexer = new XPathLexer("'literal'");
        Token token = lexer.literal();
        assertEquals(27, token.getTokenType());
    }
    
    // Test for dots method
    @Test
    public void testDots() {
        XPathLexer lexer = new XPathLexer("..");
        Token token = lexer.dots();
        assertEquals(14, token.getTokenType());
    }
    
    // Test for leftBracket method
    @Test
    public void testLeftBracket() {
        XPathLexer lexer = new XPathLexer("[");
        Token token = lexer.leftBracket();
        assertEquals(3, token.getTokenType());
    }
    
    // Test for rightBracket method
    @Test
    public void testRightBracket() {
        XPathLexer lexer = new XPathLexer("]");
        Token token = lexer.rightBracket();
        assertEquals(4, token.getTokenType());
    }
    
    // Test for leftParen method
    @Test
    public void testLeftParen() {
        XPathLexer lexer = new XPathLexer("(");
        Token token = lexer.leftParen();
        assertEquals(1, token.getTokenType());
    }
    
    // Test for rightParen method
    @Test
    public void testRightParen() {
        XPathLexer lexer = new XPathLexer(")");
        Token token = lexer.rightParen();
        assertEquals(2, token.getTokenType());
    }
    
    // Test for slashes method
    @Test
    public void testSlashes() {
        XPathLexer lexer = new XPathLexer("/");
        Token token = lexer.slashes();
        assertEquals(11, token.getTokenType());
    }
}