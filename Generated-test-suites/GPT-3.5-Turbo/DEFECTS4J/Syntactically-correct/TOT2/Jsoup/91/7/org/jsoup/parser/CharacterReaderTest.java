import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    
    @Test
    public void testPos() {
        Reader input = new StringReader("Test");
        CharacterReader characterReader = new CharacterReader(input);
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        Reader input = new StringReader("");
        CharacterReader characterReader = new CharacterReader(input);
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        Reader input = new StringReader("Test");
        CharacterReader characterReader = new CharacterReader(input);
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsume() {
        Reader input = new StringReader("Test");
        CharacterReader characterReader = new CharacterReader(input);
        assertEquals('T', characterReader.consume());
    }

    @Test
    public void testAdvance() {
        Reader input = new StringReader("Test");
        CharacterReader characterReader = new CharacterReader(input);
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testRewindToMark() {
        Reader input = new StringReader("Test");
        CharacterReader characterReader = new CharacterReader(input);
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        Reader input = new StringReader("Test");
        CharacterReader characterReader = new CharacterReader(input);
        assertEquals(1, characterReader.nextIndexOf('e'));
    }

    @Test
    public void testConsumeTo() {
        Reader input = new StringReader("Test1Test2");
        CharacterReader characterReader = new CharacterReader(input);
        assertEquals("Test1", characterReader.consumeTo('T'));
    }

}