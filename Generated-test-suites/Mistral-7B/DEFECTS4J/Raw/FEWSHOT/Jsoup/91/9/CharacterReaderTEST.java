package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setup() {
        reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
    }

    @Test
    public void testIsEmpty() {
        assertEquals(false, reader.isEmpty());
    }

    @Test
    public void testIsEmptyNoBufferUp() {
        assertEquals(false, reader.isEmptyNoBufferUp());
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(7, reader.nextIndexOf('a'));
        assertEquals(-1, reader.nextIndexOf('z'));
        assertEquals(-1, reader.nextIndexOf('x'));
        assertEquals(26, reader.nextIndexOf('A'));
        assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testNextIndexOfSequence() {
        assertEquals(0, reader.nextIndexOf("abc"));
        assertEquals(25, reader.nextIndexOf("ABC"));
        assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testConsume() {
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('f', reader.consume());
        assertEquals('g', reader.consume());
        assertEquals('h', reader.consume());
        assertEquals('i', reader.consume());
        assertEquals('j', reader.consume());
        assertEquals('k', reader.consume());
        assertEquals('l', reader.consume());
        assertEquals('m', reader.consume());
        assertEquals('n', reader.consume());
        assertEquals('o', reader.consume());
        assertEquals('p', reader.consume());
        assertEquals('q', reader.consume());
        assertEquals('r', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals('u', reader.consume());
        assertEquals('v', reader.consume());
        assertEquals('w', reader.consume());
        assertEquals('x', reader.consume());
        assertEquals('y', reader.consume());
        assertEquals('z', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("abc", reader.consumeTo('c'));
        assertEquals("defghijklmnopqrstuvwxyz", reader.consumeTo(Character.MAX_VALUE));
        assertEquals("", reader.consumeTo('z'));
    }

    @Test
    public void testConsumeToSequence() {
        assertEquals("abc", reader.consumeTo("abc"));
        assertEquals("ABC", reader.consumeTo("ABC"));
        assertEquals("", reader.consumeTo("xyz"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("abcdef", reader.consumeToAny(new char[]{'a', 'b', 'c', 'd', 'e'}));
        assertEquals("qwertyuiopasdfghjklzxcvbnm", reader.consumeToAny(new char[]{'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n'}));
        assertEquals("", reader.consumeToAny(new char[]{'z'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals("abcdef", reader.consumeToAnySorted(new char[]{'a', 'b', 'c', 'd', 'e'}));
        assertEquals("qwertyuiopasdfghjklzxcvbnm", reader.consumeToAnySorted(new char[]{'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n'}));
        assertEquals("", reader.consumeToAnySorted(new char[]{'z'}));
    }

    @Test
    public void testConsumeData() {
        assertEquals("&<null", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("abcdefghijklmnopqrstuvwxyz", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("abc1def2ghi3jkl4mno5pqr6stu7uvw8xyz9", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("abcdef", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("1234567890", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertEquals(true, reader.matches('a'));
        assertEquals(true, reader.matches("abc"));
        assertEquals(false, reader.matches('z'));
        assertEquals(false, reader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertEquals(true, reader.matchesIgnoreCase("abc"));
        assertEquals(true, reader.matchesIgnoreCase("ABC"));
        assertEquals(false, reader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testMatchesAny() {
        assertEquals(true, reader.matchesAny('a'));
        assertEquals(true, reader.matchesAny('b'));
        assertEquals(true, reader.matchesAny('c'));
        assertEquals(false, reader.matchesAny('d'));
        assertEquals(false, reader.matchesAny('z'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertEquals(true, reader.matchesAnySorted('a'));
        assertEquals(true, reader.matchesAnySorted('b'));
        assertEquals(true, reader.matchesAnySorted('c'));
        assertEquals(false, reader.matchesAnySorted('d'));
        assertEquals(false, reader.matchesAnySorted('z'));
    }

    @Test
    public void testMatchesLetter() {
        assertEquals(true, reader.matchesLetter());
        assertEquals(false, reader.matchesLetter('5'));
    }

    @Test
    public void testMatchesDigit() {
        assertEquals(false, reader.matchesDigit());
        assertEquals(true, reader.matchesDigit('5'));
    }

    @Test
    public void testMatchConsume() {
        reader.mark();
        assertEquals("abc", reader.consumeTo('c'));
        assertEquals(true, reader.matchConsume("abc"));
        reader.rewindToMark();
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        reader.mark();
        assertEquals("abc", reader.consumeTo("abc"));
        assertEquals(true, reader.matchConsumeIgnoreCase("abc"));
        reader.rewindToMark();
    }

    @Test
    public void testContainsIgnoreCase() {
        assertEquals(true, reader.containsIgnoreCase("</title>"));
        assertEquals(true, reader.containsIgnoreCase("</style>"));
        assertEquals(false, reader.containsIgnoreCase("</script>"));
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        reader.bufferUp();
        reader.reader.close();
        reader.consume();
    }
}