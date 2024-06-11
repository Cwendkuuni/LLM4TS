import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Test cases for str starting with double hyphens, single hyphen, and no hyphen
        Assert.assertEquals("test", Util.stripLeadingHyphens("--test"));
        Assert.assertEquals("test", Util.stripLeadingHyphens("-test"));
        Assert.assertEquals("test", Util.stripLeadingHyphens("test"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test cases for str with leading and trailing quotes
        Assert.assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        Assert.assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two"));
        Assert.assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two\""));

        // Test case for str without leading and trailing quotes
        Assert.assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
    }
}