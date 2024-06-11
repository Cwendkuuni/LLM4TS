import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(""));
    }

    @Test
    public void testRead() {
        // Test basic read functionality
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        // Test emit functionality
        Token token = new Token.Character("Test");
        tokeniser.emit(token);
        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testConsumeCharacterReference() {
        // Test consuming character reference
        Character c = tokeniser.consumeCharacterReference(null, false);
        assertNull(c);
    }

    // Add more test methods as needed for other public methods

}