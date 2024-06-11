import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("Test input");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.Character("test");
        tokeniser.emit(token);
        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testCharacterReferenceError() {
        Character additionalAllowedCharacter = 'a';
        boolean inAttribute = true;
        assertNull(tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute));
    }

    @Test
    public void testCreateTagPending() {
        tokeniser.createTagPending(true);
        assertNotNull(tokeniser.tagPending);
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "test";
        tokeniser.emitTagPending();
        Token token = new Token.StartTag();
        token.tagName = "test";
        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.setData("test comment");
        tokeniser.emitCommentPending();
        Token token = new Token.Comment();
        token.setData("test comment");
        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.setName("html");
        tokeniser.emitDoctypePending();
        Token token = new Token.Doctype();
        token.setName("html");
        assertEquals(token, tokeniser.read());
    }

}