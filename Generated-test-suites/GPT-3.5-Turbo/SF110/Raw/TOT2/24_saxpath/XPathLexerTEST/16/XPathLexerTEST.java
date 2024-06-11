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
        // Add test cases for different token scenarios
    }

    @Test
    public void testIdentifierOrOperatorName() {
        // Add test cases for identifier and operator name scenarios
    }

    @Test
    public void testNumber() {
        // Add test cases for numeric values
    }

    @Test
    public void testWhitespace() {
        // Add test cases for whitespace handling
    }

    // Add additional test methods for each public method in XPathLexer class

}