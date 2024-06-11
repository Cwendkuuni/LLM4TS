package org.apache.commons.cli;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str1 = "--test";
        String str2 = "-test";
        String str3 = "test";
        
        assert Util.stripLeadingHyphens(str1).equals("test");
        assert Util.stripLeadingHyphens(str2).equals("test");
        assert Util.stripLeadingHyphens(str3).equals("test");
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str1 = "\"test\"";
        String str2 = "\"test";
        String str3 = "test\"";
        String str4 = "test";

        assert Util.stripLeadingAndTrailingQuotes(str1).equals("test");
        assert Util.stripLeadingAndTrailingQuotes(str2).equals("test");
        assert Util.stripLeadingAndTrailingQuotes(str3).equals("test");
        assert Util.stripLeadingAndTrailingQuotes(str4).equals("test");
    }
}