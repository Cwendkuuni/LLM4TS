package com.example.iso8601utils; // Replace the package name with the actual package name of your test class

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

    @Test
    public void testFormatDate() throws Exception {
        // Test formatting a date with no milliseconds or timezone
        Date date = new Date();
        String formattedDate = ISO8601Utils.format(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date parsedDate = sdf.parse(formattedDate);
        assertSame(date, parsedDate);

        // Test formatting a date with milliseconds but no timezone
        Date dateWithMillis = new Date(date.getTime() + 123456);
        String formattedDateWithMillis = ISO8601Utils.format(dateWithMillis, true);
        SimpleDateFormat sdfWithMillis = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date parsedDateWithMillis = sdfWithMillis.parse(formattedDateWithMillis);
        assertSame(dateWithMillis, parsedDateWithMillis);

        // Test formatting a date with timezone
        Date dateWithTimezone = new Date(date.getTime() - 3600000);
        String formattedDateWithTimezone = ISO8601Utils.format(dateWithTimezone, false, TimeZone.getTimeZone("Europe/London"));
        SimpleDateFormat sdfWithTimezone = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdfWithTimezone.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        Date parsedDateWithTimezone = sdfWithTimezone.parse(formattedDateWithTimezone);
        assertSame(dateWithTimezone, parsedDateWithTimezone);

        // Test formatting a date with timezone offset
        Date dateWithOffset = new Date(date.getTime() + 60 * 60 * 1000);
        String formattedDateWithOffset = ISO8601Utils.format(dateWithOffset, false, new SimpleDateFormat("GMT+01:00").toZoneValues(SimpleDateFormat.SHORT)[0]);
        SimpleDateFormat sdfWithOffset = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        sdfWithOffset.setTimeZone(new SimpleDateFormat("GMT+01:00").toZoneValues(SimpleDateFormat.SHORT)[0]);
        Date parsedDateWithOffset = sdfWithOffset.parse(formattedDateWithOffset);
        assertSame(dateWithOffset, parsedDateWithOffset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatDateWithNullDate() {
        String formattedDate = ISO8601Utils.format(null);
    }

    @Test
    public void testParseDate() throws ParseException, Exception {
        // Test parsing a date with no time component or timezone
        String dateString = "2022-12-31";
        Date date = ISO8601Utils.parse(dateString, new ParsePosition(0));
        assertEquals(new Date(2022, Calendar.DECEMBER, 31), date);

        // Test parsing a date with time component but no timezone
        String dateStringWithTime = "2022-12-31T23:59:59.123";
        Date dateWithTime = ISO8601Utils.parse(dateStringWithTime, new ParsePosition(0));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(dateWithTime);
        assertEquals(23, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, calendar.get(Calendar.MINUTE));
        assertEquals(59, calendar.get(Calendar.SECOND));
        assertEquals(123, calendar.get(Calendar.MILLISECOND));
        assertEquals(TimeZone.getDefault().getID(), calendar.getTimeZone().getID());

        // Test parsing a date with time component and timezone
        String dateStringWithTimeAndZone = "2022-12-31T23:59:59.123Z";
        Date dateWithTimeAndZone = ISO8601Utils.parse(dateStringWithTimeAndZone, new ParsePosition(0));
        assertEquals(23, dateWithTimeAndZone.getHours());
        assertEquals(59, dateWithTimeAndZone.getMinutes());
        assertEquals(59, dateWithTimeAndZone.getSeconds());
        assertEquals(123, dateWithTimeAndZone.getMilliseconds());
        assertEquals("Z", dateWithTimeAndZone.toString().substring(dateWithTimeAndZone.toString().length() - 1));

        // Test parsing a date with time component and timezone offset
        String dateStringWithTimeAndOffset = "2022-12-31T23:59:59.123+01:00";
        Date dateWithTimeAndOffset = ISO8601Utils.parse(dateStringWithTimeAndOffset, new ParsePosition(0));
        Calendar calendarWithOffset = new GregorianCalendar(TimeZone.getTimeZone("GMT+01:00"));
        calendarWithOffset.setTime(dateWithTimeAndOffset);
        assertEquals(23, calendarWithOffset.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, calendarWithOffset.get(Calendar.MINUTE));
        assertEquals(59, calendarWithOffset.get(Calendar.SECOND));
        assertEquals(123, calendarWithOffset.get(Calendar.MILLISECOND));
        assertEquals("GMT+01:00", calendarWithOffset.getTimeZone().getID());
    }

    @Test(expected = ParseException.class)
    public void testParseDateWithInvalidFormat() throws ParseException {
        String dateString = "2022-12-31T23:59:59Za";
        ISO8601Utils.parse(dateString, new ParsePosition(0));
    }
}