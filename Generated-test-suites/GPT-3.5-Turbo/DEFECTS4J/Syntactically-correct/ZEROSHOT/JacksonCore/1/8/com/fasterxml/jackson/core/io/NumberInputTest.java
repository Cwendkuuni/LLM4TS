import static org.junit.Assert.*;
import org.junit.Test;

public class NumberInputTest {
    
    @Test
    public void testParseInt() {
        // Test single digit input
        char[] ch1 = {'1'};
        assertEquals(1, NumberInput.parseInt(ch1, 0, 1));
        
        // Test multiple digit input
        char[] ch2 = {'1', '2', '3'};
        assertEquals(123, NumberInput.parseInt(ch2, 0, 3));
        
        // Test input with leading zeros
        char[] ch3 = {'0', '0', '1'};
        assertEquals(1, NumberInput.parseInt(ch3, 0, 3));
    }
    
    @Test
    public void testParseIntWithString() {
        // Test single digit input
        assertEquals(1, NumberInput.parseInt("1"));
        
        // Test multiple digit input
        assertEquals(123, NumberInput.parseInt("123"));
        
        // Test input with leading zeros
        assertEquals(1, NumberInput.parseInt("001"));
    }
    
    @Test
    public void testParseLong() {
        // Test input within range
        char[] ch1 = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        assertEquals(1234567890, NumberInput.parseLong(ch1, 0, 10));
        
        // Test input at maximum range
        char[] ch2 = {'9', '2', '2', '3', '3', '7', '2', '0', '3', '6', '8', '5', '0', '7', '2', '0', '1', '2', 'e', '-', '3', '0', '8'};
        assertEquals(Long.MIN_VALUE, NumberInput.parseLong(ch2, 0, 23));
    }
    
    @Test
    public void testParseLongWithString() {
        // Test input within range
        assertEquals(1234567890, NumberInput.parseLong("1234567890"));
        
        // Test input at maximum range
        assertEquals(Long.MIN_VALUE, NumberInput.parseLong("922337203685072012e-308"));
    }
    
    @Test
    public void testInLongRange() {
        // Test input within range
        char[] ch1 = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        assertTrue(NumberInput.inLongRange(ch1, 0, 10, false));
        
        // Test input out of range
        char[] ch2 = {'9', '2', '2', '3', '3', '7', '2', '0', '3', '6', '8', '5', '0', '7', '2', '0', '1', '2', 'e', '-', '3', '0', '8'};
        assertFalse(NumberInput.inLongRange(ch2, 0, 23, false));
    }
    
    @Test
    public void testInLongRangeWithString() {
        // Test input within range
        assertTrue(NumberInput.inLongRange("1234567890", false));
        
        // Test input out of range
        assertFalse(NumberInput.inLongRange("922337203685072012e-308", false));
    }
    
    @Test
    public void testParseAsInt() {
        // Test valid integer input
        assertEquals(123, NumberInput.parseAsInt("123", 0));
        
        // Test invalid input
        assertEquals(0, NumberInput.parseAsInt("abc", 0));
        
        // Test empty string input
        assertEquals(0, NumberInput.parseAsInt("", 0));
    }
    
    @Test
    public void testParseAsLong() {
        // Test valid long input
        assertEquals(1234567890L, NumberInput.parseAsLong("1234567890", 0L));
        
        // Test invalid input
        assertEquals(0L, NumberInput.parseAsLong("abc", 0L));
        
        // Test empty string input
        assertEquals(0L, NumberInput.parseAsLong("", 0L));
    }
    
    @Test
    public void testParseAsDouble() {
        // Test valid double input
        assertEquals(1.23, NumberInput.parseAsDouble("1.23", 0.0), 0.001);
        
        // Test invalid input
        assertEquals(0.0, NumberInput.parseAsDouble("abc", 0.0), 0.001);
        
        // Test empty string input
        assertEquals(0.0, NumberInput.parseAsDouble("", 0.0), 0.001);
    }
    
    @Test
    public void testParseDouble() {
        // Test valid double input
        assertEquals(1.23, NumberInput.parseDouble("1.23"), 0.001);
        
        // Test invalid input
        try {
            NumberInput.parseDouble("abc");
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }
    
    @Test
    public void testParseBigDecimalWithString() {
        // Test valid BigDecimal input
        assertEquals(new BigDecimal("1.23"), NumberInput.parseBigDecimal("1.23"));
        
        // Test invalid input
        try {
            NumberInput.parseBigDecimal("abc");
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }
    
    @Test
    public void testParseBigDecimalWithCharArray() {
        // Test valid BigDecimal input
        char[] ch = {'1', '0', '0', '.', '5'};
        assertEquals(new BigDecimal("100.5"), NumberInput.parseBigDecimal(ch));
        
        // Test invalid input
        try {
            char[] ch2 = {'a', 'b', 'c'};
            NumberInput.parseBigDecimal(ch2);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }
}