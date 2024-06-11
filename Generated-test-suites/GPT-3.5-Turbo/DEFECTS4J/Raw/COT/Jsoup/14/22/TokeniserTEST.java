import org.jsoup.helper.Validate;
import org.jsoup.parser.ParseError;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(null);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.StartTag();
        tokeniser.emit(token);
        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        assertEquals(str, ((Token.Character) tokeniser.read()).getData());
    }

    @Test
    public void testEmitChar() {
        char c = 't';
        tokeniser.emit(c);
        assertEquals(String.valueOf(c), ((Token.Character) tokeniser.read()).getData());
    }

    @Test
    public void testGetState() {
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testConsumeCharacterReference() {
        Character character = tokeniser.consumeCharacterReference(null, false);
        assertNull(character);
    }

    @Test
    public void testCreateTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        assertTrue(tag instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertEquals(tag, tokeniser.read());
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertTrue(tokeniser.commentPending instanceof Token.Comment);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        assertEquals(tokeniser.commentPending, tokeniser.read());
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertTrue(tokeniser.doctypePending instanceof Token.Doctype);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        assertEquals(tokeniser.doctypePending, tokeniser.read());
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.lastStartTag = new Token.StartTag();
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }
    
    @Test
    public void testIsTrackErrors() {
        assertTrue(tokeniser.isTrackErrors());
    }
    
    @Test
    public void testSetTrackErrors() {
        tokeniser.setTrackErrors(false);
        assertFalse(tokeniser.isTrackErrors());
    }
    
    @Test
    public void testErrorTokeniserState() {
        tokeniser.setTrackErrors(true);
        tokeniser.error(TokeniserState.Data);
        assertFalse(tokeniser.errors.isEmpty());
    }
    
    @Test
    public void testEofErrorTokeniserState() {
        tokeniser.setTrackErrors(true);
        tokeniser.eofError(TokeniserState.Data);
        assertFalse(tokeniser.errors.isEmpty());
    }
    
    @Test
    public void testCharacterReferenceError() {
        tokeniser.setTrackErrors(true);
        tokeniser.characterReferenceError();
        assertFalse(tokeniser.errors.isEmpty());
    }
    
    @Test
    public void testError() {
        tokeniser.setTrackErrors(true);
        tokeniser.error("Error");
        assertFalse(tokeniser.errors.isEmpty());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}