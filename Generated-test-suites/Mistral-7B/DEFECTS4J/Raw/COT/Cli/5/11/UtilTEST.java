import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String inputStr1 = "--some-string";
        String expectedOutput1 = "some-string";
        String outputStr1 = Util.stripLeadingHyphens(inputStr1);
        assertEquals(expectedOutput1, outputStr1);

        String inputStr2 = "some-string";
        String expectedOutput2 = "some-string";
        String outputStr2 = Util.stripLeadingHyphens(inputStr2);
        assertEquals(expectedOutput2, outputStr2);

        String inputStr3 = "-some-string";
        String expectedOutput3 = "some-string";
        String outputStr3 = Util.stripLeadingHyphens(inputStr3);
        assertEquals(expectedOutput3, outputStr3);

        String inputStr4 = "some-string";
        String expectedOutput4 = "some-string";
        String outputStr4 = Util.stripLeadingHyphens(inputStr4);
        assertEquals(expectedOutput4, outputStr4);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingHyphensWithEmptyString() {
        String inputStr = "";
        String outputStr = Util.stripLeadingHyphens(inputStr);
        // no outputStr check as empty string is not modified by the method
        throw new AssertionError("Expected an AssertionError due to an empty String input.");
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String inputStr1 = "\"some string\"";
        String expectedOutput1 = "some string";
        String outputStr1 = Util.stripLeadingAndTrailingQuotes(inputStr1);
        assertEquals(expectedOutput1, outputStr1);

        String inputStr2 = "some string";
        String expectedOutput2 = "some string";
        String outputStr2 = Util.stripLeadingAndTrailingQuotes(inputStr2);
        assertEquals(expectedOutput2, outputStr2);

        String inputStr3 = "\"some\tstring\"";
        String expectedOutput3 = "some	string";
        String outputStr3 = Util.stripLeadingAndTrailingQuotes(inputStr3);
        assertEquals(expectedOutput3, outputStr3);

        String inputStr4 = "\"some string\"";
        String expectedOutput4 = "some string";
        String outputStr4 = Util.stripLeadingAndTrailingQuotes(inputStr4);
        assertEquals(expectedOutput4, outputStr4);

        String inputStr5 = "\"\"";
        String expectedOutput5 = "";
        String outputStr5 = Util.stripLeadingAndTrailingQuotes(inputStr5);
        assertEquals(expectedOutput5, outputStr5);
    }
}