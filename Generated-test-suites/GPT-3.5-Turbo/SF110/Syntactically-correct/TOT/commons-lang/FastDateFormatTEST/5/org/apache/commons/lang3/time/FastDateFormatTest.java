import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertTrue(instance instanceof FastDateFormat);
    }

    @Test
    public void testGetInstanceWithPattern() {
        String pattern = "dd-MM-yyyy";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        assertEquals(pattern, instance.getPattern());
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        String pattern = "dd-MM-yyyy HH:mm:ss";
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone);
        assertEquals(timeZone, instance.getTimeZone());
    }

    @Test
    public void testGetInstanceWithPatternAndLocale() {
        String pattern = "dd-MM-yyyy HH:mm:ss";
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance(pattern, locale);
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        String pattern = "dd-MM-yyyy HH:mm:ss";
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone, locale);
        assertEquals(pattern, instance.getPattern());
        assertEquals(timeZone, instance.getTimeZone());
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetDateInstance() {
        int style = FastDateFormat.FULL;
        FastDateFormat instance = FastDateFormat.getDateInstance(style);
        assertTrue(instance instanceof FastDateFormat);
    }

    @Test
    public void testGetDateInstanceWithStyleAndLocale() {
        int style = FastDateFormat.SHORT;
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateInstance(style, locale);
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetTimeInstance() {
        int style = FastDateFormat.SHORT;
        FastDateFormat instance = FastDateFormat.getTimeInstance(style);
        assertTrue(instance instanceof FastDateFormat);
    }

    @Test
    public void testGetTimeInstanceWithStyleAndTimeZone() {
        int style = FastDateFormat.MEDIUM;
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        FastDateFormat instance = FastDateFormat.getTimeInstance(style, timeZone);
        assertEquals(timeZone, instance.getTimeZone());
    }

    @Test
    public void testGetDateTimeInstance() {
        int dateStyle = FastDateFormat.SHORT;
        int timeStyle = FastDateFormat.MEDIUM;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle);
        assertTrue(instance instanceof FastDateFormat);
    }

    @Test
    public void testGetDateTimeInstanceWithStylesAndLocale() {
        int dateStyle = FastDateFormat.LONG;
        int timeStyle = FastDateFormat.FULL;
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testFormatWithObject() {
        Date date = new Date();
        FastDateFormat instance = FastDateFormat.getInstance();
        String formattedDate = instance.format(date);
        assertEquals(date.toString(), formattedDate);
    }

    @Test
    public void testFormatWithMillis() {
        long millis = System.currentTimeMillis();
        FastDateFormat instance = FastDateFormat.getInstance();
        String formattedDate = instance.format(millis);
        assertEquals(new Date(millis).toString(), formattedDate);
    }

    @Test
    public void testFormatWithDate() {
        Date date = new Date();
        FastDateFormat instance = FastDateFormat.getInstance();
        String formattedDate = instance.format(date);
        assertEquals(date.toString(), formattedDate);
    }

    @Test
    public void testFormatWithCalendar() {
        Calendar calendar = Calendar.getInstance();
        FastDateFormat instance = FastDateFormat.getInstance();
        String formattedDate = instance.format(calendar);
        assertEquals(calendar.getTime().toString(), formattedDate);
    }

    @Test
    public void testFormatWithMillisAndBuffer() {
        long millis = System.currentTimeMillis();
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        StringBuffer formattedBuffer = instance.format(millis, buffer);

        assertEquals(buffer, formattedBuffer);
    }

    @Test
    public void testFormatWithDateAndBuffer() {
        Date date = new Date();
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        StringBuffer formattedBuffer = instance.format(date, buffer);

        assertEquals(buffer, formattedBuffer);
    }

    @Test
    public void testFormatWithCalendarAndBuffer() {
        Calendar calendar = Calendar.getInstance();
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        StringBuffer formattedBuffer = instance.format(calendar, buffer);

        assertEquals(buffer, formattedBuffer);
    }

    @Test
    public void testParse() {
        String source = "2022-01-01";
        FastDateFormat instance = FastDateFormat.getInstance();
        try {
            Date parsedDate = instance.parse(source);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);
            assertEquals(2022, calendar.get(Calendar.YEAR));
            assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH));
            assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }

    @Test
    public void testParseWithParsePosition() {
        String source = "2022-01-01";
        FastDateFormat instance = FastDateFormat.getInstance();
        ParsePosition pos = new ParsePosition(5);
        Date parsedDate = instance.parse(source, pos);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parsedDate);
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(calendar.get(Calendar.JANUARY), calendar.get(Calendar.MONTH));
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testParseObjectWithParsePosition() {
        String source = "2022-01-01";
        FastDateFormat instance = FastDateFormat.getInstance();
        ParsePosition pos = new ParsePosition(5);
        Object parsedObject = instance.parseObject(source, pos);
        assertTrue(parsedObject instanceof Date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime((Date) parsedObject);
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(calendar.get(Calendar.JANUARY), calendar.get(Calendar.MONTH));
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testGetPattern() {
        String pattern = "dd/MM/yyyy";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        assertEquals(pattern, instance.getPattern());
    }

    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        FastDateFormat instance = FastDateFormat.getInstance(null, timeZone);
        assertEquals(timeZone, instance.getTimeZone());
    }

    @Test
    public void testGetLocale() {
        Locale locale = Locale.US;
        FastDateFormat instance = FastDateFormat.getInstance(null, locale);
        assertEquals(locale, instance.getLocale());
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat instance = FastDateFormat.getInstance();
        int maxLengthEstimate = instance.getMaxLengthEstimate();
        assertTrue(maxLengthEstimate >= 0);
    }

    @Test
    public void testEquals() {
        FastDateFormat instance1 = FastDateFormat.getInstance();
        FastDateFormat instance2 = FastDateFormat.getInstance();
        assertTrue(instance1.equals(instance2));
    }
    
    @Test
    public void testToString() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertTrue(instance.toString().contains(instance.getPattern()));
    }
    
    @Test
    public void testApplyRules() {
        Calendar calendar = Calendar.getInstance();
        FastDateFormat instance = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        StringBuffer formattedBuffer = instance.applyRules(calendar, buffer);

        assertEquals(buffer, formattedBuffer);
    }
}