import org.jsoup.parser.*;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setup() {
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
        tokeniser.emit(new Token.Character("test"));
        Token token = tokeniser.read();
        assertEquals(new Token.Character("test"), token);
    }

    @Test
    public void testAdvanceTransition() {
        tokeniser.advanceTransition(TokeniserState.ScriptData);
        assertEquals(TokeniserState.ScriptData, tokeniser.getState());
    }

    @Test
    public void testConsumeCharacterReference() {
        Character charRef = tokeniser.consumeCharacterReference('&', false);
        assertNull(charRef);
    }

    @Test
    public void testCreateTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        assertTrue(tag instanceof Token.StartTag);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.createTagPending(true);
        assertEquals("", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}