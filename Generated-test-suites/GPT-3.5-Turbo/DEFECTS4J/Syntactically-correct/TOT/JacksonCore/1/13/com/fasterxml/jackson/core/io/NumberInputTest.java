import static org.junit.Assert.*;

import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        // Typical use cases
        assertEquals(123, NumberInput.parseInt("123"));
        
        // Edge cases
        assertEquals(0, NumberInput.parseInt("0"));
        
        // Error scenarios
        assertEquals(0, NumberInput.parseInt(""));
        assertEquals(0, NumberInput.parseInt(null));
        assertEquals(0, NumberInput.parseInt("abc"));    
    }

    @Test
    public void testParseLong() {
        // Typical use cases
        assertEquals(123456789, NumberInput.parseLong("123456789"));
        
        // Edge cases
        assertEquals(Long.MAX_VALUE, NumberInput.parseLong(String.valueOf(Long.MAX_VALUE)));
        assertEquals(Long.MIN_VALUE, NumberInput.parseLong(String.valueOf(Long.MIN_VALUE)));
        
        // Error scenarios
        assertEquals(0, NumberInput.parseLong(""));
        assertEquals(0, NumberInput.parseLong(null));
        assertEquals(0, NumberInput.parseLong("abc"));    
    }

    @Test
    public void testInLongRange() {
        // Typical use cases
        assertTrue(NumberInput.inLongRange("1234567", false));
        assertFalse(NumberInput.inLongRange(String.valueOf(Long.MAX_VALUE), false));
        
        // Edge cases
        assertTrue(NumberInput.inLongRange(String.valueOf(Long.MAX_VALUE), true));
        assertTrue(NumberInput.inLongRange("1234567890", false));
        
        // Error scenarios
        assertTrue(NumberInput.inLongRange("", false));
        assertFalse(NumberInput.inLongRange("12345678901", true));
        assertTrue(NumberInput.inLongRange("abc", false));
    }

    @Test
    public void testParseAsInt() {
        // Typical use cases
        assertEquals(123, NumberInput.parseAsInt("123", 0));
        
        // Edge cases
        assertEquals(0, NumberInput.parseAsInt("0", 0));
        
        // Error scenarios
        assertEquals(0, NumberInput.parseAsInt("", 0));
        assertEquals(0, NumberInput.parseAsInt(null, 0));
        assertEquals(123, NumberInput.parseAsInt("abc", 123));    
    }
    
    @Test
    public void testParseAsLong() {
        // Typical use cases
        assertEquals(123456789, NumberInput.parseAsLong("123456789", 0));
        
        // Edge cases
        assertEquals(Long.MAX_VALUE, NumberInput.parseAsLong(String.valueOf(Long.MAX_VALUE), 0));
        
        // Error scenarios
        assertEquals(0, NumberInput.parseAsLong("", 0));
        assertEquals(0, NumberInput.parseAsLong(null, 0));
        assertEquals(123, NumberInput.parseAsLong("abc", 123));    
    }

    @Test
    public void testParseAsDouble() {
        // Typical use cases
        assertEquals(1.23, NumberInput.parseAsDouble("1.23", 0), 0);
        
        // Edge cases
        assertEquals(0, NumberInput.parseAsDouble("0", 0), 0);
        
        // Error scenarios
        assertEquals(0, NumberInput.parseAsDouble("", 0), 0);
        assertEquals(0, NumberInput.parseAsDouble(null, 0), 0);
        assertEquals(123, NumberInput.parseAsDouble("abc", 123), 0);    
    }

    @Test
    public void testParseDouble() {
        // Typical use cases
        assertEquals(1.23, NumberInput.parseDouble("1.23"), 0);
        
        // Edge cases
        assertEquals(0, NumberInput.parseDouble("0"), 0);
        
        // Error scenarios
        assertEquals(Double.MIN_VALUE, NumberInput.parseDouble(NumberInput.NASTY_SMALL_DOUBLE), 0);
        assertEquals(0, NumberInput.parseDouble(""), 0);
        assertEquals(0, NumberInput.parseDouble(null), 0);
        assertEquals(123, NumberInput.parseDouble("abc"), 0);    
    }

    @Test
    public void testParseBigDecimal() {
        // Typical use cases
        assertEquals(new BigDecimal("123.45"), NumberInput.parseBigDecimal("123.45"));
        
        // Edge cases
        assertEquals(new BigDecimal("0"), NumberInput.parseBigDecimal("0"));
        
        // Error scenarios
        assertEquals(new BigDecimal("0"), NumberInput.parseBigDecimal(""));
        assertEquals(new BigDecimal("0"), NumberInput.parseBigDecimal(null));
        assertEquals(new BigDecimal("123"), NumberInput.parseBigDecimal("abc"));    
    }

    @Test
    public void testParseBigDecimalCharArrayIntInt() {
        // Typical use cases
        assertEquals(new BigDecimal("123.45"), NumberInput.parseBigDecimal("123.45".toCharArray(), 0, 6));
        
        // Edge cases
        assertEquals(new BigDecimal("0"), NumberInput.parseBigDecimal("0".toCharArray(), 0, 1));
        
        // Error scenarios
        assertEquals(new BigDecimal("0"), NumberInput.parseBigDecimal("".toCharArray(), 0, 0));
        assertEquals(new BigDecimal("0"), NumberInput.parseBigDecimal(null, 0, 0));
        assertEquals(new BigDecimal("123"), NumberInput.parseBigDecimal("abc".toCharArray(), 0, 3));    
    }
    
}