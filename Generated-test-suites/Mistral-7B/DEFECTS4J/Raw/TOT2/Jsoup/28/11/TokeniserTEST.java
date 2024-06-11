import org.junit.Test;
import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testReadValidCharacter() {
        CharacterReader reader = new CharacterReader("abc");
        Tokeniser tokeniser = new Tokeniser(reader, null);
        Token token = tokeniser.read();
        assertEquals("Character", token.type.name());
        assertEquals('a', token.content.charAt(0));
    }

    // Add other basic test cases for each method here.

    // Test for reading invalid character references.
    @Test(expected = ParseError.class)
    public void testReadInvalidCharacterReference() {
        CharacterReader reader = new CharacterReader("&xxx;");
        Tokeniser tokeniser = new Tokeniser(reader, null);
        tokeniser.consumeCharacterReference(null, false);
    }

    // Test for reading character references with invalid characters.
    @Test(expected = ParseError.class)
    public void testReadCharacterReferenceWithInvalidCharacters() {
        CharacterReader reader = new CharacterReader("&&#%&");
        Tokeniser tokeniser = new Tokeniser(reader, null);
        tokeniser.consumeCharacterReference(null, false);
    }

    // Test for reading character references in attributes.
    @Test(expected = ParseError.class)
    public void testReadCharacterReferenceInAttribute() {
        CharacterReader reader = new CharacterReader("<div attr='&abc;'>");
        Tokeniser tokeniser = new Tokeniser(reader, null);
        tokeniser.consumeCharacterReference(null, true);
    }

    // Test for reading character references in invalid states.
    @Test(expected = ParseError.class)
    public void testReadCharacterReferenceInInvalidState() {
        CharacterReader reader = new CharacterReader("abc");
        Tokeniser tokeniser = new Tokeniser(reader, null);
        tokeniser.setState(TokeniserState.Script);
        tokeniser.consumeCharacterReference(null, false);
    }

    // Test for transitioning to invalid states.
    @Test(expected = ParseError.class)
    public void testTransitionToInvalidState() {
        CharacterReader reader = new CharacterReader("</div><script>");
        Tokeniser tokeniser = new Tokeniser(reader, null);
        tokeniser.transition(TokeniserState.Script);
    }

    // Test for reading self-closing tags without acknowledging the flag.
    @Test(expected = ParseError.class)
    public void testReadSelfClosingTagWithoutAcknowledgingFlag() {
        CharacterReader reader = new CharacterReader("<img src='image.jpg' />");
        Tokeniser tokeniser = new Tokeniser(reader, null);
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.read(); // Read the start tag
        tokeniser.read(); // Should read the self-closing tag here.
        tokeniser.emitPending();
        tokeniser.read(); // Read the next character, which should be an error.
    }

    // Add other test cases for edge cases and exceptions here.

}