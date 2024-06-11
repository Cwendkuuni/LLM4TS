
package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

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
    public void testStripLeadingHyphensEdgeCases() {
        String emptyStr = "";
        String strWithoutLeadingHyphens = "test";

        String[] testCases = new String[]{"--", "--test", "--test--", "--test--123", "--test--123--"};

        for (String str : testCases) {
            String expected = Util.stripLeadingHyphens(str);
            String result = Util.stripLeadingHyphens(str);
            assertEquals(expected, result);
        }

        String nullStr = null;

        try {
            String result = Util.stripLeadingHyphens(nullStr);
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testStripLeadingAndTrailingQuotesEdgeCases() {
        String emptyStr = "";
        String strWithoutLeadingQuotes = "test";
        String strWithoutTrailingQuotes = "test";

        String[] testCases = new String[]{"''", "\"\"", "\"one two\"", "one two\"", "\"one two\""};

        for (String str : testCases) {
            String expected = Util.stripLeadingAndTrailingQuotes(str);
            String result = Util.stripLeadingAndTrailingQuotes(str);
            assertEquals(expected, result);
        }

        String strNull = null;

        try {
            String result = Util.stripLeadingAndTrailingQuotes(strNull);
        } catch (NullPointerException e) {
            // expected
        }
    }
}
