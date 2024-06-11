import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("Hello, World!");
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('H', reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('H', reader.consume());
        assertEquals('e', reader.current());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('H', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        reader.advance();
        reader.mark();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals(2, reader.pos());
    }

    // Additional test cases should be added for all the other methods in the CharacterReader class

}