import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical use case
        String str1 = "--example";
        Assert.assertEquals("example", Util.stripLeadingHyphens(str1));

        // Edge case
        String str2 = "-";
        Assert.assertEquals("", Util.stripLeadingHyphens(str2));

        // Error scenario
        String str3 = "example";
        Assert.assertEquals("example", Util.stripLeadingHyphens(str3));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use case
        String str1 = "\"example\"";
        Assert.assertEquals("example", Util.stripLeadingAndTrailingQuotes(str1));

        // Edge case
        String str2 = "\"";
        Assert.assertEquals("", Util.stripLeadingAndTrailingQuotes(str2));

        // Error scenario
        String str3 = "example";
        Assert.assertEquals("example", Util.stripLeadingAndTrailingQuotes(str3));
    }

}