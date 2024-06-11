package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        CharacterReader reader = new CharacterReader("Test String");
        assertEquals("Test String", reader.toString());
        assertNotNull(reader.input);
        assertEquals(11, reader.length);
        assertEquals(0, reader.pos);
        assertEquals(0, reader.mark);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("Test String");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("TestString");
        assertEquals('T', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("TestString");
        reader.consume();
        assertEquals('e', reader.current());
        reader.consume();
        assertEquals('s', reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("TestString");
        reader.consume();
        reader.unconsume();
        assertEquals('T', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("TestString");
        reader.advance();
        reader.advance();
        assertEquals('s', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("TestString");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("TestString");
        assertEquals("Test", reader.consumeAsString());
        assertEquals("String", reader.consumeAsString());
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("TestString");
        assertEquals(1, reader.nextIndexOf('e'));
        assertEquals(-1, reader.nextIndexOf('z'));

        reader.consume();
        assertEquals(2, reader.nextIndexOf('e'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIndexOfWithNullSeq() {
        new CharacterReader("TestString").nextIndexOf(null);
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("TestString");
        assertEquals("Test", reader.consumeTo(' '));
        assertEquals("String", reader.consumeTo(' '));
        assertEquals("", reader.consumeTo(' '));
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testConsumeToSequence() {
        CharacterReader reader = new CharacterReader("TestString");
        assertEquals("Test", reader.consumeTo("st"));
        assertEquals("String", reader.consumeTo("ing"));
        assertEquals("", reader.consumeTo("x"));
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("TestString");
        assertEquals("Test", reader.consumeToAny('t', 's'));
        assertEquals("String", reader.consumeToAny('i', 'n', 'g'));
        assertEquals("", reader.consumeToAny('x'));
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("TestString");
        assertEquals("Test", reader.consumeToAnySorted('t', 's'));
        assertEquals("String", reader.consumeToAnySorted('i', 'n', 'g'));
        assertEquals("", reader.consumeToAnySorted('x'));
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("Test&String");
        assertEquals("Test&", reader.consumeData());
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("<\tTest\rString>");
        assertEquals("<Test", reader.consumeTagName());
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("TestLetterSequence");
        assertEquals("TestLetterSequence", reader.consumeLetterSequence());
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("TestLetter12345Sequence");
        assertEquals("TestLetter", reader.consumeLetterThenDigitSequence());
        assertEquals("5Sequence", reader.consumeLetterThenDigitSequence());
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("Test#x123456");
        assertEquals("Test#x1234", reader.consumeHexSequence());
        assertEquals("56", reader.consumeHexSequence());
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("Test123456");
        assertEquals("Test12345", reader.consumeDigitSequence());
        assertEquals("6", reader.consumeDigitSequence());
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("TestString");
        assertTrue(reader.matches('e'));
        assertTrue(reader.matches("est"));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesSequence() {
        CharacterReader reader = new CharacterReader("TestString");
        assertTrue(reader.matches("est"));
        assertFalse(reader.matches("tes"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("TestString");
        assertTrue(reader.matchesAny('e', 's'));
        assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("TestString");
        assertTrue(reader.matchesAnySorted('e', 's'));
        assertFalse(reader.matchesAnySorted('z'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("TestString");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('5'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("TestString");
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit('5'));
    }

    @Test
    public void testMatchConsumeSequence() {
        CharacterReader reader = new CharacterReader("TestString");
        assertTrue(reader.matchConsume("est"));
    }

    @Test
    public void testMatchConsumeSequenceIgnoreCase() {
        CharacterReader reader = new CharacterReader("TestString");
        assertTrue(reader.matchConsumeIgnoreCase("Est"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test<title>String<title>");
        assertTrue(reader.containsIgnoreCase("/title/"));
        assertFalse(reader.containsIgnoreCase("/titleX/"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("TestString");
        assertEquals("TestString", reader.toString());
    }
}