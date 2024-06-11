import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.helper.CharacterInputSource;

public class TokeniserTest {

    @Test
    public void testReadMethod() throws Exception {
        // Given
        CharacterInputSource inputSource = new CharacterInputSource("SomeText");
        Tokeniser tokeniser = new Tokeniser(inputSource, new ArrayList<>());

        // When
        Token token = tokeniser.read();

        // Then
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("SomeText", token.data);
    }
}