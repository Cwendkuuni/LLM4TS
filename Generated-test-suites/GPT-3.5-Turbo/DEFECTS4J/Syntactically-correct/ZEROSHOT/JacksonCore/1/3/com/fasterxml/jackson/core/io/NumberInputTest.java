import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {
    
    @Test
    public void testParseInt() {
        char[] digitChars = {'1', '2', '3'};
        assertEquals(123, NumberInput.parseInt(digitChars, 0, 3));
        
        char[] digitChars2 = {'9', '9', '9'};
        assertEquals(999, NumberInput.parseInt(digitChars2, 0, 3));
    }
    
    @Test
    public void testParseIntWithNegative() {
        char[] digitChars = {'-', '1', '2', '3'};
        assertEquals(-123, NumberInput.parseInt(digitChars, 0, 4));
        
        char[] digitChars2 = {'-', '9', '9', '9'};
        assertEquals(-999, NumberInput.parseInt(digitChars2, 0, 4));
    }
    
    @Test
    public void testParseIntWithString() {
        assertEquals(123, NumberInput.parseInt("123"));
        assertEquals(999, NumberInput.parseInt("999"));
    }
    
    @Test
    public void testParseIntWithNegativeString() {
        assertEquals(-123, NumberInput.parseInt("-123"));
        assertEquals(-999, NumberInput.parseInt("-999"));
    }
    
    @Test
    public void testParseLong() {
        char[] digitChars = {'1', '2', '3'};
        assertEquals(123L, NumberInput.parseLong(digitChars, 0, 3));
        
        char[] digitChars2 = {'9', '9', '9'};
        assertEquals(999L, NumberInput.parseLong(digitChars2, 0, 3));
    }
    
    @Test
    public void testParseLongWithNegative() {
        char[] digitChars = {'-', '1', '2', '3'};
        assertEquals(-123L, NumberInput.parseLong(digitChars, 0, 4));
        
        char[] digitChars2 = {'-', '9', '9', '9'};
        assertEquals(-999L, NumberInput.parseLong(digitChars2, 0, 4));
    }
    
    @Test
    public void testParseLongWithString() {
        assertEquals(123L, NumberInput.parseLong("123"));
        assertEquals(999L, NumberInput.parseLong("999"));
    }
    
    @Test
    public void testParseLongWithNegativeString() {
        assertEquals(-123L, NumberInput.parseLong("-123"));
        assertEquals(-999L, NumberInput.parseLong("-999"));
    }
    
    @Test
    public void testInLongRange() {
        char[] digitChars = {'1', '2', '3'};
        assertTrue(NumberInput.inLongRange(digitChars, 0, 3, false));
        
        char[] digitChars2 = {'9', '9', '9'};
        assertTrue(NumberInput.inLongRange(digitChars2, 0, 3, false));
    }
    
    @Test
    public void testInLongRangeWithNegative() {
        char[] digitChars = {'-', '1', '2', '3'};
        assertTrue(NumberInput.inLongRange(digitChars, 0, 4, true));
        
        char[] digitChars2 = {'-', '9', '9', '9'};
        assertTrue(NumberInput.inLongRange(digitChars2, 0, 4, true));
    }
    
    @Test
    public void testInLongRangeWithString() {
        assertTrue(NumberInput.inLongRange("123", false));
        assertTrue(NumberInput.inLongRange("999", false));
    }
    
    @Test
    public void testInLongRangeWithNegativeString() {
        assertTrue(NumberInput.inLongRange("-123", true));
        assertTrue(NumberInput.inLongRange("-999", true));
    }
    
    @Test
    public void testParseAsInt() {
        assertEquals(123, NumberInput.parseAsInt("123", 0));
        assertEquals(999, NumberInput.parseAsInt("999", 0));
    }
    
    @Test
    public void testParseAsIntWithDefault() {
        assertEquals(0, NumberInput.parseAsInt(null, 0));
        assertEquals(0, NumberInput.parseAsInt("", 0));
    }
    
    @Test
    public void testParseAsLong() {
        assertEquals(123L, NumberInput.parseAsLong("123", 0L));
        assertEquals(999L, NumberInput.parseAsLong("999", 0L));
    }
    
    @Test
    public void testParseAsLongWithDefault() {
        assertEquals(0L, NumberInput.parseAsLong(null, 0L));
        assertEquals(0L, NumberInput.parseAsLong("", 0L));
    }
    
    @Test
    public void testParseAsDouble() {
        assertEquals(123.45, NumberInput.parseAsDouble("123.45", 0.0), 0.001);
        assertEquals(999.999, NumberInput.parseAsDouble("999.999", 0.0), 0.001);
    }
    
    @Test
    public void testParseAsDoubleWithDefault() {
        assertEquals(0.0, NumberInput.parseAsDouble(null, 0.0), 0.001);
        assertEquals(0.0, NumberInput.parseAsDouble("", 0.0), 0.001);
    }
    
    @Test
    public void testParseDouble() {
        assertEquals(123.45, NumberInput.parseDouble("123.45"), 0.001);
        assertEquals(999.999, NumberInput.parseDouble("999.999"), 0.001);
    }
    
    @Test
    public void testParseBigDecimal() {
        assertEquals(new BigDecimal("123.45"), NumberInput.parseBigDecimal("123.45"));
        assertEquals(new BigDecimal("999.999"), NumberInput.parseBigDecimal("999.999"));
    }
    
    @Test
    public void testParseBigDecimalWithCharArray() {
        char[] buffer = {'1', '2', '3', '.', '4', '5'};
        assertEquals(new BigDecimal("123.45"), NumberInput.parseBigDecimal(buffer));
    }
    
    @Test
    public void testParseBigDecimalWithCharArrayAndOffset() {
        char[] buffer = {'a', 'b', 'c', '1', '2', '3', '.', '4', '5', 'd', 'e', 'f'};
        assertEquals(new BigDecimal("123.45"), NumberInput.parseBigDecimal(buffer, 3, 6));
    }
}