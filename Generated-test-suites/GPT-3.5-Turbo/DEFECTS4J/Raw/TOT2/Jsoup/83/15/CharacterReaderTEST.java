import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CharacterReaderTest {

    // Test cases for public int pos()
    @Test
    public void testPos() {
        // Test case 1: Check if initial position is 0
        CharacterReader reader = new CharacterReader("Test input");
        Assertions.assertEquals(0, reader.pos());
        
        // Test case 2: Check if position is updated after advancing
        reader.advance();
        Assertions.assertEquals(1, reader.pos());
    }
    
    // Other test cases for all public methods
    // ...
    
    // Test cases for exception handling
    @Test
    public void testIOException() {
        // Test case for IOException handling
        CharacterReader reader = new CharacterReader(new StringReader("Test input"));
        Assertions.assertThrows(UncheckedIOException.class, reader::consumeData);
    }

    // Add more test cases for edge cases and exception handling scenarios

}