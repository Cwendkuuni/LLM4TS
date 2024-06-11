import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testConstructor() {
        CharacterReader reader = new CharacterReader("test string");
        assertEquals("test string", reader.input);
        assertEquals(12, reader.length);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("test string");
        Assert.assertFalse(reader.isEmpty());
        reader.consume();
        reader.consume();
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("test string");
        assertEquals('t', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
        reader.consume();
        assertNull(reader.current());
    }

    @Test(expected = IllegalStateException.class)
    public void testConsumePastEnd() {
        CharacterReader reader = new CharacterReader("test string");
        reader.consumeToEnd();
        reader.consume();
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test string");
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals(EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test string");
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('t', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test string");
        reader.consume();
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("test string");
        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        assertEquals('t', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test string");
        assertEquals("st", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("test string tesT");
        assertEquals("esT", reader.consumeTo('T'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test string");
        assertEquals("test string", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test123string");
        assertEquals("123", reader.consumeToAny('1', '2', '3'));
    }

    @Test
    public void testMatch() {
        CharacterReader reader = new CharacterReader("test string");
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('s'));
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("test string");
        assertEquals("test", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("test#1f2x0A");
        assertEquals("1f", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("test12345");
        assertEquals("12345", reader.consumeDigitSequence());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test <title>My Title</title>");
        assertTrue(reader.containsIgnoreCase("title"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test string");
        assertEquals("string", reader.toString());
    }
}