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
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("testXPath");
        Token token = lexer.nextToken();
        assertEquals(27, token.getTokenType()); // Literal
    }

    @Test
    public void testIdentifierOrOperatorName() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("testIdentifierOrOperatorName");
        Token token = lexer.identifierOrOperatorName();
        assertEquals(15, token.getTokenType()); // Identifier
    }

    @Test
    public void testOperatorName() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("mod");
        Token token = lexer.operatorName();
        assertEquals(25, token.getTokenType()); // Mod token
    }

    @Test
    public void testNumber() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("123.45");
        Token token = lexer.number();
        assertEquals(30, token.getTokenType()); // Number
    }

    @Test
    public void testWhitespace() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("  ");
        Token token = lexer.whitespace();
        assertEquals(-2, token.getTokenType()); // Whitespace
    }

    @Test
    public void testComma() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath(",");
        Token token = lexer.comma();
        assertEquals(32, token.getTokenType()); // Comma
    }

    @Test
    public void testEquals() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("=");
        Token token = lexer.equals();
        assertEquals(21, token.getTokenType()); // Equals
    }

    @Test
    public void testMinus() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("-");
        Token token = lexer.minus();
        assertEquals(6, token.getTokenType()); // Minus
    }

    @Test
    public void testPlus() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("+");
        Token token = lexer.plus();
        assertEquals(5, token.getTokenType()); // Plus
    }

    @Test
    public void testDollar() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("$");
        Token token = lexer.dollar();
        assertEquals(26, token.getTokenType()); // Dollar
    }

    @Test
    public void testPipe() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("|");
        Token token = lexer.pipe();
        assertEquals(17, token.getTokenType()); // Pipe
    }

    @Test
    public void testAt() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("@");
        Token token = lexer.at();
        assertEquals(16, token.getTokenType()); // At
    }

    @Test
    public void testColon() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath(":");
        Token token = lexer.colon();
        assertEquals(18, token.getTokenType()); // Colon
    }

    @Test
    public void testDoubleColon() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("::");
        Token token = lexer.doubleColon();
        assertEquals(19, token.getTokenType()); // Double Colon
    }

    @Test
    public void testNot() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("!");
        Token token = lexer.not();
        assertEquals(23, token.getTokenType()); // Not
    }

    @Test
    public void testNotEquals() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("!=");
        Token token = lexer.notEquals();
        assertEquals(22, token.getTokenType()); // Not Equals
    }

    @Test
    public void testRelationalOperator() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("<=");
        Token token = lexer.relationalOperator();
        assertEquals(8, token.getTokenType()); // Less Than or Equal
    }

    @Test
    public void testStar() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("*");
        Token token = lexer.star();
        assertEquals(20, token.getTokenType()); // Star
    }

    @Test
    public void testLiteral() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("'test'");
        Token token = lexer.literal();
        assertEquals(27, token.getTokenType()); // Literal
    }

    @Test
    public void testDots() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("..");
        Token token = lexer.dots();
        assertEquals(14, token.getTokenType()); // Dots
    }

    @Test
    public void testLeftBracket() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("[");
        Token token = lexer.leftBracket();
        assertEquals(3, token.getTokenType()); // Left Bracket
    }

    @Test
    public void testRightBracket() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("]");
        Token token = lexer.rightBracket();
        assertEquals(4, token.getTokenType()); // Right Bracket
    }

    @Test
    public void testLeftParen() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("(");
        Token token = lexer.leftParen();
        assertEquals(1, token.getTokenType()); // Left Parenthesis
    }

    @Test
    public void testRightParen() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath(")");
        Token token = lexer.rightParen();
        assertEquals(2, token.getTokenType()); // Right Parenthesis
    }

    @Test
    public void testSlashes() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("//");
        Token token = lexer.slashes();
        assertEquals(12, token.getTokenType()); // Double Slashes
    }
}