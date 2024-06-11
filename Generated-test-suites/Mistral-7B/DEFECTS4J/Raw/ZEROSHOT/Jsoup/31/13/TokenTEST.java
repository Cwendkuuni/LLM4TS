import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();

        assertEquals("Doctype", doctypeToken.tokenType());

        Token doctypeTokenWithName = new Token.Doctype();
        doctypeTokenWithName.asDoctype().getName("html");
        assertEquals("Doctype", doctypeTokenWithName.tokenType());
        assertEquals("html", doctypeTokenWithName.asDoctype().getName());
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag("div");

        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("div", startTagToken.asStartTag().name());

        Token startTagTokenWithAttributes = new Token.StartTag("p", new Attributes().put("class", "example"));
        assertEquals("StartTag", startTagTokenWithAttributes.tokenType());
        assertEquals("p", startTagTokenWithAttributes.asStartTag().name());
        assertEquals(1, startTagTokenWithAttributes.asStartTag().getAttributes().size());
        assertEquals("class", startTagTokenWithAttributes.asStartTag().getAttributes().keySet().iterator().next());
        assertEquals("example", startTagTokenWithAttributes.asStartTag().getAttributes().get("class").getValue());
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag("p");

        assertEquals("EndTag", endTagToken.tokenType());
        assertEquals("p", endTagToken.asEndTag().name());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();

        assertEquals("Comment", commentToken.tokenType());

        Token commentTokenWithData = new Token.Comment();
        commentTokenWithData.asComment().getData().append("This is a comment.");
        assertEquals("Comment", commentTokenWithData.tokenType());
        assertEquals("This is a comment.", commentTokenWithData.asComment().getData());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character('>');

        assertEquals("Character", characterToken.tokenType());
        assertEquals(">", characterToken.asCharacter().getData());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidToken() {
        new Token(); // This should throw an AssertionError due to the private constructor
    }

    @Test
    public void testTokenTypeEnum() {
        assertTrue(TokenType.Doctype.isCode());
        assertTrue(TokenType.StartTag.isTag());
        assertTrue(TokenType.EndTag.isTag());
        assertTrue(TokenType.Comment.isComment());
        assertTrue(TokenType.Character.isCharacter());
        assertTrue(TokenType.EOF.isEndOfFile());
    }

    @Test
    public void testTokenTypeToString() {
        assertEquals("Doctype", TokenType.Doctype.toString());
        assertEquals("StartTag", TokenType.StartTag.toString());
        assertEquals("EndTag", TokenType.EndTag.toString());
        assertEquals("Comment", TokenType.Comment.toString());
        assertEquals("Character", TokenType.Character.toString());
        assertEquals("EOF", TokenType.EOF.toString());
    }

    @Test
    public void testIsTokenType() {
        assertTrue(Token.isDoctype(new Token.Doctype()));
        assertTrue(Token.isStartTag(new Token.StartTag("div")));
        assertTrue(Token.isEndTag(new Token.EndTag("p")));
        assertTrue(Token.isComment(new Token.Comment()));
        assertTrue(Token.isCharacter(new Token.Character('>')));
        assertTrue(Token.isEOF(new Token.EOF()));
        assertFalse(Token.isDoctype(new Token.StartTag("p")));
        assertFalse(Token.isStartTag(new Token.Comment()));
        assertFalse(Token.isEndTag(new Token.Character('>')));
        assertFalse(Token.isComment(new Token.Doctype()));
        assertFalse(Token.isCharacter(new Token.EndTag("p")));
        assertFalse(Token.isEOF(new Token.StartTag("p")));
    }

    @Test
    public void testAsTokenType() {
        assertEquals(TokenType.Doctype, new Token.Doctype().asTokenType());
        assertEquals(TokenType.StartTag, new Token.StartTag("div").asTokenType());
        assertEquals(TokenType.EndTag, new Token.EndTag("p").asTokenType());
        assertEquals(TokenType.Comment, new Token.Comment().asTokenType());
        assertEquals(TokenType.Character, new Token.Character('>').asTokenType());
        assertEquals(TokenType.EOF, new Token.EOF().asTokenType());
    }
}