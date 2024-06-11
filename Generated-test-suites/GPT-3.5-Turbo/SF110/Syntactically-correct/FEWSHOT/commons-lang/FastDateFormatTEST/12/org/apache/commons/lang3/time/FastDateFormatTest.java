
package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        assertNotNull(FastDateFormat.getInstance());
        assertNotNull(FastDateFormat.getInstance("yyyy-MM-dd"));
        assertNotNull(FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getDefault()));
        assertNotNull(FastDateFormat.getInstance("yyyy-MM-dd", Locale.getDefault()));
        assertNotNull(FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getDefault(), Locale.getDefault()));
    }

    @Test
    public void testGetDateInstance() {
        assertNotNull(FastDateFormat.getDateInstance(FastDateFormat.FULL));
        assertNotNull(FastDateFormat.getDateInstance(FastDateFormat.FULL, Locale.getDefault()));
        assertNotNull(FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getDefault()));
        assertNotNull(FastDateFormat.getDateInstance(FastDateFormat.FULL, TimeZone.getDefault(), Locale.getDefault()));
    }


    @Test
    public void testGetTimeInstance() {
        assertNotNull(FastDateFormat.getTimeInstance(FastDateFormat.LONG));
        assertNotNull(FastDateFormat.getTimeInstance(FastDateFormat.LONG, Locale.getDefault()));
        assertNotNull(FastDateFormat.getTimeInstance(FastDateFormat.LONG, TimeZone.getDefault()));
        assertNotNull(FastDateFormat.getTimeInstance(FastDateFormat.LONG, TimeZone.getDefault(), Locale.getDefault()));
    }

    @Test
    public void testGetDateTimeInstance() {
        assertNotNull(FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG));
        assertNotNull(FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG, Locale.getDefault()));
        assertNotNull(FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG, TimeZone.getDefault()));
        assertNotNull(FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG, TimeZone.getDefault(), Locale.getDefault()));
    }

    @Test
    public void testFormat() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        Date date = new Date();

        assertEquals("2022-03-25", format.format(date));

        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 2, 25);

        assertEquals("2022-03-25", format.format(calendar));

        StringBuffer buf = new StringBuffer();
        assertNotNull(format.format(date, buf));
        assertNotNull(format.format(calendar, buf));
        assertEquals("2022-03-252022-03-25", buf.toString());
    }

    @Test
    public void testParse() throws ParseException {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        Date expectedDate = new Date();

        String dateString = format.format(expectedDate);

        Date parsedDate = format.parse(dateString);

        assertEquals(expectedDate, parsedDate);

        assertNull(format.parse("invalid-date"));
    }

    @Test
    public void testGetPattern() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");

        assertEquals("yyyy-MM-dd", format.getPattern());
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");

        assertNotNull(format.getTimeZone());
    }

    @Test
    public void testGetLocale() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");

        assertNotNull(format.getLocale());
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

        assertEquals(format1, format2);

        FastDateFormat format3 = FastDateFormat.getInstance("yyyy-MM-dd", Locale.getDefault());

        assertNotNull(format3);
        assertNotNull(format1);
        assertNotNull(format2);
        assertNotNull(new Object());

        assertEquals(format1, format3);
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

        String expectedString = "FastDateFormat[yyyy-MM-dd," + format.getLocale() + "," + format.getTimeZone().getID() + "]";

        assertEquals(expectedString, format.toString());
    }

    @Test
    public void testApplyRules() {
        FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 2, 25);

        StringBuffer buf = new StringBuffer();

        assertNotNull(format.applyRules(calendar, buf));
    }
}
