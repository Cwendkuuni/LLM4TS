import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Tag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);

        // TODO: Add more test cases for read()
    }

    @Test
    public void testEmit() {
        Token token = new Token.StartTag();
        tokeniser.emit(token);
        Token emittedToken = tokeniser.read();
        assertEquals(token, emittedToken);

        // TODO: Add more test cases for emit()
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        assertEquals(str, ((Token.Character) token).getData());

        // TODO: Add more test cases for emit(String)
    }

    @Test
    public void testEmitChar() {
        char c = 'a';
        tokeniser.emit(c);
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        assertEquals(String.valueOf(c), ((Token.Character) token).getData());

        // TODO: Add more test cases for emit(char)
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.ScriptDataEscaped;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());

        // TODO: Add more test cases for transition()
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.ScriptDataEscaped;
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.getState());
        // TODO: Add more test cases for advanceTransition()
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.isTrackErrors());
        // TODO: Add more test cases for acknowledgeSelfClosingFlag()
    }

    @Test
    public void testConsumeCharacterReference() {
        // TODO: Add test cases for consumeCharacterReference()
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Tag tagPending = tokeniser.createTagPending(start);
        assertNotNull(tagPending);
        assertEquals(start, tagPending instanceof Token.StartTag);

        // TODO: Add more test cases for createTagPending()
    }

    @Test
    public void testEmitTagPending() {
        Tag tagPending = new Token.StartTag();
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();
        assertEquals(tagPending, emittedToken);

        // TODO: Add more test cases for emitTagPending()
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);

        // TODO: Add more test cases for createCommentPending()
    }

    @Test
    public void testEmitCommentPending() {
        Token.Comment commentPending = new Token.Comment();
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token emittedToken = tokeniser.read();
        assertEquals(commentPending, emittedToken);

        // TODO: Add more test cases for emitCommentPending()
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);

        // TODO: Add more test cases for createDoctypePending()
    }

    @Test
    public void testEmitDoctypePending() {
        Token.Doctype doctypePending = new Token.Doctype();
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token emittedToken = tokeniser.read();
        assertEquals(doctypePending, emittedToken);

        // TODO: Add more test cases for emitDoctypePending()
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);

        // TODO: Add more test cases for createTempBuffer()
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.lastStartTag = new Token.StartTag();
        boolean isAppropriate = tokeniser.isAppropriateEndTagToken();
        assertTrue(isAppropriate);

        // TODO: Add more test cases for isAppropriateEndTagToken()
    }

    @Test
    public void testIsTrackErrors() {
        boolean trackErrors = tokeniser.isTrackErrors();
        assertTrue(trackErrors);

        // TODO: Add more test cases for isTrackErrors()
    }

    @Test
    public void testSetTrackErrors() {
        boolean trackErrors = true;
        tokeniser.setTrackErrors(trackErrors);
        assertTrue(tokeniser.isTrackErrors());

        // TODO: Add more test cases for setTrackErrors()
    }

    @Test
    public void testError() {
        TokeniserState state = TokeniserState.AttributeName;
        tokeniser.error(state);
        List<ParseError> errors = tokeniser.errors;
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpected character in input", error.getErrorMsg());
        assertEquals(state, error.getTokeniserState());

        // TODO: Add more test cases for error()
    }

    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.AttributeValue_doubleQuoted;
        tokeniser.eofError(state);
        List<ParseError> errors = tokeniser.errors;
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpectedly reached end of file (EOF)", error.getErrorMsg());
        assertEquals(state, error.getTokeniserState());

        // TODO: Add more test cases for eofError()
    }
}