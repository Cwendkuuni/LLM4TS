
import org.jsoup.helper.Validate;
import org.jsoup.parser.CharacterReader;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testConstructorWithReader() {
        StringReader reader = new StringReader("test string");
        CharacterReader characterReader = new CharacterReader(reader);
        assertNotNull(characterReader);
    }

    @Test
    public void testPos() {
        StringReader reader = new StringReader("test string");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        StringReader reader = new StringReader("test string");
        CharacterReader characterReader = new CharacterReader(reader);
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        StringReader reader = new StringReader("test string");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testConsume() {
        StringReader reader = new StringReader("test string");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals('t', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        StringReader reader = new StringReader("test string");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('t', characterReader.current());
    }

    @Test
    public void testAdvance() {
        StringReader reader = new StringReader("test string");
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        StringReader reader = new StringReader("test string");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals(1, characterReader.nextIndexOf('e'));
    }
    
    // Add more test cases for other methods
}

