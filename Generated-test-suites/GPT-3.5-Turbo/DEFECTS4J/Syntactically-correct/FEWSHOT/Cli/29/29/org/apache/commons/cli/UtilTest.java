package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UtilTest {
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo"));
    }

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
    }
}