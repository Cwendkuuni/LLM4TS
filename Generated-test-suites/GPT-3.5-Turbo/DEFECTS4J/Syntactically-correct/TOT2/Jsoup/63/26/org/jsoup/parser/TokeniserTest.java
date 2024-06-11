import org.jsoup.parser.CharReader;
import org.jsoup.parser.ParseException;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokeniserTest {

    @Test
    public void testTokeniserRead() {
        // Test reading a token from the Tokeniser
        CharReader reader = new CharReader("Test String");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("Test String", token.toString());
    }

    @Test
    public void testTokeniserError() {
        // Test generating an error in the Tokeniser
        CharReader reader = new CharReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.error("Test Error");

        assertEquals(1, errors.size());
    }

    @Test
    public void testTokeniserTransition() {
        // Test transitioning the Tokeniser state
        CharReader reader = new CharReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.Data);

        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testTokeniserConsumeCharacterReference() {
        // Test consuming a character reference in the Tokeniser
        CharReader reader = new CharReader("&#65;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        int[] codepoints = tokeniser.consumeCharacterReference(null, false);

        assertEquals(1, codepoints.length);
        assertEquals(65, codepoints[0]);
    }

    @Test
    public void testTokeniserUnescapeEntities() {
        // Test unescaping entities in the Tokeniser
        CharReader reader = new CharReader("&lt;tag&gt;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        String unescapedString = tokeniser.unescapeEntities(false);

        assertEquals("<tag>", unescapedString);
    }

    // Add more test cases for other methods in the Tokeniser class

}