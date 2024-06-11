import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals(Token.TokenType.Doctype, token.type);
    }

    @Test
    public void testTokenType() {
        Token token = new Token.StartTag();
        assertEquals(Token.TokenType.StartTag, token.type);
    }

    @Test
    public void testTokenType() {
        Token token = new Token.EndTag();
        assertEquals(Token.TokenType.EndTag, token.type);
    }

    @Test
    public void testTokenType() {
        Token token = new Token.Comment();
        assertEquals(Token.TokenType.Comment, token.type);
    }

    @Test
    public void testTokenType() {
        Token token = new Token.Character();
        assertEquals(Token.TokenType.Character, token.type);
    }

    @Test
    public void testTokenReset() {
        Token token = new Token.Doctype();
        Token resetToken = token.reset();
        assertNotNull(resetToken);
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.isEndTag());
    }

    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Character();
        assertTrue(token.isCharacter());
    }

    @Test
    public void testIsCData() {
        Token token = new Token.CData();
        assertTrue(token.isCData());
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());
    }

    // Add more test cases to cover edge cases and error scenarios

}