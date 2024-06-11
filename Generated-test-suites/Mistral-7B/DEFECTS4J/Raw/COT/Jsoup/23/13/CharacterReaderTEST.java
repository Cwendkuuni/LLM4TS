import org.junit.Test;
import org.junit.Assert.*;
import static org.jsoup.parser.CharacterReader.*;

public class CharacterReaderTest {

    @Test
    public void testConstructor() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("Hello World", reader.input);
        assertEquals(13, reader.length);
    }

    @Test
    public void testPos() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        String input = "";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.isEmpty());

        input = "Hello World";
        reader = new CharacterReader(input);
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        assertEquals('H', reader.current());
    }

    @Test
    public void testConsume() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());

        reader.consumeTo('l');
        assertEquals("ello ", reader.input.substring(reader.pos));
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testConsumePastEnd() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
    }

    @Test
    public void testUnconsume() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        reader.consume();
        assertEquals('e', reader.current());
        reader.unconsume();
        assertEquals('H', reader.current());
    }

    @Test
    public void testAdvance() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        reader.advance();
        reader.advance();
        assertEquals('l', reader.current());
    }

    @Test
    public void testMark() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        assertEquals("He", reader.input.substring(reader.pos));
    }

    @Test
    public void testConsumeAsString() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        reader.consume();
        reader.consume();
        String consumed = reader.consumeAsString();
        assertEquals("llo ", consumed);
    }

    @Test
    public void testConsumeTo() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeTo('l');
        assertEquals("ello ", reader.input.substring(reader.pos));
        assertEquals("ll", consumed);
    }

    @Test
    public void testConsumeToEnd() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeToEnd();
        assertEquals("World", consumed);
    }

    @Test
    public void testConsumeLetterSequence() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        String sequence = reader.consumeLetterSequence();
        assertEquals("Hello", sequence);
    }

    @Test
    public void testConsumeHexSequence() {
        String input = "0x1a";
        CharacterReader reader = new CharacterReader(input);
        String sequence = reader.consumeHexSequence();
        assertEquals("1a", sequence);
    }

    @Test
    public void testConsumeDigitSequence() {
        String input = "123";
        CharacterReader reader = new CharacterReader(input);
        String sequence = reader.consumeDigitSequence();
        assertEquals("123", sequence);
    }

    @Test
    public void testMatches() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matches('H'));
        assertTrue(reader.matches("He"));
        assertFalse(reader.matches('!'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesIgnoreCase("he"));
        assertTrue(reader.matchesIgnoreCase("Hello"));
        assertFalse(reader.matchesIgnoreCase("!"));
    }

    @Test
    public void testMatchesAny() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        char[] sequence = new char[]{'H', 'e'};
        assertTrue(reader.matchesAny(sequence));
        char[] sequence2 = new char[]{'!', '!'};
        assertFalse(reader.matchesAny(sequence2));
    }

    @Test
    public void testMatchesLetter() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesDigit());
        reader.consume();
        reader.consume();
        reader.consume();
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        String sequence = "He";
        assertTrue(reader.matchConsume(sequence));
        assertEquals("llo World", reader.input.substring(reader.pos));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        String sequence = "He";
        assertTrue(reader.matchConsumeIgnoreCase(sequence));
        assertEquals("llo World", reader.input.substring(reader.pos));
    }

    @Test
    public void testContainsIgnoreCase() {
        String input = "<html><head><title>Hello World</title></head><body>";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("world"));
        assertFalse(reader.containsIgnoreCase("foo"));
    }

}