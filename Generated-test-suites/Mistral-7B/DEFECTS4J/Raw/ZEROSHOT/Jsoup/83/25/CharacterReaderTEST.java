package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        String input = "ABCde123! @#DEF<GHI>456& JKLmNOP";
        reader = new CharacterReader(new StringReader(input));
    }

    @Test
    public void testCharacterReaderConstruction() {
        CharacterReader reader1 = new CharacterReader(new StringReader(""));
        CharacterReader reader2 = new CharacterReader(new StringReader("hello"));
        CharacterReader reader3 = new CharacterReader(new StringReader("world"));
        CharacterReader reader4 = new CharacterReader(new StringReader("hello world"));

        assertNotNull(reader1);
        assertNotNull(reader2);
        assertNotNull(reader3);
        assertNotNull(reader4);
    }

    @Test
    public void testCharacterReaderBufferUp() {
        reader.bufferUp();
        reader.bufferUp();

        assertEquals(reader.pos(), 0);
        assertEquals(reader.isEmpty(), false);
        assertEquals(reader.current(), 'A');

        reader.consume();
        reader.bufferUp();

        assertEquals(reader.pos(), 1);
        assertEquals(reader.isEmpty(), false);
        assertEquals(reader.current(), 'B');

        reader.unconsume();
        reader.advance();

        reader.bufferUp();

        assertEquals(reader.pos(), 2);
        assertEquals(reader.isEmpty(), false);
        assertEquals(reader.current(), 'C');

        reader.consumeTo('C');

        assertEquals(reader.pos(), 4);
        assertEquals(reader.isEmpty(), false);
        assertEquals(reader.current(), 'e');

        reader.consumeTo('E');

        assertEquals(reader.pos(), 6);
        assertEquals(reader.isEmpty(), false);
        assertEquals(reader.current(), '1');

        reader.consumeToEnd();

        assertEquals(reader.pos(), 21);
        assertEquals(reader.isEmpty(), true);
        assertEquals(reader.current(), '\u0000');
    }

    @Test
    public void testCharacterReaderPos() {
        assertEquals(reader.pos(), 0);

        reader.advance();

        assertEquals(reader.pos(), 1);

        reader.advance();

        assertEquals(reader.pos(), 2);

        reader.consumeTo('C');

        assertEquals(reader.pos(), 4);

        reader.consumeToEnd();

        assertEquals(reader.pos(), 21);
    }

    @Test
    public void testCharacterReaderEmpty() {
        assertEquals(reader.isEmpty(), true);

        reader.advance();

        assertEquals(reader.isEmpty(), false);

        reader.consume();

        assertEquals(reader.isEmpty(), false);

        reader.consumeTo('C');

        assertEquals(reader.isEmpty(), false);

        reader.consumeToEnd();

        assertEquals(reader.isEmpty(), true);
    }

    @Test
    public void testCharacterReaderCurrent() {
        assertEquals(reader.current(), 'A');

        reader.consume();

        assertEquals(reader.current(), 'B');

        reader.consumeTo('C');

        assertEquals(reader.current(), 'e');

        reader.consumeToEnd();

        assertEquals(reader.current(), '\u0000');
    }

    @Test
    public void testCharacterReaderConsume() {
        reader.consume();

        assertEquals(reader.current(), 'B');

        reader.consume();

        assertEquals(reader.current(), 'C');

        reader.consumeTo('C');

        assertEquals(reader.current(), 'e');

        reader.consumeToEnd();

        assertEquals(reader.current(), '\u0000');
    }

    @Test
    public void testCharacterReaderUnconsume() {
        reader.consume();

        reader.unconsume();

        assertEquals(reader.current(), 'A');

        reader.consumeTo('C');

        reader.unconsume();

        reader.advance();

        assertEquals(reader.current(), 'e');
    }

    @Test
    public void testCharacterReaderAdvance() {
        reader.advance();

        assertEquals(reader.current(), 'B');

        reader.advance();

        assertEquals(reader.current(), 'C');

        reader.consumeTo('C');

        reader.advance();

        assertEquals(reader.current(), 'e');

        reader.consumeToEnd();

        assertEquals(reader.current(), '\u0000');
    }

    @Test
    public void testCharacterReaderMark() {
        reader.mark();

        reader.advance();
        reader.advance();

        reader.rewindToMark();

        assertEquals(reader.pos(), 2);
        assertEquals(reader.current(), 'C');
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        assertEquals(reader.nextIndexOf('C'), 3);
        assertEquals(reader.nextIndexOf('z'), -1);
        assertEquals(reader.nextIndexOf("AB"), 1);
        assertEquals(reader.nextIndexOf("ABC"), 3);
        assertEquals(reader.nextIndexOf("ABCD"), 4);
        assertEquals(reader.nextIndexOf("ABCD", 5), 8);
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        String consumed = reader.consumeTo('C');

        assertEquals(consumed, "Be");
        assertEquals(reader.current(), 'e');

        consumed = reader.consumeToEnd();

        assertEquals(consumed, "ABCde123! @#DEF<GHI>456& JKLmNOP");
        assertEquals(reader.current(), '\u0000');

        consumed = reader.consumeTo('E');

        assertEquals(consumed, "def<GHI>456& JKLmNOP");
        assertEquals(reader.current(), 'J');

        consumed = reader.consumeToAny(new char[]{'J', 'K', 'L'});

        assertEquals(consumed, "JKLmNOP");
        assertEquals(reader.current(), 'm');

        String consumedCaseInsensitive = reader.consumeToAnySorted(new char[]{'j', 'k', 'l'});

        assertEquals(consumedCaseInsensitive, "jkm");
        assertEquals(reader.current(), 'n');

        consumed = reader.consumeData();

        assertEquals(consumed, "& JKLmNOP");
        assertEquals(reader.current(), 'N');

        consumed = reader.consumeTagName();

        assertEquals(consumed, "<GHI>");
        assertEquals(reader.current(), '>');
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        String consumed = reader.consumeLetterSequence();

        assertEquals(consumed, "ABC");
        assertEquals(reader.current(), 'd');

        consumed = reader.consumeLetterThenDigitSequence();

        assertEquals(consumed, "DEF");
        assertEquals(reader.current(), '4');

        consumed = reader.consumeHexSequence();

        assertEquals(consumed, "123");
        assertEquals(reader.current(), '!');
    }

    @Test
    public void testCharacterReaderMatches() {
        assertTrue(reader.matches('A'));
        assertTrue(reader.matches('B'));
        assertTrue(reader.matches('C'));
        assertTrue(reader.matches('e'));
        assertTrue(reader.matches('1'));
        assertTrue(reader.matches('!'));
        assertTrue(reader.matches("AB"));
        assertTrue(reader.matches("ABC"));
        assertTrue(reader.matches("ABCD"));
        assertTrue(reader.matches("ABCD", 5));
        assertTrue(reader.matchesAny('C'));
        assertTrue(reader.matchesAny('e'));
        assertTrue(reader.matchesAnySorted(new char[]{'C', 'e'}));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchConsume("AB"));
        assertTrue(reader.matchConsumeIgnoreCase("ab"));
        assertTrue(reader.containsIgnoreCase("</title>"));
        assertTrue(reader.containsIgnoreCase("</style>"));
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader(""), 0);
        reader.bufferUp();
        reader.skip(100);
    }
}