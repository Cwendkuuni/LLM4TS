package org.apache.commons.cli;

import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str1 = "--test";
        String str2 = "-test";
        String str3 = "test";

        String result1 = Util.stripLeadingHyphens(str1);
        String result2 = Util.stripLeadingHyphens(str2);
        String result3 = Util.stripLeadingHyphens(str3);

        assert result1.equals("test");
        assert result2.equals("test");
        assert result3.equals("test");
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str1 = "\"test\"";
        String str2 = "test";

        String result1 = Util.stripLeadingAndTrailingQuotes(str1);
        String result2 = Util.stripLeadingAndTrailingQuotes(str2);

        assert result1.equals("test");
        assert result2.equals("test");
    }
}
