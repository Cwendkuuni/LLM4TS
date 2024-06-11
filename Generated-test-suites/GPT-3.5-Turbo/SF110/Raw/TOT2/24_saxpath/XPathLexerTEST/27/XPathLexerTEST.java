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
        assertEquals(20, token.getTokenType());
    }
    
    @Test
    public void testIdentifierOrOperatorName() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("test");
        Token token = lexer.identifierOrOperatorName();
        assertEquals(15, token.getTokenType());
    }
    
    @Test
    public void testOperatorName() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("and");
        Token token = lexer.operatorName();
        assertEquals(28, token.getTokenType());
    }
    
    @Test
    public void testMod() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("mod");
        Token token = lexer.mod();
        assertEquals(25, token.getTokenType());
    }
    
    @Test
    public void testDiv() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("div");
        Token token = lexer.div();
        assertEquals(24, token.getTokenType());
    }
    
    @Test
    public void testAnd() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("and");
        Token token = lexer.and();
        assertEquals(28, token.getTokenType());
    }
    
    @Test
    public void testOr() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("or");
        Token token = lexer.or();
        assertEquals(29, token.getTokenType());
    }
    
    @Test
    public void testNumber() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("1234");
        Token token = lexer.number();
        assertEquals(30, token.getTokenType());
    }
    
    @Test
    public void testWhitespace() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("   ");
        Token token = lexer.whitespace();
        assertEquals(-2, token.getTokenType());
    }
    
    @Test
    public void testComma() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath(",");
        Token token = lexer.comma();
        assertEquals(32, token.getTokenType());
    }
    
    @Test
    public void testEquals() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("=");
        Token token = lexer.equals();
        assertEquals(21, token.getTokenType());
    }
    
    @Test
    public void testMinus() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("-");
        Token token = lexer.minus();
        assertEquals(6, token.getTokenType());
    }
    
    @Test
    public void testPlus() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("+");
        Token token = lexer.plus();
        assertEquals(5, token.getTokenType());
    }
    
    @Test
    public void testDollar() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("$");
        Token token = lexer.dollar();
        assertEquals(26, token.getTokenType());
    }
    
    @Test
    public void testPipe() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("|");
        Token token = lexer.pipe();
        assertEquals(17, token.getTokenType());
    }
    
    @Test
    public void testAt() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("@");
        Token token = lexer.at();
        assertEquals(16, token.getTokenType());
    }
    
    @Test
    public void testColon() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath(":");
        Token token = lexer.colon();
        assertEquals(18, token.getTokenType());
    }
    
    @Test
    public void testDoubleColon() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("::");
        Token token = lexer.doubleColon();
        assertEquals(19, token.getTokenType());
    }
    
    @Test
    public void testNot() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("!");
        Token token = lexer.not();
        assertEquals(23, token.getTokenType());
    }
    
    @Test
    public void testNotEquals() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("!=");
        Token token = lexer.notEquals();
        assertEquals(22, token.getTokenType());
    }
    
    @Test
    public void testRelationalOperator() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath(">");
        Token token = lexer.relationalOperator();
        assertEquals(9, token.getTokenType());
    }
    
    @Test
    public void testStar() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("*");
        Token token = lexer.star();
        assertEquals(20, token.getTokenType());
    }
    
    @Test
    public void testLiteral() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("\"test\"");
        Token token = lexer.literal();
        assertEquals(27, token.getTokenType());
    }
    
    @Test
    public void testDots() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("..");
        Token token = lexer.dots();
        assertEquals(14, token.getTokenType());
    }
    
    @Test
    public void testLeftBracket() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("[");
        Token token = lexer.leftBracket();
        assertEquals(3, token.getTokenType());
    }
    
    @Test
    public void testRightBracket() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("]");
        Token token = lexer.rightBracket();
        assertEquals(4, token.getTokenType());
    }
    
    @Test
    public void testLeftParen() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("(");
        Token token = lexer.leftParen();
        assertEquals(1, token.getTokenType());
    }
    
    @Test
    public void testRightParen() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath(")");
        Token token = lexer.rightParen();
        assertEquals(2, token.getTokenType());
    }
    
    @Test
    public void testSlashes() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("//");
        Token token = lexer.slashes();
        assertEquals(12, token.getTokenType());
    }
    
}