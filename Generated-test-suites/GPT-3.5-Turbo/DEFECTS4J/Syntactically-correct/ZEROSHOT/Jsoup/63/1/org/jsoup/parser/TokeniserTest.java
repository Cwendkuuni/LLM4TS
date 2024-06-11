import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.Character();
        tokeniser.emit(token);
        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        assertEquals(new Token.Character().data(str), tokeniser.read());
    }

    @Test
    public void testEmitCharArray() {
        char[] chars = {'t', 'e', 's', 't'};
        tokeniser.emit(chars);
        assertEquals(new Token.Character().data("test"), tokeniser.read());
    }

    @Test
    public void testEmitIntArr() {
        int[] codepoints = {116, 101, 115, 116};
        tokeniser.emit(codepoints);
        assertEquals(new Token.Character().data("test"), tokeniser.read());
    }

    @Test
    public void testEmitChar() {
        char c = 't';
        tokeniser.emit(c);
        assertEquals(new Token.Character().data("t"), tokeniser.read());
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state);
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
        Character additionalAllowedCharacter = null;
        boolean inAttribute = false;
        int[] result = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNull(result);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertEquals(start, tag.isStartTag());
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
        assertNotNull(tokeniser.commentPending);
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
        assertNotNull(tokeniser.doctypePending);
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
        assertEquals("", tokeniser.dataBuffer.toString());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        assertNull(tokeniser.appropriateEndTagName());
    }

    @Test
    public void testError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.error(state);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testEofError() {
        TokeniserState state = TokeniserState.Data;
        tokeniser.eofError(state);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "Invalid character reference";
        tokeniser.characterReferenceError(message);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testErrorWithString() {
        String errorMsg = "Invalid error message";
        tokeniser.error(errorMsg);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }

    @Test
    public void testUnescapeEntities() {
        boolean inAttribute = false;
        assertEquals("", tokeniser.unescapeEntities(inAttribute));
    }
}