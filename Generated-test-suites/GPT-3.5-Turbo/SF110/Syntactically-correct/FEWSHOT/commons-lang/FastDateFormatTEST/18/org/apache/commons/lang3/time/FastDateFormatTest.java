package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FastDateFormatTest {
    @Test
    public void testGetInstance() {
        FastDateFormat format = FastDateFormat.getInstance();
        assertNotNull(format);

        format = FastDateFormat.getInstance("yyyy-MM-dd");
        assertNotNull(format);

        format = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"));
        assertNotNull(format);

        format = FastDateFormat.getInstance("yyyy-MM-dd", Locale.US);
        assertNotNull(format);

        format = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(format);
    }

    @Test
    public void testGetDateInstance() {
        FastDateFormat format = FastDateFormat.getDateInstance(FastDateFormat.FULL);
        assertNotNull(format);

        format = FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.US);
        assertNotNull(format);

        format = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"));
        assertNotNull(format);

        format = FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(format);
    }

    @Test
    public void testGetTimeInstance() {
        FastDateFormat format = FastDateFormat.getTimeInstance(FastDateFormat.FULL);
        assertNotNull(format);

        format = FastDateFormat.getTimeInstance(FastDateFormat.FULL, Locale.US);
        assertNotNull(format);

        format = FastDateFormat.getTimeInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"));
        assertNotNull(format);

        format = FastDateFormat.getTimeInstance(FastDateFormat.FULL, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(format);
    }

    @Test
    public void testGetDateTimeInstance() {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL);
        assertNotNull(format);

        format = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, Locale.US);
        assertNotNull(format);

        format = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, TimeZone.getTimeZone("GMT"));
        assertNotNull(format);

        format = FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, TimeZone.getTimeZone("GMT"), Locale.US);
        assertNotNull(format);
    }

    @Test
    public void testFormat() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        String result = format.format(1595961600000L);
        assertEquals("2020-07-28", result);

        result = format.format(new Date(1595961600000L));
        assertEquals("2020-07-28", result);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 6, 28);
        result = format.format(calendar);
        assertEquals("2020-07-28", result);
    }

    @Test
    public void testParse() throws ParseException {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        Date result = format.parse("2020-07-28");
        assertEquals(1595952000000L, result.getTime());

        result = format.parse("2020-07-28 00:00:00");
        assertEquals(1595952000000L, result.getTime());

        result = format.parse("2020-07-28 12:34:56", new ParsePosition(0));
        assertEquals(1595952000000L, result.getTime());

        assertNull(format.parse("invalid"));
    }

    @Test
    public void testGetPattern() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals("yyyy-MM-dd", format.getPattern());
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT"));
        assertEquals("GMT", format.getTimeZone().getID());
    }

    @Test
    public void testGetLocale() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd", Locale.US);
        assertEquals(Locale.US, format.getLocale());
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals(10, format.getMaxLengthEstimate());
    }

    @Test
    public void testEquals() {
        FastDateFormat format1 = FastDateFormat.getInstance("yyyy-MM-dd");
        FastDateFormat format2 = FastDateFormat.getInstance("yyyy-MM-dd");
        assertTrue(format1.equals(format2));
    }

    @Test
    public void testHashCode() {
        FastDateFormat format1 = FastDateFormat.getInstance("yyyy-MM-dd");
        FastDateFormat format2 = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals(format1.hashCode(), format2.hashCode());
    }

    @Test
    public void testToString() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        assertEquals("FastDateFormat[yyyy-MM-dd,us,GMT]", format.toString());
    }

    @Test
    public void testApplyRules() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        StringBuffer buffer = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        buffer = format.applyRules(calendar, buffer);
        assertNotNull(buffer);
    }
}