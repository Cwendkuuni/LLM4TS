import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setup() {
        tokeniser = new Tokeniser(new CharacterReader("Test input"), new ParseErrorList());
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.StartTag("div");
        tokeniser.emit(token);
        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = 'A';
        Character result = tokeniser.consumeCharacterReference(additionalAllowedCharacter, false);
        assertNull(result);
    }

    @Test
    public void testCreateTagPending() {
        Token.Tag tagPending = tokeniser.createTagPending(true);
        assertNotNull(tagPending);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        boolean result = tokeniser.isAppropriateEndTagToken();
        assertEquals(false, result);
    }

    @Test
    public void testAppropriateEndTagName() {
        String result = tokeniser.appropriateEndTagName();
        assertEquals("tag", result);
    }

    @Test
    public void testError() {
        // Not possible to directly test error handling within the Tokeniser class
    }

}