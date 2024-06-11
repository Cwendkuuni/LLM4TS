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
        // Test the read method of Tokeniser
        Token token = tokeniser.read();
        assertNull(token); // Since the character reader is empty, expected output is null
    }

    @Test
    public void testEmit() {
        // Test the emit method of Tokeniser
        Token.Tag tag = new Token.Tag();
        tokeniser.emit(tag);
        Token emittedToken = tokeniser.read();
        assertEquals(tag, emittedToken); // Check if the emitted token matches the expected token
    }

    @Test
    public void testConsumeCharacterReference() {
        // Test the consumeCharacterReference method of Tokeniser with additional allowed character
        Character additionalAllowedChar = '!';
        Character result = tokeniser.consumeCharacterReference(additionalAllowedChar, true);
        assertNull(result); // Since the reader is empty, expected output is null
    }

    @Test
    public void testEmitTagPending() {
        // Test the emitTagPending method of Tokeniser
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "testTag";
        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();
        assertEquals("testTag", ((Token.Tag) emittedToken).tagName); // Check if the emitted tag name matches the expected name
    }

    // Add more test cases for other methods as needed

}