import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.current());
    }

    // Test cases for other public methods
    
    // Edge case and exception handling test cases
    
    @Test
    public void testNextIndexOfCharNotFound() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(-1, reader.nextIndexOf('z'));
    }
    
    @Test
    public void testNextIndexOfChar() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(1, reader.nextIndexOf('e'));
    }
    
    // Additional edge cases and exception handling scenarios
    
}