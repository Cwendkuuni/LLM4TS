import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals('H', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals('H', reader.consume());
        assertEquals('e', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals('H', reader.consume());
        reader.unconsume();
        assertEquals('H', reader.consume());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals(2, reader.nextIndexOf('l'));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Hello, World");
        assertEquals("Hello", reader.consumeTo(','));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("Hello, World");
        assertEquals("Hello", reader.consumeData());
    }

    // Additional test cases for other methods should be added here

}