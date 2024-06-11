package org.apache.commons.cli;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_NullInput() {
        String result = Util.stripLeadingHyphens(null);
        assertEquals(null, result);
    }

    @Test
    public void testStripLeadingHyphens_StartsWithHyphens() {
        String result = Util.stripLeadingHyphens("--test");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingHyphens_StartsWithSingleHyphen() {
        String result = Util.stripLeadingHyphens("-test");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingHyphens_NoLeadingHyphens() {
        String result = Util.stripLeadingHyphens("test");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_StartsWithQuotes() {
        String result = Util.stripLeadingAndTrailingQuotes("\"test\"");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_EndsWithQuotes() {
        String result = Util.stripLeadingAndTrailingQuotes("test\"");
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_NoQuotes() {
        String result = Util.stripLeadingAndTrailingQuotes("test");
        assertEquals("test", result);
    }
}