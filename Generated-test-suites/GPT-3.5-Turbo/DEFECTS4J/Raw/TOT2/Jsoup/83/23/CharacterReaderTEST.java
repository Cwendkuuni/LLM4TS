import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);
        int expectedPos = 0;
        int actualPos = characterReader.pos();
        assertEquals(expectedPos, actualPos);
    }

    // Next test case method goes here

}