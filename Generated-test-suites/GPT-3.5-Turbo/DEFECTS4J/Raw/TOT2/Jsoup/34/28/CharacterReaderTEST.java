import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        // Test the initial position
        CharacterReader reader = new CharacterReader("test");
        assertEquals(0, reader.pos());
        
        // Test the position after advancing
        reader.advance();
        assertEquals(1, reader.pos());
        
        // Test the position after rewinding to mark
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        // Test when the reader is not empty
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.isEmpty());
        
        // Test when the reader is empty
        reader.consume();
        assertTrue(reader.isEmpty());
    }

    // Additional test cases for other methods should be added here

    @Test
    public void testConsumeAsString() {
        // Test consuming a single character
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeAsString());
        
        // Test consuming an empty string
        reader.consume();
        assertEquals("", reader.consumeAsString());
    }

    // Add more test cases for other methods

}