import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XPathLexerTest {

    private XPathLexer xpathLexer;

    @Before
    public void setUp() {
        xpathLexer = new XPathLexer();
    }

    @Test
    public void testSetXPath_withValidXPath() {
        xpathLexer.setXPath("/bookstore/book[1]");
        String expected = "/bookstore/book[1]";
        String result = xpathLexer.getXPath();
        assertEquals(expected, result);
    }

    @Test
    public void testSetXPath_withEmptyXPath() {
        xpathLexer.setXPath("");
        String expected = "";
        String result = xpathLexer.getXPath();
        assertEquals(expected, result);
    }

    @Test
    public void testSetXPath_withNullXPath() {
        xpathLexer.setXPath(null);
        assertNull(xpathLexer.getXPath());
    }

    @Test
    public void testNextToken_withValidToken() {
        xpathLexer.setXPath("/bookstore/book[1]");
        Token token = xpathLexer.nextToken();
        assertNotNull(token);
        assertEquals(11, token.getTokenType());
        assertEquals("/bookstore/book[1]", token.getValue());
    }

    @Test
    public void testNextToken_withInvalidToken() {
        xpathLexer.setXPath("/bookstore/book[");
        Token token = xpathLexer.nextToken();
        assertNotNull(token);
        assertEquals(-1, token.getTokenType());
        assertEquals("/bookstore/book[", token.getValue());
    }
}