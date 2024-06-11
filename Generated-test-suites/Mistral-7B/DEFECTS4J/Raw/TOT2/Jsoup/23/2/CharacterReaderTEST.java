
import static org.junit.Assert.*;

import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
        reader.consume();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.isEmpty());
        for (int i = 0; i < reader.length(); i++) {
            reader.consume();
        }
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.consume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.consume();
        assertEquals('a', reader.current());
        assertEquals(1, reader.pos());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
        assertEquals(0, reader.pos());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.advance();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
        assertEquals(0, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcabc");
        String expected = reader.consumeAsString(3, 5);
        assertEquals("cab", expected);
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcabcdef");
        String expected = reader.consumeTo('f');
        assertEquals("abcdef", expected);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abc123abc");
        String expected = reader.consumeToAny('1', '2');
        assertEquals("1", expected);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        String expected = reader.consumeToEnd();
        assertEquals("abcdef", expected);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        assertTrue(reader.matches('d'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("AbCdAbCd");
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertFalse(reader.matchesIgnoreCase("ef"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        assertTrue(reader.matchesAny('a', 'b', 'c', 'd'));
        assertFalse(reader.matchesAny('e', 'f'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('0'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("1234567890");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        assertTrue(reader.matchConsume("ab"));
        assertFalse(reader.matchConsume("ef"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("AbCdAbCd");
        assertTrue(reader.matchConsumeIgnoreCase("Ab"));
        assertFalse(reader.matchConsumeIgnoreCase("ef"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc<title>def<style>ghi");
        assertTrue(reader.containsIgnoreCase("<title>"));
        assertTrue(reader.containsIgnoreCase("<style>"));
        assertFalse(reader.containsIgnoreCase("<body>"));
    }
}
