import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class XPathLexerTest {

    XPathLexer xpathLexer;

    @Before
    public void setUp() {
        xpathLexer = new XPathLexer();
    }

    @Test
    public void testGetXPath() {
        String xpath = "test";
        xpathLexer.setXPath(xpath);
        assertEquals(xpath, xpathLexer.getXPath());
    }

    @Test
    public void testTokenMethods() {
        assertNotNull(xpathLexer.nextToken());
    }

    @Test
    public void testIdentifierOrOperatorName() {
        assertNotNull(xpathLexer.identifierOrOperatorName());
    }

    @Test
    public void testOperatorName() {
        assertNotNull(xpathLexer.operatorName());
    }

    @Test
    public void testMod() {
        assertNotNull(xpathLexer.mod());
    }

    @Test
    public void testDiv() {
        assertNotNull(xpathLexer.div());
    }

    @Test
    public void testAnd() {
        assertNotNull(xpathLexer.and());
    }

    @Test
    public void testOr() {
        assertNotNull(xpathLexer.or());
    }

    @Test
    public void testNumber() {
        assertNotNull(xpathLexer.number());
    }

    @Test
    public void testWhitespace() {
        assertNotNull(xpathLexer.whitespace());
    }

    @Test
    public void testComma() {
        assertNotNull(xpathLexer.comma());
    }

    @Test
    public void testEquals() {
        assertNotNull(xpathLexer.equals());
    }

    @Test
    public void testMinus() {
        assertNotNull(xpathLexer.minus());
    }

    @Test
    public void testPlus() {
        assertNotNull(xpathLexer.plus());
    }

    @Test
    public void testDollar() {
        assertNotNull(xpathLexer.dollar());
    }

    @Test
    public void testPipe() {
        assertNotNull(xpathLexer.pipe());
    }

    @Test
    public void testAt() {
        assertNotNull(xpathLexer.at());
    }

    @Test
    public void testColon() {
        assertNotNull(xpathLexer.colon());
    }

    @Test
    public void testDoubleColon() {
        assertNotNull(xpathLexer.doubleColon());
    }

    @Test
    public void testNot() {
        assertNotNull(xpathLexer.not());
    }

    @Test
    public void testNotEquals() {
        assertNotNull(xpathLexer.notEquals());
    }

    @Test
    public void testRelationalOperator() {
        assertNotNull(xpathLexer.relationalOperator());
    }

    @Test
    public void testStar() {
        assertNotNull(xpathLexer.star());
    }

    @Test
    public void testLiteral() {
        assertNotNull(xpathLexer.literal());
    }

    @Test
    public void testDots() {
        assertNotNull(xpathLexer.dots());
    }

    @Test
    public void testLeftBracket() {
        assertNotNull(xpathLexer.leftBracket());
    }

    @Test
    public void testRightBracket() {
        assertNotNull(xpathLexer.rightBracket());
    }

    @Test
    public void testLeftParen() {
        assertNotNull(xpathLexer.leftParen());
    }

    @Test
    public void testRightParen() {
        assertNotNull(xpathLexer.rightParen());
    }

    @Test
    public void testSlashes() {
        assertNotNull(xpathLexer.slashes());
    }

    @Test
    public void testLA() {
        assertEquals('\uffff', xpathLexer.LA(0));
    }

    @Test
    public void testConsume() {
        xpathLexer.consume();
        assertEquals(1, xpathLexer.currentPosition());
    }

    @Test
    public void testGetPreviousToken() {
        assertNull(xpathLexer.getPreviousToken());
    }

    @Test
    public void testHasMoreChars() {
        assertTrue(xpathLexer.hasMoreChars());
    }

    @Test
    public void testIsIdentifierChar() {
        assertTrue(xpathLexer.isIdentifierChar('_'));
    }

    @Test
    public void testIsIdentifierStartChar() {
        assertTrue(xpathLexer.isIdentifierStartChar('_'));
    }

}