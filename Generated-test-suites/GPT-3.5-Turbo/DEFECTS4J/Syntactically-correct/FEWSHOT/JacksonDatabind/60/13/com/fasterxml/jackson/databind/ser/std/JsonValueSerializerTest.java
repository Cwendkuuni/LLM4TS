// Test for stripLeadingAndTrailingQuotes method
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonValueSerializerTest {
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("test value", Util.stripLeadingAndTrailingQuotes("\"test value\""));
        assertEquals("test value", Util.stripLeadingAndTrailingQuotes("'test value'"));
        assertEquals("test value", Util.stripLeadingAndTrailingQuotes("test value"));
        assertEquals("\"test value\" and 'another value'", Util.stripLeadingAndTrailingQuotes("\"test value\" and 'another value'"));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));
    }
}