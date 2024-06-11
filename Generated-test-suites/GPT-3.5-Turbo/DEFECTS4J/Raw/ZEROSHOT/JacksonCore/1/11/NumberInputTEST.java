import org.junit.Test;
import java.math.BigDecimal;

public class NumberInputTest {

    @Test
    public void testParseInt() {
        char[] digitChars = "12345".toCharArray();
        int result = NumberInput.parseInt(digitChars, 0, 5);
        assert result == 12345;

        digitChars = "0".toCharArray();
        result = NumberInput.parseInt(digitChars, 0, 1);
        assert result == 0;

        digitChars = "987654321".toCharArray();
        result = NumberInput.parseInt(digitChars, 0, 9);
        assert result == 987654321;
    }

    @Test
    public void testParseIntString() {
        int result = NumberInput.parseInt("12345");
        assert result == 12345;

        result = NumberInput.parseInt("0");
        assert result == 0;

        result = NumberInput.parseInt("987654321");
        assert result == 987654321;
    }

    @Test
    public void testParseLong() {
        char[] digitChars = "123456789012345678".toCharArray();
        long result = NumberInput.parseLong(digitChars, 0, 18);
        assert result == 123456789012345678L;

        digitChars = "0".toCharArray();
        result = NumberInput.parseLong(digitChars, 0, 1);
        assert result == 0L;

        digitChars = "987654321098765432".toCharArray();
        result = NumberInput.parseLong(digitChars, 0, 18);
        assert result == 987654321098765432L;
    }

    @Test
    public void testParseLongString() {
        long result = NumberInput.parseLong("123456789012345678");
        assert result == 123456789012345678L;

        result = NumberInput.parseLong("0");
        assert result == 0L;

        result = NumberInput.parseLong("987654321098765432");
        assert result == 987654321098765432L;
    }

    @Test
    public void testInLongRange() {
        char[] digitChars = "123456789012345678".toCharArray();
        boolean result = NumberInput.inLongRange(digitChars, 0, 18, false);
        assert result == true;

        digitChars = "1111111111".toCharArray();
        result = NumberInput.inLongRange(digitChars, 0, 10, true);
        assert result == true;

        digitChars = "9999999999".toCharArray();
        result = NumberInput.inLongRange(digitChars, 0, 10, false);
        assert result == true;
    }

    @Test
    public void testInLongRangeString() {
        boolean result = NumberInput.inLongRange("123456789012345678", false);
        assert result == true;

        result = NumberInput.inLongRange("1111111111", true);
        assert result == true;

        result = NumberInput.inLongRange("9999999999", false);
        assert result == true;
    }

    @Test
    public void testParseAsInt() {
        int result = NumberInput.parseAsInt("12345", 0);
        assert result == 12345;

        result = NumberInput.parseAsInt("+12345", 0);
        assert result == 12345;

        result = NumberInput.parseAsInt("-12345", 0);
        assert result == -12345;

        result = NumberInput.parseAsInt("abc", 0);
        assert result == 0;

        result = NumberInput.parseAsInt("123.45", 0);
        assert result == 123;

        result = NumberInput.parseAsInt(null, 0);
        assert result == 0;
    }

    @Test
    public void testParseAsLong() {
        long result = NumberInput.parseAsLong("123456789012345678", 0L);
        assert result == 123456789012345678L;

        result = NumberInput.parseAsLong("+123456789012345678", 0L);
        assert result == 123456789012345678L;

        result = NumberInput.parseAsLong("-123456789012345678", 0L);
        assert result == -123456789012345678L;

        result = NumberInput.parseAsLong("abc", 0L);
        assert result == 0L;

        result = NumberInput.parseAsLong("123.45", 0L);
        assert result == 123L;

        result = NumberInput.parseAsLong(null, 0L);
        assert result == 0L;
    }

    @Test
    public void testParseAsDouble() {
        double result = NumberInput.parseAsDouble("12345.6789", 0.0);
        assert result == 12345.6789;

        result = NumberInput.parseAsDouble("+12345.6789", 0.0);
        assert result == 12345.6789;

        result = NumberInput.parseAsDouble("-12345.6789", 0.0);
        assert result == -12345.6789;

        result = NumberInput.parseAsDouble("abc", 0.0);
        assert result == 0.0;

        result = NumberInput.parseAsDouble(null, 0.0);
        assert result == 0.0;
    }

    @Test
    public void testParseDouble() {
        double result = NumberInput.parseDouble("12345.6789");
        assert result == 12345.6789;

        result = NumberInput.parseDouble("0");
        assert result == 0.0;

        result = NumberInput.parseDouble("NASTY_SMALL_DOUBLE");
        assert result == Double.MIN_VALUE;
    }

    @Test
    public void testParseBigDecimalString() {
        BigDecimal result = NumberInput.parseBigDecimal("12345.6789");
        assert result.equals(new BigDecimal("12345.6789"));

        result = NumberInput.parseBigDecimal("0");
        assert result.equals(new BigDecimal("0"));

        result = NumberInput.parseBigDecimal("NASTY_SMALL_DOUBLE");
        assert result.equals(new BigDecimal("2.2250738585072012e-308"));
    }

    @Test
    public void testParseBigDecimalCharArrayIntInt() {
        char[] buffer = "12345.6789".toCharArray();
        BigDecimal result = NumberInput.parseBigDecimal(buffer, 0, 10);
        assert result.equals(new BigDecimal("12345.6789"));

        buffer = "0".toCharArray();
        result = NumberInput.parseBigDecimal(buffer, 0, 1);
        assert result.equals(new BigDecimal("0"));

        buffer = "NASTY_SMALL_DOUBLE".toCharArray();
        result = NumberInput.parseBigDecimal(buffer, 0, 18);
        assert result.equals(new BigDecimal("2.2250738585072012e-308"));
    }
}