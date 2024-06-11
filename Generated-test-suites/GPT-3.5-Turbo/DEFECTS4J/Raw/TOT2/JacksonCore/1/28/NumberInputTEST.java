import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        char[] digits = {'1', '2', '3'};
        assertEquals(123, NumberInput.parseInt(digits, 0, 3));
    }

    @Test
    public void testParseInt_EmptyString() {
        assertEquals(0, NumberInput.parseInt(""));
    }

    @Test
    public void testParseInt_InvalidInput() {
        assertEquals(0, NumberInput.parseInt("abc"));
    }

    @Test
    public void testParseLong() {
        char[] digits = {'9', '8', '7', '6', '5', '4', '3', '2', '1', '0'};
        assertEquals(9876543210L, NumberInput.parseLong(digits, 0, 10));
    }

    @Test
    public void testParseLong_EmptyString() {
        assertEquals(0, NumberInput.parseLong(""));
    }

    @Test
    public void testParseLong_InvalidInput() {
        assertEquals(0, NumberInput.parseLong("xyz"));
    }

    @Test
    public void testParseAsInt() {
        assertEquals(123, NumberInput.parseAsInt("123", -1));
    }

    @Test
    public void testParseAsInt_InvalidInput() {
        assertEquals(-1, NumberInput.parseAsInt("abc", -1));
    }

    @Test
    public void testParseAsLong() {
        assertEquals(9876543210L, NumberInput.parseAsLong("9876543210", -1));
    }

    @Test
    public void testParseAsLong_InvalidInput() {
        assertEquals(-1, NumberInput.parseAsLong("xyz", -1));
    }

    @Test
    public void testParseAsDouble() {
        assertEquals(123.45, NumberInput.parseAsDouble("123.45", -1.0), 0.001);
    }

    @Test
    public void testParseAsDouble_InvalidInput() {
        assertEquals(-1.0, NumberInput.parseAsDouble("abc", -1.0), 0.001);
    }

    @Test
    public void testParseDouble() {
        assertEquals(123.45, NumberInput.parseDouble("123.45"), 0.001);
    }

    @Test
    public void testParseBigDecimal() {
        assertEquals(new BigDecimal("123.45"), NumberInput.parseBigDecimal("123.45"));
    }

    @Test
    public void testParseBigDecimal_InvalidInput() {
        assertThrows(NumberFormatException.class, () -> {
            NumberInput.parseBigDecimal("abc");
        });
    }
}