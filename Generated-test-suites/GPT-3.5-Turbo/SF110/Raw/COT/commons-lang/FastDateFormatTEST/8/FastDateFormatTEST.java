import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class FastDateFormatTest {

    private FastDateFormat fastDateFormat;

    @Before
    public void setUp() {
        fastDateFormat = FastDateFormat.getInstance();
    }

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPattern() {
        String pattern = "dd/MM/yyyy";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        String pattern = "dd/MM/yyyy";
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPatternAndLocale() {
        String pattern = "dd/MM/yyyy";
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getInstance(pattern, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        String pattern = "dd/MM/yyyy";
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone, locale);
        assertNotNull(instance);
    }


    @Test
    public void testGetDateInstance() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, timeZone);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, timeZone, locale);
        assertNotNull(instance);
    }

    @Test(expected = ParseException.class)
    public void testParse() throws ParseException {
        String source = "01/01/2021";
        Date result = fastDateFormat.parse(source);
        assertNotNull(result);
    }

    @Test
    public void testParseWithParsePosition() {
        String source = "01/01/2021";
        ParsePosition pos = new ParsePosition(0);
        Date result = fastDateFormat.parse(source, pos);
        assertNotNull(result);
        assertEquals(10, pos.getIndex());
    }

    @Test
    public void testParseObject() {
        String source = "01/01/2021";
        ParsePosition pos = new ParsePosition(0);
        Object result = fastDateFormat.parseObject(source, pos);
        assertNotNull(result);
        assertTrue(result instanceof Date);
        assertEquals(10, pos.getIndex());
    }

    @Test
    public void testFormatWithObjectStringBufferAndFieldPosition() {
        Object obj = new Date();
        StringBuffer buf = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = fastDateFormat.format(obj, buf, pos);
        assertNotNull(result);
        assertEquals(buf, result);
    }

    @Test
    public void testFormatWithLong() {
        long millis = System.currentTimeMillis();
        String result = fastDateFormat.format(millis);
        assertNotNull(result);
    }

    @Test
    public void testFormatWithDate() {
        Date date = new Date();
        String result = fastDateFormat.format(date);
        assertNotNull(result);
    }

    @Test
    public void testFormatWithCalendar() {
        Calendar calendar = Calendar.getInstance();
        String result = fastDateFormat.format(calendar);
        assertNotNull(result);
    }

    @Test
    public void testFormatWithLongAndStringBuffer() {
        long millis = System.currentTimeMillis();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = fastDateFormat.format(millis, buf);
        assertNotNull(result);
        assertEquals(buf, result);
    }

    @Test
    public void testFormatWithDateAndStringBuffer() {
        Date date = new Date();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = fastDateFormat.format(date, buf);
        assertNotNull(result);
        assertEquals(buf, result);
    }

    @Test
    public void testFormatWithCalendarAndStringBuffer() {
        Calendar calendar = Calendar.getInstance();
        StringBuffer buf = new StringBuffer();
        StringBuffer result = fastDateFormat.format(calendar, buf);
        assertNotNull(result);
        assertEquals(buf, result);
    }

    @Test
    public void testGetPattern() {
        String pattern = fastDateFormat.getPattern();
        assertNotNull(pattern);
    }

    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = fastDateFormat.getTimeZone();
        assertNotNull(timeZone);
    }

    @Test
    public void testGetLocale() {
        Locale locale = fastDateFormat.getLocale();
        assertNotNull(locale);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        int maxLengthEstimate = fastDateFormat.getMaxLengthEstimate();
        assertTrue(maxLengthEstimate >= 0);
    }

    @Test
    public void testEquals() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertTrue(fastDateFormat.equals(instance));
    }

    @Test
    public void testHashCode() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertEquals(instance.hashCode(), fastDateFormat.hashCode());
    }

    @Test
    public void testToString() {
        String result = fastDateFormat.toString();
        assertNotNull(result);
    }
}