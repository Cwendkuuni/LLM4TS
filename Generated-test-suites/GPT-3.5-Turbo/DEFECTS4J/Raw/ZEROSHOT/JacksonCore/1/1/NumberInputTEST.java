import static org.junit.Assert.*;
import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        // Test case 1
        char[] digitChars1 = {'1', '2', '3'};
        int offset1 = 0;
        int len1 = 3;
        int result1 = NumberInput.parseInt(digitChars1, offset1, len1);
        assertEquals(123, result1);
        
        // Test case 2
        char[] digitChars2 = {'9', '8', '7'};
        int offset2 = 0;
        int len2 = 3;
        int result2 = NumberInput.parseInt(digitChars2, offset2, len2);
        assertEquals(987, result2);
    }

    @Test
    public void testParseIntString() {
        // Test case 1
        String str1 = "123";
        int result1 = NumberInput.parseInt(str1);
        assertEquals(123, result1);
        
        // Test case 2
        String str2 = "987";
        int result2 = NumberInput.parseInt(str2);
        assertEquals(987, result2);
    }

    @Test
    public void testParseLong() {
        // Test case 1
        char[] digitChars1 = {'1', '2', '3'};
        int offset1 = 0;
        int len1 = 3;
        long result1 = NumberInput.parseLong(digitChars1, offset1, len1);
        assertEquals(123L, result1);
        
        // Test case 2
        char[] digitChars2 = {'9', '8', '7'};
        int offset2 = 0;
        int len2 = 3;
        long result2 = NumberInput.parseLong(digitChars2, offset2, len2);
        assertEquals(987L, result2);
    }

    @Test
    public void testParseLongString() {
        // Test case 1
        String str1 = "123";
        long result1 = NumberInput.parseLong(str1);
        assertEquals(123L, result1);
        
        // Test case 2
        String str2 = "987";
        long result2 = NumberInput.parseLong(str2);
        assertEquals(987L, result2);
    }

    @Test
    public void testInLongRangeChar() {
        // Test case 1
        char[] digitChars1 = {'1', '2', '3'};
        int offset1 = 0;
        int len1 = 3;
        boolean negative1 = false;
        boolean result1 = NumberInput.inLongRange(digitChars1, offset1, len1, negative1);
        assertTrue(result1);
        
        // Test case 2
        char[] digitChars2 = {'9', '8', '7'};
        int offset2 = 0;
        int len2 = 3;
        boolean negative2 = false;
        boolean result2 = NumberInput.inLongRange(digitChars2, offset2, len2, negative2);
        assertTrue(result2);
    }

    @Test
    public void testInLongRangeString() {
        // Test case 1
        String numberStr1 = "123";
        boolean negative1 = false;
        boolean result1 = NumberInput.inLongRange(numberStr1, negative1);
        assertTrue(result1);
        
        // Test case 2
        String numberStr2 = "987";
        boolean negative2 = false;
        boolean result2 = NumberInput.inLongRange(numberStr2, negative2);
        assertTrue(result2);
    }

    @Test
    public void testParseAsInt() {
        // Test case 1
        String input1 = "123";
        int defaultValue1 = 0;
        int result1 = NumberInput.parseAsInt(input1, defaultValue1);
        assertEquals(123, result1);
        
        // Test case 2
        String input2 = "987";
        int defaultValue2 = 0;
        int result2 = NumberInput.parseAsInt(input2, defaultValue2);
        assertEquals(987, result2);
    }

    @Test
    public void testParseAsLong() {
        // Test case 1
        String input1 = "123";
        long defaultValue1 = 0L;
        long result1 = NumberInput.parseAsLong(input1, defaultValue1);
        assertEquals(123L, result1);
        
        // Test case 2
        String input2 = "987";
        long defaultValue2 = 0L;
        long result2 = NumberInput.parseAsLong(input2, defaultValue2);
        assertEquals(987L, result2);
    }

    @Test
    public void testParseAsDouble() {
        // Test case 1
        String input1 = "1.23";
        double defaultValue1 = 0.0;
        double result1 = NumberInput.parseAsDouble(input1, defaultValue1);
        assertEquals(1.23, result1, 0.0001);
        
        // Test case 2
        String input2 = "9.87";
        double defaultValue2 = 0.0;
        double result2 = NumberInput.parseAsDouble(input2, defaultValue2);
        assertEquals(9.87, result2, 0.0001);
    }

    @Test
    public void testParseDouble() {
        // Test case 1
        String numStr1 = "1.23";
        double result1 = NumberInput.parseDouble(numStr1);
        assertEquals(1.23, result1, 0.0001);
        
        // Test case 2
        String numStr2 = "9.87";
        double result2 = NumberInput.parseDouble(numStr2);
        assertEquals(9.87, result2, 0.0001);
    }

    @Test
    public void testParseBigDecimalString() {
        // Test case 1
        String numStr1 = "1.23";
        BigDecimal result1 = NumberInput.parseBigDecimal(numStr1);
        assertEquals(new BigDecimal("1.23"), result1);
        
        // Test case 2
        String numStr2 = "9.87";
        BigDecimal result2 = NumberInput.parseBigDecimal(numStr2);
        assertEquals(new BigDecimal("9.87"), result2);
    }

    @Test
    public void testParseBigDecimalCharArray() {
        // Test case 1
        char[] buffer1 = {'1', '.', '2', '3'};
        BigDecimal result1 = NumberInput.parseBigDecimal(buffer1);
        assertEquals(new BigDecimal("1.23"), result1);
        
        // Test case 2
        char[] buffer2 = {'9', '.', '8', '7'};
        BigDecimal result2 = NumberInput.parseBigDecimal(buffer2);
        assertEquals(new BigDecimal("9.87"), result2);
    }

    @Test
    public void testParseBigDecimalCharArrayIntInt() {
        // Test case 1
        char[] buffer1 = {'1', '.', '2', '3'};
        int offset1 = 0;
        int len1 = 4;
        BigDecimal result1 = NumberInput.parseBigDecimal(buffer1, offset1, len1);
        assertEquals(new BigDecimal("1.23"), result1);
        
        // Test case 2
        char[] buffer2 = {'9', '.', '8', '7'};
        int offset2 = 0;
        int len2 = 4;
        BigDecimal result2 = NumberInput.parseBigDecimal(buffer2, offset2, len2);
        assertEquals(new BigDecimal("9.87"), result2);
    }

}