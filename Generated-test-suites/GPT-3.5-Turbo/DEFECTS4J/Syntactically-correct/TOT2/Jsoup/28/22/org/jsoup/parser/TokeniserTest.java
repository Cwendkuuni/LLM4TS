import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;
    
    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
    }
    
    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNull(token);
    }
    
    @Test
    public void testEmit() {
        Token tagToken = new Token.Tag("exampleTag");
        tokeniser.emit(tagToken);
        Token emittedToken = tokeniser.read();
        assertEquals(tagToken, emittedToken);
    }
    
    @Test
    public void testEmitString() {
        String exampleString = "example string";
        tokeniser.emit(exampleString);
        Token charToken = tokeniser.read();
        assertEquals(Token.TokenType.Character, charToken.type);
        assertEquals(exampleString, ((Token.Character) charToken).getData());
    }
    
    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("&amp; &COPY; &invalid;");
        tokeniser = new Tokeniser(reader, new ParseErrorList());
        Character result = tokeniser.consumeCharacterReference(null, false);
        assertEquals('&', (char) result);
        
        reader.advance();
        result = tokeniser.consumeCharacterReference(null, false);
        assertEquals('\u00A9', (char) result);
        
        reader.advance();
        result = tokeniser.consumeCharacterReference(null, false);
        assertNull(result);
    }
    
    @Test
    public void testError() {
        tokeniser.error("Test error message");
        assertTrue(tokeniser.errors.size() > 0);
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean result = tokeniser.currentNodeInHtmlNS();
        assertTrue(result);
    }
}