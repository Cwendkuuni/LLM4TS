
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {
    @Test
    public void testRead() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("sample input"));
        Token token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void testInvalidInputTokeniser() {
        try {
            Tokeniser tokeniser = new Tokeniser(null);
            fail("Expected NullPointerException not thrown");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testTokenizationStates() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader("state input"));
        tokeniser.transition(TokeniserState.Data);
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testCharacterReference() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader());
        Character ref1 = tokeniser.consumeCharacterReference(null, false);
        assertNull(ref1);

        Character ref2 = tokeniser.consumeCharacterReference('x', false);
        assertNull(ref2);
    }
}
