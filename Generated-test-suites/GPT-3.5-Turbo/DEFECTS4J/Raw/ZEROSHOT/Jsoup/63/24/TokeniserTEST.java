import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
        assertNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token();
        tokeniser.emit(token);
        assertSame(token, tokeniser.read());
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(str, token.data);
    }

    @Test
    public void testEmitCharArray() {
        char[] chars = {'t', 'e', 's', 't'};
        tokeniser.emit(chars);
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(new String(chars), token.data);
    }

    @Test
    public void testEmitIntArray() {
        int[] codepoints = {116, 101, 115, 116};
        tokeniser.emit(codepoints);
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(new String(codepoints, 0, codepoints.length), token.data);
    }

    @Test
    public void testEmitChar() {
        char c = 't';
        tokeniser.emit(c);
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(String.valueOf(c), token.data);
    }

    @Test
    public void testTransition() {
        TokeniserState state = TokeniserState.TagName;
        tokeniser.transition(state);
        assertEquals(state, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState state = TokeniserState.AttributeValue_doubleQuoted;
        tokeniser.advanceTransition(state);
        assertEquals(state, tokeniser.getState());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }
}