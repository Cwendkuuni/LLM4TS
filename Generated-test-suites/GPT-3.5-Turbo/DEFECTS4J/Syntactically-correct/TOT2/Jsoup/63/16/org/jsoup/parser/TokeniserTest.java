import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokeniserState;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    
    private Tokeniser tokeniser;
    private CharacterReader reader;
    private ParseErrorList errors;

    @Before
    public void setup() {
        reader = new CharacterReader("<html></html>");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.Builder(Token.TokenType.StartTag).tagName("test").build();
        tokeniser.emit(token);
        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testEmitString() {
        String testString = "test string";
        tokeniser.emit(testString);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(testString, token.toString());
    }

    @Test
    public void testCreateTagPending() {
        tokeniser.createTagPending(true);
        assertNotNull(tokeniser.tagPending);
    }

    @Test
    public void testAdvanceTransition() {
        tokeniser.advanceTransition(TokeniserState.Rcdata);
        assertEquals(TokeniserState.Rcdata, tokeniser.getState());
    }

    // Add more test cases
    
}