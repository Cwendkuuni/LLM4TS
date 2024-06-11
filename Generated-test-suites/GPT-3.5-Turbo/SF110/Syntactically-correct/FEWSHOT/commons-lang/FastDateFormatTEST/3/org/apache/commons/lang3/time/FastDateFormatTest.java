import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FastDateFormatTest {

    private FastDateFormat fastDateFormat;

    @Test
    public void testGetInstance() {
        fastDateFormat = FastDateFormat.getInstance();
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstance_WithPattern() {
        fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstance_WithPatternAndTimeZone() {
        fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"));
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstance_WithPatternAndLocale() {
        fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd", Locale.US);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetInstance_WithPatternTimeZoneAndLocale() {
        fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstance() {
        fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstance_WithLocale() {
        fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstance_WithTimeZone() {
        fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"));
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateInstance_WithTimeZoneAndLocale() {
        fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstance() {
        fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstance_WithLocale() {
        fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, Locale.US);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstance_WithTimeZone() {
        fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"));
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetTimeInstance_WithTimeZoneAndLocale() {
        fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstance() {
        fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstance_WithLocale() {
        fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, Locale.US);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstance_WithTimeZone() {
        fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"));
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testGetDateTimeInstance_WithTimeZoneAndLocale() {
        fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(fastDateFormat);
    }

    @Test
    public void testFormat_Object() {
        fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        String formatted = fastDateFormat.format(new Date());
        assertNotNull(formatted);
    }

    @Test
    public void testFormat_Long() {
        fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        String formatted = fastDateFormat.format(System.currentTimeMillis());
        assertNotNull(formatted);
    }

    @Test
    public void testFormat_Date() {
        fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        String formatted = fastDateFormat.format(new Date());
        assertNotNull(formatted);
    }

    @Test
    public void testFormat_Calendar() {
        fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        String formatted = fastDateFormat.format(Calendar.getInstance());
        assertNotNull(formatted);
    }

    @Test
    public void testFormatWithBuffer_Long() {
        fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        StringBuffer buffer = new StringBuffer();
        StringBuffer result = fastDateFormat.format(System.currentTimeMillis(), buffer);
        assertEquals(buffer, result);
    }

    @Test
    public void testFormatWithBuffer_Date() {
        fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        StringBuffer buffer = new StringBuffer();
        StringBuffer result = fastDateFormat.format(new Date(), buffer);
        assertEquals(buffer, result);
    }
    
    @Test
    public void testFormatWithBuffer_Calendar() {
        fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        StringBuffer buffer = new StringBuffer();
        StringBuffer result = fastDateFormat.format(Calendar.getInstance(), buffer);
        assertEquals(buffer, result);
    }

    @Test
    public void testParse() throws ParseException {
        fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        Date parsed = fastDateFormat.parse("2022-01-01");
        assertNotNull(parsed);
    }

    @Test
    public void testParse_WithParsePosition() {
        fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        ParsePosition pos = new ParsePosition(0);
        Date parsed = fastDateFormat.parse("2022-01-01", pos);
        assertNotNull(parsed);
        assertEquals(10, pos.getIndex());
    }

    @Test
    public void testParseObject() {
        fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        ParsePosition pos = new ParsePosition(0);
        Object parsed = fastDateFormat.parseObject("2022-01-01", pos);
        assertNotNull(parsed);
        assertEquals(10, pos.getIndex());
    }

    @Test
    public void testGetPattern() {
        fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US);
        String pattern = fastDateFormat.getPattern();
        assertNotNull(pattern);
        assertTrue(pattern.contains("MMM"));
    }

    @Test
    public void testGetTimeZone() {
        fastDateFormat = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        TimeZone timeZone = fastDateFormat.getTimeZone();
        assertNotNull(timeZone);
        assertEquals(TimeZone.getDefault(), timeZone);
    }

    @Test
    public void testGetLocale() {
        fastDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US);
        Locale locale = fastDateFormat.getLocale();
        assertNotNull(locale);
        assertEquals(Locale.US, locale);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
        assertTrue(maxLengthEstimate > 0);
    }

    @Test
    public void testEquals() {
        fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        FastDateFormat other = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertTrue(fastDateFormat.equals(other));
    }

    @Test
    public void testHashCode() {
        fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        FastDateFormat other = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertEquals(fastDateFormat.hashCode(), other.hashCode());
    }

    @Test
    public void testToString() {
        fastDateFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        String toString = fastDateFormat.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("FastDateFormat"));
    }
}