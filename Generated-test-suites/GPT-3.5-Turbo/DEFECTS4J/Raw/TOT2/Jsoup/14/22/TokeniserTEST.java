import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(""));
    }

    @Test
    public void testRead() {
        assertNotNull(tokeniser.read());
    }

    @Test
    public void testEmit() {
        tokeniser.emit("test");
        assertEquals("test", tokeniser.read().toString());
    }

    @Test
    public void testEmitChar() {
        tokeniser.emit('c');
        assertEquals("c", tokeniser.read().toString());
    }

    @Test
    public void testSetTrackErrors() {
        tokeniser.setTrackErrors(false);
        assertFalse(tokeniser.isTrackErrors());
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError();
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}