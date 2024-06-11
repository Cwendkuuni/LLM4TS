import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    @Test
    public void testRead() {
        CharacterReader reader = new CharacterReader("<html></html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("html", token.asStartTag().tagName);

        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("html", token.asEndTag().tagName);
    }

    @Test
    public void testEmit() {
        CharacterReader reader = new CharacterReader("<p></p>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token.Tag tag = tokeniser.createTagPending(true);
        tag.tagName = "p";
        tokeniser.emitTagPending();

        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("p", token.asEndTag().tagName);
    }

    @Test
    public void testCreateTagPending() {
        CharacterReader reader = new CharacterReader("<p></p>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token.Tag tag = tokeniser.createTagPending(true);
        assertNotNull(tag);
        assertTrue(tag.isStartTag());
        assertEquals("p", tag.asStartTag().tagName);
    }

    @Test
    public void testEmitCommentPending() {
        CharacterReader reader = new CharacterReader("<!-- comment -->");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();

        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Comment, token.type);
        assertEquals(" comment ", token.asComment().getData());
    }

    @Test
    public void testCreateDoctypePending() {
        CharacterReader reader = new CharacterReader("<!doctype html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Doctype, token.type);
        assertEquals("html", token.asDoctype().getName());
    }

    @Test
    public void testUnescapeEntities() {
        CharacterReader reader = new CharacterReader("&lt;p&gt;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        String unescaped = tokeniser.unescapeEntities(false);

        assertEquals("<p>", unescaped);
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        CharacterReader reader = new CharacterReader("<html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        boolean inHtmlNS = tokeniser.currentNodeInHtmlNS();

        assertTrue(inHtmlNS);
    }
}