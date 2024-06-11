import static org.junit.Assert.*;
import org.junit.Test;

public class NumberInputTest {
    
    @Test
    public void testParseInt() {
        char[] input = {'1', '2', '3'};
        assertEquals(123, NumberInput.parseInt(input, 0, 3));
    }
    
    @Test
    public void testParseLong() {
        char[] input = {'1', '2', '3', '4'};
        assertEquals(1234, NumberInput.parseLong(input, 0, 4));
    }
    
    @Test
    public void testInLongRange() {
        char[] input = {'0', '9', '2', '3', '3', '7', '2', '0', '3', '6', '8'};
        assertTrue(NumberInput.inLongRange(input, 0, 11, false));
    }
    
    @Test
    public void testParseAsInt() {
        assertEquals(123, NumberInput.parseAsInt("123", 0));
    }
    
    @Test
    public void testParseAsLong() {
        assertEquals(1234, NumberInput.parseAsLong("1234", 0));
    }
    
    @Test
    public void testParseAsDouble() {
        assertEquals(123.0, NumberInput.parseAsDouble("123.0", 0.0), 0.1);
    }
    
    @Test
    public void testParseDouble() {
        assertEquals(1.234, NumberInput.parseDouble("1.234"), 0.001);
    }
    
    @Test
    public void testParseBigDecimal() {
        assertEquals(new BigDecimal("123"), NumberInput.parseBigDecimal("123"));
    }
    
    @Test
    public void testParseBigDecimalFromArray() {
        char[] input = {'1', '2', '3'};
        assertEquals(new BigDecimal("123"), NumberInput.parseBigDecimal(input));
    }
    
    @Test
    public void testParseBigDecimalFromArrayWithOffset() {
        char[] input = {'0', '9', '2', '3', '3', '7', '2', '0', '3', '6', '8'};
        assertEquals(new BigDecimal("9233720368"), NumberInput.parseBigDecimal(input, 1, 10));
    }
}