package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // Typical use case
        String formattedDate = ISO8601Utils.format(new Date());
        assertNotNull(formattedDate);
        
        // Edge case - include millis precision
        String formattedDateMillis = ISO8601Utils.format(new Date(), true);
        assertNotNull(formattedDateMillis);
        
        // Edge case - custom timezone
        TimeZone customTimeZone = TimeZone.getTimeZone("GMT+05:30");
        String formattedDateCustomTZ = ISO8601Utils.format(new Date(), true, customTimeZone);
        assertNotNull(formattedDateCustomTZ);
    }

    @Test
    public void testParse() {
        ParsePosition pos = new ParsePosition(0);
        
        // Typical use case
        try {
            String dateStr = "2022-01-01T12:00:00Z";
            Date parsedDate = ISO8601Utils.parse(dateStr, pos);
            assertNotNull(parsedDate);
        } catch (ParseException e) {
            fail("Failed to parse date: " + e.getMessage());
        }

        
        // Edge case - date with timezone offset
        try {
            String dateStr = "2022-01-01T12:00:00+05:30";
            Date parsedDate = ISO8601Utils.parse(dateStr, pos);
            assertNotNull(parsedDate);
        } catch (ParseException e) {
            fail("Failed to parse date: " + e.getMessage());
        }
        
        // Error scenario - invalid date format
        try {
            String dateStr = "2022/01/01T12:00:00Z";
            Date parsedDate = ISO8601Utils.parse(dateStr, pos);
            fail("Expected ParseException but parsing succeeded");
        } catch (ParseException e) {
            // Expected exception
        }
    }

}