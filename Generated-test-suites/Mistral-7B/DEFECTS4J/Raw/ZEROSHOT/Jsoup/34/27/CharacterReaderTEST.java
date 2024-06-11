import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.parser.CharacterReader.*;

public class CharacterReaderTest {

    @Test
    public void testEmptyReader() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testBasicReader() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(0, reader.pos());
        assertEquals(false, reader.isEmpty());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('b', reader.current());
        assertEquals("", reader.consumeAsString());
        assertEquals(1, reader.pos());
        assertEquals('b', reader.current());
        assertEquals("bc", reader.consumeTo('c'));
        assertEquals(3, reader.pos());
        assertEquals('d', reader.current());
        assertEquals("def", reader.consumeToEnd());
        assertEquals(7, reader.pos());
        assertEquals(EOF, reader.current());
        assertEquals(true, reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testScanChar() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertEquals(0, reader.pos());
        assertEquals(false, reader.isEmpty());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals('b', reader.current());
        assertEquals(1, reader.nextIndexOf('a'));
        assertEquals(2, reader.nextIndexOf('b'));
        assertEquals(1, reader.nextIndexOf('c'));
        assertEquals(-1, reader.nextIndexOf('d'));
    }

    @Test
    public void testScanString() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertEquals(0, reader.pos());
        assertEquals(false, reader.isEmpty());
        assertEquals(true, reader.matches("a"));
        assertEquals(true, reader.matches("bc"));
        assertEquals(true, reader.matches("ab"));
        assertEquals(false, reader.matches("def"));
        assertEquals(true, reader.matchesIgnoreCase("AB"));
        assertEquals(true, reader.matchesIgnoreCase("bc"));
        assertEquals(true, reader.matchesAny('a', 'b'));
        assertEquals(true, reader.matchesLetter());
        assertEquals(true, reader.matchesDigit());
        assertEquals("ab", reader.consumeLetterSequence());
        assertEquals("ab12", reader.consumeLetterThenDigitSequence());
        assertEquals("0x12", reader.consumeHexSequence());
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("abc", reader.matchConsume("a"));
        assertEquals("def", reader.matchConsume("ef"));
        assertEquals(true, reader.containsIgnoreCase("ABC"));
    }
}