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

        Assert.assertEquals(TokenType.Doctype, doctypeToken.type);
        Assert.assertEquals("Doctype", doctypeToken.tokenType());
        Assert.assertEquals("html", doctypeToken.asDoctype().getName());
        Assert.assertTrue(doctypeToken.asDoctype().isForceQuirks());

        Token resetDoctypeToken = doctypeToken.reset();
        Assert.assertNotSame(doctypeToken, resetDoctypeToken);
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        startTagToken.asStartTag().name("div").attributes(new Attributes().put("class", "test"));

        Assert.assertEquals(TokenType.StartTag, startTagToken.type);
        Assert.assertEquals("StartTag", startTagToken.tokenType());
        Assert.assertEquals("div", startTagToken.asStartTag().name());
        Assert.assertEquals("class", startTagToken.asStartTag().getAttributes().get("class"));

        Token resetStartTagToken = startTagToken.reset();
        Assert.assertNotSame(startTagToken, resetStartTagToken);
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        endTagToken.asEndTag().name("div");

        Assert.assertEquals(TokenType.EndTag, endTagToken.type);
        Assert.assertEquals("EndTag", endTagToken.tokenType());
        Assert.assertEquals("div", endTagToken.asEndTag().name());

        Token resetEndTagToken = endTagToken.reset();
        Assert.assertNotSame(endTagToken, resetEndTagToken);
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        commentToken.asComment().getData("This is a comment.");

        Assert.assertEquals(TokenType.Comment, commentToken.type);
        Assert.assertEquals("Comment", commentToken.tokenType());
        Assert.assertEquals("This is a comment.", commentToken.asComment().getData());

        Token resetCommentToken = commentToken.reset();
        Assert.assertNotSame(commentToken, resetCommentToken);
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        characterToken.asCharacter().data("A");

        Assert.assertEquals(TokenType.Character, characterToken.type);
        Assert.assertEquals("Character", characterToken.tokenType());
        Assert.assertEquals("A", characterToken.asCharacter().getData());

        Token resetCharacterToken = characterToken.reset();
        Assert.assertNotSame(characterToken, resetCharacterToken);
    }

    @Test
    public void testCDataToken() {
        Token cDataToken = new Token.CData("<![CDATA[Hello World]]>");

        Assert.assertEquals(TokenType.Character, cDataToken.type); // note: CDATA is treated as an extension of Character in the Token class
        Assert.assertEquals("Character", cDataToken.tokenType());
        Assert.assertEquals("<![CDATA[Hello World]]>", cDataToken.toString());

        Token resetCDataToken = cDataToken.reset();
        Assert.assertNotSame(cDataToken, resetCDataToken);
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();

        Assert.assertEquals(TokenType.EOF, eofToken.type);
        Assert.assertEquals("EOF", eofToken.tokenType());

        Token resetEofToken = eofToken.reset();
        Assert.assertSame(eofToken, resetEofToken); // EOF token should not be reset
    }

    @Test(expected = AssertionError.class)
    public void testInvalidToken() {
        new Token(); // this class is abstract and has no constructors that take no arguments
    }
}