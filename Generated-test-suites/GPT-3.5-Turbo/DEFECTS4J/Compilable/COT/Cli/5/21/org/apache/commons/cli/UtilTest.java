package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str1 = Util.stripLeadingHyphens("--example");
        assertEquals("example", str1);

        String str2 = Util.stripLeadingHyphens("-example");
        assertEquals("example", str2);

        String str3 = Util.stripLeadingHyphens("example");
        assertEquals("example", str3);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str1 = Util.stripLeadingAndTrailingQuotes("\"example\"");
        assertEquals("example", str1);

        String str2 = Util.stripLeadingAndTrailingQuotes("\"example");
        assertEquals("example", str2);

        String str3 = Util.stripLeadingAndTrailingQuotes("example\"");
        assertEquals("example", str3);

        String str4 = Util.stripLeadingAndTrailingQuotes("example");
        assertEquals("example", str4);
    }

    // Additional edge case and exception handling test cases:

    @Test
    public void testStripLeadingHyphens_EmptyString() {
        String str = Util.stripLeadingHyphens("");
        assertEquals("", str);
    }

    @Test
    public void testStripLeadingHyphens_NullString() {
        String str = Util.stripLeadingHyphens(null);
        assertEquals(null, str);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_EmptyString() {
        String str = Util.stripLeadingAndTrailingQuotes("");
        assertEquals("", str);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_NullString() {
        String str = Util.stripLeadingAndTrailingQuotes(null);
        assertEquals(null, str);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_OnlyQuotes() {
        String str = Util.stripLeadingAndTrailingQuotes("\"\"");
        assertEquals("", str);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_NoQuotes() {
        String str = Util.stripLeadingAndTrailingQuotes("example");
        assertEquals("example", str);
    }

}