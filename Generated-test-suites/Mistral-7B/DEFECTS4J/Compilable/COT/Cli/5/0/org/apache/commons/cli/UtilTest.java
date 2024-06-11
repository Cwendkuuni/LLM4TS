package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str = "--foo";
        String expected = "foo";
        String actual = Util.stripLeadingHyphens(str);
        assertEquals(expected, actual);

        str = "-foo";
        expected = "foo";
        actual = Util.stripLeadingHyphens(str);
        assertEquals(expected, actual);

        str = "foo";
        expected = "foo";
        actual = Util.stripLeadingHyphens(str);
        assertEquals(expected, actual);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingHyphensNullInput() {
        Util.stripLeadingHyphens(null);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = "\"one two\"";
        String expected = "one two";
        String actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);

        str = "one two";
        expected = "one two";
        actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);

        str = "";
        expected = "";
        actual = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingAndTrailingQuotesNullInput() {
        Util.stripLeadingAndTrailingQuotes(null);
    }
}