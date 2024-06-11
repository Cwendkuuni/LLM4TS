import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        Token resetDoctypeToken = doctypeToken.reset();

        assertEquals(TokenType.Doctype, doctypeToken.type);
        assertEquals(TokenType.Doctype, resetDoctypeToken.type);

        doctypeToken.getName("html");
        assertEquals("html", doctypeToken.getName());

        doctypeToken.setForceQuirks(true);
        assertEquals(true, doctypeToken.isForceQuirks());
    }

    @Test
    public void testTagToken() {
        Token tagToken = new Token.StartTag();
        Token resetTagToken = tagToken.reset();

        assertEquals(TokenType.StartTag, tagToken.type);
        assertEquals(TokenType.StartTag, resetTagToken.type);

        tagToken.name("div");
        assertEquals("div", tagToken.name());

        tagToken.selfClosing(true);
        assertEquals(true, tagToken.isSelfClosing());

        tagToken.newAttribute().name("id").value("test");
        assertEquals("test", tagToken.getAttributes().getValue("id"));

        tagToken.finaliseTag();
        assertNull(tagToken.getAttributes());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        Token resetCommentToken = commentToken.reset();

        assertEquals(TokenType.Comment, commentToken.type);
        assertEquals(TokenType.Comment, resetCommentToken.type);

        commentToken.getData("This is a comment");
        assertEquals("This is a comment", commentToken.getData());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        Token resetCharacterToken = characterToken.reset();

        assertEquals(TokenType.Character, characterToken.type);
        assertEquals(TokenType.Character, resetCharacterToken.type);

        characterToken.data("A");
        assertEquals("A", characterToken.getData());

        characterToken.reset();
        assertNull(characterToken.getData());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidToken() {
        new Token();
    }
}