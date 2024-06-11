
import org.junit.Assert;
import org.junit.Test;
import com.werken.saxpath.Token;

public class XPathLexerTest {

    @Test
    public void testGetXPath() {
        XPathLexer lexer = new XPathLexer("//*");
        String xpath = lexer.getXPath();
        Assert.assertEquals("//*", xpath);
    }

    @Test
    public void testSetXPath() {
        XPathLexer lexer = new XPathLexer();
        lexer.setXPath("//*[attribute='value']");
        String xpath = lexer.getXPath();
        Assert.assertEquals("//*[attribute='value']", xpath);
    }

    @Test
    public void testNextToken() {
        XPathLexer lexer = new XPathLexer("//*");
        Token token = lexer.nextToken();
        Assert.assertEquals(11, token.getTokenType());
        Assert.assertEquals("//*", token.getValue());
    }

    @Test
    public void testNextTokenWithInvalidXPath() {
        XPathLexer lexer = new XPathLexer("invalid xpath");
        Token token = lexer.nextToken();
        Assert.assertEquals(-1, token.getTokenType());
        Assert.assertEquals("invalid xpath", token.getValue());
    }

    @Test
    public void testNextTokenWithWhitespace() {
        XPathLexer lexer = new XPathLexer("//   /");
        Token token = lexer.nextToken();
        Assert.assertEquals(11, token.getTokenType());
        Assert.assertEquals("//", token.getValue());
    }

    // Add more test cases for other methods

}

