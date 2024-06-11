import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Test with string starting with hyphens
        String input1 = "--test";
        String expected1 = "test";
        String result1 = Util.stripLeadingHyphens(input1);
        Assert.assertEquals(expected1, result1);

        // Test with string starting with a single hyphen
        String input2 = "-test";
        String expected2 = "test";
        String result2 = Util.stripLeadingHyphens(input2);
        Assert.assertEquals(expected2, result2);

        // Test with string without hyphens
        String input3 = "test";
        String expected3 = "test";
        String result3 = Util.stripLeadingHyphens(input3);
        Assert.assertEquals(expected3, result3);

        // Test with null input
        String input4 = null;
        String expected4 = null;
        String result4 = Util.stripLeadingHyphens(input4);
        Assert.assertEquals(expected4, result4);

        // Test with empty string
        String input5 = "";
        String expected5 = "";
        String result5 = Util.stripLeadingHyphens(input5);
        Assert.assertEquals(expected5, result5);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test with string starting and ending with quotes
        String input1 = "\"test\"";
        String expected1 = "test";
        String result1 = Util.stripLeadingAndTrailingQuotes(input1);
        Assert.assertEquals(expected1, result1);

        // Test with string starting with a quote
        String input2 = "\"test";
        String expected2 = "\"test";
        String result2 = Util.stripLeadingAndTrailingQuotes(input2);
        Assert.assertEquals(expected2, result2);

        // Test with string ending with a quote
        String input3 = "test\"";
        String expected3 = "test\"";
        String result3 = Util.stripLeadingAndTrailingQuotes(input3);
        Assert.assertEquals(expected3, result3);

        // Test with string without quotes
        String input4 = "test";
        String expected4 = "test";
        String result4 = Util.stripLeadingAndTrailingQuotes(input4);
        Assert.assertEquals(expected4, result4);

        // Test with null input
        String input5 = null;
        String expected5 = null;
        String result5 = Util.stripLeadingAndTrailingQuotes(input5);
        Assert.assertEquals(expected5, result5);

        // Test with empty string
        String input6 = "";
        String expected6 = "";
        String result6 = Util.stripLeadingAndTrailingQuotes(input6);
        Assert.assertEquals(expected6, result6);
    }
}