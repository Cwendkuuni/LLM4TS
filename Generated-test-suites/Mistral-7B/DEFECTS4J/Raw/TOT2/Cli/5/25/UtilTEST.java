import org.junit.Test;
import org.junit.Assert;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--test-case";
        String expectedOutput = "test-case";
        String actualOutput = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "\"test-case\"";
        String expectedOutput = "test-case";
        String actualOutput = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }
}