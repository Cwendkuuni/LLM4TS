import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader("Test Input"), new ParseErrorList());
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type); // Testing for a character token by default
    }

    @Test
    public void testEmit() {
        Token tagToken = new Token.Tag();
        tokeniser.emit(tagToken);
        Token emittedToken = tokeniser.read();
        assertEquals(tagToken, emittedToken);
    }

    @Test
    public void testConsumeCharacterReference() {
        CharacterReader characterReader = new CharacterReader("&#65;");
        Tokeniser tokeniser = new Tokeniser(characterReader, new ParseErrorList());
        
        Character result = tokeniser.consumeCharacterReference(null, true);
        assertEquals('A', result.charValue());
    }

    // Add more test cases for other methods in the Tokeniser class

}