import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NumberInputTest {
    @Test
    public void testParseInt() {
        char[] digits1 = {'1'};
        char[] digits2 = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        
        assertEquals(1, NumberInput.parseInt(digits1, 0, 1));
        assertEquals(123456789, NumberInput.parseInt(digits2, 0, 9));
    }
    
    @Test
    public void testParseIntWithString() {
        String number1 = "1";
        String number2 = "123456789";
        String number3 = "1234567890";
        
        assertEquals(1, NumberInput.parseInt(number1));
        assertEquals(123456789, NumberInput.parseInt(number2));
        assertEquals(1234567890, NumberInput.parseInt(number3));
    }
    
    @Test
    public void testParseLong() {
        char[] digits1 = {'1', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
        char[] digits2 = {'9', '8', '7', '6', '5', '4', '3', '2', '1'};
        
        assertEquals(1000000000L, NumberInput.parseLong(digits1, 0, 10));
        assertEquals(987654321, NumberInput.parseLong(digits2, 0, 9));
    }
    
    @Test
    public void testParseLongWithString() {
        String number1 = "1000000000";
        String number2 = "987654321";
        String number3 = "1234567890123456789";
        
        assertEquals(1000000000L, NumberInput.parseLong(number1));
        assertEquals(987654321, NumberInput.parseLong(number2));
        assertEquals(1234567890123456789L, NumberInput.parseLong(number3));
    }
    
    @Test
    public void testInLongRangeWithCharArray() {
        char[] digits1 = {'9', '2', '2', '8', '3', '7', '2', '0', '3', '6'};
        char[] digits2 = {'9', '2', '2', '8', '3', '7', '2', '0', '3', '7'};
        char[] digits3 = {'9', '2', '2', '8', '3', '7', '2', '0', '3', '8'};
        
        assertTrue(NumberInput.inLongRange(digits1, 0, 10, false));
        assertTrue(NumberInput.inLongRange(digits2, 0, 10, false));
        assertTrue(NumberInput.inLongRange(digits3, 0, 10, false));
    }
    
    @Test
    public void testInLongRangeWithString() {
        String number1 = "9228372036";
        String number2 = "9228372037";
        String number3 = "9228372038";
        
        assertTrue(NumberInput.inLongRange(number1, false));
        assertTrue(NumberInput.inLongRange(number2, false));
        assertTrue(NumberInput.inLongRange(number3, false));
    }
    
    @Test
    public void testParseAsInt() {
        String input1 = "123";
        String input2 = "-123";
        String input3 = "12.34";
        
        assertEquals(123, NumberInput.parseAsInt(input1, 0));
        assertEquals(-123, NumberInput.parseAsInt(input2, 0));
        assertEquals(12, NumberInput.parseAsInt(input3, 0));
    }
    
    @Test
    public void testParseAsLong() {
        String input1 = "1234567890123456789";
        String input2 = "-1234567890123456789";
        String input3 = "12.34";
        
        assertEquals(1234567890123456789L, NumberInput.parseAsLong(input1, 0L));
        assertEquals(-1234567890123456789L, NumberInput.parseAsLong(input2, 0L));
        assertEquals(12L, NumberInput.parseAsLong(input3, 0L));
    }
    
    @Test
    public void testParseAsDouble() {
        String input1 = "123";
        String input2 = "-123";
        String input3 = "12.34";
        
        assertEquals(123.0, NumberInput.parseAsDouble(input1, 0.0), 0.0);
        assertEquals(-123.0, NumberInput.parseAsDouble(input2, 0.0), 0.0);
        assertEquals(12.34, NumberInput.parseAsDouble(input3, 0.0), 0.0);
    }
    
    @Test
    public void testParseDouble() {
        String numStr1 = "2.2250738585072012e-308";
        String numStr2 = "123.45";
        
        assertEquals(2.2250738585072014E-308, NumberInput.parseDouble(numStr1), 0.0);
        assertEquals(123.45, NumberInput.parseDouble(numStr2), 0.0);
    }
    
    @Test
    public void testParseBigDecimalWithString() {
        String numStr = "123.45";
        assertEquals(new BigDecimal(numStr), NumberInput.parseBigDecimal(numStr));
    }
    
    @Test
    public void testParseBigDecimalWithCharArray() {
        char[] digits = {'1', '2', '3', '.', '4', '5'};
        assertEquals(new BigDecimal("123.45"), NumberInput.parseBigDecimal(digits));
    }
}