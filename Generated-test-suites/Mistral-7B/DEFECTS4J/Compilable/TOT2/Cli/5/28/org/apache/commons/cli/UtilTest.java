
package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str = "--test";
        String expected = "test";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = "'one two'";
        String expected = "one two";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingHyphensEmptyString() {
        String str = "";
        String expected = "";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesEmptyString() {
        String str = "";
        String expected = "";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingHyphensMultipleLeadingHyphens() {
        String str = "---test";
        String expected = "test";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesMultipleLeadingAndTrailingQuotes() {
        String str = "'''one two'''";
        String expected = "one two";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingHyphensStringWithoutHyphens() {
        String str = "test";
        String result = Util.stripLeadingHyphens(str);
        // Expected exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingAndTrailingQuotesStringWithoutQuotes() {
        String str = "test";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        // Expected exception
    }
}
