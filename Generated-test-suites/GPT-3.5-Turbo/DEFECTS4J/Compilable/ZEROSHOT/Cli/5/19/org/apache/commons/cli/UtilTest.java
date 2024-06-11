package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str1 = "--test";
        String str2 = "-test";
        String str3 = "test";

        String result1 = Util.stripLeadingHyphens(str1);
        String result2 = Util.stripLeadingHyphens(str2);
        String result3 = Util.stripLeadingHyphens(str3);

        assertEquals("test", result1);
        assertEquals("test", result2);
        assertEquals("test", result3);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str1 = "\"one two\"";
        String str2 = "one two";

        String result1 = Util.stripLeadingAndTrailingQuotes(str1);
        String result2 = Util.stripLeadingAndTrailingQuotes(str2);

        assertEquals("one two", result1);
        assertEquals("one two", result2);
    }

}