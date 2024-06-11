import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.asDoctype().getName("html");
        doctypeToken.asDoctype().setForceQuirks(true);

        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals("html", doctypeToken.asDoctype().getName());
        assertTrue(doctypeToken.asDoctype().isForceQuirks());

        Token resetDoctypeToken = doctypeToken.reset();
        assertNotSame(doctypeToken, resetDoctypeToken);
        assertEquals("Doctype", resetDoctypeToken.tokenType());
        assertNull(resetDoctypeToken.asDoctype().getName());
        assertFalse(resetDoctypeToken.asDoctype().isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        startTagToken.asStartTag().name("html");
        startTagToken.asStartTag().attributes(new Attributes().put("class", "test"));

        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("html", startTagToken.asStartTag().name());
        assertEquals("class", startTagToken.asStartTag().getAttributes().getClassAttribute("class").getName());
        assertEquals("test", startTagToken.asStartTag().getAttributes().getClassAttribute("class").getValue());

        Token resetStartTagToken = startTagToken.reset();
        assertNotSame(startTagToken, resetStartTagToken);
        assertEquals("StartTag", resetStartTagToken.tokenType());
        assertNull(resetStartTagToken.asStartTag().name());
        assertNull(resetStartTagToken.asStartTag().getAttributes());
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        endTagToken.asEndTag().name("html");

        assertEquals("EndTag", endTagToken.tokenType());
        assertEquals("html", endTagToken.asEndTag().name());

        Token resetEndTagToken = endTagToken.reset();
        assertNotSame(endTagToken, resetEndTagToken);
        assertEquals("EndTag", resetEndTagToken.tokenType());
        assertNull(resetEndTagToken.asEndTag().name());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        commentToken.asComment().getData("This is a comment");

        assertEquals("Comment", commentToken.tokenType());
        assertEquals("This is a comment", commentToken.asComment().getData());

        Token resetCommentToken = commentToken.reset();
        assertNotSame(commentToken, resetCommentToken);
        assertEquals("Comment", resetCommentToken.tokenType());
        assertNull(resetCommentToken.asComment().getData());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        characterToken.asCharacter().data("&amp;");

        assertEquals("Character", characterToken.tokenType());
        assertEquals("&amp;", characterToken.asCharacter().getData());

        Token resetCharacterToken = characterToken.reset();
        assertNotSame(characterToken, resetCharacterToken);
        assertEquals("Character", resetCharacterToken.tokenType());
        assertNull(resetCharacterToken.asCharacter().getData());
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();

        assertEquals("EOF", eofToken.tokenType());

        Token resetEofToken = eofToken.reset();
        assertNotSame(eofToken, resetEofToken);
        assertEquals("EOF", resetEofToken.tokenType());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidToken() {
        Token invalidToken = new Token();
        invalidToken.tokenType();
    }

    @Test
    public void testResetTokens() {
        Token doctypeToken = new Token.Doctype();
        Token startTagToken = new Token.StartTag();
        Token endTagToken = new Token.EndTag();
        Token commentToken = new Token.Comment();
        Token characterToken = new Token.Character();
        Token eofToken = new Token.EOF();

        doctypeToken.asDoctype().getName("html");
        startTagToken.asStartTag().name("html");
        endTagToken.asEndTag().name("html");
        commentToken.asComment().getData("This is a comment");
        characterToken.asCharacter().data("&amp;");

        // Reset all tokens
        for (Token token : new Token[]{doctypeToken, startTagToken, endTagToken, commentToken, characterToken, eofToken}) {
            Token resetToken = token.reset();
            assertEquals("Doctype", token.tokenType());
            assertEquals("Doctype", resetToken.tokenType());
            if (token instanceof Token.Doctype) {
                assertNull(((Token.Doctype) token).getName());
                assertNull(((Token.Doctype) resetToken).getName());
                assertFalse(((Token.Doctype) token).isForceQuirks());
                assertFalse(((Token.Doctype) resetToken).isForceQuirks());
            } else if (token instanceof Token.StartTag) {
                assertNull(((Token.StartTag) token).name());
                assertNull(((Token.StartTag) resetToken).name());
                assertNull(((Token.StartTag) token).getAttributes());
                assertNull(((Token.StartTag) resetToken).getAttributes());
            } else if (token instanceof Token.EndTag) {
                assertNull(((Token.EndTag) token).name());
                assertNull(((Token.EndTag) resetToken).name());
            } else if (token instanceof Token.Comment) {
                assertNull(((Token.Comment) token).getData());
                assertNull(((Token.Comment) resetToken).getData());
            } else if (token instanceof Token.Character) {
                assertNull(((Token.Character) token).getData());
                assertNull(((Token.Character) resetToken).getData());
            } else if (token instanceof Token.EOF) {
                // EOF token should be the same object after reset
                assertEquals(token, resetToken);
            }
        }
    }
}