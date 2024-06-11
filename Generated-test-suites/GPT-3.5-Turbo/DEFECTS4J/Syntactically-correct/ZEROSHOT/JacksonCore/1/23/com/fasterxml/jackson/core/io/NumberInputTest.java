import static org.junit.Assert.*;
import org.junit.Test;

public class NumberInputTest {

    @Test
    public void testParseInt_charArray() {
        char[] digitChars = {'1','2','3','4','5'};
        int offset = 0;
        int len = digitChars.length;
        int expected = Integer.parseInt(new String(digitChars));
        int result = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(expected, result);
    }

    @Test
    public void testParseInt_string() {
        String str = "12345";
        int expected = Integer.parseInt(str);
        int result = NumberInput.parseInt(str);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLong_charArray() {
        char[] digitChars = {'1','2','3','4','5'};
        int offset = 0;
        int len = digitChars.length;
        long expected = Long.parseLong(new String(digitChars));
        long result = NumberInput.parseLong(digitChars, offset, len);
        assertEquals(expected, result);
    }

    @Test
    public void testParseLong_string() {
        String str = "12345";
        long expected = Long.parseLong(str);
        long result = NumberInput.parseLong(str);
        assertEquals(expected, result);
    }

    @Test
    public void testInLongRange_charArray() {
        char[] digitChars = {'1','2','3','4','5'};
        int offset = 0;
        int len = digitChars.length;
        boolean negative = false;
        boolean expected = true;
        boolean result = NumberInput.inLongRange(digitChars, offset, len, negative);
        assertEquals(expected, result);
    }

    @Test
    public void testInLongRange_string() {
        String numberStr = "12345";
        boolean negative = false;
        boolean expected = true;
        boolean result = NumberInput.inLongRange(numberStr, negative);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsInt() {
        String input = "12345";
        int defaultValue = 0;
        int expected = Integer.parseInt(input);
        int result = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsLong() {
        String input = "12345";
        long defaultValue = 0;
        long expected = Long.parseLong(input);
        long result = NumberInput.parseAsLong(input, defaultValue);
        assertEquals(expected, result);
    }

    @Test
    public void testParseAsDouble() {
        String input = "123.45";
        double defaultValue = 0;
        double expected = Double.parseDouble(input);
        double result = NumberInput.parseAsDouble(input, defaultValue);
        assertEquals(expected, result, 0.001);
    }

    @Test
    public void testParseDouble() {
        String numStr = "123.45";
        double expected = Double.parseDouble(numStr);
        double result = NumberInput.parseDouble(numStr);
        assertEquals(expected, result, 0.001);
    }

    @Test
    public void testParseBigDecimal_string() {
        String numStr = "123.45";
        BigDecimal expected = new BigDecimal(numStr);
        BigDecimal result = NumberInput.parseBigDecimal(numStr);
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimal_charArray() {
        char[] buffer = {'1','2','3','4','5'};
        BigDecimal expected = new BigDecimal(new String(buffer));
        BigDecimal result = NumberInput.parseBigDecimal(buffer);
        assertEquals(expected, result);
    }

    @Test
    public void testParseBigDecimal_charArray_offset_len() {
        char[] buffer = {'1','2','3','4','5'};
        int offset = 0;
        int len = buffer.length;
        BigDecimal expected = new BigDecimal(new String(buffer,offset,len));
        BigDecimal result = NumberInput.parseBigDecimal(buffer, offset, len);
        assertEquals(expected, result);
    }

}