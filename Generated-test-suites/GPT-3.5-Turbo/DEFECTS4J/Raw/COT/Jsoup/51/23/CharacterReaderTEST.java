import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void posTest() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(0, reader.pos());
    }

    @Test
    public void isEmptyTest() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void currentTest() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.current());
    }

    @Test
    public void consumeTest() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
    }

    @Test
    public void unconsumeTest() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.unconsume();
        assertEquals('t', reader.current());
    }

    @Test
    public void advanceTest() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void markTest() {
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void rewindToMarkTest() {
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('t', reader.current());
    }

    @Test
    public void consumeAsStringTest() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeAsString());
    }

    @Test
    public void nextIndexOfCharTest() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(1, reader.nextIndexOf('e'));
    }

    @Test
    public void nextIndexOfStringTest() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(1, reader.nextIndexOf("es"));
    }

    @Test
    public void consumeToCharTest() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeTo('e'));
    }

    @Test
    public void consumeToStringTest() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeTo("es"));
    }

    @Test
    public void consumeToAnyTest() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeToAny('x', 'y', 'z'));
    }

    @Test
    public void consumeToAnySortedTest() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("es", reader.consumeToAnySorted('e', 's', 't'));
    }

    @Test
    public void consumeDataTest() {
        CharacterReader reader = new CharacterReader("test&<");
        assertEquals("test", reader.consumeData());
    }

    @Test
    public void consumeTagNameTest() {
        CharacterReader reader = new CharacterReader("test\t\n\r\f /");
        assertEquals("test", reader.consumeTagName());
    }

    @Test
    public void consumeToEndTest() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeToEnd());
    }

    @Test
    public void consumeLetterSequenceTest() {
        CharacterReader reader = new CharacterReader("test123");
        assertEquals("test", reader.consumeLetterSequence());
    }

    @Test
    public void consumeLetterThenDigitSequenceTest() {
        CharacterReader reader = new CharacterReader("test123");
        assertEquals("test123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void consumeHexSequenceTest() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc123", reader.consumeHexSequence());
    }

    @Test
    public void consumeDigitSequenceTest() {
        CharacterReader reader = new CharacterReader("123abc");
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void matchesTest() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches('t'));
    }

    @Test
    public void matchesAnyTest() {
         CharacterReader reader = new CharacterReader("test");
         assertTrue(reader.matchesAny('a', 'b', 't'));
    }

    @Test
    public void matchesAnySortedTest() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesAnySorted('a', 'b', 'e', 's', 't'));
    }

    @Test
    public void matchesLetterTest() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void matchesDigitTest() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void matchConsumeTest() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsume("test"));
    }

    @Test
    public void matchConsumeIgnoreCaseTest() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.matchConsumeIgnoreCase("test"));
    }

    @Test
    public void containsIgnoreCaseTest() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.containsIgnoreCase("ES"));
    }

    @Override
    public String toString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.toString());
    }

    @Test
    public void rangeEqualsTest() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.rangeEquals(0, 4, "test"));
    }
}