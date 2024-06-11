import org.jsoup.parser.CharacterReader;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        characterReader = new CharacterReader("Test String");
    }

    @Test
    public void testPos() {
        // Typical case
        int expectedPos = 0;
        int actualPos = characterReader.pos();
        assertEquals(expectedPos, actualPos);

        // Edge case
        characterReader.consume();
        expectedPos = 1;
        actualPos = characterReader.pos();
        assertEquals(expectedPos, actualPos);
    }

    @Test
    public void testIsEmpty() {
        // Typical case
        boolean isEmpty = characterReader.isEmpty();
        assertFalse(isEmpty);

        // Edge case
        while (!characterReader.isEmpty()) {
            characterReader.consume();
        }
        isEmpty = characterReader.isEmpty();
        assertTrue(isEmpty);
    }

    @Test
    public void testCurrent() {
        // Typical case
        char expectedCurrent = 'T';
        char actualCurrent = characterReader.current();
        assertEquals(expectedCurrent, actualCurrent);

        // Edge case
        while (!characterReader.isEmpty()) {
            characterReader.consume();
        }
        expectedCurrent = CharacterReader.EOF;
        actualCurrent = characterReader.current();
        assertEquals(expectedCurrent, actualCurrent);
    }

    // Add more test cases for other methods here

}