import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals(Token.TokenType.Doctype, token.type);
        token = new Token.StartTag();
        assertEquals(Token.TokenType.StartTag, token.type);
        token = new Token.EndTag();
        assertEquals(Token.TokenType.EndTag, token.type);
        token = new Token.Comment();
        assertEquals(Token.TokenType.Comment, token.type);
        token = new Token.Character();
        assertEquals(Token.TokenType.Character, token.type);
        token = new Token.EOF();
        assertEquals(Token.TokenType.EOF, token.type);
    }

    @Test
    public void testTokenReset() {
        Token token = new Token.Doctype();
        Token resetToken = token.reset();
        assertEquals(token.getClass(), resetToken.getClass());
        assertTrue(((Token.Doctype) resetToken).name.toString().isEmpty());
        assertTrue(((Token.Doctype) resetToken).pubSysKey == null);
        assertTrue(((Token.Doctype) resetToken).publicIdentifier.toString().isEmpty());
        assertTrue(((Token.Doctype) resetToken).systemIdentifier.toString().isEmpty());
        assertFalse(((Token.Doctype) resetToken).forceQuirks);

        token = new Token.StartTag();
        resetToken = token.reset();
        assertEquals(token.getClass(), resetToken.getClass());
        assertTrue(((Token.StartTag) resetToken).tagName == null);
        assertTrue(((Token.StartTag) resetToken).normalName == null);
        assertTrue(((Token.StartTag) resetToken).pendingAttributeName == null);
        assertTrue(((Token.StartTag) resetToken).pendingAttributeValue.toString().isEmpty());
        assertTrue(((Token.StartTag) resetToken).pendingAttributeValueS == null);
        assertFalse(((Token.StartTag) resetToken).hasEmptyAttributeValue);
        assertFalse(((Token.StartTag) resetToken).hasPendingAttributeValue);
        assertFalse(((Token.StartTag) resetToken).selfClosing);
        assertTrue(((Token.StartTag) resetToken).attributes == null);

        token = new Token.EndTag();
        resetToken = token.reset();
        assertEquals(token.getClass(), resetToken.getClass());
        assertTrue(((Token.EndTag) resetToken).tagName == null);
        assertTrue(((Token.EndTag) resetToken).normalName == null);
        assertTrue(((Token.EndTag) resetToken).pendingAttributeName == null);
        assertTrue(((Token.EndTag) resetToken).pendingAttributeValue.toString().isEmpty());
        assertTrue(((Token.EndTag) resetToken).pendingAttributeValueS == null);
        assertFalse(((Token.EndTag) resetToken).hasEmptyAttributeValue);
        assertFalse(((Token.EndTag) resetToken).hasPendingAttributeValue);
        assertFalse(((Token.EndTag) resetToken).selfClosing);
        assertTrue(((Token.EndTag) resetToken).attributes == null);

        token = new Token.Comment();
        resetToken = token.reset();
        assertEquals(token.getClass(), resetToken.getClass());
        assertTrue(((Token.Comment) resetToken).data.toString().isEmpty());
        assertFalse(((Token.Comment) resetToken).bogus);

        token = new Token.Character();
        resetToken = token.reset();
        assertEquals(token.getClass(), resetToken.getClass());
        assertTrue(((Token.Character) resetToken).data == null);

        token = new Token.CData("");
        resetToken = token.reset();
        assertEquals(token.getClass(), resetToken.getClass());
        assertTrue(((Token.CData) resetToken).data == null);

        token = new Token.EOF();
        resetToken = token.reset();
        assertEquals(token.getClass(), resetToken.getClass());
    }

    // Add more test cases for each method...

}