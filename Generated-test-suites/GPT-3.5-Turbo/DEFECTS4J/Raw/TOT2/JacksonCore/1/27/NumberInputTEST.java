import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {
    
    @Test
    public void testParseInt_charArray() {
        char[] digits = {'1', '2', '3', '4', '5'};
        int result = NumberInput.parseInt(digits, 0, 5);
        assertEquals(12345, result);
    }

    @Test
    public void testParseInt_string() {
        int result = NumberInput.parseInt("54321");
        assertEquals(54321, result);
    }
    
    // Additional test cases for edge cases and error scenarios can be added here
    
}