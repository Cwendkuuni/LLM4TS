import static org.junit.Assert.*;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.helper.CharacterInput;
import org.jsoup.nodes.Token;

public class TokeniserTest {
    @Test
    public void testReadMethod() throws Exception {
        CharacterInput characterInput = new CharacterInput("Hello World");
        Tokeniser tokeniser = new Tokeniser(characterInput, new ArrayList<>());
        Token expectedToken = new Token.Character('H');
        Token actualToken = tokeniser.read();
        assertEquals(expectedToken, actualToken);
    }
}