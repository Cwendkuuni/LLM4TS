import static org.junit.Assert.*;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt_CharArray() {
        char[] input = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int result = NumberInput.parseInt(input, 0, input.length);
        assertEquals(123456789, result);
    }

    @Test
    public void testParseInt_OverLimit() {
        char[] input = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        int result = NumberInput.parseInt(input, 0, input.length);
        assertEquals(Integer.parseInt(String.valueOf(input)), result);
    }

    @Test
    public void testParseInt_EmptyInput() {
        char[] input = {};
        int result = NumberInput.parseInt(input, 0, input.length);
        assertEquals(0, result);
    }

    // More test cases should be added for other public methods in NumberInput class

}