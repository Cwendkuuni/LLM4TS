import static org.junit.Assert.*;
import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt_charArray() {
        char[] digits = {'1', '2', '3'};
        assertEquals(123, NumberInput.parseInt(digits, 0, 3));
    }

    @Test
    public void testParseInt_string() {
        assertEquals(456, NumberInput.parseInt("456"));
    }

    @Test
    public void testParseLong_charArray() {
        char[] digits = {'9', '8', '7', '6'};
        assertEquals(9876, NumberInput.parseLong(digits, 0, 4));
    }

    @Test
    public void testParseLong_string() {
        assertEquals(54321, NumberInput.parseLong("54321"));
    }

    @Test
    public void testParseAsInt() {
        assertEquals(123, NumberInput.parseAsInt("123", 0));
    }

    @Test
    public void testParseAsLong() {
        assertEquals(54321, NumberInput.parseAsLong("54321", 0L));
    }

    @Test
    public void testParseAsDouble() {
        assertEquals(3.14, NumberInput.parseAsDouble("3.14", 0.0), 0.001);
    }

    @Test
    public void testParseDouble() {
        assertEquals(2.0, NumberInput.parseDouble("2.0"), 0.001);
    }

    @Test
    public void testParseBigDecimal_string() {
        assertEquals("123.45", NumberInput.parseBigDecimal("123.45").toString());
    }

    @Test
    public void testParseBigDecimal_charArray() {
        char[] digits = {'9', '8', '7', '6'};
        assertEquals("9876", NumberInput.parseBigDecimal(digits).toString());
    }

    @Test(expected = NumberFormatException.class)
    public void testParseDouble_exception() {
        NumberInput.parseDouble("abc");
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimal_exception() {
        NumberInput.parseBigDecimal("abc");
    }
}