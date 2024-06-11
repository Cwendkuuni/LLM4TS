import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        Reader reader = new StringReader("test");
        CharacterReader charReader = new CharacterReader(reader);

        assertEquals(0, charReader.pos());
    }

    @Test
    public void testIsEmpty() {
        Reader reader = new StringReader("");
        CharacterReader charReader = new CharacterReader(reader);

        assertTrue(charReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        Reader reader = new StringReader("test");
        CharacterReader charReader = new CharacterReader(reader);

        assertEquals('t', charReader.current());
    }

    @Test
    public void testConsume() {
        Reader reader = new StringReader("test");
        CharacterReader charReader = new CharacterReader(reader);

        assertEquals('t', charReader.consume());
        assertEquals('e', charReader.consume());
    }

    @Test
    public void testUnconsume() {
        Reader reader = new StringReader("test");
        CharacterReader charReader = new CharacterReader(reader);

        charReader.consume();
        charReader.unconsume();

        assertEquals('t', charReader.current());
    }

    @Test
    public void testAdvance() {
        Reader reader = new StringReader("test");
        CharacterReader charReader = new CharacterReader(reader);

        charReader.advance();

        assertEquals('e', charReader.current());
    }

    @Test
    public void testMark() {
        Reader reader = new StringReader("test");
        CharacterReader charReader = new CharacterReader(reader);

        charReader.mark();

        assertEquals('t', charReader.current());
    }

    @Test
    public void testRewindToMark() {
        Reader reader = new StringReader("test");
        CharacterReader charReader = new CharacterReader(reader);

        charReader.mark();
        charReader.consume();
        charReader.rewindToMark();

        assertEquals('t', charReader.current());
    }

    // Add more test cases for the remaining methods in the CharacterReader class

}