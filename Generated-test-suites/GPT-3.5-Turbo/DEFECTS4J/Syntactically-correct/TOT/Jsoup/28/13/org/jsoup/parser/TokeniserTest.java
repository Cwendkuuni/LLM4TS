
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

import static org.junit.Assert.assertEquals;

public class TokeniserTest {

    @Test
    public void testRead_withCharBuffer_thenReturnCharacterToken() {
        CharacterReader reader = new CharacterReader("abc");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("abc", token.toString());
    }

    @Test
    public void testRead_withEmitPending_thenReturnPendingToken() {
        CharacterReader reader = new CharacterReader("abc");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.emit(new Token.StartTag("div"));

        Token token = tokeniser.read();

        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("div", token.toString());
    }

    @Test
    public void testEmit_withStartTag_thenSetSelfClosingFlagAcknowledgedFalseOnSelfClosingStartTag() {
        CharacterReader reader = new CharacterReader("abc");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.emit(new Token.StartTag("div", true));

        assertEquals(false, tokeniser.selfClosingFlagAcknowledged);
    }

    // Add more test cases for other methods in Tokeniser class...

}

