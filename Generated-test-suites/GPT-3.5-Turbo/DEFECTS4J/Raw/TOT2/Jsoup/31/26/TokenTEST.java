import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    // Test for tokenType method
    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    // Test for isDoctype method
    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
    }

    // Test for isStartTag method
    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());
    }

    // Test for isEndTag method
    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.isEndTag());
    }

    // Test for isComment method
    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());
    }

    // Test for isCharacter method
    @Test
    public void testIsCharacter() {
        Token token = new Token.Character("Test");
        assertTrue(token.isCharacter());
    }

    // Test for isEOF method
    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());
    }
}