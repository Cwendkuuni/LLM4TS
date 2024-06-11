import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

     @Test
     public void testCharacterReaderConstructor() {
        CharacterReader characterReader = new CharacterReader("TestString");
        assertEquals("TestString", characterReader.input);
        assertEquals(10, characterReader.length);
        assertFalse(characterReader.isEmpty());
        assertEquals('T', characterReader.current());
     }

     // Test methods here...

     @Test(expected = java.lang.IndexOutOfBoundsException.class)
     public void testConsumePastEndOfInput() {
        CharacterReader characterReader = new CharacterReader("TestString");
        characterReader.advance();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
     }

     // Test other methods here...

     @Test
     public void testToString() {
        CharacterReader characterReader = new CharacterReader("TestString");
        assertEquals("TestString", characterReader.toString());
     }

}