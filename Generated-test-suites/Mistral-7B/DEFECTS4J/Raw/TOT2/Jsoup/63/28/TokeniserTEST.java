import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Test;

public class TokeniserTest {
    @Test
    public void testReadBasic() {
        CharacterReader reader = new CharacterReader("abcdef");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('a', token.charValue());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('b', token.charValue());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('c', token.charValue());

        // test read when no character is present
        token = tokeniser.read();
        assertNull(token);
    }

    // other test cases for each method, handling edge cases and exception scenarios could be added here
}