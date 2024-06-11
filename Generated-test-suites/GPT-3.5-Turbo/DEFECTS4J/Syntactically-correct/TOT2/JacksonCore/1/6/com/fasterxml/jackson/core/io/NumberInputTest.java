import static org.junit.Assert.*;
import org.junit.Test;

public class NumberInputTest {
    
    @Test
    public void testParseInt_charArray() {
        char[] digits = {'1', '2', '3', '4', '5'};
        assertEquals(12345, NumberInput.parseInt(digits, 0, 5));
    }
    
    @Test
    public void testParseInt_string() {
        assertEquals(67890, NumberInput.parseInt("67890"));
    }
    
    @Test
    public void testParseLong_charArray() {
        char[] digits = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        assertEquals(123456789, NumberInput.parseLong(digits, 0, 9));
    }
    
    @Test
    public void testParseLong_string() {
        assertEquals(987654321, NumberInput.parseLong("987654321"));
    }
    
    @Test
    public void testInLongRange_charArray() {
        char[] digits = {'1', '2', '3'};
        assertTrue(NumberInput.inLongRange(digits, 0, 3, false));
        assertFalse(NumberInput.inLongRange(digits, 0, 3, true));
    }
    
    @Test
    public void testInLongRange_string() {
        assertTrue(NumberInput.inLongRange("123456789", false));
        assertFalse(NumberInput.inLongRange("9223372036854775808", false));
    }
    
    @Test
    public void testParseAsInt() {
        assertEquals(100, NumberInput.parseAsInt("100", -1));
        assertEquals(-50, NumberInput.parseAsInt("-50", -1));
        assertEquals(-1, NumberInput.parseAsInt("abc", -1));
    }
    
    @Test
    public void testParseAsLong() {
        assertEquals(500, NumberInput.parseAsLong("500", -1));
        assertEquals(-750, NumberInput.parseAsLong("-750", -1));
        assertEquals(-1, NumberInput.parseAsLong("def", -1));
    }
    
    @Test
    public void testParseAsDouble() {
        assertEquals(3.14, NumberInput.parseAsDouble("3.14", -1.0), 0.0001);
        assertEquals(-2.71, NumberInput.parseAsDouble("-2.71", -1.0), 0.0001);
        assertEquals(-1.0, NumberInput.parseAsDouble("xyz", -1.0), 0.0001);
    }
    
    @Test
    public void testParseDouble() {
        assertEquals(Double.MIN_VALUE, NumberInput.parseDouble("2.2250738585072012e-308"), 0.0001);
    }
    
    @Test
    public void testParseBigDecimal_string() {
        assertEquals(new BigDecimal("123456789"), NumberInput.parseBigDecimal("123456789"));
    }
    
    @Test
    public void testParseBigDecimal_charArray() {
        char[] digits = {'9', '8', '7', '6', '5', '4', '3', '2', '1'};
        assertEquals(new BigDecimal("987654321"), NumberInput.parseBigDecimal(digits));
    }
    
    @Test
    public void testParseBigDecimal_charArrayWithRange() {
        char[] digits = {'2', '3', '4', '5', '6', '7', '8', '9', '1', '0'};
        assertEquals(new BigDecimal("2345678910"), NumberInput.parseBigDecimal(digits, 0, 10));
    }
    
}