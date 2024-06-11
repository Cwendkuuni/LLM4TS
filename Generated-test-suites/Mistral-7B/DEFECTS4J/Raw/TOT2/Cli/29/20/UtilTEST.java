package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {
    @Test
    public void testStripLeadingHyphens() {
        String str = "--test";
        String expected = "test";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test
    public void testNullInput() {
        String str = null;
        String result = Util.stripLeadingHyphens(str);
        assertEquals(null, result);
    }

    @Test
    public void testEmptyString() {
        String str = "";
        String result = Util.stripLeadingHyphens(str);
        assertEquals("", result);
    }

    @Test
    public void testStringWithoutLeadingHyphens() {
        String str = "test";
        String result = Util.stripLeadingHyphens(str);
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = "\"one two\"";
        String expected = "one two";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testNullInputQuotes() {
        String str = null;
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(null, result);
    }

    @Test
    public void testEmptyStringQuotes() {
        String str = "";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals("", result);
    }

    @Test
    public void testStringWithoutLeadingOrTrailingQuotes() {
        String str = "one two";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals("one two", result);
    }
}