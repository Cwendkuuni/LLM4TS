package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str = "---foo";
        String expected = "foo";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = "foo";
        result = Util.stripLeadingHyphens(str);
        assertEquals(str, result);

        str = "foo--";
        expected = "foo";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = null;
        result = Util.stripLeadingHyphens(str);
        assertNull(result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = "\"one two\"";
        String expected = "one two";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "\"one two\"\"";
        expected = "one two";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "one two";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(str, result);

        str = null;
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertNull(result);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingHyphensWithNullInputThrowsAssertionError() {
        String str = null;
        Util.stripLeadingHyphens(str);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testStripLeadingHyphensWithEmptyStringThrowsStringIndexOutOfBoundsException() {
        String str = "";
        Util.stripLeadingHyphens(str);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingAndTrailingQuotesWithNullInputThrowsAssertionError() {
        String str = null;
        Util.stripLeadingAndTrailingQuotes(str);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithEmptyString() {
        String str = "";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(str, result);
    }
}