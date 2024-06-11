package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_nullString() {
        String str = null;
        String result = Util.stripLeadingHyphens(str);
        assertNull(result);
    }

    @Test
    public void testStripLeadingHyphens_startsWithDoubleHyphen() {
        String str = "--test";
        String result = Util.stripLeadingHyphens(str);
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingHyphens_startsWithSingleHyphen() {
        String str = "-test";
        String result = Util.stripLeadingHyphens(str);
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingHyphens_noLeadingHyphen() {
        String str = "test";
        String result = Util.stripLeadingHyphens(str);
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_startsWithQuote() {
        String str = "\"test";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_endsWithQuote() {
        String str = "test\"";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_startAndEndWithQuotes() {
        String str = "\"test\"";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals("test", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_noQuotes() {
        String str = "test";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals("test", result);
    }
}