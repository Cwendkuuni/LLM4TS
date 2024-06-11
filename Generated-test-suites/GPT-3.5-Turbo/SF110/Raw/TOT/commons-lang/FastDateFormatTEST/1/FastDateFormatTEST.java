import org.junit.Test;
import static org.junit.Assert.*;
import java.text.*;
import java.util.*;

public class FastDateFormatTest {

    private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("GMT");
    private static final Locale LOCALE = Locale.US;
    
    @Test
    public void testGetInstance() {
        FastDateFormat format = FastDateFormat.getInstance();
        assertNotNull(format);
        assertEquals(TIME_ZONE, format.getTimeZone());
        assertEquals(LOCALE, format.getLocale());
    }
    
    @Test
    public void testGetInstanceWithPattern() {
        String pattern = "yyyy-MM-dd";
        FastDateFormat format = FastDateFormat.getInstance(pattern);
        assertNotNull(format);
        assertEquals(pattern, format.getPattern());
    }
    
    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        String pattern = "HH:mm:ss";
        FastDateFormat format = FastDateFormat.getInstance(pattern, TIME_ZONE);
        assertNotNull(format);
        assertEquals(pattern, format.getPattern());
        assertEquals(TIME_ZONE, format.getTimeZone());
    }
    
    @Test
    public void testGetInstanceWithPatternAndLocale() {
        String pattern = "dd/MM/yyyy";
        FastDateFormat format = FastDateFormat.getInstance(pattern, LOCALE);
        assertNotNull(format);
        assertEquals(pattern, format.getPattern());
        assertEquals(LOCALE, format.getLocale());
    }
    
    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        FastDateFormat format = FastDateFormat.getInstance(pattern, TIME_ZONE, LOCALE);
        assertNotNull(format);
        assertEquals(pattern, format.getPattern());
        assertEquals(TIME_ZONE, format.getTimeZone());
        assertEquals(LOCALE, format.getLocale());
    }
    
    @Test
    public void testGetDateInstance() {
        int style = FastDateFormat.FULL;
        FastDateFormat format = FastDateFormat.getDateInstance(style);
        assertNotNull(format);
        assertEquals(style, format.getCalendar().get(Calendar.DAY_OF_WEEK));
    }
    
    @Test
    public void testGetDateInstanceWithLocale() {
        int style = FastDateFormat.MEDIUM;
        FastDateFormat format = FastDateFormat.getDateInstance(style, LOCALE);
        assertNotNull(format);
        assertEquals(style, format.getCalendar().get(Calendar.DAY_OF_WEEK));
        assertEquals(LOCALE, format.getLocale());
    }
    
    @Test
    public void testGetDateInstanceWithTimeZone() {
        int style = FastDateFormat.LONG;
        FastDateFormat format = FastDateFormat.getDateInstance(style, TIME_ZONE);
        assertNotNull(format);
        assertEquals(style, format.getCalendar().get(Calendar.DAY_OF_WEEK));
        assertEquals(TIME_ZONE, format.getTimeZone());
    }
    
    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        int style = FastDateFormat.SHORT;
        FastDateFormat format = FastDateFormat.getDateInstance(style, TIME_ZONE, LOCALE);
        assertNotNull(format);
        assertEquals(style, format.getCalendar().get(Calendar.DAY_OF_WEEK));
        assertEquals(TIME_ZONE, format.getTimeZone());
        assertEquals(LOCALE, format.getLocale());
    }
    
    @Test
    public void testGetTimeInstance() {
        int style = FastDateFormat.SHORT;
        FastDateFormat format = FastDateFormat.getTimeInstance(style);
        assertNotNull(format);
        assertEquals(style, format.getCalendar().get(Calendar.HOUR_OF_DAY));
    }
    
    @Test
    public void testGetTimeInstanceWithLocale() {
        int style = FastDateFormat.MEDIUM;
        FastDateFormat format = FastDateFormat.getTimeInstance(style, LOCALE);
        assertNotNull(format);
        assertEquals(style, format.getCalendar().get(Calendar.HOUR_OF_DAY));
        assertEquals(LOCALE, format.getLocale());
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZone() {
        int style = FastDateFormat.LONG;
        FastDateFormat format = FastDateFormat.getTimeInstance(style, TIME_ZONE);
        assertNotNull(format);
        assertEquals(style, format.getCalendar().get(Calendar.HOUR_OF_DAY));
        assertEquals(TIME_ZONE, format.getTimeZone());
    }
    
    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        int style = FastDateFormat.FULL;
        FastDateFormat format = FastDateFormat.getTimeInstance(style, TIME_ZONE, LOCALE);
        assertNotNull(format);
        assertEquals(style, format.getCalendar().get(Calendar.HOUR_OF_DAY));
        assertEquals(TIME_ZONE, format.getTimeZone());
        assertEquals(LOCALE, format.getLocale());
    }
    
    @Test
    public void testGetDateTimeInstance() {
        int dateStyle = FastDateFormat.SHORT;
        int timeStyle = FastDateFormat.SHORT;
        FastDateFormat format = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle);
        assertNotNull(format);
        assertEquals(dateStyle, format.getCalendar().get(Calendar.DAY_OF_WEEK));
        assertEquals(timeStyle, format.getCalendar().get(Calendar.HOUR_OF_DAY));
    }
    
    @Test
    public void testGetDateTimeInstanceWithLocale() {
        int dateStyle = FastDateFormat.FULL;
        int timeStyle = FastDateFormat.FULL;
        FastDateFormat format = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, LOCALE);
        assertNotNull(format);
        assertEquals(dateStyle, format.getCalendar().get(Calendar.DAY_OF_WEEK));
        assertEquals(timeStyle, format.getCalendar().get(Calendar.HOUR_OF_DAY));
        assertEquals(LOCALE, format.getLocale());
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        int dateStyle = FastDateFormat.LONG;
        int timeStyle = FastDateFormat.LONG;
        FastDateFormat format = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, TIME_ZONE);
        assertNotNull(format);
        assertEquals(dateStyle, format.getCalendar().get(Calendar.DAY_OF_WEEK));
        assertEquals(timeStyle, format.getCalendar().get(Calendar.HOUR_OF_DAY));
        assertEquals(TIME_ZONE, format.getTimeZone());
    }
    
    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        int dateStyle = FastDateFormat.MEDIUM;
        int timeStyle = FastDateFormat.MEDIUM;
        FastDateFormat format = FastDateFormat.getDateTimeInstance(dateStyle, timeStyle, TIME_ZONE, LOCALE);
        assertNotNull(format);
        assertEquals(dateStyle, format.getCalendar().get(Calendar.DAY_OF_WEEK));
        assertEquals(timeStyle, format.getCalendar().get(Calendar.HOUR_OF_DAY));
        assertEquals(TIME_ZONE, format.getTimeZone());
        assertEquals(LOCALE, format.getLocale());
    }
    
    @Test
    public void testFormatWithObject() {
        FastDateFormat format = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, Calendar.MARCH, 20, 9, 30, 0);
        String expected = "2019-03-20 09:30:00";
        assertEquals(expected, format.format(calendar.getTime()));
    }
    
    @Test
    public void testFormatWithMillis() {
        long millis = 1553045400000L;
        FastDateFormat format = FastDateFormat.getInstance();
        String expected = "2019-03-20 09:30:00";
        assertEquals(expected, format.format(millis));
    }
    
    @Test
    public void testFormatWithDate() {
        Date date = new Date(1553045400000L);
        FastDateFormat format = FastDateFormat.getInstance();
        String expected = "2019-03-20 09:30:00";
        assertEquals(expected, format.format(date));
    }
    
    @Test
    public void testFormatWithCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, Calendar.MARCH, 20, 9, 30, 0);
        FastDateFormat format = FastDateFormat.getInstance();
        String expected = "2019-03-20 09:30:00";
        assertEquals(expected, format.format(calendar));
    }
    
    @Test
    public void testFormatWithMillisAndBuffer() {
        long millis = 1553045400000L;
        FastDateFormat format = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        format.format(millis, buffer);
        String expected = "2019-03-20 09:30:00";
        assertEquals(expected, buffer.toString());
    }
    
    @Test
    public void testFormatWithDateAndBuffer() {
        Date date = new Date(1553045400000L);
        FastDateFormat format = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        format.format(date, buffer);
        String expected = "2019-03-20 09:30:00";
        assertEquals(expected, buffer.toString());
    }
    
    @Test
    public void testFormatWithCalendarAndBuffer() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, Calendar.MARCH, 20, 9, 30, 0);
        FastDateFormat format = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        format.format(calendar, buffer);
        String expected = "2019-03-20 09:30:00";
        assertEquals(expected, buffer.toString());
    }
    
    @Test
    public void testParse() throws ParseException {
        String source = "2019-03-20";
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        Date expected = format.parse(source);
        assertEquals(expected, format.parseObject(source));
    }
    
    @Test
    public void testParseWithPosition() {
        String source = "2019-03-20";
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        ParsePosition position = new ParsePosition(0);
        Date expected = format.parse(source, position);
        assertEquals(expected, format.parseObject(source, position));
    }
    
    @Test
    public void testGetPattern() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        FastDateFormat format = FastDateFormat.getInstance(pattern);
        assertEquals(pattern, format.getPattern());
    }
    
    @Test
    public void testGetTimeZone() {
        FastDateFormat format = FastDateFormat.getInstance();
        assertEquals(TIME_ZONE, format.getTimeZone());
    }
    
    @Test
    public void testGetLocale() {
        FastDateFormat format = FastDateFormat.getInstance();
        assertEquals(LOCALE, format.getLocale());
    }
    
    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat format = FastDateFormat.getInstance();
        assertTrue(format.getMaxLengthEstimate() >= 0);
    }
    
    @Test
    public void testEquals() {
        FastDateFormat format1 = FastDateFormat.getInstance();
        FastDateFormat format2 = FastDateFormat.getInstance();
        assertEquals(format1, format2);
    }
    
    @Test
    public void testToString() {
        FastDateFormat format = FastDateFormat.getInstance();
        String expected = "FastDateFormat[yyyy-MM-dd HH:mm:ss,en_US,GMT]";
        assertEquals(expected, format.toString());
    }
    
    @Test
    public void testApplyRules() {
        Calendar calendar = Calendar.getInstance();
        FastDateFormat format = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        format.applyRules(calendar, buffer);
        assertFalse(buffer.toString().isEmpty());
    }
}