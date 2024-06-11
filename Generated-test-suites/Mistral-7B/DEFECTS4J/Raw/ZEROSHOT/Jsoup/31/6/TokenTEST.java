import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctypeToken = new Token.Doctype();
        assertTrue(doctypeToken.isDoctype());
        assertEquals("Doctype", doctypeToken.tokenType());

        Token doctypeTokenWithName = new Token.Doctype();
        doctypeTokenWithName.asDoctype().getName("html");
        assertEquals("Doctype", doctypeTokenWithName.tokenType());
        assertEquals("html", doctypeTokenWithName.asDoctype().getName());
    }

    @Test
    public void testStartTag() {
        Token startTagToken = new Token.StartTag("div");
        assertTrue(startTagToken.isStartTag());
        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("div", startTagToken.asStartTag().name());

        Token startTagTokenWithAttributes = new Token.StartTag("img").name("image").attr("src", "image.jpg");
        assertTrue(startTagTokenWithAttributes.isStartTag());
        assertEquals("StartTag", startTagTokenWithAttributes.tokenType());
        assertEquals("img", startTagTokenWithAttributes.asStartTag().name());
        Assert.assertEquals("src", startTagTokenWithAttributes.asStartTag().getAttributes().keySet().iterator().next());
        Assert.assertEquals("image.jpg", startTagTokenWithAttributes.asStartTag().getAttributes().get("src"));
    }

    @Test
    public void testEndTag() {
        Token endTagToken = new Token.EndTag("div");
        assertTrue(endTagToken.isEndTag());
        assertEquals("EndTag", endTagToken.tokenType());
        assertEquals("div", endTagToken.asEndTag().name());
    }

    @Test
    public void testComment() {
        Token commentToken = new Token.Comment();
        assertTrue(commentToken.isComment());
        assertEquals("Comment", commentToken.tokenType());

        Token commentTokenWithData = new Token.Comment();
        commentTokenWithData.asComment().getData("This is a comment");
        assertEquals("Comment", commentTokenWithData.tokenType());
        assertEquals("This is a comment", commentTokenWithData.asComment().getData());
    }

    @Test
    public void testCharacter() {
        Token characterToken = new Token.Character("A");
        assertTrue(characterToken.isCharacter());
        assertEquals("Character", characterToken.tokenType());
        assertEquals("A", characterToken.asCharacter().getData());
    }

    @Test
    public void testEOF() {
        Token eofToken = new Token.EOF();
        assertTrue(eofToken.isEOF());
        assertEquals("EOF", eofToken.tokenType());
    }
}