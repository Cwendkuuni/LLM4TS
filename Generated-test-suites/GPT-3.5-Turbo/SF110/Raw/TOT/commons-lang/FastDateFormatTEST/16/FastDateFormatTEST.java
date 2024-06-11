import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormatTest {

    private FastDateFormat fastDateFormat;

    @Before
    public void setup() {
        fastDateFormat = new FastDateFormat("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("UTC"), Locale.US);
    }

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        Assert.assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        Assert.assertNotNull(instance);
        Assert.assertEquals("yyyy-MM-dd", instance.getPattern());
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("EST"));
        Assert.assertNotNull(instance);
        Assert.assertEquals(TimeZone.getTimeZone("EST"), instance.getTimeZone());
    }

    @Test
    public void testGetInstanceWithPatternAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
        Assert.assertNotNull(instance);
        Assert.assertEquals(Locale.FRANCE, instance.getLocale());
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("EST"), Locale.FRANCE);
        Assert.assertNotNull(instance);
        Assert.assertEquals(TimeZone.getTimeZone("EST"), instance.getTimeZone());
        Assert.assertEquals(Locale.FRANCE, instance.getLocale());
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        Assert.assertNotNull(instance);
        Assert.assertEquals(FastDateFormat.MEDIUM, instance.getStyle());
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.FRANCE);
        Assert.assertNotNull(instance);
        Assert.assertEquals(FastDateFormat.MEDIUM, instance.getStyle());
        Assert.assertEquals(Locale.FRANCE, instance.getLocale());
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("EST"));
        Assert.assertNotNull(instance);
        Assert.assertEquals(FastDateFormat.MEDIUM, instance.getStyle());
        Assert.assertEquals(TimeZone.getTimeZone("EST"), instance.getTimeZone());
    }

    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("EST"), Locale.FRANCE);
        Assert.assertNotNull(instance);
        Assert.assertEquals(FastDateFormat.MEDIUM, instance.getStyle());
        Assert.assertEquals(TimeZone.getTimeZone("EST"), instance.getTimeZone());
        Assert.assertEquals(Locale.FRANCE, instance.getLocale());
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        Assert.assertNotNull(instance);
        Assert.assertEquals(FastDateFormat.MEDIUM, instance.getStyle());
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, Locale.FRANCE);
        Assert.assertNotNull(instance);
        Assert.assertEquals(FastDateFormat.MEDIUM, instance.getStyle());
        Assert.assertEquals(Locale.FRANCE, instance.getLocale());
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("EST"));
        Assert.assertNotNull(instance);
        Assert.assertEquals(FastDateFormat.MEDIUM, instance.getStyle());
        Assert.assertEquals(TimeZone.getTimeZone("EST"), instance.getTimeZone());
    }

    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("EST"), Locale.FRANCE);
        Assert.assertNotNull(instance);
        Assert.assertEquals(FastDateFormat.MEDIUM, instance.getStyle());
        Assert.assertEquals(TimeZone.getTimeZone("EST"), instance.getTimeZone());
        Assert.assertEquals(Locale.FRANCE, instance.getLocale());
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        Assert.assertNotNull(instance);
        Assert.assertEquals(FastDateFormat.MEDIUM, instance.getDateStyle());
        Assert.assertEquals(FastDateFormat.SHORT, instance.getTimeStyle());
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.FRANCE);
        Assert.assertNotNull(instance);
        Assert.assertEquals(FastDateFormat.MEDIUM, instance.getDateStyle());
        Assert.assertEquals(FastDateFormat.SHORT, instance.getTimeStyle());
        Assert.assertEquals(Locale.FRANCE, instance.getLocale());
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("EST"));
        Assert.assertNotNull(instance);
        Assert.assertEquals(FastDateFormat.MEDIUM, instance.getDateStyle());
        Assert.assertEquals(FastDateFormat.SHORT, instance.getTimeStyle());
        Assert.assertEquals(TimeZone.getTimeZone("EST"), instance.getTimeZone());
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("EST"), Locale.FRANCE);
        Assert.assertNotNull(instance);
        Assert.assertEquals(FastDateFormat.MEDIUM, instance.getDateStyle());
        Assert.assertEquals(FastDateFormat.SHORT, instance.getTimeStyle());
        Assert.assertEquals(TimeZone.getTimeZone("EST"), instance.getTimeZone());
        Assert.assertEquals(Locale.FRANCE, instance.getLocale());
    }

    @Test
    public void testFormatObject() {
        String expected = "2022-01-01 12:00:00";
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 1, 12, 0, 0);
        String result = fastDateFormat.format(calendar.getTime());
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFormatLong() {
        String expected = "2022-01-01 12:00:00";
        long millis = 1641049200000L;
        String result = fastDateFormat.format(millis);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFormatDate() {
        String expected = "2022-01-01 12:00:00";
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 1, 12, 0, 0);
        Date date = calendar.getTime();
        String result = fastDateFormat.format(date);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFormatCalendar() {
        String expected = "2022-01-01 12:00:00";
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 1, 12, 0, 0);
        String result = fastDateFormat.format(calendar);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFormatLongAndStringBuffer() {
        String expected = "2022-01-01 12:00:00";
        long millis = 1641049200000L;
        StringBuffer buffer = new StringBuffer();
        fastDateFormat.format(millis, buffer);
        String result = buffer.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFormatDateAndStringBuffer() {
        String expected = "2022-01-01 12:00:00";
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 1, 12, 0, 0);
        Date date = calendar.getTime();
        StringBuffer buffer = new StringBuffer();
        fastDateFormat.format(date, buffer);
        String result = buffer.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFormatCalendarAndStringBuffer() {
        String expected = "2022-01-01 12:00:00";
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 1, 12, 0, 0);
        StringBuffer buffer = new StringBuffer();
        fastDateFormat.format(calendar, buffer);
        String result = buffer.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testParseString() throws ParseException {
        String source = "2022-01-01 12:00:00";
        Calendar expected = Calendar.getInstance();
        expected.set(2022, Calendar.JANUARY, 1, 12, 0, 0);
        Date result = fastDateFormat.parse(source);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(result);
        Assert.assertEquals(expected, calendar);
    }

    @Test
    public void testParseStringAndParsePosition() {
        String source = "2022-01-01 12:00:00";
        ParsePosition pos = new ParsePosition(0);
        Date result = fastDateFormat.parse(source, pos);
        Assert.assertNotNull(result);
        Assert.assertEquals(19, pos.getIndex());
    }

    @Test
    public void testParseObjectAndParsePosition() {
        String source = "2022-01-01 12:00:00";
        ParsePosition pos = new ParsePosition(0);
        Object result = fastDateFormat.parseObject(source, pos);
        Assert.assertNotNull(result);
        Assert.assertEquals(19, pos.getIndex());
    }

    @Test
    public void testGetPattern() {
        String expected = "yyyy-MM-dd HH:mm:ss";
        String result = fastDateFormat.getPattern();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetTimeZone() {
        TimeZone expected = TimeZone.getTimeZone("UTC");
        TimeZone result = fastDateFormat.getTimeZone();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetLocale() {
        Locale expected = Locale.US;
        Locale result = fastDateFormat.getLocale();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        int expected = 19;
        int result = fastDateFormat.getMaxLengthEstimate();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        FastDateFormat other = new FastDateFormat("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("UTC"), Locale.US);
        Assert.assertTrue(fastDateFormat.equals(other));
    }

    @Test
    public void testNotEquals() {
        FastDateFormat other = new FastDateFormat("yyyy-MM-dd", TimeZone.getTimeZone("EST"), Locale.FRANCE);
        Assert.assertFalse(fastDateFormat.equals(other));
    }

    @Test
    public void testHashCode() {
        FastDateFormat other = new FastDateFormat("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("UTC"), Locale.US);
        Assert.assertEquals(fastDateFormat.hashCode(), other.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "FastDateFormat[yyyy-MM-dd HH:mm:ss,en_US,UTC]";
        String result = fastDateFormat.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testApplyRules() {
        StringBuffer buf = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.MARCH, 10, 12, 0, 0);
        fastDateFormat.applyRules(calendar, buf);
        String result = buf.toString();
        Assert.assertEquals("2022-03-10 12:00:00", result);
    }
}