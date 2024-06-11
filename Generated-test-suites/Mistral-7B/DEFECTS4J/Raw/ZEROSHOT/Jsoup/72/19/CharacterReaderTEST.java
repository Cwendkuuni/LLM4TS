package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

    private CharacterReader reader;
    private final String input = "abcd1234efgh<i>ijk</i>lmnopqrst";

    @Before
    public void setup() {
        reader = new CharacterReader(new StringReader(input));
    }

    @After
    public void teardown() {
        reader.close();
    }

    @Test
    public void testNewInstance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("hello"));
        assertEquals("hello", characterReader.toString());
        characterReader.close();
    }

    @Test
    public void testNewInstanceWithMaxBufferLen() {
        CharacterReader characterReader = new CharacterReader(new StringReader("hello"), 5);
        assertEquals("hello", characterReader.toString());
        characterReader.close();
    }

    @Test
    public void testNewInstanceWithMaxBufferLenAndSize() {
        CharacterReader characterReader = new CharacterReader(new StringReader("hello"), 5, 10);
        assertEquals("hello", characterReader.toString());
        characterReader.close();
    }

    @Test
    public void testNewInstanceWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("hello"));
        CharacterReader characterReaderString = new CharacterReader("world");
        assertEquals("hello", characterReader.toString());
        assertEquals("world", characterReaderString.toString());
        characterReader.close();
        characterReaderString.close();
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        reader.advance();
        reader.advance();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(reader.isEmpty());
        reader.consume();
        assertFalse(reader.isEmpty());
        reader.consumeTo('d');
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
    }

    @Test
    public void testConsume() {
        reader.consume();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        reader.advance();
        assertEquals('c', reader.current());
    }

    @Test
    public void testMark() {
        reader.mark();
        reader.consumeTo('d');
        reader.rewindToMark();
        reader.consume();
        reader.consume();
        assertEquals('d', reader.current());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMarkUnsupported() {
        reader.mark();
        reader.mark();
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(4, reader.nextIndexOf('d'));
        assertEquals(-1, reader.nextIndexOf('z'));
        reader.consumeTo('e');
        assertEquals(-1, reader.nextIndexOf('d'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        assertEquals(5, reader.nextIndexOf("ef"));
        assertEquals(-1, reader.nextIndexOf("xyz"));
        reader.consumeTo('i');
        assertEquals(-1, reader.nextIndexOf("ef"));
    }

    @Test
    public void testConsumeTo() {
        String result = reader.consumeTo('d');
        assertEquals("abcd", result);
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeToString() {
        String result = reader.consumeTo("ef");
        assertEquals("abcdef", result);
        assertEquals('h', reader.current());
    }

    @Test
    public void testConsumeToAny() {
        String result = reader.consumeToAny(',');
        assertEquals("abcd1234", result);
        assertEquals('e', reader.current());
        reader.consume();
        reader.consume();
        reader.consume();
        String result2 = reader.consumeToAny(new char[]{'i', 'j'});
        assertEquals("lmnopqrst", result2);
        assertEquals('i', reader.current());
    }

    @Test
    public void testConsumeToAnySorted() {
        String result = reader.consumeToAnySorted(new char[]{'h', 'e'});
        assertEquals("abcd1234ef", result);
        assertEquals('i', reader.current());
    }

    @Test
    public void testConsumeData() {
        String result = reader.consumeData();
        assertEquals("abcd1234efgh<i>ijk</i>lmnopqrst", result);
        assertEquals('i', reader.current());
    }

    @Test
    public void testConsumeTagName() {
        String result = reader.consumeTagName();
        assertEquals("/i", result);
        assertEquals('i', reader.current());
    }

    @Test
    public void testConsumeToEnd() {
        String result = reader.consumeToEnd();
        assertEquals("lmnopqrst", result);
        assertEquals('\u0000', reader.current());
    }

    @Test
    public void testConsumeLetterSequence() {
        String result = reader.consumeLetterSequence();
        assertEquals("abcd", result);
        assertEquals('1', reader.current());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String result = reader.consumeLetterThenDigitSequence();
        assertEquals("abcd1234", result);
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeHexSequence() {
        String result = reader.consumeHexSequence();
        assertEquals("1234", result);
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeDigitSequence() {
        String result = reader.consumeDigitSequence();
        assertEquals("1234", result);
        assertEquals('e', reader.current());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('e'));
        assertTrue(reader.matches("ef"));
        assertFalse(reader.matches('z'));
        assertFalse(reader.matches("xyz"));
        reader.consumeTo('h');
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("ef"));
        assertFalse(reader.matchesIgnoreCase("xyz"));
        reader.consumeTo('i');
        assertTrue(reader.matchesIgnoreCase("ef"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny(new char[]{'d', 'e'}));
        assertFalse(reader.matchesAny(new char[]{'z', 'a'}));
        reader.consumeTo('h');
        assertTrue(reader.matchesAny(new char[]{'i', 'j'}));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(reader.matchesAnySorted(new char[]{'d', 'e'}));
        assertFalse(reader.matchesAnySorted(new char[]{'z', 'a'}));
        reader.consumeTo('i');
        assertTrue(reader.matchesAnySorted(new char[]{'i', 'j'}));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter("1234"));
        reader.consumeTo('h');
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit("1234"));
        reader.consumeTo('h');
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("ef"));
        assertFalse(reader.matchConsume("xyz"));
        reader.consumeTo('i');
        assertTrue(reader.matchConsume("ef"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("ef"));
        assertFalse(reader.matchConsumeIgnoreCase("xyz"));
        reader.consumeTo('i');
        assertTrue(reader.matchConsumeIgnoreCase("ef"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("/i"));
        assertFalse(reader.containsIgnoreCase("/xyz"));
        reader.consumeTo('i');
        assertTrue(reader.containsIgnoreCase("/i"));
    }

    @Test
    public void testToString() {
        assertEquals(input, reader.toString());
    }
}