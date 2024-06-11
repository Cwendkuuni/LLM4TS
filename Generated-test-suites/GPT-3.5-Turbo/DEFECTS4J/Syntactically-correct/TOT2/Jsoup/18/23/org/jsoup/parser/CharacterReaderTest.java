import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals('T', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }
    
    // Add more test cases for other public methods in the CharacterReader class
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Test");
        assertEquals("T", reader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Test123");
        assertEquals("Test", reader.consumeTo('1'));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Test123");
        assertEquals("Test", reader.consumeToAny('1', '2'));
    }
    
    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.matches('T'));
        assertFalse(reader.matches('e'));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("TeSt");
        assertTrue(reader.matchesIgnoreCase("tEsT"));
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.matchConsume("Te"));
        assertEquals('s', reader.current());
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test");
        assertTrue(reader.containsIgnoreCase("es"));
    }
}