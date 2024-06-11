
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void testEmit() {
        Token token = new Token.StartTag();
        tokeniser.emit(token);
        assertEquals(token, tokeniser.read());
    }

    @Test
    public void testEmitString() {
        String str = "example";
        tokeniser.emit(str);
        assertEquals(new Token.Character(str), tokeniser.read());
    }

    @Test
    public void testEmitChar() {
        char c = 'a';
        tokeniser.emit(c);
        assertEquals(new Token.Character(String.valueOf(c)), tokeniser.read());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "test";
        tokeniser.lastStartTag = new Token.StartTag();
        tokeniser.lastStartTag.tagName = "test";
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        assertTrue(tokeniser.isTrackErrors());
        tokeniser.setTrackErrors(false);
        assertFalse(tokeniser.isTrackErrors());
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testTokeniserWithNullReader() {
        try {
            new Tokeniser(null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("reader must not be null", e.getMessage());
        }
    }

    @Test
    public void testTokeniserWithEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        Tokeniser tokeniser = new Tokeniser(reader);
        assertNull(tokeniser.read());
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);
        List<ParseError> errors = new ArrayList<>();
        errors.add(new ParseError("Unexpected character in input", '\0', TokeniserState.Data, 0));
        assertEquals(errors, tokeniser.errors);
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Data);
        List<ParseError> errors = new ArrayList<>();
        errors.add(new ParseError("Unexpectedly reached end of file (EOF)", TokeniserState.Data, 0));
        assertEquals(errors, tokeniser.errors);
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError();
        List<ParseError> errors = new ArrayList<>();
        errors.add(new ParseError("Invalid character reference", 0));
        assertEquals(errors, tokeniser.errors);
    }

    @Test
    public void testErrorWithCustomMessage() {
        tokeniser.error("Test error message");
        List<ParseError> errors = new ArrayList<>();
        errors.add(new ParseError("Test error message", 0));
        assertEquals(errors, tokeniser.errors);
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}