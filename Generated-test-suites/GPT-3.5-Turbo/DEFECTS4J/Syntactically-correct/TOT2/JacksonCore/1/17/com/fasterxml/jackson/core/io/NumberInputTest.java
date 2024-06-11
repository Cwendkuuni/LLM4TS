import static org.junit.Assert.*;
import org.junit.Test;

public class NumberInputTest {

    // Test cases for parseInt method
    @Test
    public void testParseInt() {
        // Typical use case
        char[] digits = {'1', '2', '3'};
        assertEquals(123, NumberInput.parseInt(digits, 0, 3));
        
        // Edge case - empty input
        char[] empty = {};
        assertEquals(0, NumberInput.parseInt(empty, 0, 0));
        
        // Edge case - single digit
        char[] singleDigit = {'9'};
        assertEquals(9, NumberInput.parseInt(singleDigit, 0, 1));
        
        // Error scenario - non-numeric input
        char[] nonNumeric = {'a', 'b', 'c'};
        assertEquals(0, NumberInput.parseInt(nonNumeric, 0, 3));
    }

    // Test cases for parseInt(String) method
    @Test
    public void testParseIntFromString() {
        // Typical use case
        assertEquals(456, NumberInput.parseInt("456"));
        
        // Edge case - negative number
        assertEquals(-789, NumberInput.parseInt("-789"));
        
        // Edge case - maximum value
        assertEquals(Integer.MAX_VALUE, NumberInput.parseInt(String.valueOf(Integer.MAX_VALUE)));
        
        // Error scenario - invalid input
        assertEquals(0, NumberInput.parseInt("abc"));
    }

    // Add test cases for other methods as well ...

}