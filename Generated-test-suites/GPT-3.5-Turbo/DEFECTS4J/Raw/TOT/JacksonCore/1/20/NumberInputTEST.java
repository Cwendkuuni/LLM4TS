import static org.junit.Assert.*;
import org.junit.Test;

public class NumberInputTest {

    // Test cases for parseInt(char[] digitChars, int offset, int len) method
    @Test
    public void testParseInt() {
        char[] digitChars = {'1', '2', '3'};
        int offset = 0;
        int len = 3;
        int expectedResult = 123;
        int result = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(expectedResult, result);
    }
    
    // Test cases for parseInt(String str) method
    @Test
    public void testParseInt2() {
        String str = "123";
        int expectedResult = 123;
        int result = NumberInput.parseInt(str);
        assertEquals(expectedResult, result);
    }
    
    // Test cases for parseInt(String str) method with negative value
    @Test
    public void testParseInt3() {
        String str = "-123";
        int expectedResult = -123;
        int result = NumberInput.parseInt(str);
        assertEquals(expectedResult, result);
    }
    
    // Test cases for parseLong(char[] digitChars, int offset, int len) method
    @Test
    public void testParseLong() {
        char[] digitChars = {'1', '2', '3'};
        int offset = 0;
        int len = 3;
        long expectedResult = 123L;
        long result = NumberInput.parseLong(digitChars, offset, len);
        assertEquals(expectedResult, result);
    }
    
    // Test cases for parseLong(String str) method
    @Test
    public void testParseLong2() {
        String str = "123";
        long expectedResult = 123L;
        long result = NumberInput.parseLong(str);
        assertEquals(expectedResult, result);
    }
    
    // Test cases for parseLong(String str) method with negative value
    @Test
    public void testParseLong3() {
        String str = "-123";
        long expectedResult = -123L;
        long result = NumberInput.parseLong(str);
        assertEquals(expectedResult, result);
    }
    
    // Test cases for inLongRange(char[] digitChars, int offset, int len, boolean negative) method
    @Test
    public void testInLongRange() {
        char[] digitChars = {'1', '0', '0', '0', '0', '0', '0', '0', '0', '1'};
        int offset = 0;
        int len = 10;
        boolean negative = true;
        boolean expectedResult = true;
        boolean result = NumberInput.inLongRange(digitChars, offset, len, negative);
        assertEquals(expectedResult, result);
    }
    
    // Test cases for inLongRange(String numberStr, boolean negative) method
    @Test
    public void testInLongRange2() {
        String numberStr = "1000000001";
        boolean negative = true;
        boolean expectedResult = true;
        boolean result = NumberInput.inLongRange(numberStr, negative);
        assertEquals(expectedResult, result);
    }
    
    // Test cases for parseAsInt(String input, int defaultValue) method
    @Test
    public void testParseAsInt() {
        String input = "123";
        int defaultValue = 0;
        int expectedResult = 123;
        int result = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(expectedResult, result);
    }
    
    // Test cases for parseAsLong(String input, long defaultValue) method
    @Test
    public void testParseAsLong() {
        String input = "123";
        long defaultValue = 0L;
        long expectedResult = 123L;
        long result = NumberInput.parseAsLong(input, defaultValue);
        assertEquals(expectedResult, result);
    }
    
    // Test cases for parseAsDouble(String input, double defaultValue) method
    @Test
    public void testParseAsDouble() {
        String input = "123.45";
        double defaultValue = 0.0;
        double expectedResult = 123.45;
        double result = NumberInput.parseAsDouble(input, defaultValue);
        assertEquals(expectedResult, result, 0.0001);
    }
    
    // Test cases for parseDouble(String numStr) method
    @Test
    public void testParseDouble() {
        String numStr = "123.45";
        double expectedResult = 123.45;
        double result = NumberInput.parseDouble(numStr);
        assertEquals(expectedResult, result, 0.0001);
    }
    
    // Test cases for parseBigDecimal(String numStr) method
    @Test
    public void testParseBigDecimal() {
        String numStr = "123.45";
        BigDecimal expectedResult = new BigDecimal("123.45");
        BigDecimal result = NumberInput.parseBigDecimal(numStr);
        assertEquals(expectedResult, result);
    }
    
    // Test cases for parseBigDecimal(char[] buffer) method
    @Test
    public void testParseBigDecimal2() {
        char[] buffer = {'1', '2', '3', '.', '4', '5'};
        BigDecimal expectedResult = new BigDecimal("123.45");
        BigDecimal result = NumberInput.parseBigDecimal(buffer);
        assertEquals(expectedResult, result);
    }
    
    // Test cases for parseBigDecimal(char[] buffer, int offset, int len) method
    @Test
    public void testParseBigDecimal3() {
        char[] buffer = {'1', '2', '3', '.', '4', '5'};
        int offset = 0;
        int len = 6;
        BigDecimal expectedResult = new BigDecimal("123.45");
        BigDecimal result = NumberInput.parseBigDecimal(buffer, offset, len);
        assertEquals(expectedResult, result);
    }

}