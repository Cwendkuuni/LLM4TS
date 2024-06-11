
import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Test with hyphens at the beginning
        String input1 = "--test";
        String expected1 = "test";
        String result1 = Util.stripLeadingHyphens(input1);
        Assert.assertEquals(expected1, result1);

        // Test with hyphen at the beginning
        String input2 = "-test";
        String expected2 = "test";
        String result2 = Util.stripLeadingHyphens(input2);
        Assert.assertEquals(expected2, result2);

        // Test without hyphens
        String input3 = "test";
        String expected3 = "test";
        String result3 = Util.stripLeadingHyphens(input3);
        Assert.assertEquals(expected3, result3);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test with leading and trailing quotes
        String input1 = "\"test\"";
        String expected1 = "test";
        String result1 = Util.stripLeadingAndTrailingQuotes(input1);
        Assert.assertEquals(expected1, result1);

        // Test with only leading quotes
        String input2 = "\"test";
        String expected2 = "\"test";
        String result2 = Util.stripLeadingAndTrailingQuotes(input2);
        Assert.assertEquals(expected2, result2);

        // Test with only trailing quotes
        String input3 = "test\"";
        String expected3 = "test\"";
        String result3 = Util.stripLeadingAndTrailingQuotes(input3);
        Assert.assertEquals(expected3, result3);

        // Test without quotes
        String input4 = "test";
        String expected4 = "test";
        String result4 = Util.stripLeadingAndTrailingQuotes(input4);
        Assert.assertEquals(expected4, result4);
    }
}
