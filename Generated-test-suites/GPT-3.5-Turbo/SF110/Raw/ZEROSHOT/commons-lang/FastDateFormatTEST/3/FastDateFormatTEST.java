import org.junit.Test;
import static org.junit.Assert.*;

public class FastDateFormatTest {

    private FastDateFormat fastDateFormat;

    @Test
    public void testGetInstance() {
        assertNotNull(FastDateFormat.getInstance());
    }

    @Test
    public void testGetInstanceWithPattern() {
        assertNotNull(FastDateFormat.getInstance("yyyy-MM-dd"));
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        assertNotNull(FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getDefault()));
    }

    @Test
    public void testGetInstanceWithPatternAndLocale() {
        assertNotNull(FastDateFormat.getInstance("yyyy-MM-dd", Locale.getDefault()));
    }

    @Test
    public void testGetInstanceWithPatternTimeZoneAndLocale() {
        assertNotNull(FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getDefault(), Locale.getDefault()));
    }

    @Test
    public void testGetDateInstance() {
        assertNotNull(FastDateFormat.getDateInstance(FastDateFormat.FULL));
    }

    @Test
    public void testGetDateInstanceWithLocale() {
        assertNotNull(FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.getDefault()));
    }

    @Test
    public void testGetDateInstanceWithTimeZone() {
        assertNotNull(FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getDefault()));
    }

    @Test
    public void testGetDateInstanceWithTimeZoneAndLocale() {
        assertNotNull(FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getDefault(), Locale.getDefault()));
    }

    @Test
    public void testGetTimeInstance() {
        assertNotNull(FastDateFormat.getTimeInstance(FastDateFormat.FULL));
    }

    @Test
    public void testGetTimeInstanceWithLocale() {
        assertNotNull(FastDateFormat.getTimeInstance(FastDateFormat.FULL, Locale.getDefault()));
    }

    @Test
    public void testGetTimeInstanceWithTimeZone() {
        assertNotNull(FastDateFormat.getTimeInstance(FastDateFormat.FULL, TimeZone.getDefault()));
    }

    @Test
    public void testGetTimeInstanceWithTimeZoneAndLocale() {
        assertNotNull(FastDateFormat.getTimeInstance(FastDateFormat.FULL, TimeZone.getDefault(), Locale.getDefault()));
    }

    @Test
    public void testGetDateTimeInstance() {
        assertNotNull(FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL));
    }

    @Test
    public void testGetDateTimeInstanceWithLocale() {
        assertNotNull(FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.getDefault()));
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZone() {
        assertNotNull(FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, TimeZone.getDefault()));
    }

    @Test
    public void testGetDateTimeInstanceWithTimeZoneAndLocale() {
        assertNotNull(FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, TimeZone.getDefault(), Locale.getDefault()));
    }

    @Test
    public void testFormatObjectStringBufferFieldPosition() {
        fastDateFormat = FastDateFormat.getInstance();
        assertEquals("2021-01-01", fastDateFormat.format(new Date(1609477200000L)));
    }

    @Test
    public void testFormatLong() {
        fastDateFormat = FastDateFormat.getInstance();
        assertEquals("2021-01-01", fastDateFormat.format(1609477200000L));
    }

    @Test
    public void testFormatDate() {
        fastDateFormat = FastDateFormat.getInstance();
        assertEquals("2021-01-01", fastDateFormat.format(new Date(1609477200000L)));
    }

    @Test
    public void testFormatCalendar() {
        fastDateFormat = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, 0, 1);
        assertEquals("2021-01-01", fastDateFormat.format(calendar));
    }

    @Test
    public void testFormatLongStringBuffer() {
        fastDateFormat = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        assertEquals("2021-01-01", fastDateFormat.format(1609477200000L, buffer));
    }

    @Test
    public void testFormatDateStringBuffer() {
        fastDateFormat = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        assertEquals("2021-01-01", fastDateFormat.format(new Date(1609477200000L), buffer));
    }

    @Test
    public void testFormatCalendarStringBuffer() {
        fastDateFormat = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, 0, 1);
        StringBuffer buffer = new StringBuffer();
        assertEquals("2021-01-01", fastDateFormat.format(calendar, buffer));
    }

    @Test
    public void testParse() throws ParseException {
        fastDateFormat = FastDateFormat.getInstance();
        Date date = fastDateFormat.parse("2021-01-01");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        assertEquals(2021, calendar.get(Calendar.YEAR));
        assertEquals(0, calendar.get(Calendar.MONTH));
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testParseWithParsePosition() {
        fastDateFormat = FastDateFormat.getInstance();
        ParsePosition position = new ParsePosition(0);
        Date date = fastDateFormat.parse("2021-01-01", position);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        assertEquals(2021, calendar.get(Calendar.YEAR));
        assertEquals(0, calendar.get(Calendar.MONTH));
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testParseObject() {
        fastDateFormat = FastDateFormat.getInstance();
        ParsePosition position = new ParsePosition(0);
        Object object = fastDateFormat.parseObject("2021-01-01", position);
        assertTrue(object instanceof Date);
        Date date = (Date) object;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        assertEquals(2021, calendar.get(Calendar.YEAR));
        assertEquals(0, calendar.get(Calendar.MONTH));
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testGetPattern() {
        fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals("yyyy-MM-dd", fastDateFormat.getPattern());
    }

    @Test
    public void testGetTimeZone() {
        fastDateFormat = FastDateFormat.getInstance();
        assertEquals(TimeZone.getDefault(), fastDateFormat.getTimeZone());
    }

    @Test
    public void testGetLocale() {
        fastDateFormat = FastDateFormat.getInstance();
        assertEquals(Locale.getDefault(), fastDateFormat.getLocale());
    }

    @Test
    public void testGetMaxLengthEstimate() {
        fastDateFormat = FastDateFormat.getInstance();
        assertEquals(10, fastDateFormat.getMaxLengthEstimate());
    }

    @Test
    public void testEqualsWithEqualObject() {
        fastDateFormat = FastDateFormat.getInstance();
        FastDateFormat fastDateFormat2 = FastDateFormat.getInstance();
        assertTrue(fastDateFormat.equals(fastDateFormat2));
    }

    @Test
    public void testEqualsWithDifferentObject() {
        fastDateFormat = FastDateFormat.getInstance();
        assertFalse(fastDateFormat.equals(""));
    }

    @Test
    public void testHashCode() {
        fastDateFormat = FastDateFormat.getInstance();
        assertNotNull(fastDateFormat.hashCode());
    }

    @Test
    public void testToString() {
        fastDateFormat = FastDateFormat.getInstance();
        assertEquals("FastDateFormat[yyyy-MM-dd, en_US, GMT]", fastDateFormat.toString());
    }

    @Test
    public void testApplyRules() {
        fastDateFormat = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, 0, 1);
        StringBuffer buffer = new StringBuffer();
        assertNotNull(fastDateFormat.applyRules(calendar, buffer));
    }
}