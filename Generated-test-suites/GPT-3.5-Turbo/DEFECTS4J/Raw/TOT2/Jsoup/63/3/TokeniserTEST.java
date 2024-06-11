import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("Test input");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertEquals("Test input", token.toString());
    }

    @Test
    public void testEmit() {
        Token token = new Token(Character.valueOf('a'));
        tokeniser.emit(token);
        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.appendTagName("testTag");
        tokeniser.emitTagPending();
        Token expectedToken = new Token.Tag("testTag");
        assertEquals(expectedToken, tokeniser.read());
    }

    @Test
    public void testUnescapeEntities() {
        String input = "This is a &amp; test &lt; input";
        String expectedOutput = "This is a & test < input";
        assertEquals(expectedOutput, tokeniser.unescapeEntities(true));
    }

    // Additional test cases for other methods should be added here

}