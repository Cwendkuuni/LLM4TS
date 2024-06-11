
import org.jsoup.helper.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("Hello");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Test");
        char result = reader.consume();
        assertEquals('T', result);
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("Testing nextIndexOf method");
        int result = reader.nextIndexOf('x');
        assertEquals(8, result);
    }

    // Add more test cases for other methods

}