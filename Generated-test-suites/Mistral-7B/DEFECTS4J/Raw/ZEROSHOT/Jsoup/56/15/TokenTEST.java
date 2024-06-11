import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        assertEquals("Doctype", doctypeToken.tokenType());

        Token resetDoctypeToken = doctypeToken.reset();
        assertSame(doctypeToken, resetDoctypeToken);

        doctypeToken.asDoctype().getName().append("html");
        assertEquals("html", doctypeToken.asDoctype().getName());

        doctypeToken.asDoctype().setForceQuirks(true);
        assertTrue(doctypeToken.asDoctype().isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        assertEquals("StartTag", startTagToken.tokenType());

        Token resetStartTagToken = startTagToken.reset();
        assertSame(startTagToken, resetStartTagToken);

        startTagToken.name("html");
        assertEquals("html", startTagToken.name());

        startTagToken.name("html").nameAttr("class", "testClass");
        assertEquals("html class=\"testClass\"", startTagToken.toString());

        Token newAttributeStartTagToken = startTagToken.newAttribute();
        assertNotNull(startTagToken.asStartTag().getAttributes());
        assertEquals(1, startTagToken.asStartTag().getAttributes().size());
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        assertEquals("EndTag", endTagToken.tokenType());

        Token resetEndTagToken = endTagToken.reset();
        assertSame(endTagToken, resetEndTagToken);

        endTagToken.name("html");
        assertEquals("html", endTagToken.name());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        assertEquals("Comment", commentToken.tokenType());

        Token resetCommentToken = commentToken.reset();
        assertSame(commentToken, resetCommentToken);

        commentToken.asComment().getData().append("This is a comment");
        assertEquals("This is a comment", commentToken.asComment().getData());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        assertEquals("Character", characterToken.tokenType());

        Token resetCharacterToken = characterToken.reset();
        assertSame(characterToken, resetCharacterToken);

        characterToken.asCharacter().getData().append('A');
        assertEquals('A', characterToken.asCharacter().getData().charAt(0));
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        assertEquals("EOF", eofToken.tokenType());

        Token resetEofToken = eofToken.reset();
        assertSame(eofToken, resetEofToken);

        assertFalse(eofToken.asEOF().isDoctype());
        assertFalse(eofToken.asEOF().isStartTag());
        assertFalse(eofToken.asEOF().isEndTag());
        assertFalse(eofToken.asEOF().isComment());
        assertFalse(eofToken.asEOF().isCharacter());
    }
}