import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setup() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.Character("test");
        tokeniser.emit(token);
        Token emittedToken = tokeniser.read();
        assertEquals(token, emittedToken);
    }

    @Test
    public void testEmitString() {
        tokeniser.emit("test");
        Token emittedToken = tokeniser.read();
        assertEquals(new Token.Character("test"), emittedToken);
    }

    @Test
    public void testEmitChar() {
        tokeniser.emit('t');
        tokeniser.emit('e');
        Token emittedToken = tokeniser.read();
        assertEquals(new Token.Character("te"), emittedToken);
    }

    // Write additional test cases for other public methods to test edge cases and exceptions

}