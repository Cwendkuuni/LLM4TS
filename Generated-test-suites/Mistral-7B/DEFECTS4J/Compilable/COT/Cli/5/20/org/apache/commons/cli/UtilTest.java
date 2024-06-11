package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str = "--some-string";
        String expected = "some-string";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = "some-string";
        expected = "some-string";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = "some-string";
        expected = "some-string";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = "some-string--";
        expected = "some-string";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = "";
        result = Util.stripLeadingHyphens(str);
        assertNull(result);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingHyphensNullInput() {
        String str = null;
        Util.stripLeadingHyphens(str);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = "\"one two\"";
        String expected = "one two";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "one two";
        expected = "one two";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "\"one two\"";
        expected = "one two";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertNull(result);

        str = "\"\"";
        expected = "";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesEmptyString() {
        String str = "\"\"";
        String expected = "";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }
}