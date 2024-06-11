import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        Assert.assertEquals(TokenType.Doctype, doctypeToken.tokenType());
        doctypeToken.reset();
        Assert.assertNull(doctypeToken.asDoctype().getName());
        doctypeToken.asDoctype().getName("html");
        Assert.assertEquals("html", doctypeToken.asDoctype().getName());
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        startTagToken.name("div");
        Assert.assertEquals(TokenType.StartTag, startTagToken.tokenType());
        Assert.assertEquals("div", startTagToken.name());
        startTagToken.reset();
        Assert.assertNull(startTagToken.name());
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        endTagToken.name("div");
        Assert.assertEquals(TokenType.EndTag, endTagToken.tokenType());
        Assert.assertEquals("div", endTagToken.name());
        endTagToken.reset();
        Assert.assertNull(endTagToken.name());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        commentToken.data("This is a comment.");
        Assert.assertEquals(TokenType.Comment, commentToken.tokenType());
        Assert.assertEquals("This is a comment.", commentToken.asComment().getData());
        commentToken.reset();
        Assert.assertNull(commentToken.getData());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        characterToken.data("A");
        Assert.assertEquals(TokenType.Character, characterToken.tokenType());
        Assert.assertEquals("A", characterToken.getData());
        characterToken.reset();
        Assert.assertNull(characterToken.getData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTokenType() {
        new Token("InvalidType") {}.tokenType();
    }
}