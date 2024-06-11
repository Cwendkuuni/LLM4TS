import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals(0, reader.pos());
        reader.advance();
        assertEquals(1, reader.pos());
        reader.consume();
        assertEquals(2, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        
        assertTrue(reader.isEmpty());
        
        reader = new CharacterReader("Hello World");
        assertFalse(reader.isEmpty());
        
        while (!reader.isEmpty()) {
            reader.consume();
        }
        
        assertTrue(reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        
        assertEquals('H', reader.current());
        reader.advance();
        assertEquals('e', reader.current());
        reader.consume();
        assertEquals('l', reader.current());
    }
    
    // Add more test methods to cover all methods in the CharacterReader class
    
    // ...

}