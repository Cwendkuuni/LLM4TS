package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(true, reader.isEmpty());
        reader.consume();
        assertEquals(true, reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        reader.mark();
        reader.advance();
        assertEquals('c', reader.current());
        reader.rewindToMark();
        assertEquals('b', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeAsString());
        assertEquals("b", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("ab", reader.consumeTo('c'));
        reader.consume();
        assertEquals("c", reader.consumeTo('d'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeToAny('b', 'c'));
        assertEquals("", reader.consumeToAny('b', 'c'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("ab123");
        assertEquals("ab", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("123abc");
        assertEquals("123abc", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(true, reader.matches('a'));
        reader.consume();
        assertEquals(false, reader.matches('a'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(true, reader.matchesIgnoreCase("AB"));
        reader.consume();
        assertEquals(false, reader.matchesIgnoreCase("AB"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(true, reader.matchesAny('a', 'c'));
        reader.consume();
        assertEquals(false, reader.matchesAny('a', 'c'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(true, reader.matchesLetter());
        reader.consume();
        assertEquals(false, reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abc1");
        assertEquals(true, reader.matchesDigit());
        reader.consume();
        assertEquals(false, reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(true, reader.matchConsume("ab"));
        assertEquals(false, reader.matchConsume("ab"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(true, reader.matchConsumeIgnoreCase("AB"));
        assertEquals(false, reader.matchConsumeIgnoreCase("AB"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(false, reader.containsIgnoreCase("def"));
        assertEquals(true, reader.containsIgnoreCase("ab"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.toString());
        reader.consume();
        assertEquals("bc", reader.toString());
    }
}