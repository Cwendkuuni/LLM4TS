import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        assertEquals("Doctype", doctypeToken.tokenType());

        Token resetDoctypeToken = doctypeToken.reset();
        assertSame(doctypeToken, resetDoctypeToken);

        doctypeToken.getName().append("html");
        assertEquals("html", doctypeToken.getName());

        doctypeToken.getPublicIdentifier().append("public");
        assertEquals("public", doctypeToken.getPublicIdentifier());

        doctypeToken.getSystemIdentifier().append("system.html");
        assertEquals("system.html", doctypeToken.getSystemIdentifier());

        assertTrue(doctypeToken.isDoctype());
        assertFalse(doctypeToken.isStartTag());
        assertFalse(doctypeToken.isEndTag());
        assertFalse(doctypeToken.isComment());
        assertFalse(doctypeToken.isCharacter());
        assertFalse(doctypeToken.isEOF());
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        assertEquals("StartTag", startTagToken.tokenType());

        Token resetStartTagToken = startTagToken.reset();
        assertSame(startTagToken, resetStartTagToken);

        startTagToken.name("html");
        assertEquals("html", startTagToken.name());

        startTagToken.name("html").attributes().put("class", "class1 class2");
        assertEquals("class1 class2", startTagToken.getAttributes().get("class"));

        assertTrue(startTagToken.isStartTag());
        assertFalse(startTagToken.isEndTag());
        assertFalse(startTagToken.isComment());
        assertFalse(startTagToken.isCharacter());
        assertFalse(startTagToken.isEOF());
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        assertEquals("EndTag", endTagToken.tokenType());

        Token resetEndTagToken = endTagToken.reset();
        assertSame(endTagToken, resetEndTagToken);

        endTagToken.name("html");
        assertEquals("html", endTagToken.name());

        assertTrue(endTagToken.isEndTag());
        assertFalse(endTagToken.isStartTag());
        assertFalse(endTagToken.isComment());
        assertFalse(endTagToken.isCharacter());
        assertFalse(endTagToken.isEOF());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        assertEquals("Comment", commentToken.tokenType());

        Token resetCommentToken = commentToken.reset();
        assertSame(commentToken, resetCommentToken);

        commentToken.getData().append("This is a comment");
        assertEquals("This is a comment", commentToken.getData());

        assertTrue(commentToken.isComment());
        assertFalse(commentToken.isStartTag());
        assertFalse(commentToken.isEndTag());
        assertFalse(commentToken.isCharacter());
        assertFalse(commentToken.isEOF());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        assertEquals("Character", characterToken.tokenType());

        Token resetCharacterToken = characterToken.reset();
        assertSame(characterToken, resetCharacterToken);

        characterToken.data("&amp;");
        assertEquals("&amp;", characterToken.getData());

        assertTrue(characterToken.isCharacter());
        assertFalse(characterToken.isStartTag());
        assertFalse(characterToken.isEndTag());
        assertFalse(characterToken.isComment());
        assertFalse(characterToken.isEOF());
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        assertEquals("EOF", eofToken.tokenType());

        Token resetEofToken = eofToken.reset();
        assertSame(eofToken, resetEofToken);

        assertTrue(eofToken.isEOF());
        assertFalse(eofToken.isStartTag());
        assertFalse(eofToken.isEndTag());
        assertFalse(eofToken.isComment());
        assertFalse(eofToken.isCharacter());
    }
}