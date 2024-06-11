import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit. Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        Token resetDoctypeToken = doctypeToken.reset();

        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals("Doctype", resetDoctypeToken.tokenType());

        doctypeToken.name("html");
        assertEquals("html", doctypeToken.name());
        assertEquals("HTML", doctypeToken.normalName());

        doctypeToken.forceQuirks(true);
        assertTrue(doctypeToken.isForceQuirks());

        assertEquals("<!DOCTYPE html>", doctypeToken.toString());
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        Token resetStartTagToken = startTagToken.reset();

        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("StartTag", resetStartTagToken.tokenType());

        startTagToken.name("p");
        assertEquals("p", startTagToken.name());
        assertEquals("P", startTagToken.normalName());

        startTagToken.selfClosing(true);
        assertTrue(startTagToken.isSelfClosing());

        startTagToken.attributes().put("id", "myId");
        assertNotNull(startTagToken.getAttributes());
        assertEquals("id=\"myId\"", startTagToken.toString());
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        Token resetEndTagToken = endTagToken.reset();

        assertEquals("EndTag", endTagToken.tokenType());
        assertEquals("EndTag", resetEndTagToken.tokenType());

        endTagToken.name("p");
        assertEquals("p", endTagToken.name());
        assertEquals("P", endTagToken.normalName());

        assertEquals("</p>", endTagToken.toString());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        Token resetCommentToken = commentToken.reset();

        assertEquals("Comment", commentToken.tokenType());
        assertEquals("Comment", resetCommentToken.tokenType());

        commentToken.data("Hello World");
        assertEquals("Hello World", commentToken.getData());
        assertEquals("<!--Hello World-->", commentToken.toString());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        Token resetCharacterToken = characterToken.reset();

        assertEquals("Character", characterToken.tokenType());
        assertEquals("Character", resetCharacterToken.tokenType());

        characterToken.data("A");
        assertEquals("A", characterToken.getData());
        assertEquals("A", characterToken.toString());
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        Token resetEofToken = eofToken.reset();

        assertEquals("EOF", eofToken.tokenType());
        assertEquals("EOF", resetEofToken.tokenType());

        assertFalse(eofToken.isDoctype());
        assertFalse(eofToken.isStartTag());
        assertFalse(eofToken.isEndTag());
        assertFalse(eofToken.isComment());
        assertFalse(eofToken.isCharacter());

        assertEquals("", eofToken.toString());
    }
}