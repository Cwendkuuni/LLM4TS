import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormatTest {

    private FastDateFormat fastDateFormat;

    @Before
    public void setUp() {
        fastDateFormat = FastDateFormat.getInstance();
    }

    @Test
    public void testFormatObject_withValidInput_shouldReturnFormattedString() {
        Object obj = new Object();
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);

        StringBuffer result = fastDateFormat.format(obj, toAppendTo, pos);

        Assert.assertNotNull(result);
        // Additional asserts based on expected behavior
    }

    @Test
    public void testFormatLong_withValidInput_shouldReturnFormattedString() {
        long millis = 1594233600000L;

        String result = fastDateFormat.format(millis);

        Assert.assertNotNull(result);
        // Additional asserts based on expected behavior
    }

    @Test
    public void testFormatDate_withValidInput_shouldReturnFormattedString() {
        Date date = new Date();

        String result = fastDateFormat.format(date);

        Assert.assertNotNull(result);
        // Additional asserts based on expected behavior
    }

    @Test
    public void testFormatCalendar_withValidInput_shouldReturnFormattedString() {
        Calendar calendar = Calendar.getInstance();

        String result = fastDateFormat.format(calendar);

        Assert.assertNotNull(result);
        // Additional asserts based on expected behavior
    }

    @Test
    public void testFormatLongStringBuffer_withValidInput_shouldReturnFormattedStringBuffer() {
        long millis = 1594233600000L;
        StringBuffer buf = new StringBuffer();

        StringBuffer result = fastDateFormat.format(millis, buf);

        Assert.assertNotNull(result);
        // Additional asserts based on expected behavior
    }

    @Test
    public void testFormatDateStringBuffer_withValidInput_shouldReturnFormattedStringBuffer() {
        Date date = new Date();
        StringBuffer buf = new StringBuffer();

        StringBuffer result = fastDateFormat.format(date, buf);

        Assert.assertNotNull(result);
        // Additional asserts based on expected behavior
    }

    @Test
    public void testFormatCalendarStringBuffer_withValidInput_shouldReturnFormattedStringBuffer() {
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();

        StringBuffer result = fastDateFormat.format(calendar, buf);

        Assert.assertNotNull(result);
        // Additional asserts based on expected behavior
    }

    @Test
    public void testParse_withValidInput_shouldReturnParsedDate() throws ParseException {
        String source = "2020-07-09";

        Date result = fastDateFormat.parse(source);

        Assert.assertNotNull(result);
        // Additional asserts based on expected behavior
    }

    @Test
    public void testParse_withValidInput_shouldReturnParsedDateWithParsePosition() {
        String source = "2020-07-09";
        ParsePosition pos = new ParsePosition(0);

        Date result = fastDateFormat.parse(source, pos);

        Assert.assertNotNull(result);
        // Additional asserts based on expected behavior
    }

    @Test
    public void testParseObject_withValidInput_shouldReturnParsedObjectWithParsePosition() {
        String source = "2020-07-09";
        ParsePosition pos = new ParsePosition(0);

        Object result = fastDateFormat.parseObject(source, pos);

        Assert.assertNotNull(result);
        // Additional asserts based on expected behavior
    }

    @Test
    public void testGetPattern_shouldReturnPatternUsed() {
        String pattern = fastDateFormat.getPattern();

        Assert.assertNotNull(pattern);
        // Additional asserts based on expected behavior
    }

    @Test
    public void testGetTimeZone_shouldReturnTimeZoneUsed() {
        TimeZone timeZone = fastDateFormat.getTimeZone();

        Assert.assertNotNull(timeZone);
        // Additional asserts based on expected behavior
    }

    @Test
    public void testGetLocale_shouldReturnLocaleUsed() {
        Locale locale = fastDateFormat.getLocale();

        Assert.assertNotNull(locale);
        // Additional asserts based on expected behavior
    }

    @Test
    public void testGetMaxLengthEstimate_shouldReturnMaxLengthEstimate() {
        int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();

        // Additional asserts based on expected behavior
    }

    @Test
    public void testEquals_withEqualObject_shouldReturnTrue() {
        Object obj = new FastDateFormat("yyyy-MM-dd", TimeZone.getTimeZone("UTC"), Locale.US);

        boolean result = fastDateFormat.equals(obj);

        Assert.assertTrue(result);
    }

    @Test
    public void testEquals_withDifferentObject_shouldReturnFalse() {
        Object obj = new Object();

        boolean result = fastDateFormat.equals(obj);

        Assert.assertFalse(result);
    }

    @Test
    public void testHashCode_withEqualObject_shouldReturnEqualHashCode() {
        FastDateFormat other = new FastDateFormat("yyyy-MM-dd", TimeZone.getTimeZone("UTC"), Locale.US);

        int hashCode1 = fastDateFormat.hashCode();
        int hashCode2 = other.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString_shouldReturnStringRepresentation() {
        String str = fastDateFormat.toString();

        Assert.assertNotNull(str);
        // Additional asserts based on expected behavior
    }

    @Test
    public void testApplyRules_withValidInput_shouldReturnStringBuffer() {
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();

        StringBuffer result = fastDateFormat.applyRules(calendar, buf);

        Assert.assertNotNull(result);
        // Additional asserts based on expected behavior
    }
}