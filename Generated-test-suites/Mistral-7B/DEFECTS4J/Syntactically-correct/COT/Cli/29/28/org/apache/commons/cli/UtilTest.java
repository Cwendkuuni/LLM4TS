import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        String inputStr = "--test";
        String expectedOutput = "test";
        String outputStr = Util.stripLeadingHyphens(inputStr);
        Assert.assertEquals(expectedOutput, outputStr);

        inputStr = "test";
        expectedOutput = "test";
        outputStr = Util.stripLeadingHyphens(inputStr);
        Assert.assertEquals(expectedOutput, outputStr);

        inputStr = "---test";
        expectedOutput = "test";
        outputStr = Util.stripLeadingHyphens(inputStr);
        Assert.assertEquals(expectedOutput, outputStr);

        inputStr = null;
        expectedOutput = null;
        outputStr = Util.stripLeadingHyphens(inputStr);
        Assert.assertEquals(expectedOutput, outputStr);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingHyphensWithNullInputThrowsException()
    {
        String inputStr = null;
        Util.stripLeadingHyphens(inputStr);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String inputStr = "\"test1 test2\"";
        String expectedOutput = "test1 test2";
        String outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
        Assert.assertEquals(expectedOutput, outputStr);

        inputStr = "test1 test2";
        expectedOutput = "test1 test2";
        outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
        Assert.assertEquals(expectedOutput, outputStr);

        inputStr = "\"test1 test2\"\n";
        expectedOutput = "test1 test2";
        outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
        Assert.assertEquals(expectedOutput, outputStr);

        inputStr = "";
        expectedOutput = "";
        outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
        Assert.assertEquals(expectedOutput, outputStr);

        inputStr = null;
        expectedOutput = null;
        outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
        Assert.assertEquals(expectedOutput, outputStr);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testStripLeadingAndTrailingQuotesWithEmptyStringThrowsException()
    {
        String inputStr = "";
        Util.stripLeadingAndTrailingQuotes(inputStr);
    }
}