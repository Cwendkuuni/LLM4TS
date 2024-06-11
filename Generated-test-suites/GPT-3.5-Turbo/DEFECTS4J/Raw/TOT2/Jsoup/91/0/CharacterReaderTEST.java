// JUnit 4 test cases for CharacterReader class
import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {

    // Test Case for pos()
    @Test
    public void testPos() {
        CharacterReader cr = new CharacterReader(new StringReader("test"));
        assertEquals(0, cr.pos());
    }

    // Test Case for isEmpty()
    @Test
    public void testIsEmpty() {
        CharacterReader cr = new CharacterReader(new StringReader("test"));
        assertFalse(cr.isEmpty());
    }

    // Add more test cases for other methods...

}