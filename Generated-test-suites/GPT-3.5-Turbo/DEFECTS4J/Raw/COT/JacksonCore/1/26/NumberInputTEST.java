import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseInt_charArray() {
        char[] digitChars = {'1', '2', '3'};
        int offset = 0;
        int len = 3;
        int result = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(123, result);
    }

    @Test
    public void testParseInt_String() {
        String str = "123";
        int result = NumberInput.parseInt(str);
        assertEquals(123, result);
    }

    @Test
    public void testParseLong_charArray() {
        char[] digitChars = {'1', '2', '3', '4'};
        int offset = 0;
        int len = 4;
        long result = NumberInput.parseLong(digitChars, offset, len);
        assertEquals(1234L, result);
    }

    @Test
    public void testParseLong_String() {
        String str = "1234";
        long result = NumberInput.parseLong(str);
        assertEquals(1234L, result);
    }

    @Test
    public void testInLongRange_charArray() {
        char[] digitChars = {'1', '2', '3'};
        int offset = 0;
        int len = 3;
        boolean result = NumberInput.inLongRange(digitChars, offset, len, false);
        assertTrue(result);
    }

    @Test
    public void testInLongRange_String() {
        String numberStr = "123";
        boolean result = NumberInput.inLongRange(numberStr, false);
        assertTrue(result);
    }

    @Test
    public void testParseAsInt() {
        String input = "123";
        int defaultValue = 0;
        int result = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(123, result);
    }

    @Test
    public void testParseAsLong() {
        String input = "123456";
        long defaultValue = 0L;
        long result = NumberInput.parseAsLong(input, defaultValue);
        assertEquals(123456L, result);
    }

    @Test
    public void testParseAsDouble() {
        String input = "123.45";
        double defaultValue = 0.0;
        double result = NumberInput.parseAsDouble(input, defaultValue);
        assertEquals(123.45, result, 0.0001);
    }

    @Test
    public void testParseDouble() {
        String numStr = "123.45";
        double result = NumberInput.parseDouble(numStr);
        assertEquals(123.45, result, 0.0001);
    }

    @Test
    public void testParseBigDecimal_String() {
        String numStr = "123.45";
        BigDecimal result = NumberInput.parseBigDecimal(numStr);
        assertEquals(new BigDecimal("123.45"), result);
    }

    @Test
    public void testParseBigDecimal_charArray() {
        char[] buffer = {'1', '2', '3', '.', '4', '5'};
        BigDecimal result = NumberInput.parseBigDecimal(buffer);
        assertEquals(new BigDecimal("123.45"), result);
    }

    @Test
    public void testParseBigDecimal_charArray_offset_len() {
        char[] buffer = {'1', '2', '3', '4', '5', '.', '6', '7', '8', '9'};
        int offset = 2;
        int len = 6;
        BigDecimal result = NumberInput.parseBigDecimal(buffer, offset, len);
        assertEquals(new BigDecimal("345.6789"), result);
    }
}