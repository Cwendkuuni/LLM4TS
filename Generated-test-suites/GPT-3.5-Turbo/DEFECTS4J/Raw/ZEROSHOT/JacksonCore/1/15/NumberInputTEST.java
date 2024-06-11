import org.junit.*;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        char[] digitChars = {'1', '2', '3'};
        int offset = 0;
        int len = digitChars.length;
        int expectedResult = 123;
        int actualResult = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseIntWithNegative() {
        char[] digitChars = {'-', '5', '0'};
        int offset = 0;
        int len = digitChars.length;
        int expectedResult = -50;
        int actualResult = NumberInput.parseInt(digitChars, offset, len);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseIntWithString() {
        String str = "789";
        int expectedResult = 789;
        int actualResult = NumberInput.parseInt(str);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testParseIntWithNegativeString() {
        String str = "-123";
        int expectedResult = -123;
        int actualResult = NumberInput.parseInt(str);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseLong() {
        char[] digitChars = {'1', '2', '3'};
        int offset = 0;
        int len = digitChars.length;
        long expectedResult = 123;
        long actualResult = NumberInput.parseLong(digitChars, offset, len);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseLongWithNegative() {
        char[] digitChars = {'-', '5', '0'};
        int offset = 0;
        int len = digitChars.length;
        long expectedResult = -50;
        long actualResult = NumberInput.parseLong(digitChars, offset, len);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseLongWithString() {
        String str = "789";
        long expectedResult = 789;
        long actualResult = NumberInput.parseLong(str);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testParseLongWithNegativeString() {
        String str = "-123";
        long expectedResult = -123;
        long actualResult = NumberInput.parseLong(str);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testInLongRange() {
        char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int offset = 0;
        int len = digitChars.length;
        boolean negative = false;
        boolean expectedResult = true;
        boolean actualResult = NumberInput.inLongRange(digitChars, offset, len, negative);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testInLongRangeWithNegative() {
        char[] digitChars = {'-', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int offset = 0;
        int len = digitChars.length;
        boolean negative = true;
        boolean expectedResult = true;
        boolean actualResult = NumberInput.inLongRange(digitChars, offset, len, negative);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testInLongRangeWithString() {
        String numberStr = "123456789";
        boolean negative = false;
        boolean expectedResult = true;
        boolean actualResult = NumberInput.inLongRange(numberStr, negative);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testInLongRangeWithNegativeString() {
        String numberStr = "-123456789";
        boolean negative = true;
        boolean expectedResult = true;
        boolean actualResult = NumberInput.inLongRange(numberStr, negative);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseAsInt() {
        String input = "123";
        int defaultValue = 0;
        int expectedResult = 123;
        int actualResult = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseAsIntWithDefault() {
        String input = "";
        int defaultValue = 100;
        int expectedResult = 100;
        int actualResult = NumberInput.parseAsInt(input, defaultValue);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseAsLong() {
        String input = "123";
        long defaultValue = 0;
        long expectedResult = 123;
        long actualResult = NumberInput.parseAsLong(input, defaultValue);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseAsLongWithDefault() {
        String input = "";
        long defaultValue = 100;
        long expectedResult = 100;
        long actualResult = NumberInput.parseAsLong(input, defaultValue);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseAsDouble() {
        String input = "3.14";
        double defaultValue = 0.0;
        double expectedResult = 3.14;
        double actualResult = NumberInput.parseAsDouble(input, defaultValue);
        assertEquals(expectedResult, actualResult, 0.000001);
    }

    @Test
    public void testParseAsDoubleWithDefault() {
        String input = "";
        double defaultValue = 2.71;
        double expectedResult = 2.71;
        double actualResult = NumberInput.parseAsDouble(input, defaultValue);
        assertEquals(expectedResult, actualResult, 0.000001);
    }

    @Test
    public void testParseDouble() {
        String numStr = "3.14";
        double expectedResult = 3.14;
        double actualResult = NumberInput.parseDouble(numStr);
        assertEquals(expectedResult, actualResult, 0.000001);
    }

    @Test
    public void testParseBigDecimalWithString() {
        String numStr = "3.14";
        BigDecimal expectedResult = new BigDecimal("3.14");
        BigDecimal actualResult = NumberInput.parseBigDecimal(numStr);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseBigDecimalWithCharArray() {
        char[] buffer = {'3', '.', '1', '4'};
        BigDecimal expectedResult = new BigDecimal("3.14");
        BigDecimal actualResult = NumberInput.parseBigDecimal(buffer);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseBigDecimalWithOffsetAndLen() {
        char[] buffer = {'3', '.', '1', '4'};
        int offset = 0;
        int len = buffer.length;
        BigDecimal expectedResult = new BigDecimal("3.14");
        BigDecimal actualResult = NumberInput.parseBigDecimal(buffer, offset, len);
        assertEquals(expectedResult, actualResult);
    }

}