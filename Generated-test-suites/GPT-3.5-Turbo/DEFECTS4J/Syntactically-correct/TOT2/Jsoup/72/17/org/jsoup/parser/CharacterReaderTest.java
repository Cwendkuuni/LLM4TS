import org.jsoup.parser.CharacterReader;
import org.jsoup.helper.Validate;
import org.jsoup.UncheckedIOException;
import org.junit.Before;
import org.junit.Test;
import java.io.StringReader;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setup() {
        characterReader = new CharacterReader(new StringReader("Test String"));
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
        characterReader.advance();
        assertEquals(1, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
        for (int i = 0; i < 10; i++) {
            characterReader.consume();
        }
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('T', characterReader.current());
        for (int i = 0; i < 10; i++) {
            characterReader.consume();
        }
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    // Add more test cases for other methods as needed

}