import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.Token.Tag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(""));
    }

    @Test
    public void testRead() {
        Token result = tokeniser.read();
        Assert.assertNull(result);
    }

    @Test
    public void testEmit() {
        Token.Character token = new Token.Character("");
        tokeniser.emit(token);
        Token result = tokeniser.read();
        Assert.assertEquals(token, result);
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        Token result = tokeniser.read();
        Assert.assertEquals(new Token.Character(str), result);
    }

    @Test
    public void testEmitChar() {
        char c = 'a';
        tokeniser.emit(c);
        Token result = tokeniser.read();
        Assert.assertEquals(new Token.Character(String.valueOf(c)), result);
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        Assert.assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.transition(state);
        Assert.assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.advanceTransition(state);
        Assert.assertEquals(state, tokeniser.getState());
        Assert.assertEquals(1, tokeniser.reader.pos());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        Assert.assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = 'a';
        boolean inAttribute = true;
        Character result = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Tag result = tokeniser.createTagPending(start);
        Assert.assertEquals(start, result.getStartTag());
        Assert.assertEquals("", result.tagName);
    }

    @Test
    public void testEmitTagPending() {
        Tag tagPending = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token result = tokeniser.read();
        Assert.assertEquals(tagPending, result);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        Assert.assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        Comment commentPending = new Token.Comment();
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token result = tokeniser.read();
        Assert.assertEquals(commentPending, result);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        Assert.assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        Token.Doctype doctypePending = new Token.Doctype();
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token result = tokeniser.read();
        Assert.assertEquals(doctypePending, result);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        Assert.assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.tagPending = new Token.StartTag("div", new ArrayList<>(), false);
        tokeniser.lastStartTag = new Token.StartTag("div", new ArrayList<>(), false);
        boolean result = tokeniser.isAppropriateEndTagToken();
        Assert.assertTrue(result);
    }

    @Test
    public void testIsTrackErrors() {
        boolean result = tokeniser.isTrackErrors();
        Assert.assertTrue(result);
    }

    @Test
    public void testSetTrackErrors() {
        boolean flag = false;
        tokeniser.setTrackErrors(flag);
        boolean result = tokeniser.isTrackErrors();
        Assert.assertFalse(result);
    }

    @Test
    public void testError() {
        ParseError error = new ParseError("Unexpected character in input", "", tokeniser.getState(), 0);
        tokeniser.error(tokeniser.getState());
        List<ParseError> errors = tokeniser.errors;
        Assert.assertTrue(errors.contains(error));
    }

    @Test
    public void testEofError() {
        ParseError error = new ParseError("Unexpectedly reached end of file (EOF)", tokeniser.getState(), 0);
        tokeniser.eofError(tokeniser.getState());
        List<ParseError> errors = tokeniser.errors;
        Assert.assertTrue(errors.contains(error));
    }

}