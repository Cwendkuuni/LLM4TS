import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        char[] digitChars = {'1', '2', '3'};
        int offset = 0;
        int len = 3;
        
        int result = NumberInput.parseInt(digitChars, offset, len);
        
        assertEquals(123, result);
    }
    
    @Test
    public void testParseInt_String() {
        String str = "456";
        
        int result = NumberInput.parseInt(str);
        
        assertEquals(456, result);
    }
    
    @Test
    public void testParseLong() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'};
        int offset = 0;
        int len = 11;
        
        long result = NumberInput.parseLong(digitChars, offset, len);
        
        assertEquals(12345678901L, result);
    }
    
    @Test
    public void testParseLong_String() {
        String str = "9876543210";
        
        long result = NumberInput.parseLong(str);
        
        assertEquals(9876543210L, result);
    }
    
    @Test
    public void testInLongRange_CharArray() {
        char[] digitChars1 = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int offset1 = 0;
        int len1 = 9;
        boolean negative1 = false;
        
        boolean result1 = NumberInput.inLongRange(digitChars1, offset1, len1, negative1);
        
        assertTrue(result1);
        
        char[] digitChars2 = {'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int offset2 = 0;
        int len2 = 10;
        boolean negative2 = true;
        
        boolean result2 = NumberInput.inLongRange(digitChars2, offset2, len2, negative2);
        
        assertTrue(result2);
        
        char[] digitChars3 = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int offset3 = 0;
        int len3 = 9;
        boolean negative3 = true;
        
        boolean result3 = NumberInput.inLongRange(digitChars3, offset3, len3, negative3);
        
        assertFalse(result3);
    }
    
    @Test
    public void testInLongRange_String() {
        String numberStr1 = "123456789";
        boolean negative1 = false;
        
        boolean result1 = NumberInput.inLongRange(numberStr1, negative1);
        
        assertTrue(result1);
        
        String numberStr2 = "1234567890";
        boolean negative2 = true;
        
        boolean result2 = NumberInput.inLongRange(numberStr2, negative2);
        
        assertTrue(result2);
        
        String numberStr3 = "123456789";
        boolean negative3 = true;
        
        boolean result3 = NumberInput.inLongRange(numberStr3, negative3);
        
        assertFalse(result3);
    }
    
    @Test
    public void testParseAsInt() {
        String input1 = "123";
        int defaultValue1 = 0;
        
        int result1 = NumberInput.parseAsInt(input1, defaultValue1);
        
        assertEquals(123, result1);
        
        String input2 = "";
        int defaultValue2 = 0;
        
        int result2 = NumberInput.parseAsInt(input2, defaultValue2);
        
        assertEquals(0, result2);
        
        String input3 = "abc";
        int defaultValue3 = 10;
        
        int result3 = NumberInput.parseAsInt(input3, defaultValue3);
        
        assertEquals(10, result3);
    }
    
    @Test
    public void testParseAsLong() {
        String input1 = "123456789";
        long defaultValue1 = 0L;
        
        long result1 = NumberInput.parseAsLong(input1, defaultValue1);
        
        assertEquals(123456789L, result1);
        
        String input2 = "";
        long defaultValue2 = 0L;
        
        long result2 = NumberInput.parseAsLong(input2, defaultValue2);
        
        assertEquals(0L, result2);
        
        String input3 = "abc";
        long defaultValue3 = 10L;
        
        long result3 = NumberInput.parseAsLong(input3, defaultValue3);
        
        assertEquals(10L, result3);
    }
    
    @Test
    public void testParseAsDouble() {
        String input1 = "3.14";
        double defaultValue1 = 0.0;
        
        double result1 = NumberInput.parseAsDouble(input1, defaultValue1);
        
        assertEquals(3.14, result1, 0.001);
        
        String input2 = "";
        double defaultValue2 = 0.0;
        
        double result2 = NumberInput.parseAsDouble(input2, defaultValue2);
        
        assertEquals(0.0, result2, 0.001);
        
        String input3 = "abc";
        double defaultValue3 = 10.0;
        
        double result3 = NumberInput.parseAsDouble(input3, defaultValue3);
        
        assertEquals(10.0, result3, 0.001);
    }
    
    @Test
    public void testParseDouble() {
        String numStr1 = "123.45";
        
        double result1 = NumberInput.parseDouble(numStr1);
        
        assertEquals(123.45, result1, 0.001);
        
        String numStr2 = "2.2250738585072012e-308";
        
        double result2 = NumberInput.parseDouble(numStr2);
        
        assertEquals(Double.MIN_VALUE, result2, 0.001);
    }
    
    @Test
    public void testParseBigDecimal_String() {
        String numStr = "123.45";
        
        BigDecimal result = NumberInput.parseBigDecimal(numStr);
        
        assertEquals(new BigDecimal("123.45"), result);
    }
    
    @Test
    public void testParseBigDecimal_CharArray() {
        char[] buffer = {'1', '2', '3', '4', '5'};
        
        BigDecimal result = NumberInput.parseBigDecimal(buffer);
        
        assertEquals(new BigDecimal("12345"), result);
    }
    
    @Test
    public void testParseBigDecimal_CharArray_OffsetAndLen() {
        char[] buffer = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int offset = 1;
        int len = 5;
        
        BigDecimal result = NumberInput.parseBigDecimal(buffer, offset, len);
        
        assertEquals(new BigDecimal("23456"), result);
    }

}