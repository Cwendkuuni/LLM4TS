import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

public class TokeniserTest {
    private Tokeniser tokeniser;
    private ParseErrorList errors;

    @Before
    public void setup() {
        String input = "<html><head><title>Test Page</title></head><body><p>Hello, World!</p></body></html>";
        CharacterReader reader = new CharacterReader(input);
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token;
        for (int i = 0; i < 10 && (token = tokeniser.read()) != null; i++) {
            System.out.println("Token [" + i + "]: " + token);
        }
        assertTrue(tokeniser.getState() == TokeniserState.Data);
    }
}