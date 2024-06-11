import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.nextIndexOf('a'));
        assertEquals(-1, reader.nextIndexOf("ab"));
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("a");
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals(-1, reader.nextIndexOf('b'));
        assertEquals("a", reader.consumeAsString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals('c', reader.current());
        assertEquals('c', reader.consume());
        assertEquals(3, reader.pos());
        assertEquals('d', reader.current());
        assertEquals('d', reader.consume());
        assertEquals(4, reader.pos());
        assertEquals(-1, reader.nextIndexOf('e'));
        assertEquals("abcd", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderScanForChar() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('b', reader.current());
        int index = reader.nextIndexOf('d');
        assertEquals(5, index);
        assertEquals('d', reader.consume());
        assertEquals(6, reader.pos());
        assertEquals(-1, reader.nextIndexOf('e'));
        assertEquals("abcd", reader.consumeTo('d'));
        assertEquals("cd", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderScanForString() {
        CharacterReader reader = new CharacterReader("abcdabcdabcdabcd");
        assertEquals(0, reader.pos());
        assertEquals('a', reader.current());
        assertEquals("abcd", reader.consumeTo("cd"));
        assertEquals(5, reader.pos());
        assertEquals("abcdabcd", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderScanForSequence() {
        CharacterReader reader = new CharacterReader("12345678901234567890");
        assertEquals(0, reader.pos());
        assertEquals('1', reader.current());
        assertEquals('1', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals('2', reader.current());
        assertEquals("23", reader.consumeToDigitSequence());
        assertEquals(3, reader.pos());
        assertEquals('5', reader.current());
        assertEquals('5', reader.consume());
        assertEquals(4, reader.pos());
        assertEquals("67890", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderMarkPosition() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        assertEquals(0, reader.pos());
        reader.advance();
        assertEquals(1, reader.pos());
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test(expected = IllegalStateException.class)
    public void testCharacterReaderNegativeIndex() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.consumeTo(-1);
    }
}