package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str = "--test";
        String expected = "test";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = "-test";
        expected = "test";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = "test";
        expected = "test";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingHyphensNullInput() {
        String str = null;
        Util.stripLeadingHyphens(str);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = "\"test1 test2\"";
        String expected = "test1 test2";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesNoQuotes() {
        String str = "test1 test2";
        String expected = "test1 test2";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingAndTrailingQuotesNullInput() {
        String str = null;
        Util.stripLeadingAndTrailingQuotes(str);
    }
}