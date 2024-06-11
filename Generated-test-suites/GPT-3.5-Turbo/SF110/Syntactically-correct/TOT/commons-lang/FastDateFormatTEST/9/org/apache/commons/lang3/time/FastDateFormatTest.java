import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class FastDateFormatTest {

    private static final FastDateFormat dateFormat = FastDateFormat.getInstance();

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy/MM/dd");
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy/MM/dd", TimeZone.getTimeZone("GMT+0"));
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPatternAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy/MM/dd", Locale.US);
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy/MM/dd", TimeZone.getTimeZone("GMT+0"), Locale.US);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, Locale.US);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT+0"));
        assertNotNull(instance);
    }

    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT+0"), Locale.US);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, Locale.US);
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT+0"));
        assertNotNull(instance);
    }

    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT+0"), Locale.US);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, Locale.US);
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT+0"));
        assertNotNull(instance);
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, TimeZone.getTimeZone("GMT+0"), Locale.US);
        assertNotNull(instance);
    }

    @Test
    public void testFormatObject() {
        Date date = new Date();
        String dateString = dateFormat.format(date);
        assertNotNull(dateString);
    }

    @Test
    public void testFormatLong() {
        long millis = 1617648000000L; // 2021-04-06 00:00:00 in GMT+0
        String dateString = dateFormat.format(millis);
        assertEquals("2021-04-06 00:00:00", dateString);
    }

    @Test
    public void testFormatDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse("2021/04/06");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateString = dateFormat.format(date);
        assertEquals("2021-04-06 00:00:00", dateString);
    }

    @Test
    public void testFormatCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, 3, 6, 0, 0, 0); // 2021-04-06 00:00:00 in GMT+0
        String dateString = dateFormat.format(calendar);
        assertEquals("2021-04-06 00:00:00", dateString);
    }

    @Test
    public void testFormatLongWithStringBuffer() {
        long millis = 1617648000000L; // 2021-04-06 00:00:00 in GMT+0
        StringBuffer buffer = new StringBuffer();
        StringBuffer resultBuffer = dateFormat.format(millis, buffer);
        assertNotNull(resultBuffer);
    }

    @Test
    public void testFormatDateWithStringBuffer() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse("2021/04/06");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        StringBuffer buffer = new StringBuffer();
        StringBuffer resultBuffer = dateFormat.format(date, buffer);
        assertNotNull(resultBuffer);
    }

    @Test
    public void testFormatCalendarWithStringBuffer() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, 3, 6, 0, 0, 0); // 2021-04-06 00:00:00 in GMT+0
        StringBuffer buffer = new StringBuffer();
        StringBuffer resultBuffer = dateFormat.format(calendar, buffer);
        assertNotNull(resultBuffer);
    }

    @Test
    public void testParse() {
        String dateString = "2021-04-06 00:00:00";
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertNotNull(date);
    }

    @Test
    public void testParseWithParsePosition() {
        String dateString = "2021-04-06 00:00:00";
        ParsePosition pos = new ParsePosition(0);
        Date date = dateFormat.parse(dateString, pos);
        assertNotNull(date);
    }

    @Test
    public void testParseObject() {
        String dateString = "2021-04-06 00:00:00";
        ParsePosition pos = new ParsePosition(0);
        Object obj = dateFormat.parseObject(dateString, pos);
        assertNotNull(obj);
    }

    @Test
    public void testGetPattern() {
        String pattern = dateFormat.getPattern();
        assertNotNull(pattern);
    }

    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = dateFormat.getTimeZone();
        assertNotNull(timeZone);
    }

    @Test
    public void testGetLocale() {
        Locale locale = dateFormat.getLocale();
        assertNotNull(locale);
    }

    @Test
    public void testGetMaxLengthEstimate() {
        int maxLengthEstimate = dateFormat.getMaxLengthEstimate();
        assertNotEquals(0, maxLengthEstimate);
    }

    @Test
    public void testEquals() {
        FastDateFormat instance1 = FastDateFormat.getInstance();
        FastDateFormat instance2 = FastDateFormat.getInstance();
        assertEquals(instance1, instance2);
    }

    @Test
    public void testHashCode() {
        FastDateFormat instance1 = FastDateFormat.getInstance();
        FastDateFormat instance2 = FastDateFormat.getInstance();
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testToString() {
        String toString = dateFormat.toString();
        assertNotNull(toString);
    }

    @Test
    public void testApplyRules() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, 3, 6, 0, 0, 0); // 2021-04-06 00:00:00 in GMT+0
        StringBuffer buffer = new StringBuffer();
        StringBuffer resultBuffer = dateFormat.applyRules(calendar, buffer);
        assertNotNull(resultBuffer);
    }
}