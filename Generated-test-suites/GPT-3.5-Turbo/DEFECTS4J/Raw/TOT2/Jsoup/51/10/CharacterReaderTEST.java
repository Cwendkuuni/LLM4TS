import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {
    
    private CharacterReader characterReader;
    
    @Before
    public void setup() {
        characterReader = new CharacterReader("Hello, World!");
    }
    
    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        assertEquals('H', characterReader.current());
    }
    
    @Test
    public void testConsume() {
        assertEquals('H', characterReader.consume());
        assertEquals('e', characterReader.consume());
    }
    
    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('H', characterReader.current());
    }
    
    // Add more test cases for other methods as per requirement
    
}