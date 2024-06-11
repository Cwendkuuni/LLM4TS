
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Token.Characters characters = new Token.Character();
        tokeniser.emit(characters);
        assertEquals(characters, tokeniser.emitPending);
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        assertEquals(str, tokeniser.charsString);
    }

    @Test
    public void testEmitCharArray() {
        char[] chars = {'t', 'e', 's', 't'};
        tokeniser.emit(chars);
        assertEquals("test", tokeniser.charsString);
    }

    @Test
    public void testEmitIntArrar() {
        int[] codepoints = {'t', 'e', 's', 't'};
        tokeniser.emit(codepoints);
        assertEquals("test", tokeniser.charsString);
    }

    @Test
    public void testEmitChar() {
        char c = 't';
        tokeniser.emit(c);
        assertEquals("t", tokeniser.charsString);
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
        boolean flag = tokeniser.selfClosingFlagAcknowledged;
        tokeniser.acknowledgeSelfClosingFlag();
        assertNotEquals(flag, tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = null;
        boolean inAttribute = false;
        int[] codeRef = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);
        assertNull(codeRef);
    }

    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag tag = tokeniser.createTagPending(start);
        assertNotNull(tag);
        assertEquals(start, tag.isStartTag());
        assertEquals("", tag.tagName());
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertEquals(tokeniser.tagPending, tokeniser.emitPending);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
        assertEquals("", tokeniser.commentPending.getData());
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        assertEquals(tokeniser.commentPending, tokeniser.emitPending);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
        assertEquals("", tokeniser.doctypePending.getName());
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        assertEquals(tokeniser.doctypePending, tokeniser.emitPending);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertEquals("", tokeniser.dataBuffer.toString());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        boolean appropriate = tokeniser.isAppropriateEndTagToken();
        assertFalse(appropriate);
    }

    @Test
    public void testAppriateEndTagName() {
        String tagName = tokeniser.appropriateEndTagName();
        assertNull(tagName);
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
    public void testErrorWithMsg() {
        String errorMsg = "Error";
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
        String unescaped = tokeniser.unescapeEntities(inAttribute);
        assertEquals("", unescaped);
    }
}

