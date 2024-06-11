
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        String testString = "This is a test";
        CharacterReader reader = new CharacterReader(testString);
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        String testString = "";
        CharacterReader reader = new CharacterReader(testString);
        assertTrue(reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        String testString = "This is a test";
        CharacterReader reader = new CharacterReader(testString);
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsume() {
        String testString = "This is a test";
        CharacterReader reader = new CharacterReader(testString);
        assertEquals('T', reader.consume());
        assertEquals('h', reader.consume());
    }
  
    @Test
    public void testUnconsume() {
        String testString = "This is a test";
        CharacterReader reader = new CharacterReader(testString);
        assertEquals('T', reader.consume());
        reader.unconsume();
        assertEquals('T', reader.consume());
    }

    @Test
    public void testAdvance() {
        String testString = "This is a test";
        CharacterReader reader = new CharacterReader(testString);
        reader.advance();
        assertEquals('h', reader.current());
    }
    
    // Additional test cases for the identified edge cases and exceptions
    
    @Test
    public void testNextIndexOf() {
        String testString = "This is a test";
        CharacterReader reader = new CharacterReader(testString);
        assertEquals(2, reader.nextIndexOf('i'));
    }
    
    @Test
    public void testConsumeTo() {
        String testString = "This is a test";
        CharacterReader reader = new CharacterReader(testString);
        assertEquals("This ", reader.consumeTo('i'));
        assertEquals("is", reader.consumeTo('a'));
    }
  
    @Test
    public void testConsumeToAny() {
        String testString = "This is a test";
        CharacterReader reader = new CharacterReader(testString);
        assertEquals("Th", reader.consumeToAny('i', 'a'));
    }
    
    @Test
    public void testConsumeToEnd() {
        String testString = "This is a test";
        CharacterReader reader = new CharacterReader(testString);
        assertEquals("This is a test", reader.consumeToEnd());
    }
}

