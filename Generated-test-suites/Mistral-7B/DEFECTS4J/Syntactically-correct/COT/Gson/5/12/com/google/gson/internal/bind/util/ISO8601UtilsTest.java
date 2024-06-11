package com.google.gson.internal.bind.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

/**
 * Test cases for ISO8601Utils class
 */
public class ISO8601UtilsTest {

    @Test
    public void testFormat() throws Exception {
        // Testing format method with various inputs
        Date date1 = new GregorianCalendar(2019, Calendar.JANUARY, 1).getTime();
        String dateString1 = ISO8601Utils.format(date1);
        assertEquals("2019-01-01T00:00:00Z", dateString1);

        Date date2 = new GregorianCalendar(2019, Calendar.APRIL, 15, 14, 30, 45).getTime();
        String dateString2 = ISO8601Utils.format(date2, true, TimeZone.getDefault());
        assertEquals("2019-04-15T14:30:45.045Z", dateString2);

        Date date3 = new GregorianCalendar(2020, Calendar.DECEMBER, 31, 23, 59, 59).getTime();
        String dateString3 = ISO8601Utils.format(date3, true, TimeZone.getTimeZone("Asia/Kolkata"));
        assertEquals("2020-12-31T23:59:59.000+05:30", dateString3);
    }

    @Test
    public void testParse() throws ParseException {
        // Testing parse method with various inputs
        String dateString1 = "2019-01-01T00:00:00Z";
        ParsePosition parsePosition1 = new ParsePosition(0);
        Date date1 = ISO8601Utils.parse(dateString1, parsePosition1);
        assertEquals("Wed Jan 01 00:00:00 IST 2019", date1.toString());

        String dateString2 = "2019-04-15T14:30:45.045Z";
        ParsePosition parsePosition2 = new ParsePosition(0);
        Date date2 = ISO8601Utils.parse(dateString2, parsePosition2);
        assertEquals("Mon Apr 15 14:30:45 IST 2019", date2.toString());

        String dateString3 = "2020-12-31T23:59:59.000+05:30";
        ParsePosition parsePosition3 = new ParsePosition(0);
        Date date3 = ISO8601Utils.parse(dateString3, parsePosition3);
        assertEquals("Fri Dec 31 23:59:59 IST 2020", date3.toString());

        // Testing parse method with invalid inputs
        String invalidDateString1 = "2019-13-01";
        ParsePosition parsePositionInvalid1 = new ParsePosition(0);
        try {
            ISO8601Utils.parse(invalidDateString1, parsePositionInvalid1);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        String invalidDateString2 = "2019-01-01T14:60:00"; // Invalid minute value
        ParsePosition parsePositionInvalid2 = new ParsePosition(0);
        try {
            ISO8601Utils.parse(invalidDateString2, parsePositionInvalid2);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        String invalidDateString3 = "2019-01-01T14:30:60"; // Invalid second value
        ParsePosition parsePositionInvalid3 = new ParsePosition(0);
        try {
            ISO8601Utils.parse(invalidDateString3, parsePositionInvalid3);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }
}