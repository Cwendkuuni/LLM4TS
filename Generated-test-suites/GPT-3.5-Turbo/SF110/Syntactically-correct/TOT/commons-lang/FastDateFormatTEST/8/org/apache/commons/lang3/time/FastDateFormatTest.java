
import org.junit.Test;

import java.text.FieldPosition;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FastDateFormatTest {

    // Test getInstance() methods
    @Test
    public void testGetInstance_Default() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        assertEquals("FastDateFormat[yyyy-MM-dd'T'HH:mm:ss.SSSZ,en_US,GMT]", instance.toString());
    }

    @Test
    public void testGetInstance_WithPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("dd-MM-yyyy");
        assertNotNull(instance);
        assertEquals("FastDateFormat[dd-MM-yyyy,en_US,GMT]", instance.toString());
    }

    @Test
    public void testGetInstance_WithPatternAndTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance("dd-MM-yyyy", TimeZone.getTimeZone("PST"));
        assertNotNull(instance);
        assertEquals("FastDateFormat[dd-MM-yyyy,en_US,PST]", instance.toString());
    }

    @Test
    public void testGetInstance_WithPatternAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("dd-MM-yyyy", Locale.FRANCE);
        assertNotNull(instance);
        assertEquals("FastDateFormat[dd-MM-yyyy,fr_FR,GMT]", instance.toString());
    }

    @Test
    public void testGetInstance_WithPatternTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("dd-MM-yyyy", TimeZone.getTimeZone("PST"), Locale.FRANCE);
        assertNotNull(instance);
        assertEquals("FastDateFormat[dd-MM-yyyy,fr_FR,PST]", instance.toString());
    }

    // Test getDateInstance() methods
    @Test
    public void testGetDateInstance_Style() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        assertNotNull(instance);
        assertEquals("FastDateFormat[MMM d, yyyy,h:mm:ss a,en_US,GMT]", instance.toString());
    }

    @Test
    public void testGetDateInstance_StyleAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.SHORT, Locale.ITALY);
        assertNotNull(instance);
        assertEquals("FastDateFormat[dd/MM/yy,en_IT,GMT]", instance.toString());
    }

    @Test
    public void testGetDateInstance_StyleAndTimeZone() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.LONG, TimeZone.getTimeZone("PST"));
        assertNotNull(instance);
        assertEquals("FastDateFormat[MMMM d, yyyy,h:mm:ss a,en_US,PST]", instance.toString());
    }

    @Test
    public void testGetDateInstance_StyleTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getTimeZone("PST"), Locale.FRANCE);
        assertNotNull(instance);
        assertEquals("FastDateFormat[EEEE, MMMM d, yyyy,h:mm:ss a,fr_FR,PST]", instance.toString());
    }

    // Test getTimeInstance() methods
    @Test
    public void testGetTimeInstance_Style() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM);
        assertNotNull(instance);
        assertEquals("FastDateFormat[h:mm:ss a,en_US,GMT]", instance.toString());
    }

    @Test
    public void testGetTimeInstance_StyleAndLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.SHORT, Locale.ITALY);
        assertNotNull(instance);
        assertEquals("FastDateFormat[h:mm a,en_IT,GMT]", instance.toString());
    }

    @Test
    public void testGetTimeInstance_StyleAndTimeZone() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.LONG, TimeZone.getTimeZone("PST"));
        assertNotNull(instance);
        assertEquals("FastDateFormat[h:mm:ss a z,en_US,PST]", instance.toString());
    }

    @Test
    public void testGetTimeInstance_StyleTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getTimeInstance(FastDateFormat.FULL, TimeZone.getTimeZone("PST"), Locale.FRANCE);
        assertNotNull(instance);
        assertEquals("FastDateFormat[h:mm:ss a zzzz,fr_FR,PST]", instance.toString());
    }

    // Test getDateTimeInstance() methods
    @Test
    public void testGetDateTimeInstance_Style() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT);
        assertNotNull(instance);
        assertEquals("FastDateFormat[MMM d, yyyy h:mm a,en_US,GMT]", instance.toString());
    }

    @Test
    public void testGetDateTimeInstance_StyleAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.FULL, Locale.ITALY);
        assertNotNull(instance);
        assertEquals("FastDateFormat[dd/MM/yy EEEE h:mm:ss a,en_IT,GMT]", instance.toString());
    }

    @Test
    public void testGetDateTimeInstance_StyleAndTimeZone() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.MEDIUM, TimeZone.getTimeZone("PST"));
        assertNotNull(instance);
        assertEquals("FastDateFormat[MMMM d, yyyy h:mm:ss a z,en_US,PST]", instance.toString());
    }

    @Test
    public void testGetDateTimeInstance_StyleTimeZoneAndLocale() {
        FastDateFormat instance = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.LONG, TimeZone.getTimeZone("PST"), Locale.FRANCE);
        assertNotNull(instance);
        assertEquals("FastDateFormat[EEEE, MMMM d, yyyy h:mm:ss a zzzz,fr_FR,PST]", instance.toString());
    }

    // Test format() methods
    @Test
    public void testFormat_Object() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        assertEquals("2019-05-28T14:30:59.000GMT", instance.format(new Date()));
    }

    @Test
    public void testFormat_Long() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        assertEquals("2019-05-28T14:30:59.000GMT", instance.format(System.currentTimeMillis()));
    }

    @Test
    public void testFormat_Date() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        Date date = new Date();
        assertEquals(instance.format(date.getTime()), instance.format(date));
    }

    @Test
    public void testFormat_Calendar() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        Calendar calendar = Calendar.getInstance();
        assertEquals(instance.format(calendar.getTimeInMillis()), instance.format(calendar));
    }

    @Test
    public void testFormat_LongStringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        StringBuffer buffer = new StringBuffer();
        assertEquals("2019-05-28T14:30:59.000GMT", instance.format(System.currentTimeMillis(), buffer).toString());
    }

    @Test
    public void testFormat_DateStringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        Date date = new Date();
        StringBuffer buffer = new StringBuffer();
        assertEquals(instance.format(date.getTime(), buffer).toString(), instance.format(date, buffer).toString());
    }

    @Test
    public void testFormat_CalendarStringBuffer() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        Calendar calendar = Calendar.getInstance();
        StringBuffer buffer = new StringBuffer();
        assertEquals(instance.format(calendar.getTimeInMillis(), buffer).toString(), instance.format(calendar, buffer).toString());
    }

    // Test parse() methods
    @Test
    public void testParse_String() throws ParseException {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        String source = "28-05-2019";
        Date date = instance.parse(source);
        assertNotNull(date);
        assertEquals(instance.format(date), source);
    }

    @Test
    public void testParse_StringParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        String source = "28-05-2019";
        ParsePosition position = new ParsePosition(0);
        Date date = instance.parse(source, position);
        assertNotNull(date);
        assertEquals(instance.format(date), source);
    }

    // Test parseObject() method
    @Test
    public void testParseObject_StringParsePosition() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        String source = "28-05-2019";
        ParsePosition position = new ParsePosition(0);
        Object obj = instance.parseObject(source, position);
        assertNotNull(obj);
        assertEquals(instance.format((Date) obj), source);
    }

    // Test getter methods
    @Test
    public void testGetPattern() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSSZ", instance.getPattern());
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        assertEquals(TimeZone.getTimeZone("GMT"), instance.getTimeZone());
    }

    @Test
    public void testGetLocale() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        assertEquals(Locale.US, instance.getLocale());
    }

    // Test other methods
    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        assertNotEquals(0, instance.getMaxLengthEstimate());
    }

    @Test
    public void testEquals() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        FastDateFormat other = FastDateFormat.getInstance();
        assertNotNull(other);
        assertEquals(instance, other);
    }

    @Test
    public void testHashCode() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        FastDateFormat other = FastDateFormat.getInstance();
        assertNotNull(other);
        assertEquals(instance.hashCode(), other.hashCode());
    }

    @Test
    public void testToString() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        assertEquals("FastDateFormat[yyyy-MM-dd'T'HH:mm:ss.SSSZ,en_US,GMT]", instance.toString());
    }

    @Test
    public void testApplyRules() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
        Calendar calendar = Calendar.getInstance();
        StringBuffer buffer = new StringBuffer();
        assertEquals(instance.format(calendar.getTimeInMillis(), buffer).toString(), instance.applyRules(calendar, buffer).toString());
    }
}
