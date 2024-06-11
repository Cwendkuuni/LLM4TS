import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseInt_withValidInput_returnsIntegerValue() {
        assertEquals(123456789, NumberInput.parseInt("123456789"));
    }
    
    @Test
    public void testParseInt_withEmptyString_returnsDefaultValue() {
        assertEquals(0, NumberInput.parseInt(""));
    }
    
    @Test
    public void testParseInt_withNullInput_returnsDefaultValue() {
        assertEquals(0, NumberInput.parseInt(null));
    }
    
    @Test(expected = NumberFormatException.class)
    public void testParseInt_withInvalidInput_throwsNumberFormatException() {
        NumberInput.parseInt("abc");
    }
    
    @Test
    public void testParseLong_withValidInput_returnsLongValue() {
        assertEquals(123456789012345678L, NumberInput.parseLong("123456789012345678"));
    }
    
    @Test
    public void testParseLong_withEmptyString_returnsDefaultValue() {
        assertEquals(0L, NumberInput.parseLong(""));
    }
    
    @Test
    public void testParseLong_withNullInput_returnsDefaultValue() {
        assertEquals(0L, NumberInput.parseLong(null));
    }
    
    @Test(expected = NumberFormatException.class)
    public void testParseLong_withInvalidInput_throwsNumberFormatException() {
        NumberInput.parseLong("abc");
    }
    
    @Test
    public void testInLongRange_withValidInput_returnsTrue() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        assertTrue(NumberInput.inLongRange(digitChars, 0, 10, false));
    }
    
    @Test
    public void testInLongRange_withInValidInput_returnsFalse() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        assertFalse(NumberInput.inLongRange(digitChars, 0, 11, false));
    }
    
    @Test
    public void testParseAsInt_withValidInput_returnsIntegerValue() {
        assertEquals(123456789, NumberInput.parseAsInt("123456789", 0));
    }
    
    @Test
    public void testParseAsInt_withEmptyString_returnsDefaultValue() {
        assertEquals(0, NumberInput.parseAsInt("", 0));
    }
    
    @Test
    public void testParseAsInt_withNullInput_returnsDefaultValue() {
        assertEquals(0, NumberInput.parseAsInt(null, 0));
    }
    
    @Test
    public void testParseAsInt_withInvalidInput_parsesAsDoubleAndCoerceToInt() {
        assertEquals(123, NumberInput.parseAsInt("12.3", 0));
    }
    
    @Test
    public void testParseAsLong_withValidInput_returnsLongValue() {
        assertEquals(123456789012345678L, NumberInput.parseAsLong("123456789012345678", 0L));
    }
    
    @Test
    public void testParseAsLong_withEmptyString_returnsDefaultValue() {
        assertEquals(0L, NumberInput.parseAsLong("", 0L));
    }
    
    @Test
    public void testParseAsLong_withNullInput_returnsDefaultValue() {
        assertEquals(0L, NumberInput.parseAsLong(null, 0L));
    }
    
    @Test
    public void testParseAsLong_withInvalidInput_parsesAsDoubleAndCoerceToLong() {
        assertEquals(123L, NumberInput.parseAsLong("12.3", 0L));
    }
    
    @Test
    public void testParseAsDouble_withValidInput_returnsDoubleValue() {
        assertEquals(12.34, NumberInput.parseAsDouble("12.34", 0.0), 0.0001);
    }
    
    @Test
    public void testParseAsDouble_withEmptyString_returnsDefaultValue() {
        assertEquals(0.0, NumberInput.parseAsDouble("", 0.0), 0.0001);
    }
    
    @Test
    public void testParseAsDouble_withNullInput_returnsDefaultValue() {
        assertEquals(0.0, NumberInput.parseAsDouble(null, 0.0), 0.0001);
    }
    
    @Test(expected = NumberFormatException.class)
    public void testParseAsDouble_withInvalidInput_throwsNumberFormatException() {
        NumberInput.parseAsDouble("abc", 0.0);
    }
    
    @Test
    public void testParseBigDecimal_withValidInput_returnsBigDecimal() {
        assertEquals(new BigDecimal("12345678901234567890"), NumberInput.parseBigDecimal("12345678901234567890"));
    }
    
    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimal_withInvalidInput_throwsNumberFormatException() {
        NumberInput.parseBigDecimal("abc");
    }
    
    @Test
    public void testParseBigDecimal_withCharArray_returnsBigDecimal() {
        char[] buffer = "123456789".toCharArray();
        assertEquals(new BigDecimal("123456789"), NumberInput.parseBigDecimal(buffer, 0, buffer.length));
    }

}