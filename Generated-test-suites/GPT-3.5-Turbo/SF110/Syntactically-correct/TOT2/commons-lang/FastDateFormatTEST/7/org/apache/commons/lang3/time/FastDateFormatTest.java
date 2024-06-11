package org.apache.commons.lang3.time;

import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.util.*;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat instance = FastDateFormat.getInstance();
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPattern() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPatternAndTimeZone() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT"));
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceWithPatternAndLocale() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss", Locale.US);
        assertNotNull(instance);
    }

    // Add additional test cases for other getInstance methods

    // Add test cases for other public methods in FastDateFormat class

    @Test
    public void testParse() throws ParseException {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        Date date = instance.parse("2021-05-20 12:30:00");
        assertNotNull(date);
    }

    @Test
    public void testParseWithInvalidInput() {
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = instance.parse("InvalidDate");
            fail("ParseException expected");
        } catch (ParseException e) {
            // Expected exception
        }
    }

    // Add test cases for other exception handling scenarios

}
