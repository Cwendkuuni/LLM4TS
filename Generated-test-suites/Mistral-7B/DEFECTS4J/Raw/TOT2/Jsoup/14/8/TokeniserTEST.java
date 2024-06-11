import org.junit.Test;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.Token;
import static org.junit.Assert.assertEquals;

public class TokeniserTest {
    @Test
    public void testReadMethod() {
        CharacterReader characterReader = new StringReader("Hello World");
        Tokeniser tokeniser = new Tokeniser(characterReader);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("html", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("H", token.text);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("e", token.text);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("l", token.text);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("l", token.text);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("o", token.text);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("html", token.tagName);

        token = tokeniser.read(); // should be null
        assertEquals(null, token);
    }
}