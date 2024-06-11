
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
        
        tokeniser.emit(new Token.StartTag());
        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        
        // More test cases for other token types and scenarios can be added here
    }

    @Test
    public void testEmit() {
        Token.StartTag startTag = new Token.StartTag();
        tokeniser.emit(startTag);

        Token token = tokeniser.read();
        assertEquals(startTag, token);
    }

    @Test
    public void testEmitString() {
        String str = "Test String";
        tokeniser.emit(str);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(str, ((Token.Character) token).getData());
    }

    @Test
    public void testEmitChar() {
        char c = 'A';
        tokeniser.emit(c);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(String.valueOf(c), ((Token.Character) token).getData());
    }

    @Test
    public void testGetState() {
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.Rcdata);
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Rcdata, state);
    }

    @Test
    public void testAdvanceTransition() {
        tokeniser.advanceTransition(TokeniserState.Data);
        TokeniserState state = tokeniser.getState();
        assertEquals(TokeniserState.Data, state);
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser.isSelfClosingFlagAcknowledged());
    }

    // More test cases for other methods can be added here

}

