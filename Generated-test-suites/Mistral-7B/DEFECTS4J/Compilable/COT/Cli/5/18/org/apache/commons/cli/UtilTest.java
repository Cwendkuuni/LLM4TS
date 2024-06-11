package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str = "--test";
        String expected = "test";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = "test";
        expected = "test";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = "test--";
        expected = "test";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testStripLeadingHyphensWithNullInput() {
        String str = null;
        Util.stripLeadingHyphens(str);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = "\"test1 test2\"";
        String expected = "test1 test2";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "test1 test2";
        expected = "test1 test2";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "\"test1 test2\"\n";
        expected = "test1 test2";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testStripLeadingAndTrailingQuotesWithNullInput() {
        String str = null;
        Util.stripLeadingAndTrailingQuotes(str);
    }
}