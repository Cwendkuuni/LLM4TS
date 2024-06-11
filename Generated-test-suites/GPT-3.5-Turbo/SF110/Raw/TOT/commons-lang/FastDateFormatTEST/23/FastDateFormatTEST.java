import org.junit.*;
import static org.junit.Assert.*;
import java.text.*;
import java.util.*;

public class FastDateFormatTest {

    private static final long ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000;
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private FastDateFormat fastDateFormat;

    @Before
    public void setUp() {
        fastDateFormat = FastDateFormat.getInstance(DEFAULT_PATTERN);
    }

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPattern() {
        String pattern = "yyyy/MM/dd";
        FastDateFormat instance = FastDateFormat.getInstance(pattern);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        String pattern = "yyyy/MM/dd";
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPatternAndLocale() {
        String pattern = "yyyy/MM/dd";
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getInstance(pattern, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        String pattern = "yyyy/MM/dd";
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getInstance(pattern, timeZone, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.SHORT);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.SHORT, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.SHORT, timeZone);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.SHORT, timeZone, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, timeZone);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, timeZone, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, locale);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, timeZone);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Locale locale = Locale.ENGLISH;
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, timeZone, locale);
        assertNotNull(instance);
    }

    @Test
    public void testFormatObjectWithStringBufferFieldPosition() {
        StringBuffer stringBuffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        String expected = "2022-08-18 12:30:00";
        Date date = getDate(2022, 7, 18, 12, 30, 0);
        StringBuffer result = fastDateFormat.format(date, stringBuffer, fieldPosition);
        assertEquals(expected, result.toString());
    }

    @Test
    public void testFormatLong() {
        long millis = 1650192000000L; // 2022-05-18 00:00:00
        String expected = "2022-05-18 00:00:00";
        String result = fastDateFormat.format(millis);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatDate() {
        String expected = "2022-06-18 15:45:00";
        Date date = getDate(2022, 5, 18, 15, 45, 0);
        String result = fastDateFormat.format(date);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatCalendar() {
        String expected = "2022-07-18 08:10:30";
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Calendar calendar = getCalendar(2022, 6, 18, 8, 10, 30, timeZone);
        String result = fastDateFormat.format(calendar);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatLongWithStringBuffer() {
        long millis = 1706889600000L; // 2024-01-01 00:00:00
        String expected = "2024-01-01 00:00:00";
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer result = fastDateFormat.format(millis, stringBuffer);
        assertEquals(expected, result.toString());
    }

    @Test
    public void testFormatDateWithStringBuffer() {
        String expected = "2021-12-24 12:00:00";
        Date date = getDate(2021, 11, 24, 12, 0, 0);
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer result = fastDateFormat.format(date, stringBuffer);
        assertEquals(expected, result.toString());
    }

    @Test
    public void testFormatCalendarWithStringBuffer() {
        String expected = "2022-06-30 18:15:30";
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Calendar calendar = getCalendar(2022, 5, 30, 18, 15, 30, timeZone);
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer result = fastDateFormat.format(calendar, stringBuffer);
        assertEquals(expected, result.toString());
    }

    @Test
    public void testParse() throws ParseException {
        String source = "2022-09-01 10:30:00";
        Date expected = getDate(2022, 8, 1, 10, 30, 0);
        Date result = fastDateFormat.parse(source);
        assertEquals(expected, result);
    }

    @Test
    public void testParseWithParsePosition() {
        String source = "2022-09-01 10:30:00";
        ParsePosition parsePosition = new ParsePosition(0);
        Date expected = getDate(2022, 8, 1, 10, 30, 0);
        Date result = fastDateFormat.parse(source, parsePosition);
        assertEquals(expected, result);
    }

    @Test
    public void testParseObject() {
        String source = "2022-09-01 10:30:00";
        ParsePosition parsePosition = new ParsePosition(0);
        Object expected = getDate(2022, 8, 1, 10, 30, 0);
        Object result = fastDateFormat.parseObject(source, parsePosition);
        assertEquals(expected, result);
    }

    @Test
    public void testGetPattern() {
        String expected = DEFAULT_PATTERN;
        String result = fastDateFormat.getPattern();
        assertEquals(expected, result);
    }

    @Test
    public void testGetTimeZone() {
        TimeZone expected = TimeZone.getDefault();
        TimeZone result = fastDateFormat.getTimeZone();
        assertEquals(expected, result);
    }

    @Test
    public void testGetLocale() {
        Locale expected = Locale.getDefault();
        Locale result = fastDateFormat.getLocale();
        assertEquals(expected, result);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        int expected = DEFAULT_PATTERN.length();
        int result = fastDateFormat.getMaxLengthEstimate();
        assertEquals(expected, result);
    }

    private Date getDate(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Calendar getCalendar(int year, int month, int day, int hour, int minute, int second, TimeZone timeZone) {
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.set(year, month, day, hour, minute, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

}