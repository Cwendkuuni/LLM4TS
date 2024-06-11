import static org.junit.Assert.*;
import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        char[] digits = {'1', '2', '3'};
        int result = NumberInput.parseInt(digits, 0, 3);
        assertEquals(123, result);
    }

    @Test
    public void testParseIntWithNegative() {
        char[] digits = {'-', '5', '0'};
        int result = NumberInput.parseInt(digits, 0, 3);
        assertEquals(-50, result);
    }

    @Test
    public void testParseIntWithOutOfBounds() {
        char[] digits = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int result = NumberInput.parseInt(digits, 0, 9);
        assertEquals(123456789, result);
    }

    @Test
    public void testParseIntSingleDigit() {
        char[] digits = {'6'};
        int result = NumberInput.parseInt(digits, 0, 1);
        assertEquals(6, result);
    }

    // Add more test cases for other methods

}