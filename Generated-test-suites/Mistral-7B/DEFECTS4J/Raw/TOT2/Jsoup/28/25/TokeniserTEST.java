import org.junit.Test;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.Token;
import static org.junit.Assert.assertEquals;

public class TokeniserTest {
    @Test
    public void testRead() {
        CharacterReader reader = new CharacterReader("Hello World");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("H", token.content);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("e", token.content);

        // ... Add more test cases for other character combinations, empty input, etc.
    }
}