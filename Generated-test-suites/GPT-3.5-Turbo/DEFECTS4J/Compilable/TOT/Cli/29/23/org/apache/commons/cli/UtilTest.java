package org.apache.commons.cli;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical use cases
        Assert.assertEquals("string", Util.stripLeadingHyphens("--string"));
        Assert.assertEquals("value", Util.stripLeadingHyphens("-value"));

        // Edge cases
        Assert.assertEquals("single-", Util.stripLeadingHyphens("-single-"));
        Assert.assertEquals("-double-", Util.stripLeadingHyphens("--double-"));
        Assert.assertNull(Util.stripLeadingHyphens(null));

        // Error scenarios
        Assert.assertEquals("", Util.stripLeadingHyphens(""));
        Assert.assertEquals("        ", Util.stripLeadingHyphens("        "));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use cases
        Assert.assertEquals("string", Util.stripLeadingAndTrailingQuotes("\"string\""));
        Assert.assertEquals("value", Util.stripLeadingAndTrailingQuotes("value"));

        // Edge cases
        Assert.assertEquals("\"single-", Util.stripLeadingAndTrailingQuotes("\"single-"));
        Assert.assertEquals("\"double-", Util.stripLeadingAndTrailingQuotes("\"double-"));
        Assert.assertEquals("\"", Util.stripLeadingAndTrailingQuotes("\""));
        Assert.assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        Assert.assertNull(Util.stripLeadingAndTrailingQuotes(null));

        // Error scenarios
        Assert.assertEquals("        ", Util.stripLeadingAndTrailingQuotes("        "));
    }
}