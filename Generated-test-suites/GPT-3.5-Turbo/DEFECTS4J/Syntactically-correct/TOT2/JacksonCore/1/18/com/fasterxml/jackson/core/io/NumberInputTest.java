import static org.junit.Assert.*;
import org.junit.Test;

public class NumberInputTest {
    
    @Test
    public void testParseInt_charArray() {
        char[] testChars = {'1', '2', '3'};
        assertEquals(123, NumberInput.parseInt(testChars, 0, 3));
    }
    
    @Test
    public void testParseInt_String() {
        assertEquals(456, NumberInput.parseInt("456"));
    }
    
    @Test
    public void testParseLong_charArray() {
        char[] testChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        assertEquals(1234567890, NumberInput.parseLong(testChars, 0, 10));
    }
    
    @Test
    public void testParseLong_String() {
        assertEquals(9876543210L, NumberInput.parseLong("9876543210"));
    }
    
    @Test
    public void testInLongRange_charArray() {
        char[] testChars = {'9', '2', '2', '3', '7', '2', '8', '5', '8', '5', '0', '7', '2', '0', '1', '2', 'e', '-', '2', '3', '0', '8'};
        assertFalse(NumberInput.inLongRange(testChars, 0, 22, false));
    }
    
    @Test
    public void testInLongRange_String() {
        assertFalse(NumberInput.inLongRange("9223372036850772000", false));
    }
    
    @Test
    public void testParseAsInt() {
        assertEquals(789, NumberInput.parseAsInt("789", -1));
    }
    
    @Test
    public void testParseAsLong() {
        assertEquals(123456, NumberInput.parseAsLong("123456", -1));
    }
    
    @Test
    public void testParseAsDouble() {
        assertEquals(3.14, NumberInput.parseAsDouble("3.14", -1.0), 0.001);
    }
    
    @Test
    public void testParseDouble() {
        assertEquals(2.2250738585072014e-308, NumberInput.parseDouble("2.2250738585072012e-308"), 0.001);
    }
    
    @Test
    public void testParseBigDecimal_String() {
        assertEquals(new BigDecimal("123.456"), NumberInput.parseBigDecimal("123.456"));
    }
    
    @Test
    public void testParseBigDecimal_charArray() {
        char[] testChars = {'1', '2', '3', '.', '4', '5', '6'};
        assertEquals(new BigDecimal("123.456"), NumberInput.parseBigDecimal(testChars));
    }
    
    @Test
    public void testCompleteScenario() {
        // Add more comprehensive scenarios here
    }

}