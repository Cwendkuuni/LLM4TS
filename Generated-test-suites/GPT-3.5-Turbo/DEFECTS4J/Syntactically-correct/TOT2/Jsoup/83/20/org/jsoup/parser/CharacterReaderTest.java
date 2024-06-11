import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Test string");
        assertEquals(0, reader.pos());
        reader.advance();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        reader = new CharacterReader("Non-empty string");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Test string");
        assertEquals('T', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    // Additional test cases for other public methods in CharacterReader class

    // Edge cases testing

    @Test
    public void testNextIndexOfEdgeCase() {
        CharacterReader reader = new CharacterReader("Testing nextIndexOf");
        assertEquals(8, reader.nextIndexOf('n'));
    }

    @Test
    public void testConsumeToEndEdgeCase() {
        CharacterReader reader = new CharacterReader("Test consumeToEnd edge case");
        assertEquals(" consumeToEnd edge case", reader.consumeToEnd());
    }

    @Test
    public void testRangeEqualsEdgeCase() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.rangeEquals(0, 0, ""));
    }

    // Exception handling scenarios testing

    @Test(expected = UncheckedIOException.class)
    public void testBufferUpExceptionHandling() {
        CharacterReader reader = new CharacterReader("Test exception handling");
        reader.consumeTo('x');
    }

}