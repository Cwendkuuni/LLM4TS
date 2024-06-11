import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.parser.*;

public class TokeniserTest {

    @Test
    public void testRead() {
        CharacterReader reader = new CharacterReader("test input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();

        assertNotNull(token);
    }

    @Test
    public void testEmit() {
        CharacterReader reader = new CharacterReader("test input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = new Token.StartTag();
        tokeniser.emit(token);

        assertTrue(tokeniser.isEmitPending);
    }

    @Test
    public void testAdvanceTransition() {
        CharacterReader reader = new CharacterReader("test input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        TokeniserState nextState = TokeniserState.Data;
        tokeniser.advanceTransition(nextState);

        assertEquals(nextState, tokeniser.getState());
    }

    // Additional test cases for other methods can be added here

}