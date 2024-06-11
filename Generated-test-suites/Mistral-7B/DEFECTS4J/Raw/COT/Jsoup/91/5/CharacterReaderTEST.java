import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("Hello World", reader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("Hello World", reader.toString());
    }

    @Test
    public void testBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        assertEquals("Hello", reader.consumeTo(' '));
        assertEquals("World", reader.consumeToEnd());
    }

    @Test(expected = UncheckedIOException.class)
    public void testBufferUpWithIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello") {
            @Override
            public int read(char[] c, int offset, int length) throws IOException {
                throw new IOException();
            }
        });
        reader.bufferUp();
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertEquals(0, reader.pos());
        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals(3, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertTrue(reader.isEmpty());

        reader = new CharacterReader(new StringReader("Hello World"));
        assertFalse(reader.isEmpty());

        reader.consume();
        reader.consume();
        reader.consume();
        assertTrue(reader.isEmptyNoBufferUp());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.consume();
        reader.unconsume();
        assertEquals('H', reader.current());

        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.advance();
        assertEquals('e', reader.current());

        reader.consume();
        reader.consume();
        reader.advance();
        assertEquals('r', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        assertEquals('H', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testRewindToMarkWithInvalidMark() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.rewindToMark();
        reader.consume();
        reader.consume();
        reader.mark();
        reader.consume();
        reader.rewindToMark();
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertEquals(4, reader.nextIndexOf(' '));
        assertEquals(-1, reader.nextIndexOf('Z'));
    }

    @Test
    public void testNextIndexOfWithLongSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("ababababababababa"));
        assertEquals(1, reader.nextIndexOf("aba", 0));
        assertEquals(1, reader.nextIndexOf("aba", 1));
        assertEquals(1, reader.nextIndexOf("aba", 4));
        assertEquals(1, reader.nextIndexOf("aba", 5));
        assertEquals(1, reader.nextIndexOf("aba", 8));
        assertEquals(1, reader.nextIndexOf("aba", 9));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("Hell", reader.consumeTo('l'));
        assertEquals("ld", reader.consumeTo('d'));
        assertEquals("World", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToWithEmptySequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("", reader.consumeTo(''));
    }

    @Test
    public void testConsumeToWithNullSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("", reader.consumeTo(null));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("Hell", reader.consumeToAny(new char[]{'e', 'l'}));
        assertEquals("World", reader.consumeToAny(new char[]{'o', 'r'}));
        assertEquals("", reader.consumeToAny(new char[]{'\n'}));
        assertEquals("", reader.consumeToAny(new char[]{'z'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("Hell", reader.consumeToAnySorted(new char[]{'e', 'l'}));
        assertEquals("World", reader.consumeToAnySorted(new char[]{'o', 'r'}));
        assertEquals("", reader.consumeToAnySorted(new char[]{'\n'}));
        assertEquals("", reader.consumeToAnySorted(new char[]{'z'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        assertEquals("&<null", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<h1>Hello World</h1>"));
        assertEquals("<h1>", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("World", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertEquals("abcdefghijklmno", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def456ghi789"));
        assertEquals("abc123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("0x1a 0x2b 0x3c"));
        assertEquals("1a2b3c", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
        assertEquals("1234567890", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(reader.matches('h'));
        assertTrue(reader.matches('o'));
        assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(reader.matchesIgnoreCase("hello"));
        assertTrue(reader.matchesIgnoreCase("World"));
        assertFalse(reader.matchesIgnoreCase("X"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(reader.matchesAny(new char[]{'h', 'e'}));
        assertTrue(reader.matchesAny(new char[]{'w', 'r'}));
        assertFalse(reader.matchesAny(new char[]{'x'}));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(reader.matchesAnySorted(new char[]{'h', 'e'}));
        assertTrue(reader.matchesAnySorted(new char[]{'w', 'r'}));
        assertFalse(reader.matchesAnySorted(new char[]{'x'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(reader.matchConsume("hel"));
        assertTrue(reader.matchConsume("wor"));
        assertFalse(reader.matchConsume("x"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(reader.matchConsumeIgnoreCase("hello"));
        assertTrue(reader.matchConsumeIgnoreCase("World"));
        assertFalse(reader.matchConsumeIgnoreCase("X"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Hello World</title></head></html>"));
        assertTrue(reader.containsIgnoreCase("/title>"));
        assertTrue(reader.containsIgnoreCase("/head>"));
        assertFalse(reader.containsIgnoreCase("/body>"));
    }
}