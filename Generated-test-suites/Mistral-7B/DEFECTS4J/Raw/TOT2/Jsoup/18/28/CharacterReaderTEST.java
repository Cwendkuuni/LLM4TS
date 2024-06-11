import static org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testConstructor() {
        String input = "Hello World";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(input, reader.input);
    }

    // Add test cases for each method here
}