import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals(Token.TokenType.Doctype, token.type);
    }

    @Test
    public void testTokenTypeStartTag() {
        Token token = new Token.StartTag();
        assertEquals(Token.TokenType.StartTag, token.type);
    }

    @Test
    public void testTokenResetDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.reset();
        // Add assertions for resetting the Doctype token
    }

    @Test
    public void testTokenResetStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.reset();
        // Add assertions for resetting the StartTag token
    }

    // Add more test cases for other methods and scenarios

}