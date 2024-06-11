package org.apache.commons.lang3.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.junit.Test;

public class FastDateFormatTest {

    @Test
    public void testFormat_Object_StringBuffer_FieldPosition() {
        FastDateFormat format = FastDateFormat.getInstance();
        StringBuffer buffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);

        assertEquals(buffer, format.format("obj", buffer, fieldPosition));
        assertEquals(0, fieldPosition.getBeginIndex());
        assertEquals(0, fieldPosition.getEndIndex());
    }

    @Test
    public void testFormat_long() {
        FastDateFormat format = FastDateFormat.getInstance();
        long millis = 123456789L;

        assertEquals("1973-11-29 21:33:09", format.format(millis));
    }

    @Test
    public void testFormat_Date() {
        FastDateFormat format = FastDateFormat.getInstance();
        Date date = new Date(123456789L);

        assertEquals("1973-11-29 21:33:09", format.format(date));
    }

    @Test
    public void testFormat_Calendar() {
        FastDateFormat format = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(1973, 10, 29, 21, 33, 9);

        assertEquals("1973-11-29 21:33:09", format.format(calendar));
    }

    @Test
    public void testFormat_long_StringBuffer() {
        FastDateFormat format = FastDateFormat.getInstance();
        long millis = 123456789L;
        StringBuffer buffer = new StringBuffer();

        assertEquals(buffer, format.format(millis, buffer));
    }

    @Test
    public void testFormat_Date_StringBuffer() {
        FastDateFormat format = FastDateFormat.getInstance();
        Date date = new Date(123456789L);
        StringBuffer buffer = new StringBuffer();

        assertEquals(buffer, format.format(date, buffer));
    }

    @Test
    public void testFormat_Calendar_StringBuffer() {
        FastDateFormat format = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(1973, 10, 29, 21, 33, 9);
        StringBuffer buffer = new StringBuffer();

        assertEquals(buffer, format.format(calendar, buffer));
    }

    @Test
    public void testParse_String() throws ParseException {
        FastDateFormat format = FastDateFormat.getInstance();
        String source = "1973-11-29 21:33:09";

        assertEquals(format.parse(source), format.parse(source, new ParsePosition(0)));
    }

    @Test
    public void testParse_String_ParsePosition() {
        FastDateFormat format = FastDateFormat.getInstance();
        String source = "1973-11-29 21:33:09";

        assertNull(format.parse(source, new ParsePosition(-1)));
    }

    @Test
    public void testParseObject_String_ParsePosition() {
        FastDateFormat format = FastDateFormat.getInstance();
        String source = "1973-11-29 21:33:09";

        assertNull(format.parseObject(source, new ParsePosition(-1)));
    }

    @Test
    public void testGetPattern() {
        FastDateFormat format = FastDateFormat.getInstance();

        assertEquals("yyyy-MM-dd HH:mm:ss", format.getPattern());
    }

    @Test
    public void testGetTimeZone() {
        FastDateFormat format = FastDateFormat.getInstance();

        assertEquals(TimeZone.getDefault(), format.getTimeZone());
    }

    @Test
    public void testGetLocale() {
        FastDateFormat format = FastDateFormat.getInstance();

        assertEquals(Locale.getDefault(), format.getLocale());
    }

    @Test
    public void testGetMaxLengthEstimate() {
        FastDateFormat format = FastDateFormat.getInstance();

        assertTrue(format.getMaxLengthEstimate() > 0);
    }

    @Test
    public void testEquals_Object() {
        FastDateFormat format1 = FastDateFormat.getInstance();
        FastDateFormat format2 = FastDateFormat.getInstance();

        assertTrue(format1.equals(format2));
    }

    @Test
    public void testHashCode() {
        FastDateFormat format = FastDateFormat.getInstance();

        assertEquals(format.hashCode(), format.hashCode());
    }

    @Test
    public void testToString() {
        FastDateFormat format = FastDateFormat.getInstance();

        assertEquals("FastDateFormat[yyyy-MM-dd HH:mm:ss," + format.getLocale() +
                "," + format.getTimeZone().getID() + "]", format.toString());
    }

    @Test
    public void testApplyRules_Calendar_StringBuffer() {
        FastDateFormat format = FastDateFormat.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(1973, 10, 29, 21, 33, 9);
        StringBuffer buffer = new StringBuffer();

        assertEquals(buffer, format.applyRules(calendar, buffer));
    }
}