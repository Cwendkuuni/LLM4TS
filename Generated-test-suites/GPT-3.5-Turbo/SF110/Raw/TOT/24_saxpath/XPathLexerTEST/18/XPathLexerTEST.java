import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XPathLexerTest {

    private XPathLexer xpathLexer;

    @Before
    public void setUp() {
        xpathLexer = new XPathLexer();
    }

    @Test
    public void testSetXPath() {
        String xpath = "/path/to/node";
        xpathLexer.setXPath(xpath);
        assertEquals(xpath, xpathLexer.getXPath());
    }

    @Test
    public void testGetXPath() {
        String xpath = "/path/to/node";
        xpathLexer.setXPath(xpath);
        assertEquals(xpath, xpathLexer.getXPath());
    }

    @Test
    public void testNextTokenLiteral() {
        String xpath = "'Hello World'";
        xpathLexer.setXPath(xpath);
        Token token = xpathLexer.nextToken();
        assertEquals(27, token.getTokenType());
        assertEquals(xpath, token.getXPath());
        assertEquals(0, token.getStart());
        assertEquals(xpath.length(), token.getEnd());
    }

    @Test
    public void testNextTokenRelationalOperator() {
        String xpath = ">=";
        xpathLexer.setXPath(xpath);
        Token token = xpathLexer.nextToken();
        assertEquals(10, token.getTokenType());
        assertEquals(xpath, token.getXPath());
        assertEquals(0, token.getStart());
        assertEquals(xpath.length(), token.getEnd());
    }

    @Test
    public void testNextTokenIdentifier() {
        String xpath = "test123";
        xpathLexer.setXPath(xpath);
        Token token = xpathLexer.nextToken();
        assertEquals(15, token.getTokenType());
        assertEquals(xpath, token.getXPath());
        assertEquals(0, token.getStart());
        assertEquals(xpath.length(), token.getEnd());
    }

    // Add more test cases for each method

}