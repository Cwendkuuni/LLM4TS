import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        // Initialize the Tokeniser with a CharacterReader and ParseErrorList
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
    }

    @Test
    public void testRead() {
        // Test the read method
        Token token = tokeniser.read();
        assertNull(token);
        // Add more test cases here for different scenarios
    }

    @Test
    public void testEmit() {
        // Test the emit method
        Token token = new Token.Character("test");
        tokeniser.emit(token);
        assertTrue(tokeniser.isEmitPending);
        assertEquals(token, tokeniser.emitPending);
        // Add more test cases here for different scenarios
    }

    // Add more test methods for other public methods in the Tokeniser class

}