import static org.junit.Assert.*;
import org.junit.Test;

public class NumberInputTest {
    
    @Test
    public void testParseInt_CharArray_Offset_Len() {
        char[] digits = {'1', '2', '3'};
        int result = NumberInput.parseInt(digits, 0, 3);
        assertEquals(123, result);
    }
    
    @Test
    public void testParseInt_String() {
        int result = NumberInput.parseInt("456");
        assertEquals(456, result);
    }
    
    @Test
    public void testParseLong_CharArray_Offset_Len() {
        char[] digits = {'1', '2', '3', '4', '5', '6'};
        long result = NumberInput.parseLong(digits, 0, 6);
        assertEquals(123456, result);
    }
    
    @Test
    public void testParseLong_String() {
        long result = NumberInput.parseLong("789");
        assertEquals(789, result);
    }
    
    @Test
    public void testInLongRange_CharArray_Offset_Len() {
        char[] digits = {'9', '2', '2', '3', '3', '7', '2', '0', '3', '6'};
        boolean result = NumberInput.inLongRange(digits, 0, 10, false);
        assertTrue(result);
    }
    
    @Test
    public void testInLongRange_String() {
        boolean result = NumberInput.inLongRange("9223372036854775807", false);
        assertTrue(result);
    }
    
    @Test
    public void testParseAsInt() {
        int result = NumberInput.parseAsInt("124", 0);
        assertEquals(124, result);
    }
    
    @Test
    public void testParseAsLong() {
        long result = NumberInput.parseAsLong("345", 0);
        assertEquals(345, result);
    }
    
    @Test
    public void testParseAsDouble() {
        double result = NumberInput.parseAsDouble("3.14", 0.0);
        assertEquals(3.14, result, 0.001);
    }
    
    @Test
    public void testParseDouble() {
        double result = NumberInput.parseDouble("2.718");
        assertEquals(2.718, result, 0.001);
    }
    
    @Test
    public void testParseBigDecimal() {
        BigDecimal result = NumberInput.parseBigDecimal("123.456");
        assertEquals(new BigDecimal("123.456"), result);
    }

}