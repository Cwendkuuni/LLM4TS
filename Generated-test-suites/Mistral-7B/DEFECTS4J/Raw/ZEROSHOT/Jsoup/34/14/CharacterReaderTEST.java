import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        assertEquals(0, reader.pos());
        assertEquals('A', reader.current());
        reader.consume();
        assertEquals(1, reader.pos());
        assertEquals('A', reader.current());
        assertEquals("A", reader.toString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals(0, reader.pos());
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
        reader.consume();
        assertEquals('l', reader.current());
        reader.consume();
        assertEquals('l', reader.current());
        reader.consume();
        assertEquals('o', reader.current());
        reader.consume();
        assertEquals(' ', reader.current());
        reader.consume();
        assertEquals('W', reader.current());
        reader.consume();
        assertEquals('o', reader.current());
        reader.consume();
        assertEquals('r', reader.current());
        reader.consume();
        assertEquals('l', reader.current());
        reader.consume();
        assertEquals('d', reader.current());
        reader.consume();
        assertEquals('', reader.current());
        assertEquals(13, reader.pos());
        assertEquals("Hello World", reader.toString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.mark();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
        assertEquals(6, reader.pos());
        assertEquals("Hello", reader.toString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader("Hello World");
        int index = reader.nextIndexOf('l');
        assertEquals(5, index);
        index = reader.nextIndexOf('x');
        assertEquals(-1, index);
    }

    @Test
    public void testCharacterReaderConsumeAsString() {
        CharacterReader reader = new CharacterReader("Hello World");
        String str = reader.consumeAsString();
        assertEquals("Hello World", str);
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader("Hello World");
        String str = reader.consumeTo('l');
        assertEquals("Hello", str);
        str = reader.consumeTo('r');
        assertEquals("Hello World", str);
        str = reader.consumeTo('x');
        assertEquals("Hello World", str);
    }

    @Test
    public void testCharacterReaderConsumeToSequence() {
        CharacterReader reader = new CharacterReader("Hello World");
        String str = reader.consumeTo("ll");
        assertEquals("Hello World", str);
        str = reader.consumeTo("ll");
        assertEquals("llo World", str);
        str = reader.consumeTo("llo");
        assertEquals("llo World", str);
        str = reader.consumeTo("lloW");
        assertEquals("llo World", str);
        str = reader.consumeTo("lloWo");
        assertEquals("llo World", str);
        str = reader.consumeTo("lloWorld");
        assertEquals("lloWorld", str);
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("Hello World");
        String str = reader.consumeToAny('l', 'o');
        assertEquals("Hello", str);
        str = reader.consumeToAny('l', 'x');
        assertEquals("Hello World", str);
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Hello World");
        String str = reader.consumeToEnd();
        assertEquals("Hello World", str);
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matches('l'));
        assertTrue(reader.matches("ll"));
        assertFalse(reader.matches('x'));
        assertFalse(reader.matches("xx"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchesIgnoreCase("ll"));
        assertTrue(reader.matchesIgnoreCase("Ll"));
        assertFalse(reader.matchesIgnoreCase("XX"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchesAny('l', 'o'));
        assertFalse(reader.matchesAny('x', 'o'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('0'));
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertFalse(reader.matchesDigit());
        reader.advance();
        assertTrue(reader.matchesDigit());
        reader.advance();
        assertTrue(reader.matchesDigit());
        reader.advance();
        assertTrue(reader.matchesDigit());
        reader.advance();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        String str = reader.matchConsume("ll");
        assertEquals("llo World", str);

        reader.mark();
        str = reader.matchConsume("ll");
        assertEquals("llo World", str);
        reader.rewindToMark();
        assertEquals("Hello World", reader.toString());

        str = reader.matchConsumeIgnoreCase("LL");
        assertEquals("llo World", str);
        str = reader.matchConsumeIgnoreCase("Ll");
        assertEquals("llo World", str);
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.containsIgnoreCase("ll"));
        assertTrue(reader.containsIgnoreCase("Ll"));
        assertFalse(reader.containsIgnoreCase("XX"));
    }
}