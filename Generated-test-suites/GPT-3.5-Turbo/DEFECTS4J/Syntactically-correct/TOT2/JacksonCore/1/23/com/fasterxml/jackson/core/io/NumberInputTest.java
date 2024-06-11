import static org.junit.Assert.*;
import org.junit.Test;

public class NumberInputTest {
    
    @Test
    public void testParseInt() {
        char[] digits = {'1', '2', '3', '4'};
        assertEquals(1234, NumberInput.parseInt(digits, 0, 4));
    }
    
    @Test
    public void testParseLong() {
        char[] digits = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        assertEquals(1234567890, NumberInput.parseLong(digits, 0, 10));
    }
    
    @Test
    public void testParseIntFromString() {
        assertEquals(1234, NumberInput.parseInt("1234"));
    }
    
    @Test
    public void testParseLongFromString() {
        assertEquals(1234567890, NumberInput.parseLong("1234567890"));
    }
    
    @Test
    public void testInLongRange() {
        char[] digits = {'9', '2', '2', '3', '3', '8', '5', '8', '5', '0', '7', '2', '2', '0', '1', '2'};
        assertFalse(NumberInput.inLongRange(digits, 0, 16, false));
    }
    
    @Test
    public void testParseAsInt() {
        assertEquals(1234, NumberInput.parseAsInt("1234", 0));
    }
    
    @Test
    public void testParseAsLong() {
        assertEquals(1234567890, NumberInput.parseAsLong("1234567890", 0));
    }
    
    @Test
    public void testParseAsDouble() {
        assertEquals(1.234, NumberInput.parseAsDouble("1.234", 0.0), 0.001);
    }
    
    @Test
    public void testParseBigDecimalFromString() {
        assertEquals(new BigDecimal("123.45"), NumberInput.parseBigDecimal("123.45"));
    }
    
    @Test
    public void testParseBigDecimalFromCharArray() {
        char[] digits = {'1','2','3','4','5'};
        assertEquals(new BigDecimal("12345"), NumberInput.parseBigDecimal(digits, 0, 5));
    }
}