import org.junit.Test;
import org.junit.Assert.*;
import static org.jsoup.parser.CharacterReader.*; // for convenience

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        String input = "Hello World!";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("Hello World!", reader.input);
        assertEquals(11, reader.length);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
        reader.consume(); // EOF
        assertEquals(EOF, reader.current());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testConsumeBeyondEnd() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume(); // EOF, should not throw an exception
        reader.consume(); // should throw an exception
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume(); // a
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance(); // b
        assertEquals('b', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.consume(); // a
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertEquals("abc", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdefabc");
        assertEquals("def", reader.consumeTo('f'));
        assertEquals("abc", reader.consumeTo('a'));
        assertEquals("abc", reader.consumeTo('c'));
        assertEquals("", reader.consumeTo('z'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("012345abcxyz");
        assertEquals("5", reader.consumeToAny('5'));
        assertEquals("xyz", reader.consumeToAny('x', 'y', 'z'));
        assertEquals("", reader.consumeToAny('a'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("def", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("1a2b3c");
        assertEquals("a", reader.consumeLetterSequence().charAt(0));
        assertEquals("b", reader.consumeLetterSequence().charAt(1));
        assertEquals("c", reader.consumeLetterSequence().charAt(2));
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("1234abcd5e6");
        assertEquals("23", reader.consumeHexSequence().substring(0, 2));
        assertEquals("ab", reader.consumeHexSequence().substring(4, 6));
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345");
        assertEquals("1", reader.consumeDigitSequence().charAt(0));
        assertEquals("2", reader.consumeDigitSequence().charAt(1));
        assertEquals("3", reader.consumeDigitSequence().charAt(2));
        assertEquals("4", reader.consumeDigitSequence().charAt(3));
        assertEquals("5", reader.consumeDigitSequence().charAt(4));
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("a"));
        assertFalse(reader.matches('b'));
        assertFalse(reader.matches("b"));
        assertFalse(reader.matches(EOF));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("A"));
        assertTrue(reader.matchesIgnoreCase("C"));
        assertFalse(reader.matchesIgnoreCase("b"));
        assertFalse(reader.matchesIgnoreCase("e"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAny('a', 'b', 'c'));
        assertTrue(reader.matchesAny("abc"));
        assertFalse(reader.matchesAny('d'));
        assertFalse(reader.matchesAny("def"));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcabc");
        assertEquals("abc", reader.matchConsume("abc"));
        assertEquals("", reader.matchConsume("def"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCabc");
        assertEquals("abc", reader.matchConsumeIgnoreCase("ABC"));
        assertEquals("", reader.matchConsumeIgnoreCase("def"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("1<html><head><title>Test</title></head><body>");
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("head"));
        assertFalse(reader.containsIgnoreCase("body"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.toString());
    }
}