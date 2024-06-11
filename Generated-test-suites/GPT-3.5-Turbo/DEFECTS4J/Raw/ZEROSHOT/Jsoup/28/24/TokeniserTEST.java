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
    public void setUp() {
        reader = new CharacterReader("test input");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }
    
    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertTrue(token instanceof Token.Character);
        
        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Comment, token.type);
        assertTrue(token instanceof Token.Comment);
        
        // continue testing for other types of tokens
    }
    
    @Test
    public void testEmit() {
        Token.Character character = new Token.Character("test");
        tokeniser.emit(character);
        
        Token token = tokeniser.read();
        assertEquals(character, token);
        
        // test for other types of tokens
    }
    
    // continue testing for other methods
    
}