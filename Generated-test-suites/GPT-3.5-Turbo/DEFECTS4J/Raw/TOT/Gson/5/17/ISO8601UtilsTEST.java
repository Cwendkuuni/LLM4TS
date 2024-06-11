
package com.google.gson.internal.bind.util;

import org.junit.*;
import static org.junit.Assert.*;

public class ISO8601UtilsTest {

    @Test
    public void testFormat() {
        // Typical use case with a date without milliseconds
        String formattedDate = ISO8601Utils.format(new Date(1560816000000L));
        assertEquals("2019-06-18T00:00:00Z", formattedDate);

        // Typical use case with a date with milliseconds
        String formattedDateWithMillis = ISO8601Utils.format(new Date(1560816000123L), true);
        assertEquals("2019-06-18T00:00:00.123Z", formattedDateWithMillis);
    }

    @Test
    public void testParse() throws ParseException {
        // Typical use case with a date without milliseconds
        ParsePosition parsePosition = new ParsePosition(0);
        Date parsedDate = ISO8601Utils.parse("2019-06-18T00:00:00Z", parsePosition);
        assertNotNull(parsedDate);
        assertEquals(1560816000000L, parsedDate.getTime());
        assertEquals(20, parsePosition.getIndex());

        // Typical use case with a date with milliseconds
        ParsePosition parsePositionWithMillis = new ParsePosition(0);
        Date parsedDateWithMillis = ISO8601Utils.parse("2019-06-18T00:00:00.123Z", parsePositionWithMillis);
        assertNotNull(parsedDateWithMillis);
        assertEquals(1560816000123L, parsedDateWithMillis.getTime());
        assertEquals(24, parsePositionWithMillis.getIndex());

        // Edge case with minimum valid date
        ParsePosition parsePositionMin = new ParsePosition(0);
        Date parsedDateMin = ISO8601Utils.parse("0001-01-01T00:00:00Z", parsePositionMin);
        assertNotNull(parsedDateMin);
        assertEquals(-62135596800000L, parsedDateMin.getTime());
        assertEquals(20, parsePositionMin.getIndex());

        // Edge case with maximum valid date
        ParsePosition parsePositionMax = new ParsePosition(0);
        Date parsedDateMax = ISO8601Utils.parse("9999-12-31T23:59:59Z", parsePositionMax);
        assertNotNull(parsedDateMax);
        assertEquals(253402300799000L, parsedDateMax.getTime());
        assertEquals(24, parsePositionMax.getIndex());

        // Error scenario with invalid date format
        ParsePosition parsePositionInvalid = new ParsePosition(0);
        try {
            ISO8601Utils.parse("2019-06-18T00:00:00", parsePositionInvalid);
            fail("ParseException expected");
        } catch (ParseException e) {
            assertEquals("Failed to parse date [\"2019-06-18T00:00:00\"]: (java.text.ParseException)", e.getMessage());
            assertEquals(19, e.getErrorOffset());
        }
    }

    @Test
    public void customTestMethod() {
        // Add your custom test method here
    }
}

