import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.asDoctype().getName("html");
        doctypeToken.asDoctype().setForceQuirks(true);
        doctypeToken.asDoctype().getPubSysKey("pubsysid");
        doctypeToken.asDoctype().getPublicIdentifier("publicid");
        doctypeToken.asDoctype().getSystemIdentifier("systemid");

        Assert.assertEquals("Doctype", doctypeToken.tokenType());
        Assert.assertEquals(TokenType.Doctype, doctypeToken.type);
        Assert.assertEquals("html", doctypeToken.asDoctype().getName());
        Assert.assertTrue(doctypeToken.asDoctype().isForceQuirks());
        Assert.assertEquals("pubsysid", doctypeToken.asDoctype().getPubSysKey());
        Assert.assertEquals("publicid", doctypeToken.asDoctype().getPublicIdentifier());
        Assert.assertEquals("systemid", doctypeToken.asDoctype().getSystemIdentifier());

        Token resetDoctypeToken = doctypeToken.reset();
        Assert.assertSame(doctypeToken, resetDoctypeToken);
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        startTagToken.asStartTag().name("div").attributes(new Attributes().put("class", "test"));

        Assert.assertEquals("StartTag", startTagToken.tokenType());
        Assert.assertEquals(TokenType.StartTag, startTagToken.type);
        Assert.assertEquals("div", startTagToken.asStartTag().name());
        Assert.assertEquals("div", startTagToken.asStartTag().name().toLowerCase());
        Assert.assertEquals("class", startTagToken.asStartTag().getAttributes().get("class").name());
        Assert.assertEquals("test", startTagToken.asStartTag().getAttributes().get("class").getValue());

        Token resetStartTagToken = startTagToken.reset();
        Assert.assertSame(startTagToken, resetStartTagToken);
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        endTagToken.asEndTag().name("div");

        Assert.assertEquals("EndTag", endTagToken.tokenType());
        Assert.assertEquals(TokenType.EndTag, endTagToken.type);
        Assert.assertEquals("div", endTagToken.asEndTag().name());
        Assert.assertEquals("div", endTagToken.asEndTag().name().toLowerCase());

        Token resetEndTagToken = endTagToken.reset();
        Assert.assertSame(endTagToken, resetEndTagToken);
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        commentToken.asComment().getData("This is a comment");

        Assert.assertEquals("Comment", commentToken.tokenType());
        Assert.assertEquals(TokenType.Comment, commentToken.type);
        Assert.assertEquals("This is a comment", commentToken.asComment().getData());

        Token resetCommentToken = commentToken.reset();
        Assert.assertSame(commentToken, resetCommentToken);
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        characterToken.asCharacter().data("&nbsp;");

        Assert.assertEquals("Character", characterToken.tokenType());
        Assert.assertEquals(TokenType.Character, characterToken.type);
        Assert.assertEquals("&nbsp;", characterToken.asCharacter().getData());

        Token resetCharacterToken = characterToken.reset();
        Assert.assertSame(characterToken, resetCharacterToken);
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();

        Assert.assertEquals("EOF", eofToken.tokenType());
        Assert.assertEquals(TokenType.EOF, eofToken.type);

        Token resetEOFToken = eofToken.reset();
        Assert.assertSame(eofToken, resetEOFToken);
    }

    @Test(expected = AssertionError.class)
    public void testInvalidToken() {
        Token invalidToken = new Token();

        Assert.fail("Invalid token should not be created");
    }
}