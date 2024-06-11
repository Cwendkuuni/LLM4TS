import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Tag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.ParseError;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TokeniserTest {

    @Test
    public void testReadNormalCharacter() {
        CharacterReader reader = new CharacterReader("abc");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertNull(token);

        token = tokeniser.read();
        assertEquals(Character.class, token.getClass());
        assertEquals('a', token.data());

        token = tokeniser.read();
        assertEquals('b', token.data());

        token = tokeniser.read();
        assertEquals('c', token.data());

        assertEquals(Token.TokenType.Character, token.type);
        assertNull(token.tag());
        assertNull(token.attributes());
    }

    // Add test cases here for other methods and edge cases

    // ...
}