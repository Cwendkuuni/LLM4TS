import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        int expected = 0;
        int result = reader.pos();
        assertEquals(expected, result);
    }

    // Continue with test cases for all other methods...

}