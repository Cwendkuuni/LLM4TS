
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XPathLexerTest {
    
    private XPathLexer lexer;
    
    @Before
    public void setUp() {
        lexer = new XPathLexer();
    }
    
    @Test
    public void testGetXPath() {
        lexer.setXPath("test");
        
        String result = lexer.getXPath();
        
        assertEquals("test", result);
    }
    
    @Test
    public void testSetXPath() {
        lexer.setXPath("test");
        
        String result = lexer.getXPath();
        
        assertEquals("test", result);
    }
    
    @Test
    public void testNextTokenWithInvalidXPath() {
        lexer.setXPath("");
        
        Token result = lexer.nextToken();
        
        assertNotNull(result);
        assertEquals(-1, result.getTokenType());
        assertEquals(0, result.getStartPosition());
        assertEquals(0, result.getEndPosition());
    }
    
    @Test
    public void testNextTokenWithDollar() {
        lexer.setXPath("$");
        
        Token result = lexer.nextToken();
        
        assertNotNull(result);
        assertEquals(26, result.getTokenType());
        assertEquals(0, result.getStartPosition());
        assertEquals(1, result.getEndPosition());
    }
    
    @Test
    public void testNextTokenWithLiteral() {
        lexer.setXPath("'literal'");
        
        Token result = lexer.nextToken();
        
        assertNotNull(result);
        assertEquals(27, result.getTokenType());
        assertEquals(0, result.getStartPosition());
        assertEquals(9, result.getEndPosition());
    }
    
    // Add more test cases for other methods
    
}

