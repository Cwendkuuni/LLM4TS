import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
    }

    @Test
    public void testRead() {
        assertNull(tokeniser.read());
    }

    @Test
    public void testEmit() {
        Token token = new Token.Tag();
        tokeniser.emit(token);
        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        assertEquals(str, ((Token.Character) token).getData());
    }

    @Test
    public void testEmitCharArray() {
        char[] chars = {'t', 'e', 's', 't'};
        tokeniser.emit(chars);
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        assertEquals(new String(chars), ((Token.Character) token).getData());
    }

    @Test
    public void testEmitIntCodepoints() {
        int[] codepoints = {'t', 'e', 's', 't'};
        tokeniser.emit(codepoints);
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        assertEquals(new String(codepoints, 0, codepoints.length), ((Token.Character) token).getData());
    }

    @Test
    public void testEmitChar() {
        char c = 't';
        tokeniser.emit(c);
        Token token = tokeniser.read();
        assertTrue(token instanceof Token.Character);
        assertEquals(String.valueOf(c), ((Token.Character) token).getData());
    }

    @Test
    public void testGetState() {
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        assertNull(tokeniser.consumeCharacterReference(null, false));
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Rcdata);
        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpected character '' in input state [Rcdata]", tokeniser.errors.get(0).toString());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, tokeniser.errors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", tokeniser.errors.get(0).toString());
    }

    @Test
    public void testCharacterReferenceError() {
        String message = "numeric reference with no numerals";
        tokeniser.characterReferenceError(message);
        assertEquals(1, tokeniser.errors.size());
        assertEquals("Invalid character reference: " + message, tokeniser.errors.get(0).toString());
    }

    @Test
    public void testErrorWithMessage() {
        String errorMsg = "Error message";
        tokeniser.error(errorMsg);
        assertEquals(1, tokeniser.errors.size());
        assertEquals(errorMsg, tokeniser.errors.get(0).toString());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }

    @Test
    public void testUnescapeEntities() {
        String str = "test";
        tokeniser.unescapeEntities(false);
        assertEquals(str, tokeniser.unescapeEntities(false));
    }
}