import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader1 = new CharacterReader("");
        CharacterReader reader2 = new CharacterReader("xyz");
        assertTrue(reader1.isEmpty());
        assertFalse(reader2.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader1 = new CharacterReader("hello");
        CharacterReader reader2 = new CharacterReader("");
        assertEquals('h', reader1.current());
        assertEquals(CharacterReader.EOF, reader2.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("xyz");
        assertEquals('x', reader.consume());
        reader.unconsume();
        assertEquals('x', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("html");
        assertEquals('h', reader.current());
        reader.advance();
        assertEquals('t', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.advance();
        reader.mark();
        assertEquals(1, reader.pos());
        reader.advance();
        assertEquals(2, reader.pos());
        reader.rewindToMark();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testNextIndexOfChar() {
        CharacterReader reader = new CharacterReader("abcabcabc");
        assertEquals(3, reader.nextIndexOf('a'));
        assertEquals(2, reader.nextIndexOf('b'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfSeq() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals(6, reader.nextIndexOf("world"));
        assertEquals(-1, reader.nextIndexOf("foo"));
    }

    @Test
    public void testConsumeToChar() {
        CharacterReader reader = new CharacterReader("Hello, World!");
        assertEquals("Hello, ", reader.consumeTo(','));
        assertEquals("World!", reader.consumeTo('#'));
    }

    @Test
    public void testConsumeToSeq() {
        CharacterReader reader = new CharacterReader("The quick brown fox jumps over the lazy dog.");
        assertEquals("The ", reader.consumeTo("quick"));
        assertEquals("quick brown fox jumps over ", reader.consumeTo("the"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        assertEquals("abcdefghi", reader.consumeToAny('j', 'k', 'l'));
        assertEquals("jklmnopqrstuvwxyz", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("final test");
        assertEquals("final test", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc", reader.consumeLetterSequence());
        assertEquals("123", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("alpha123");
        assertEquals("alpha", reader.consumeLetterThenDigitSequence());
        assertEquals("123", reader.consumeToEnd());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("G0ODC0FFEE");
        assertEquals("G0ODC0FFEE", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("987xyz");
        assertEquals("987", reader.consumeDigitSequence());
        assertEquals("xyz", reader.consumeToEnd());
    }

    @Test
    public void testMatchesChar() {
        CharacterReader reader1 = new CharacterReader("a");
        CharacterReader reader2 = new CharacterReader("xyz");
        assertTrue(reader1.matches('a'));
        assertFalse(reader2.matches('a'));
    }

    @Test
    public void testMatchesSeq() {
        CharacterReader reader1 = new CharacterReader("abc");
        CharacterReader reader2 = new CharacterReader("xyz");
        assertTrue(reader1.matches("ab"));
        assertFalse(reader1.matches("bc"));
        assertFalse(reader2.matches("ab"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader1 = new CharacterReader("Hello");
        CharacterReader reader2 = new CharacterReader("hello");
        assertTrue(reader1.matchesIgnoreCase("heLLo"));
        assertFalse(reader2.matchesIgnoreCase("heLLo"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesAny('a', 'e', 'i'));
        assertFalse(reader.matchesAny('g', 'h', 'j'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader1 = new CharacterReader("A");
        CharacterReader reader2 = new CharacterReader("1xy");
        assertTrue(reader1.matchesLetter());
        assertFalse(reader2.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader1 = new CharacterReader("7");
        CharacterReader reader2 = new CharacterReader("xyz");
        assertTrue(reader1.matchesDigit());
        assertFalse(reader2.matchesDigit());
    }

    @Test
    public void testMatchConsumeSeq() {
        CharacterReader reader = new CharacterReader("java");
        assertFalse(reader.matchConsume("JavaScript"));
        assertTrue(reader.matchConsume("java"));
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCaseSeq() {
        CharacterReader reader = new CharacterReader("Java");
        assertFalse(reader.matchConsumeIgnoreCase("JavaScript"));
        assertTrue(reader.matchConsumeIgnoreCase("java"));
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("The quick brown fox jumps over the lazy Dog.");
        assertTrue(reader.containsIgnoreCase("quick"));
        assertFalse(reader.containsIgnoreCase("foo"));
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Test String");
        assertEquals("Test String", reader.toString());
    }
}