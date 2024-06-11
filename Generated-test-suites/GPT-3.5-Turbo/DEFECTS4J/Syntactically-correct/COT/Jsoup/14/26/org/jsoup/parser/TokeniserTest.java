
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setup() {
        tokeniser = new Tokeniser(new CharacterReader());
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("", ((Token.Character) token).getData());
    }

    @Test
    public void testEmit() {
        Token token = new Token.Character("data");

        tokeniser.emit(token);

        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testEmitString() {
        String str = "data";

        tokeniser.emit(str);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(str, ((Token.Character) token).getData());
    }

    @Test
    public void testEmitChar() {
        char c = 'a';

        tokeniser.emit(c);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(Character.toString(c), ((Token.Character) token).getData());
    }

    @Test
    public void testTransition() {
        TokeniserState nextState = TokeniserState.Doctype;

        tokeniser.transition(nextState);

        assertEquals(nextState, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        TokeniserState nextState = TokeniserState.Doctype;

        tokeniser.advanceTransition(nextState);

        assertEquals(nextState, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();

        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("&#x41;");
        tokeniser = new Tokeniser(reader);
        Character additionalAllowedCharacter = null;
        boolean inAttribute = false;

        Character characterReference = tokeniser.consumeCharacterReference(additionalAllowedCharacter, inAttribute);

        assertEquals('A', characterReference.charValue());
    }
    
    // Add additional test methods for other public methods in the class that require testing
    
    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "test";
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "test";

        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        tokeniser.setTrackErrors(true);
        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testError() {
        tokeniser.setTrackErrors(true);
        tokeniser.error(TokeniserState.Data);

        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testEofError() {
        tokeniser.setTrackErrors(true);
        tokeniser.eofError(TokeniserState.Data);

        assertEquals(1, tokeniser.errors.size());
    }
}
