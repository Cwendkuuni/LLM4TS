package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str1 = "--example";
        String str2 = "-example";
        String str3 = "example";
        String result1 = Util.stripLeadingHyphens(str1);
        String result2 = Util.stripLeadingHyphens(str2);
        String result3 = Util.stripLeadingHyphens(str3);
        assertEquals("example", result1);
        assertEquals("example", result2);
        assertEquals("example", result3);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str1 = "\"example\"";
        String str2 = "example";
        String result1 = Util.stripLeadingAndTrailingQuotes(str1);
        String result2 = Util.stripLeadingAndTrailingQuotes(str2);
        assertEquals("example", result1);
        assertEquals("example", result2);
    }

}