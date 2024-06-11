package org.jsoup.parser; // Make sure to import the necessary classes at the beginning

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

/**
 * Test the methods in the Tokeniser class.
 */
public class TokeniserTest {

    // assume the following class members are provided for testing
    private Tokeniser tokeniser;
    private CharacterReader characterReader;
    private ParseErrorList parseErrorList;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("test data") {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };
        parseErrorList = new ParseErrorList();
        tokeniser = new Tokeniser(characterReader, parseErrorList);
    }

    // Test for read() method
    @Test
    public void testRead() {
        tokeniser.transition(TokeniserState.Reset);
        tokeniser.read();
        assertNull(tokeniser.getState().data);
        assertNull(tokeniser.getState().tag);
    }

    // Test for emit() method
    @Test
    public void testEmitStartTag() {
        tokeniser.transition(TokeniserState.Reset);
        tokeniser.emit(new Token.StartTag("div"));
        assertSame("div", tokeniser.lastStartTag);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmitEndTagWithoutStartTag() {
        tokeniser.transition(TokeniserState.Reset);
        tokeniser.emit(new Token.EndTag("div"));
    }

    @Test
    public void testEmitCharacter() {
        tokeniser.transition(TokeniserState.Reset);
        tokeniser.emit("test");
        assertEquals("test", tokeniser.getState().data);
    }

    @Test
    public void testEmitString() {
        tokeniser.transition(TokeniserState.Reset);
        tokeniser.emit("test string");
        assertEquals("test string", tokeniser.getState().data);
    }

    @Test
    public void testEmitCharacterReference() {
        tokeniser.transition(TokeniserState.Reset);
        tokeniser.emit("&#123;");
        assertEquals('\u007B', tokeniser.getState().data[0]);
    }

    @Test
    public void testEmitCharacterReferenceWithHex() {
        tokeniser.transition(TokeniserState.Reset);
        tokeniser.emit("#x{41}");
        assertEquals('A', tokeniser.getState().data[0]);
    }

    @Test
    public void testEmitEntityReference() {
        tokeniser.transition(TokeniserState.Reset);
        tokeniser.emit("&amp;");
        assertEquals("&amp;", tokeniser.getState().data);
    }

    @Test
    public void testEmitEntityReferenceWithSpace() {
        tokeniser.transition(TokeniserState.Reset);
        tokeniser.emit("&nbsp;");
        assertEquals("\u00A0", tokeniser.getState().data[0]);
    }

    @Test
    public void testEmitDoctype() {
        tokeniser.transition(TokeniserState.Reset);
        tokeniser.emit("<!DOCTYPE html>");
        assertEquals("<!DOCTYPE html>", tokeniser.getState().data);
    }

    @Test
    public void testEmitComment() {
        tokeniser.transition(TokeniserState.Reset);
        tokeniser.emit("<!-- comment -->");
        assertEquals("<!-- comment -->", tokeniser.getState().data);
    }

    // Test for error handling
    @Test(expected = IllegalArgumentException.class)
    public void testErrorInvalidState() {
        tokeniser.error(new TokeniserState("InvalidState"));
    }

    @Test
    public void testErrorWithMessage() {
        tokeniser.transition(TokeniserState.Reset);
        tokeniser.error("Unexpected character 'X' in input state [Reset]");
        assertEquals("Unexpected character 'X' in input state [Reset]", parseErrorList.getErrorList().get(0).getMessage());
    }

    @Test(expected = IllegalStateException.class)
    public void testErrorWithNullErrorList() {
        tokeniser.transition(TokeniserState.Reset);
        tokeniser.error(null);
    }

    @Test
    public void testCanAddError() {
        tokeniser.transition(TokeniserState.Reset);
        assert(parseErrorList.canAddError());
    }

    @Test
    public void testAddError() {
        tokeniser.transition(TokeniserState.Reset);
        tokeniser.error("Unexpected character 'X' in input state [Reset]");
        assertEquals(1, parseErrorList.getErrorList().size());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.transition(TokeniserState.Reset);
        tokeniser.createTagPending(true).name("div");
        tokeniser.emitTagPending();
        tokeniser.createTagPending(false).name("div");
        assertTrue(tokeniser.isAppropriateEndTagToken());
        assertFalse(tokeniser.isAppropriateEndTagToken()); // Should not match again.
    }

    @Test
    public void testErrorReadWithEmptyReader() {
        characterReader = new CharacterReader("") {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };
        tokeniser = new Tokeniser(characterReader, parseErrorList);
        tokeniser.transition(TokeniserState.Reset);
        try {
            tokeniser.read();
            fail("Expected an error");
        } catch (ParseError error) {
            assertEquals("Unexpectedly reached end of file (EOF)", error.getMessage());
        }
    }

    @Test
    public void testConsumeCharacterReference() {
        tokeniser.transition(TokeniserState.Reset);
        assertNull(tokeniser.consumeCharacterReference(null, false));
        assertNull(tokeniser.consumeCharacterReference('X', false));
        assertNull(tokeniser.consumeCharacterReference(null, true));
        assertNull(tokeniser.consumeCharacterReference('X', true));

        tokeniser.reader.mark(1);
        tokeniser.reader.consume('&');
        int[] result = tokeniser.consumeCharacterReference('#', false);
        assertNull(result);
        tokeniser.reader.rewindToMark();

        tokeniser.reader.mark(1);
        tokeniser.reader.consume('#');
        tokeniser.reader.consume('X');
        tokeniser.reader.consume(';');
        result = tokeniser.consumeCharacterReference(null, false);
        assertEquals(Character.toCodePoint('\u0058'), result[0]);
        assertEquals('\u0058', result[0]); // For Java 8
        assertEquals(0, result.length - 1);

        tokeniser.reader.mark(1);
        tokeniser.reader.consume('<');
        tokeniser.reader.consume('!');
        tokeniser.reader.consume('D');
        tokeniser.reader.consume('O');
        tokeniser.reader.consume('C');
        tokeniser.reader.consume('T');
        tokeniser.reader.consume('Y');
        tokeniser.reader.consume('P');
        tokeniser.reader.consume('E');
        tokeniser.reader.consume('>');
        result = tokeniser.consumeCharacterReference("#", false);
        assertNull(result);
        tokeniser.reader.rewindToMark();

        tokeniser.reader.mark(1);
        tokeniser.reader.consume('&');
        tokeniser.reader.consume('A');
        tokeniser.reader.consume('M');
        tokeniser.reader.consume('P');
        tokeniser.reader.consume(';');
        result = tokeniser.consumeCharacterReference(null, false);
        assertEquals(Character.toCodePoint('\u0026'), result[0]);
        assertEquals('\u0026', result[0]); // For Java 8
        assertEquals(0, result.length - 1);
    }
}