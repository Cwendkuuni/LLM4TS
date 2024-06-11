import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.*;

public class TokeniserTest {

    // Test the Tokeniser constructor
    @Test
    public void testTokeniserConstructor() {
        CharacterReader reader = new CharacterReader("test");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        assertNotNull(tokeniser);
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    // Test the read method
    @Test
    public void testRead() {
        CharacterReader reader = new CharacterReader("<html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();

        assertTrue(token instanceof Token.StartTag);
        Token.StartTag startTag = (Token.StartTag) token;
        assertEquals("html", startTag.tagName);
    }

    // Test the emit method
    @Test
    public void testEmit() {
        CharacterReader reader = new CharacterReader("<img>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        Token token = new Token.StartTag();
        tokeniser.emit(token);

        Token emittedToken = tokeniser.read();
        assertEquals(token, emittedToken);
    }

    // Test the consumeCharacterReference method
    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("&#65;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Character ref = tokeniser.consumeCharacterReference(null, false);
        assertNotNull(ref);
        assertEquals('A', ref.charValue());
    }

    // Test the error method
    @Test
    public void testError() {
        CharacterReader reader = new CharacterReader("<div>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.error("Test error message");

        assertTrue(errors.size() > 0);
    }
}