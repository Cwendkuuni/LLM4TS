import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XPathLexerTest {

    private XPathLexer lexer;

    @Before
    public void setup() {
        lexer = new XPathLexer();
    }

    @Test
    public void testgetXPath_EmptyXPath_ReturnsEmptyString() {
        lexer.setXPath("");
        assertEquals("", lexer.getXPath());
    }

    @Test
    public void testnextToken_NullXPath_ReturnsEndOfFileToken() {
        lexer.setXPath(null);
        assertEquals(new Token(-1, null, 0, 0), lexer.nextToken());
    }

    @Test
    public void testnextToken_SpecialCharactersInXPath_ReturnsExpectedTokens() {
        lexer.setXPath("[$,(''')]");
        assertEquals(new Token(32, "[$,(''')]", 0, 1), lexer.nextToken());
        assertEquals(new Token(26, "[$,(''')]", 1, 2), lexer.nextToken());
        assertEquals(new Token(1, "[$,(''')]", 2, 3), lexer.nextToken());
        assertEquals(new Token(27, "[$,(''')]", 3, 6), lexer.nextToken());
        assertEquals(new Token(2, "[$,(''')]", 6, 7), lexer.nextToken());
        assertEquals(new Token(4, "[$,(''')]", 7, 8), lexer.nextToken());
    }

    // Add more test cases for the other identified scenarios

}