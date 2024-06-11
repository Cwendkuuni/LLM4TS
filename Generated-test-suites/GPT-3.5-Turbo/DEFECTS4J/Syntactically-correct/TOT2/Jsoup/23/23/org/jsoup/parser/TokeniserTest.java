import org.junit.Test;
import org.jsoup.helper.Validate;
import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testTokeniserCreation() {
        CharacterReader reader = new CharacterReader("Test input");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        assertNotNull(tokeniser);
        assertEquals(errors, tokeniser.errors);
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testReadMethod() {
        // Create Tokeniser instance with mock reader and errors
        CharacterReader reader = new CharacterReader("<div>Test</div>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertNotNull(token);
        assertTrue(token instanceof Token.Character);
    }

    @Test
    public void testEmitMethod() {
        // Create Tokeniser instance with mock reader and errors
        CharacterReader reader = new CharacterReader("<div>Test</div>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = new Token.Comment();
        tokeniser.emit(token);

        Token emittedToken = tokeniser.emitPending;
        assertNotNull(emittedToken);
        assertEquals(token, emittedToken);
    }

    @Test
    public void testCreateTagPending() {
        // Create Tokeniser instance with mock reader and errors
        CharacterReader reader = new CharacterReader("<div>Test</div>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token.Tag tag = tokeniser.createTagPending(true);
        assertNotNull(tag);
        assertTrue(tag instanceof Token.StartTag);
    }
}