import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        Reader reader = new StringReader("Test");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals(0, characterReader.pos());
        
        characterReader.consume();
        assertEquals(1, characterReader.pos());
        
        characterReader.advance();
        assertEquals(2, characterReader.pos());
    }
  
    @Test
    public void testIsEmpty() {
        Reader reader = new StringReader("");
        CharacterReader characterReader = new CharacterReader(reader);
        assertTrue(characterReader.isEmpty());
        
        reader = new StringReader("Test");
        characterReader = new CharacterReader(reader);
        assertFalse(characterReader.isEmpty());
        
        characterReader.consume();
        assertFalse(characterReader.isEmpty());
    }
  
    @Test
    public void testCurrent() {
        Reader reader = new StringReader("Test");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals('T', characterReader.current());
        
        characterReader.consume();
        assertEquals('e', characterReader.current());
    }
  
    //... continue with other tests for the remaining methods

}