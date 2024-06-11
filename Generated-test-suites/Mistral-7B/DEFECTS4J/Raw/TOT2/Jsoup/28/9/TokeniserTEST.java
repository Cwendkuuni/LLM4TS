import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.Token.Tag;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.Comment;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TokeniserTest {

    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @BeforeAll
    public void setUp() {
        reader = new MockCharacterReader("");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testReadValidCharacter() {
        // Test case for reading a valid character
    }

    @Test
    public void testEmitValidToken() {
        // Test case for emitting a valid token
    }

    @Test
    public void testReadSelfClosingTag() {
        // Test case for reading a self-closing tag
        reader = new MockCharacterReader("<br/>");
        tokeniser.transition(TokeniserState.SelfClosingStartTag);
        Token token = tokeniser.read();
        assertEquals(TokenType.EndTag, token.type);
        assertEquals("br", token.getData().tagName);
    }

    @Test
    public void testReadInvalidCharacter() {
        // Test case for reading an invalid character
        reader = new MockCharacterReader("&");
        tokeniser.transition(TokeniserState.Data);
        tokeniser.reader.consume();
        assertThrows(ParseError.class, () -> tokeniser.read());
    }

    @Test
    public void testEmitSelfClosingTag() {
        // Test case for emitting a self-closing tag
        reader = new MockCharacterReader("<br/>");
        Token startTag = new StartTag("br");
        tokeniser.transition(TokeniserState.Data);
        tokeniser.emit(startTag);
        assertEquals(TokenType.EndTag, tokeniser.read().type);
        assertEquals("br", tokeniser.read().getData().tagName);
    }

    @Test
    public void testReadUnreadTokenPending() {
        // Test case for attempting to read an unread token
        Character character = new Character('a');
        tokeniser.emit(character);
        assertThrows(IllegalStateException.class, () -> tokeniser.read());
    }

    @Test
    public void testEmitInvalidToken() {
        // Test case for attempting to emit an invalid token
        Token invalidToken = new Token(TokenType.EndTag, "invalid");
        assertThrows(IllegalArgumentException.class, () -> tokeniser.emit(invalidToken));
    }

    @Test
    public void testReadAfterError() {
        // Test case for reading after an error has been encountered
        tokeniser
            .transition(TokeniserState.StartTagBegin)
            .error("Invalid character: &");
        assertThrows(ParseError.class, () -> tokeniser.read());
    }

    @Test
    public void testReadAfterEOF() {
        // Test case for reading after reaching the end of the input stream
        reader = new MockCharacterReader("foo<bar>");
        tokeniser.transition(TokeniserState.StartTagBegin);
        tokeniser.reader.consumeAll();
        assertThrows(ParseError.class, () -> tokeniser.read());
    }
}