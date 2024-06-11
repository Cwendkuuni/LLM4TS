
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class FastDateFormatTest {

    private FastDateFormat fastDateFormat;

    @Before
    public void setUp() {
        fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
    }

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        assertEquals(fastDateFormat, instance);
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd");
        assertNotNull(instance);
        assertEquals("yyyy-MM-dd", instance.getPattern());
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT+00:00");
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", timeZone);
        assertNotNull(instance);
        assertEquals("yyyy-MM-dd", instance.getPattern());
        assertEquals(timeZone, instance.getTimeZone());
    }

    @Test
    public void testGetInstanceWithPatternAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", Locale.US);
        assertNotNull(instance);
        assertEquals("yyyy-MM-dd", instance.getPattern());
        assertEquals(Locale.US, instance.getLocale());
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd", timeZone, Locale.US);
        assertNotNull(instance);
        assertEquals("yyyy-MM-dd", instance.getPattern());
        assertEquals(timeZone, instance.getTimeZone());
        assertEquals(Locale.US, instance.getLocale());
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.SHORT);
        assertNotNull(instance);
        assertEquals("MM/dd/yy", instance.getPattern());
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.SHORT, Locale.US);
        assertNotNull(instance);
        assertEquals("MM/dd/yy", instance.getPattern());
        assertEquals(Locale.US, instance.getLocale());
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT+00:00");
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.SHORT, timeZone);
        assertNotNull(instance);
        assertEquals("MM/dd/yy", instance.getPattern());
        assertEquals(timeZone, instance.getTimeZone());
    }

    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.SHORT, timeZone, Locale.US);
        assertNotNull(instance);
        assertEquals("MM/dd/yy", instance.getPattern());
        assertEquals(timeZone, instance.getTimeZone());
        assertEquals(Locale.US, instance.getLocale());
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertNotNull(instance);
        assertEquals("hh:mm:ss a", instance.getPattern());
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, Locale.US);
        assertNotNull(instance);
        assertEquals("hh:mm:ss a", instance.getPattern());
        assertEquals(Locale.US, instance.getLocale());
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT+00:00");
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(instance);
        assertEquals("hh:mm:ss a", instance.getPattern());
        assertEquals(timeZone, instance.getTimeZone());
    }

    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone, Locale.US);
        assertNotNull(instance);
        assertEquals("hh:mm:ss a", instance.getPattern());
        assertEquals(timeZone, instance.getTimeZone());
        assertEquals(Locale.US, instance.getLocale());
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertNotNull(instance);
        assertEquals("MM/dd/yy hh:mm a", instance.getPattern());
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.US);
        assertNotNull(instance);
        assertEquals("MM/dd/yy hh:mm a", instance.getPattern());
        assertEquals(Locale.US, instance.getLocale());
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT+00:00");
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, timeZone);
        assertNotNull(instance);
        assertEquals("MM/dd/yy hh:mm a", instance.getPattern());
        assertEquals(timeZone, instance.getTimeZone());
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, timeZone, Locale.US);
        assertNotNull(instance);
        assertEquals("MM/dd/yy hh:mm a", instance.getPattern());
        assertEquals(timeZone, instance.getTimeZone());
        assertEquals(Locale.US, instance.getLocale());
    }

    @Test
    public void testFormatWithCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.SEPTEMBER, 5, 12, 30, 0);

        String expected = "2022-09-05 12:30:00";
        String result = fastDateFormat.format(calendar);

        assertEquals(expected, result);
    }

    @Test
    public void testFormatWithMillis() {
        long millis = 1662466200000L;

        String expected = "2022-09-05 12:30:00";
        String result = fastDateFormat.format(millis);

        assertEquals(expected, result);
    }

    @Test
    public void testFormatWithDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse("2022-09-05 12:30:00");

        String expected = "2022-09-05 12:30:00";
        String result = fastDateFormat.format(date);

        assertEquals(expected, result);
    }

    @Test
    public void testParse() throws ParseException {
        String source = "2022-09-05 12:30:00";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date expected = sdf.parse(source);
        Date result = fastDateFormat.parse(source);

        assertEquals(expected, result);
    }

    @Test
    public void testParseWithInvalidSource() {
        String source = "2022/09/05 12:30:00";

        try {
            fastDateFormat.parse(source);
            fail("Expected ParseException was not thrown.");
        } catch (ParseException e) {
            // Exception expected
        }
    }

    @Test
    public void testParseWithParsePosition() {
        String source = "2022-09-05 12:30:00";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date expected = sdf.parse(source);

        ParsePosition parsePosition = new ParsePosition(0);
        Date result = fastDateFormat.parse(source, parsePosition);

        assertEquals(expected, result);
        assertEquals(source.length(), parsePosition.getIndex());
    }

    @Test
    public void testParseObject() {
        String source = "2022-09-05 12:30:00";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date expected = sdf.parse(source);

        ParsePosition parsePosition = new ParsePosition(0);
        Object result = fastDateFormat.parseObject(source, parsePosition);

        assertTrue(result instanceof Date);
        assertEquals(expected, result);
        assertEquals(source.length(), parsePosition.getIndex());
    }

    @Test
    public void testEquals() {
        FastDateFormat other = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

        assertTrue(fastDateFormat.equals(other));
    }

    @Test
    public void testHashCode() {
        FastDateFormat other = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

        assertEquals(fastDateFormat.hashCode(), other.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("FastDateFormat[yyyy-MM-dd HH:mm:ss,en_US,GMT]", fastDateFormat.toString());
    }

    @Test
    public void testApplyRules() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.SEPTEMBER, 5, 12, 30, 0);

        StringBuffer buf = new StringBuffer();
        StringBuffer result = fastDateFormat.applyRules(calendar, buf);

        assertSame(buf, result);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();

        assertEquals(19, maxLengthEstimate);
    }
}

