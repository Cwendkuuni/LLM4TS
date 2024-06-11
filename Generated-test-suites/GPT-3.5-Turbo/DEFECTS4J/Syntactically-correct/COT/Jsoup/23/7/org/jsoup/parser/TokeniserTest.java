import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {
    private Tokeniser tokeniser;
    private CharacterReader reader;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        reader = new CharacterReader("");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void testEmit() {
        Token tag = new Token.Tag();
        tokeniser.emit(tag);
        Token emittedToken = tokeniser.read();
        assertEquals(tag, emittedToken);
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        Token emittedToken = tokeniser.read();
        assertEquals(str, emittedToken.getData());
    }

    @Test
    public void testEmitChar() {
        char c = 't';
        tokeniser.emit(c);
        Token emittedToken = tokeniser.read();
        assertEquals(String.valueOf(c), emittedToken.getData());
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertNotNull(state);
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
        assertTrue(tokeniser.isSelfClosingFlagAcknowledged());
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = 't';
        boolean inAttribute = true;
        Character charRef = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNull(charRef);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tagPending = tokeniser.createTagPending(start);
        assertNotNull(tagPending);
        assertEquals(start, tagPending.isStartTag());
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken.isStartTag());
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken.isComment());
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token emittedToken = tokeniser.read();
        assertTrue(emittedToken.isDoctype());
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.lastStartTag = (Token.StartTag)tokeniser.tagPending;
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.createTagPending(true);
        tokeniser.lastStartTag = (Token.StartTag)tokeniser.tagPending;
        String tagName = tokeniser.appropriateEndTagName();
        assertEquals(tokeniser.lastStartTag.tagName, tagName);
    }

    @Test
    public void testErrorTokeniserState() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.error(state);
        assertEquals(1, errors.count());
    }

    @Test
    public void testEofErrorTokeniserState() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);
        assertEquals(1, errors.count());
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "Invalid character reference";
        tokeniser.characterReferenceError(message);
        assertEquals(1, errors.count());
    }

    @Test
    public void testError() {
        String errorMsg = "Error message";
        tokeniser.error(errorMsg);
        assertEquals(1, errors.count());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean inHtmlNS = tokeniser.currentNodeInHtmlNS();
        assertTrue(inHtmlNS); // Assuming currentNodeInHtmlNS() always returns true
    }

    @Test
    public void testReadWithNonCharacterToken() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        Token token = tokeniser.read();
        assertTrue(token instanceof Token.StartTag);
    }

    @Test
    public void testReadWithCharacterToken() {
        String str = "test";
        tokeniser.emit(str);

        Token token = tokeniser.read();
        assertEquals(str, token.getData());
    }

    @Test
    public void testReadWithEmptyBuffer() {
        Token token = tokeniser.read();
        assertNull(token);
    }
}