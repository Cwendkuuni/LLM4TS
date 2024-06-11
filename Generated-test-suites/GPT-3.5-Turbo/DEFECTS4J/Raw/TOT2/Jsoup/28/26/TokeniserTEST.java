import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setup() {
        tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
    }

    @Test
    public void testRead() {
        // Test reading a token from the tokeniser
        Token token = tokeniser.read();
        assertEquals(null, token);
    }

    @Test
    public void testEmit() {
        // Test emitting a token from the tokeniser
        Token token = new Token.Character("test");
        tokeniser.emit(token);
        Token emittedToken = tokeniser.read();
        assertEquals(token, emittedToken);
    }

    @Test
    public void testConsumeCharacterReference() {
        // Test consuming a character reference from the tokeniser
        Character character = tokeniser.consumeCharacterReference('#', false);
        assertEquals(null, character);
    }

    @Test
    public void testCreateTagPending() {
        // Test creating a tag pending in the tokeniser
        Token.Tag tag = tokeniser.createTagPending(true);
        assertTrue(tag instanceof Token.StartTag);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // Test checking if the end tag token is appropriate in the tokeniser
        boolean isAppropriate = tokeniser.isAppropriateEndTagToken();
        assertTrue(isAppropriate);
    }

    @Test
    public void testAppropriateEndTagName() {
        // Test getting the appropriate end tag name in the tokeniser
        String endTagName = tokeniser.appropriateEndTagName();
        assertEquals("", endTagName); // Assuming an empty string as the last start tag name
    }

    // Add more test cases for other methods in Tokeniser class

}