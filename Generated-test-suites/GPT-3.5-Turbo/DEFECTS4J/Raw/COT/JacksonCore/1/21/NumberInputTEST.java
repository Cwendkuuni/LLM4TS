
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        String numStr = "123456";
        char[] digits = numStr.toCharArray();
        int offset = 0;
        int len = digits.length;
        
        int parsedInt = NumberInput.parseInt(digits, offset, len);
        
        assertEquals(123456, parsedInt);
    }
    
    @Test
    public void testParseIntWithLeadingZero() {
        String numStr = "01234";
        char[] digits = numStr.toCharArray();
        int offset = 0;
        int len = digits.length;
        
        int parsedInt = NumberInput.parseInt(digits, offset, len);
        
        assertEquals(1234, parsedInt);
    }
    
    @Test
    public void testParseIntWithString() {
        String numStr = "123456";
        
        int parsedInt = NumberInput.parseInt(numStr);
        
        assertEquals(123456, parsedInt);
    }
    
    @Test
    public void testParseLong() {
        String numStr = "123456789";
        char[] digits = numStr.toCharArray();
        int offset = 0;
        int len = digits.length;
        
        long parsedLong = NumberInput.parseLong(digits, offset, len);
        
        assertEquals(123456789, parsedLong);
    }
    
    @Test
    public void testParseLongWithLeadingZero() {
        String numStr = "012345678";
        char[] digits = numStr.toCharArray();
        int offset = 0;
        int len = digits.length;
        
        long parsedLong = NumberInput.parseLong(digits, offset, len);
        
        assertEquals(12345678, parsedLong);
    }
    
    @Test
    public void testParseLongWithString() {
        String numStr = "123456789";
        
        long parsedLong = NumberInput.parseLong(numStr);
        
        assertEquals(123456789, parsedLong);
    }
    
    @Test
    public void testInLongRange() {
        String numStr = "1234567890";
        char[] digits = numStr.toCharArray();
        int offset = 0;
        int len = digits.length;
        
        boolean inRange = NumberInput.inLongRange(digits, offset, len, false);
        
        assertTrue(inRange);
    }
    
    @Test
    public void testInLongRangeOutOfRange() {
        String numStr = "12345678900";
        char[] digits = numStr.toCharArray();
        int offset = 0;
        int len = digits.length;
        
        boolean inRange = NumberInput.inLongRange(digits, offset, len, false);
        
        assertFalse(inRange);
    }
    
    @Test
    public void testInLongRangeWithString() {
        String numStr = "1234567890";
        
        boolean inRange = NumberInput.inLongRange(numStr, false);
        
        assertTrue(inRange);
    }
    
    @Test
    public void testInLongRangeOutOfRangeWithString() {
        String numStr = "12345678900";
        
        boolean inRange = NumberInput.inLongRange(numStr, false);
        
        assertFalse(inRange);
    }
    
    @Test
    public void testParseAsInt() {
        String input = "123";
        int defaultValue = 0;
        
        int parsedInt = NumberInput.parseAsInt(input, defaultValue);
        
        assertEquals(123, parsedInt);
    }
    
    @Test
    public void testParseAsIntWithDefaultValue() {
        String input = "abc";
        int defaultValue = 456;
        
        int parsedInt = NumberInput.parseAsInt(input, defaultValue);
        
        assertEquals(456, parsedInt);
    }
    
    @Test
    public void testParseAsLong() {
        String input = "123";
        long defaultValue = 0L;
        
        long parsedLong = NumberInput.parseAsLong(input, defaultValue);
        
        assertEquals(123L, parsedLong);
    }
    
    @Test
    public void testParseAsLongWithDefaultValue() {
        String input = "abc";
        long defaultValue = 456L;
        
        long parsedLong = NumberInput.parseAsLong(input, defaultValue);
        
        assertEquals(456L, parsedLong);
    }
    
    @Test
    public void testParseAsDouble() {
        String input = "123.45";
        double defaultValue = 0.0;
        
        double parsedDouble = NumberInput.parseAsDouble(input, defaultValue);
        
        assertEquals(123.45, parsedDouble, 0.001);
    }
    
    @Test
    public void testParseAsDoubleWithDefaultValue() {
        String input = "abc";
        double defaultValue = 456.78;
        
        double parsedDouble = NumberInput.parseAsDouble(input, defaultValue);
        
        assertEquals(456.78, parsedDouble, 0.001);
    }
    
    @Test
    public void testParseDouble() {
        String numStr = "123.45";
        
        double parsedDouble = NumberInput.parseDouble(numStr);
        
        assertEquals(123.45, parsedDouble, 0.001);
    }
    
    @Test
    public void testParseBigDecimal() {
        String numStr = "123.45";
        
        BigDecimal parsedBigDecimal = NumberInput.parseBigDecimal(numStr);
        
        assertEquals(new BigDecimal("123.45"), parsedBigDecimal);
    }
    
    @Test
    public void testParseBigDecimalWithCharArray() {
        char[] buffer = "123.45".toCharArray();
        
        BigDecimal parsedBigDecimal = NumberInput.parseBigDecimal(buffer);
        
        assertEquals(new BigDecimal("123.45"), parsedBigDecimal);
    }
    
    @Test
    public void testParseBigDecimalWithCharArrayAndOffsetAndLen() {
        char[] buffer = "abc123.45def".toCharArray();
        int offset = 3;
        int len = 6;
        
        BigDecimal parsedBigDecimal = NumberInput.parseBigDecimal(buffer, offset, len);
        
        assertEquals(new BigDecimal("123.45"), parsedBigDecimal);
    }
}

