package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        Date date = new Date(1627822200000);  // 2021-08-01T12:23:20Z
        String formattedDate = ISO8601Utils.format(date);
        assertEquals("2021-08-01T12:23:20Z", formattedDate);
    }
    
    @Test
    public void testFormatWithMillis() {
        Date date = new Date(1627822200123);  // 2021-08-01T12:23:20.123Z
        String formattedDate = ISO8601Utils.format(date, true);
        assertEquals("2021-08-01T12:23:20.123Z", formattedDate);
    }
    
    @Test
    public void testFormatWithCustomTimezone() {
        Date date = new Date(1627822200000);  // 2021-08-01T12:23:20Z
        TimeZone customTimezone = TimeZone.getTimeZone("GMT+02:00");
        String formattedDate = ISO8601Utils.format(date, false, customTimezone);
        assertEquals("2021-08-01T14:23:20+02:00", formattedDate);
    }

    @Test
    public void testParse() throws ParseException {
        String iso8601Date = "2021-08-01T12:23:20Z";
        ParsePosition pos = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse(iso8601Date, pos);
        
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(parsedDate);
        
        assertEquals(2021, calendar.get(Calendar.YEAR));
        assertEquals(7, calendar.get(Calendar.MONTH)); // Month is zero-based
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(12, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(23, calendar.get(Calendar.MINUTE));
        assertEquals(20, calendar.get(Calendar.SECOND));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidFormat() throws ParseException {
        String invalidIso8601Date = "2021-08-01T12:23:20";
        ParsePosition pos = new ParsePosition(0);
        ISO8601Utils.parse(invalidIso8601Date, pos);
    }

}