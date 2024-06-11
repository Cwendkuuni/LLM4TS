import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Basic test case
        String input = "--test";
        String expected = "test";
        String result = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expected, result);

        // Test case with single hyphen
        input = "-test";
        expected = "test";
        result = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expected, result);

        // Test case with no hyphen
        input = "test";
        expected = "test";
        result = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expected, result);

        // Test case with null input
        input = null;
        expected = null;
        result = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Basic test case
        String input = "\"one two\"";
        String expected = "one two";
        String result = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expected, result);

        // Test case with no quotes
        input = "one two";
        expected = "one two";
        result = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expected, result);

        // Test case with leading quote only
        input = "\"one two";
        expected = "\"one two";
        result = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expected, result);

        // Test case with trailing quote only
        input = "one two\"";
        expected = "one two\"";
        result = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expected, result);
    }
}