import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        Reader reader = new StringReader("test");
        CharacterReader characterReader = new CharacterReader(reader);
        int expected = 0;
        int actual = characterReader.pos();
        assertEquals(expected, actual);
    }
}